package main

import (
	"fmt"
	"reflect"
)

type student struct {
	Name  string
	Age   int
	Name2 string
}

func (stu student) SetName(name string, name2 string) {
	stu.Name = name
	stu.Name2 = name2
}

func (stu student) SetAge(age int) {
	stu.Age = age
}

func (stu student) Print() string {
	return fmt.Sprintf("Name: %s, Age: %d, Name2: %s", stu.Name, stu.Age, stu.Name2)
}

func main() {
	stu := student{"tom", 23, "HanMei"}
	fmt.Println("origin student: ", stu)

	fun := reflect.ValueOf(&stu).Elem()
	fmt.Println(fun.MethodByName("Print").Call(nil)[0])

	params := make([]reflect.Value, 2)
	params[0] = reflect.ValueOf("Tom")
	params[1] = reflect.ValueOf("LiLei")
	fun.MethodByName("SetName").Call(params)

	fmt.Println("hello world")

	params2 := make([]reflect.Value, 1)
	params2[0] = reflect.ValueOf(34)
	fun.MethodByName("SetAge").Call(params2)

	fmt.Println(fun.MethodByName("Print").Call(nil))
}
