package main

import (
	"fmt"
)

func main() {
	m := map[string]string{"key1": "value1", "key2": "value2"}
	fmt.Printf("type: %T, value: %[1]s \n", m["key3"])

	fmt.Println("Hello, world!")
}
