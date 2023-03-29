package main
import "fmt"

func main() {
    var a string = "lol"
    {
        var a float64 = 3.14
        {
            {
                var a bool = true
                a = false
            }
            a = 3.3
        }
    }
    a = "rofl"
}