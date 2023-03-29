package main

import "fmt"

func main() {
	var i int = 2 + 5					// add_int
	var b bool = true 					// assign_bool
	b = false							// assign_bool
	b = true || (false && true)			// assign_bool
	b = boolean_func() 					// assign_bool_func_call

	var f float64 = float_func()		// assign_float64_func_call
	f = float_func()					// assign_float64_func_call

	i = 999 + 1 + 2 * 9 % 3				// assign_int
	b = 10 > 3 + 3						// assign_int_compare
	b = (10 > 3) && (9 > 3) || 1 >= 1 	// assign_int_compare2

	i = int_func(10 + 3 * 2)			// assign_int_func_call
	i = int_func(10)					// assign_int_func_call
	i = int_func2()						// assign_int_func_call

	f = 13								// assign_int_to_float64
	f = -13								// assign_neg_float64
	f = -3.144							// assign_neg_float64

	i = -10								// assign_neg_int
	var s string = "hello"				// assign_string
	s = "hello" + "world"				// assign_string
	s = s + "!!!" 						// assign_string2
	s = string_func()					// assign_string_func_call
	s = string_func2(s)					// assign_string_func_call

	f = float_func2(10)					// call_float64_func_with_int_arg

	// call_in_if
	if boolean_func() {
		fmt.Println("call_in_if")
		if (boolean_func() && false) { fmt.Println("hello") }
	}

	// call_in_for
	/*for boolean_func() {
		fmt.Println("call_in_for")
		if (boolean_func() && true) { fmt.Println("hello") }
	}*/

	var b2 bool = true					// declare_bool
	var f2 float64 = 1.00				// declare_float64
	var i2 int = 99						// declare_int
	var s2 string = "string"			// declare_string

	// double_in_for
	/*for 7.33 >= 3.14 {
		for boolean_func() {
			for boolean_func() {
				fmt.Println("triple")
			}
		}
	}*/

	1.337 < 5							// double_lt_int
	/*for true {							// empty_for

	}*/

	10 + 20								// expr_add_int
	true && true && false				// expr_bool_and
	true == true || false == false 		// expr_bool_eq
	10 >= 9								// expr_bool_ge
	10 > 9								// expr_bool_gt
	3 <= 4								// expr_bool_le
	3 < 4								// expr_bool_lt
	true != true 						// expr_bool_ne
	true != !true 						// expr_bool_not
	!false		 						// expr_bool_not
	true || false 						// expr_bool_or
	1.23 + 3.456						// expr_float64_add
	1.23 / 3.456						// expr_float64_div
	1.23 * 3.456						// expr_float64_mul
	3.456 - 1.23						// expr_float64_sub
	10 / 3 								// expr_int_div
	10 / 5 								// expr_int_div
	10 % 2								// expr_int_mod
	10 * 2								// expr_int_mul
	10 - 2								// expr_int_sub
	-10									// expr_unary_minus_int
	+10									// expr_unary_plus_int

	// for_if
	/*for true {
		if true {
			fmt.Println("hello")
		}
	}*/

	// if_for
	if true {
		/*for true {
			fmt.Println("hello")
		}*/
	}

	for 10 < 3 - 2 {

	}

	main2()
	float_func2(1.033)

	if true {

	} else {

	}

	if true {

	} else if true {
		
	} else {

	}

	if true {
		
	} else if false {

	}

	if "hello" == "hello" {

	}

	/*
		Multiline comment
	*/

	// Single line comment

	fmt.Println(float_func2(float_func()) <= 10.3)

	// boolean_func = 20
	// boolean_func() = true
}

func main2() {							// empty_main

}

func boolean_func() bool {
	return true
}

func float_func() float64 {
	return 3.14
}

func float_func2(f float64) float64 {
	return -f
}

func int_func(i int) int {
	return 1337
}

func int_func2() int {
	return 10
}

func string_func() string {
	return "lol"
}

func string_func2(s string) string {
	return s + "lol"
}