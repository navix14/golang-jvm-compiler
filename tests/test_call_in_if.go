package main

import "fmt"

func boolean_func(b bool) bool {
	return true
}

func main() {
	if (boolean_func(true && true || false) || boolean_func(false) && boolean_func(false)) {fmt.Println("hello")} else {}
}