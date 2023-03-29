package main
import "fmt"

/*
	.bytecode 60.0
	.source HelloWorld.java
	.class public HelloWorld
	.super java/lang/Object

	.method public <init>()V
	.limit stack 1
	.limit locals 1
	.line 1
	0: aload_0
	1: invokespecial java/lang/Object/<init>()V
	4: return
	.end method

	.method private static float64_func(I)F
	.limit stack 2
	.limit locals 2
	.line 3
	0: ldc 10.0
	2: iload_0
	3: i2f
	4: fadd
	5: fstore_1
	.line 4
	6: fload_1
	7: freturn
	.end method

	.method private static int_func(IIZ)I
	.limit stack 1
	.limit locals 6
	.line 8
	0: bipush 10
	2: istore_3
	.line 9
	3: bipush 20
	5: istore 4
	.line 10
	7: iconst_1
	8: istore 5
	.line 11
	10: iconst_3
	11: ireturn
	.end method

	.method public static Main([Ljava/lang/String;)V
	.limit stack 2
	.limit locals 2
	.line 15
	0: bipush 10
	2: invokestatic Hello/float64_func(I)F
	5: fstore_1
	.line 17
	6: fload_1
	7: fconst_0
	8: fcmpl
	9: ifle Label19
	.line 18
	12: getstatic java/lang/System/out Ljava/io/PrintStream;
	15: fload_1
	16: invokevirtual java/io/PrintStream/println(F)V
	Label19:
	.line 21
	19: return
	; append_frame (frameNumber = 0)
	; frame_type = 252, offset_delta = 19
	; frame bytes: 252 0 19 2
	.stack
		offset 19
		locals Float
		.end stack
	.end method                 
*/

func float64_func(f int) float64 {
	var abc float64 = 10.0 + f
	return abc
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
}
