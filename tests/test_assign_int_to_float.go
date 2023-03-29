package main

import "fmt"

func main() {
	var a float64 = 5 / 3
	a = 10.00
	{
		var a bool = true
		a = !(false || true && !!false) || false
		a = 3.33
	}

	a = 3.14 * (3 * false)
}