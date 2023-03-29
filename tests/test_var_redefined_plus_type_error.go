package main

import "fmt"

func main() {
    var a int = 3

    var a bool = true

    {
        var a int = 4
        var b int = 3
    }

    fmt.Println("yeahhh")
}

func add(a int, b int) {
    var c bool = true
    return c
}