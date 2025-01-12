package main

import (
	"fmt"
	"io/ioutil"
	"sync"
	"time"
)

type result struct {
	path string
	hash []byte
	err  error
}

func calculateHash(path string, c chan result, done <-chan struct{}) {
	var wg sync.WaitGroup
	wg.Add(1)
	go func() {
		defer wg.Done()
		data, err := ioutil.ReadFile(path)
		if err != nil {
			c <- result{path, nil, err}
			return
		}

		slice := make([]byte, 1024)
		copy(slice, data)

		// block until one case is meet
		select {
		case c <- result{path, slice, nil}:
		case <-done:
			fmt.Println("计算被取消")
		}
	}()
	wg.Wait()
}

func main() {
	c := make(chan result)
	done := make(chan struct{})
	path := "/Users/sunyindong.syd/codespace/learn/test_go/demo/pipeline.go"

	go calculateHash(path, c, done)

	// 模拟一些其他操作
	time.Sleep(1 * time.Second)

	res := <-c
	if res.err == nil {
		fmt.Printf("文件 %s 的 MD5 哈希值为: %x\n", res.path, res.hash)
	} else {
		fmt.Printf("读取文件 %s 失败: %v\n", res.path, res.err)
	}

	// 发送取消信号
	close(done)
}
