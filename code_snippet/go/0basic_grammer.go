package main

import "fmt"

func main() {
	var m map[string]string
	for k, v := range m {
		fmt.Println(k, v)
	}
	fmt.Println(m == nil)
}
