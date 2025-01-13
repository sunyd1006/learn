// https://draveness.me/golang/docs/part2-foundation/ch04-basic/golang-reflect/

package main

import (
	"fmt"
	"reflect"
)

/**
	教程：https://draveness.me/golang/docs/part2-foundation/ch04-basic/golang-reflect/
	NOTE: https://qppw4bc6rk.feishu.cn/wiki/SRIUw2DuWiW1KWk7Lncc5KW8n7d
	参考：ch12_reflection/reflection_demo.go

	1. 能将 Go 语言的 interface{} 变量转换成反射对象
	2. 从反射类型转换回interface{} 变量。
		如果原始类型是interface{}，那么还原是不需要显示类型转换的。
		如果原始类型是非 interface{}类型，那么需要形如 v.Interface().(int)
	3.
		 Go 语言的函数调用都是传值的，所以如果reflect.ValueOf(传值）我们得到的反射对象跟最开始的变量没有任何关系，那么直接修改反射对象无法改变原始变量，程序为了防止错误就会崩溃。
**/

type CustomError struct{}

func (*CustomError) Error() string {
	return ""
}

func main() {
	// case1: 转为为 reflection 变量
	author := "draven"
	fmt.Println("TypeOf author:", reflect.TypeOf(author))
	fmt.Println("ValueOf author:", reflect.ValueOf(author))

	// case2:
	v := reflect.ValueOf(1)
	raw := v.Interface().(int)
	fmt.Println("raw:", raw)

	// case3: 请使用指针
	i := 1
	v2 := reflect.ValueOf(&i)
	// 调用 reflect.ValueOf 获取变量指针；
	// 调用 reflect.Value.Elem 获取指针指向的变量；
	// 调用 reflect.Value.SetInt 更新变量的值：
	v2.Elem().SetInt(10)
	fmt.Println("change value by reflection, i: ", i)

	// case4:
	typeOfError := reflect.TypeOf((*error)(nil)).Elem()
	customErrorPtr := reflect.TypeOf(&CustomError{})
	customError := reflect.TypeOf(CustomError{})

	fmt.Println(customErrorPtr.Implements(typeOfError)) // #=> true,   CustomErrorPtr 类型实现了    error 接口
	fmt.Println(customError.Implements(typeOfError))    // #=> false,  CustomError    类型并没有实现 error 接口

	CallAddByReflection()
}

func Add(a, b int) int { return a + b }

func CallAddByReflection() {
	v := reflect.ValueOf(Add)
	if v.Kind() != reflect.Func {
		return
	}
	t := v.Type()
	argv := make([]reflect.Value, t.NumIn())
	for i := range argv {
		if t.In(i).Kind() != reflect.Int {
			return
		}
		argv[i] = reflect.ValueOf(i)
	}
	result := v.Call(argv)
	if len(result) != 1 || result[0].Kind() != reflect.Int {
		return
	}
	fmt.Println(result[0].Int()) // #=> 1
}
