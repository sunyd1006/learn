package main

import (
	"fmt"
	"sync"
	"time"
)

var (
	mu    sync.RWMutex
	queue []int
	idx   int

	notifier chan struct{}
)

func init() {
	queue = make([]int, 0)
	idx = 0

	notifier = make(chan struct{}, 10)
}

func producer(wg *sync.WaitGroup, stopCh chan struct{}) {
	defer wg.Done()
	for {
		select {
		case <-stopCh:
			return
		default:
			mu.Lock()
			queue = append(queue, idx)
			fmt.Println("produce: ", idx)
			idx++
			mu.Unlock()

			notifier <- struct{}{} // notify consumer
			time.Sleep(100 * time.Millisecond)
		}
	}
}

func consumer(wg *sync.WaitGroup, stopCh chan struct{}) {
	defer wg.Done()
	for {
		select {
		case <-stopCh:
			return
		case <-notifier:
			mu.Lock()
			if len(queue) > 0 {
				out := queue[0]
				queue = queue[1:]
				fmt.Println("	consumed: ", out)
			}
			// EE: 因为defer 会在函数返回时才执行, 所以如果使用defer延迟释放锁，但因为函数一直不返回则不会释放锁，最终会导致死锁
			// defer mu.Unlock() // EE:
			mu.Unlock()
		}
	}
}

func main() {
	stopCh := make(chan struct{})
	var wg sync.WaitGroup

	// Start 3 producers
	wg.Add(3)
	go producer(&wg, stopCh)
	go producer(&wg, stopCh)
	go producer(&wg, stopCh)

	// Start 2 consumers
	wg.Add(2)
	go consumer(&wg, stopCh)
	go consumer(&wg, stopCh)

	// Let producers and consumers run for 3 seconds
	time.Sleep(3 * time.Second)

	// Close the stopCh to signal all goroutines to stop
	close(stopCh)

	// Wait for all goroutines to finish
	wg.Wait()
}
