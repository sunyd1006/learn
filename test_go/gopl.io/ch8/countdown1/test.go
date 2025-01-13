package main

import (
	"fmt"
	"time"
)

func main() {
	// 创建一个定时器，2 秒后执行 func 函数
	timer := time.AfterFunc(2*time.Second, func() {
		fmt.Println("Function executed")
	})

	// 在定时器到期之前，停止定时器
	if timer.Stop() {
		fmt.Println("Timer already expired, function started")
	}

	// 等待一段时间以确保函数 f 执行完毕
	time.Sleep(3 * time.Second)
}
