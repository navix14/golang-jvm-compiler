package main

import "fmt"

func factorial(n int) int {
	var result int = 1

	for n > 1 {

		// This will calculate
		/*
			A factorial
		*/

		result = result * n

		for n > 0 {

		}

	}

	return result
}

func main() {
	fmt.Println(Fib(5))
	fmt.Println(Fib(10))

	var s1 string = "hello"
	var s2 string = "world"
	var s3 string = s1 + s2

	fmt.Println(s1 + " " + s2)
	fmt.Println(s3)
}

func Fib(n int) int {
	if n > 0 {
		if n <= 2 {
			return 1
		} else {
			return Fib(n-1) + Fib(n-2)
		}
	} else {
		return 0
	}
}