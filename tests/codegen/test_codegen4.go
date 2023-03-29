package main
import "fmt"

func float64_func(f int) float64 {
	var abc float64 = 10.0 + f
	return abc
}

func bool_func(b bool) float64 {
	if b == true {
		var a int = 1337
		var bean float64 = 42.2
		var c float64 = a + bean
		return c
	}

	return 0.0
}

func int_func(i int, i2 int, b3 bool) int {
	var a int = 10
	var b int = 20
	var c bool = true

	var result int = a + b + i * i2
	var f float64 = 42

	if c == true {
		result = result + 1
	}

	return result
}

func main() {
	var f float64 = float64_func(15)
	f = f + 10 * 3.33

	if "hello" == "hello" {
		fmt.Println(f)
	} else if f <= 20 {
		fmt.Println(0)
	} else {
		fmt.Println(1337)
	}

	fmt.Println(int_func(1, 2, true))
	fmt.Println(bool_func(true || false))
	fmt.Println(bool_func(true && true))
	fmt.Println(bool_func(!false && false))

	var a int = 13 % 7
	a = -(a * 2 + 1)
	fmt.Println(a)
}
