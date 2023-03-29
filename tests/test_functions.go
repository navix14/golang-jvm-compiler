package main

import "fmt"

func plus(a int, b int) int {

    return a + b
}

func plusPlus(a int, b int, c int) int {
    return a + b + c
}

func returnsWithoutExpr() {
    var a int = 42
    return
}

func main() {

    var res int = plus(1, 2)
    fmt.Println("1+2 =")
    fmt.Println(res)

    res = plusPlus(1, 2, 3)
    fmt.Println("1+2+3 =")
    fmt.Println(res)
}