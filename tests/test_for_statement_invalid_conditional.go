package main
import "fmt"

// This code tests a broken for-loop
// The conditional for the for-loop needs to be a boolean expression,
// however here the expression is "i + 3" which is resolved to have integer type
func main() {
    var i int = 5
    for i + 3 {
        fmt.Println(i)
        i = i - 1
    }
}