package main

import (
	"context"
	"fmt"
	"sync"
	"time"
)

// task 模拟一个需要一定时间完成的任务
func task(ctx context.Context, name string, duration time.Duration, wg *sync.WaitGroup) {
	defer wg.Done()
	select {
	case <-time.After(duration):
		fmt.Printf("任务 %s 完成\n", name)
	case <-ctx.Done():
		fmt.Printf("任务 %s 被取消: %v\n", name, ctx.Err())
	}
}

func main() {
	var wg sync.WaitGroup

	// 创建一个带有超时的根 Context
	rootCtx, rootCancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer rootCancel()

	// 启动多个任务
	tasks := []struct {
		name     string
		duration time.Duration
	}{
		{"A", 2 * time.Second},
		{"B", 4 * time.Second},
		{"C", 6 * time.Second}, // context deadline exceeded
	}

	for _, t := range tasks {
		wg.Add(1)
		go task(rootCtx, t.name, t.duration, &wg)
	}

	// 创建一个子 Context，允许在3秒后取消子任务
	childCtx, childCancel := context.WithCancel(rootCtx)
	go func() {
		time.Sleep(3 * time.Second)
		fmt.Println("子 Context: 取消子任务")
		childCancel()
	}()

	// 启动一个子任务，受子 Context 控制
	wg.Add(1)
	go task(childCtx, "D", 4*time.Second, &wg) // context canceled

	// 等待所有任务完成
	wg.Wait()
	fmt.Println("所有任务已完成")
}
