package main

import (
	"fmt"
	"runtime"
	"testing"
	"time"
)

// https://www.topgoer.com/%E8%B5%84%E6%96%99%E4%B8%8B%E8%BD%BD/Golang%E6%96%B0%E6%89%8B%E5%8F%AF%E8%83%BD%E4%BC%9A%E8%B8%A9%E7%9A%8450%E4%B8%AA%E5%9D%91.html#%E9%AB%98%E7%BA%A7%E7%AF%87%EF%BC%9A52-58

func GetFunctionName() string {
	// 获取当前函数的程序计数器
	pc, _, _, _ := runtime.Caller(1)
	// 获取函数名
	return runtime.FuncForPC(pc).Name()
}

func PrintFunctionName() {
	pc, _, _, _ := runtime.Caller(1)
	fmt.Println("\n" + runtime.FuncForPC(pc).Name())
}

func test54_nil_interface() {
	PrintFunctionName()
	bad := func() {
		doIt := func(arg int) interface{} {
			var result *struct{} = nil
			if arg > 0 {
				result = &struct{}{}
			}
			return result
		}

		if res := doIt(-1); res != nil {
			fmt.Println("Good result: ", res) // Good result:  <nil>
			fmt.Printf("%T\n", res)           // *struct {}    // res 不是 nil，它的值为 nil
			fmt.Printf("%v\n", res)           // <nil>
		}
	}
	good := func() {
		doIt := func(arg int) interface{} {
			var result *struct{} = nil
			if arg > 0 {
				result = &struct{}{}
			} else {
				return nil // 明确指明返回 nil
			}
			return result
		}

		if res := doIt(-1); res != nil {
			fmt.Println("Good result: ", res)
		} else {
			fmt.Println("Bad result: ", res) // Bad result:  <nil>
		}
	}

	bad()
	good()
}

func test_GOMAXPROCS() {
	runtime.GOMAXPROCS(2) // 设置 P 的数量为 2

	for i := 0; i < 10; i++ {
		go func(id int) {
			fmt.Println("Goroutine", id)
			time.Sleep(time.Second)
		}(i)
	}

	fmt.Println("GOMAXPROCS =", runtime.GOMAXPROCS(0))
	fmt.Println("NumGoroutine =", runtime.NumGoroutine())

	time.Sleep(2 * time.Second)
}

func test56_showMaxProcs() {
	PrintFunctionName()
	fmt.Println(runtime.GOMAXPROCS(-1)) // 4
	fmt.Println(runtime.NumCPU())       // 4
	runtime.GOMAXPROCS(20)
	fmt.Println(runtime.GOMAXPROCS(-1)) // 20
	runtime.GOMAXPROCS(300)
	fmt.Println(runtime.GOMAXPROCS(-1)) // Go 1.9.2 // 300
}

func TestBasicGrammer(t *testing.T) {

	test54_nil_interface()
	test56_showMaxProcs()

	test_GOMAXPROCS()
}
