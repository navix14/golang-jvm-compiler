package main

import "fmt"

func other() int {
	if 10 > 5 {
		return 1
	} else {
		return 2
	}
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

	return 9
}

func Fact(n int) int {
	var i int = 1
	var fact int = 1

	for i <= n {
		fact = fact * i
		i = i + 1
	}

	return fact
}

func main() {
	var i int = 10

	fmt.Println("Fibonacci: ")

	for i > 0 {
		var result int = Fib(10 - i + 1)
		fmt.Println(result)
		i = i - 1
	}

	fmt.Println("\nFactorial: ")
	fmt.Println(Fact(7))

	fmt.Println("\nDone :)")
}
