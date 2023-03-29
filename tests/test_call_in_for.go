package main

import "fmt"

func boolean_func() bool {
	return true
}

func lol(n float64) float64 {
	return 3.14
}

func main() {
	var i int = 10
	for boolean_func() || true == true {
		i = i + 1
		var b bool = boolean_func()
	}

	lol(10.5)
}