package main

import "fmt"

// var a int = 10

func callme(a int) int {
    return a - 2
}

func main() {
    if 7%2==   0 {
        fmt.Println("7 is even")
    } else {
        fmt.Println("7 is odd")
    }

    if 8%4 == 0 - callme(8) {
        fmt.Println("8 is divisible by 4")
    }

    var num int = 3

    if num < 0 {
        fmt.Println(num)
        fmt.Println("is negative")
    } else if num < 10 {
        fmt.Println(num)
        fmt.Println("has 1 digit")
    } else {
        fmt.Println(num)
        fmt.Println("has multiple digits")
    }
}

func lol() 
{
    var b bool = true /*yee*/|| false //l
}