package main

import (
	"errors"
	"fmt"
	"testing"
	"time"
)

// 在高并发服务中，如果每个请求都直接启动一个 goroutine 来执行耗时的同步调用（如慢接口或数据库查询），可能导致大量 goroutine 阻塞等待，一旦请求激增会耗尽内存或线程资源，造成服务雪崩。为此常使用协程池（goroutine pool来隔离请求并限制并发数，从而保护服务稳定性。  协程池的目的就是“限制任务并发，以避免资源耗尽” ￼。
// 协程池的本质是在后台启动固定数量的 worker goroutine，通过 channel 分发任务；
// * 任务封装：每个请求被封装成 func() 类型的任务函数，并通过 channel 提交给协程池。
// * 有界队列：使用缓冲 channel 限定队列长度，当队列满时通过非阻塞 select 拒绝新任务，防止任务无限堆积。
// * 固定 worker 数量, 从而避免 goroutine 无限制增长。 事先启动若干个 worker goroutine，并让它们循环从任务通道中读取任务执行。
// * 模拟阻塞调用：示例中使用 time.Sleep 模拟慢接口或数据库调用，说明即使任务内部阻塞，也只能由固定的 worker 处理，不会无限制扩展 goroutine。
// 辨析说明：
// * 虽然协程是百万的轻量级，但是过多阻塞的协程依然会消耗内存并增加调度开销；百万协程在运行时如果从1kb的栈动态增长到大栈也会占据海量的内存，所以线程协程数量也是有意义的。

// Task 类型定义了要执行的任务函数
type Task func()

// WorkerPool 表示一个简单的协程池
type WorkerPool struct {
	tasks chan Task // 带缓冲的任务通道
}

// NewWorkerPool 创建一个具有 numWorkers 个 worker 和 queueSize 大小队列的协程池
func NewWorkerPool(numWorkers int, queueSize int) *WorkerPool {
	pool := &WorkerPool{
		tasks: make(chan Task, queueSize), // 有界队列，超过此长度后拒绝新任务
	}
	// 启动固定数量的 worker goroutine
	for i := 0; i < numWorkers; i++ {
		go func(id int) {
			for task := range pool.tasks {
				// 执行任务（同步调用）
				fmt.Printf("[Worker %d] 开始执行任务\n", id)
				task()
				fmt.Printf("[Worker %d] 完成任务\n", id)
			}
		}(i)
	}
	return pool
}

// Submit 尝试将任务提交到池中。如果队列已满则立即返回错误拒绝
func (p *WorkerPool) Submit(task Task) error {
	select {
	case p.tasks <- task:
		return nil // 成功入队
	default:
		return errors.New("任务队列已满，拒绝请求")
	}
}

// Shutdown 关闭任务通道，停止所有 worker（该示例中简化处理）
func (p *WorkerPool) Shutdown() {
	close(p.tasks)
}

func TestGoroutinePool(t *testing.T) {
	// 创建一个协程池：3 个 worker，队列容量 5
	pool := NewWorkerPool(3, 5)

	// 模拟提交 10 个任务
	for i := 0; i < 10; i++ {
		i := i // 捕获循环变量
		err := pool.Submit(func() {
			// 模拟耗时同步调用
			fmt.Printf("任务 %d: 开始慢操作\n", i)
			time.Sleep(2 * time.Second)
			fmt.Printf("任务 %d: 完成慢操作\n", i)
		})
		if err != nil {
			// 当任务队列已满时，会走到这里
			fmt.Printf("任务 %d 被拒绝: %v\n", i, err)
		}
	}

	// 等待一段时间，观察部分任务执行情况
	time.Sleep(10 * time.Second)
	pool.Shutdown()
}
