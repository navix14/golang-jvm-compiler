package main
import "fmt"

// This code tests a valid for-loop
func main() {
    var i int = 5
    for i > 0 {
        fmt.Println(i)
        i = i - 1
    }
}