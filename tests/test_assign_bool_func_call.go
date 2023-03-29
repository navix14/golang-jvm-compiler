package main

import "fmt"

func boolean_func() bool {
	return true
}

func int_func() int {
	return 10
}

func float64_func() float64 {
	return 1337.37
}

func string_func() string {
	return "hello world"
}

func main() {
	var i int = int_func()
	var b bool = boolean_func()
	boolean_func() = true
	var f float64 = float64_func()
	var f2 float64 = /**/int_func()
	//	 var b2 bool = float64_func()
	//var b3 bool = int_func()
	var s string = string_func()
}