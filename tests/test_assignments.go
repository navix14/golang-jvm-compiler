package main

import "fmt"

func main() {
	var i int = 5-3
	var f float64 = 1.0+2.14
	var b bool = true || !false
	var s string = "Hallo"

	fmt.Println(i)
	fmt.Println(f)
	fmt.Println(b)
	fmt.Println(s)

	fmt.Println("Hello" == "Hello")
	fmt.Println("HellO" == "Hello")
	fmt.Println("HellO" != "Hello")
}