package main

import (
	"fmt"
	"sync"
	"testing"
	"time"
)

type test struct {
	name string
}

func willPanic(i int, iptr *test, wg *sync.WaitGroup) {
	defer wg.Done()
	ticker := time.NewTicker(1 * time.Second)
	defer ticker.Stop()

	for {
		select {
		case <-ticker.C:
			// defer func() {
			// 	if r := recover(); r != nil {
			// 		fmt.Printf("startCheckLoop panic, error: %v, stack: %s", r, debug.Stack())
			// 	}
			// }()
			if i == 2 {
				time.Sleep(1 * time.Second)
				fmt.Print("\ncurrent_idx: ", i, " will_panic: ", iptr == nil, iptr, iptr.name) // will panic
			}
			fmt.Print("\ncurrent_idx: ", i)
			time.Sleep(1 * time.Second)
		default:
			time.Sleep(1 * time.Second)
		}
	}
}

func wrapper(wg *sync.WaitGroup) {
	var np *test

	wg.Add(1)
	go willPanic(2, np, wg)

	wg.Wait()
}

func TestMain(t *testing.M) {
	t.Run()
}

func TestSubChannelThrowPanic(t *testing.T) {
	var wg sync.WaitGroup
	go wrapper(&wg)

	wg.Wait()

	time.Sleep(5 * time.Second)
}
