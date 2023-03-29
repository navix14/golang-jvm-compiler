package main

import "fmt"

func main() {
	fmt.Println(Fib(5))
	fmt.Println(Fib(10))
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