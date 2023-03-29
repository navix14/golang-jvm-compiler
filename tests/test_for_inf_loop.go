package main

import "fmt"

func checkDone() bool {
	return true
}

func main() {
	for checkDone() {
		fmt.Println("infinity")
	}
}