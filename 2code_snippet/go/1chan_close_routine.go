package main

import (
	"fmt"
	"sync"
	"time"
)

func runTask(id string, stopChan <-chan struct{}, done chan<- string, wg *sync.WaitGroup) {
	defer wg.Done()
	ticker := time.NewTicker(3 * time.Second)
	for {
		select {
		case <-ticker.C:
			fmt.Println("	task running, id: ", id)
		case <-stopChan:
			fmt.Println("	task terminated, id: ", id)
			done <- id
			return
		}
	}
}

// close(stopChan) 是异步的close, 不会等待 stopChan 所对应的函数体运行结束和退出
func main() {
	var wg sync.WaitGroup
	stopChan := make(chan struct{})
	done := make(chan string, 2)

	wg.Add(1)
	go runTask("1", stopChan, done, &wg)

	wg.Add(1)
	go runTask("2", stopChan, done, &wg)

	fmt.Println("will send close signal")
	close(stopChan)
	fmt.Println("will send close signal done.")

	tmp := <-done
	fmt.Println("receive done: ", tmp)

	tmp = <-done
	fmt.Println("receive done: ", tmp)
	wg.Wait()
}
