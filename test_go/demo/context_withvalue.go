package main

import (
	"context"
	"fmt"
	"time"
)

// 定义 Context 中使用的键类型，以避免键冲突
type key string

func main() {
	// 定义 Context Key
	userKey := key("user")

	// 创建带有值的 Context
	ctx := context.WithValue(context.Background(), userKey, "Alice")

	// 启动一个 goroutine，传递 Context
	go func(ctx context.Context) {
		// 从 Context 中获取值
		user, ok := ctx.Value(userKey).(string)
		if !ok {
			fmt.Println("无法在 Context 中找到 user")
			return
		}
		fmt.Printf("Goroutine: user = %s\n", user)
	}(ctx)

	// 等待 goroutine 执行完成
	time.Sleep(1 * time.Second)
}
