package com.tj.myandroid.kotlin

fun main(args: Array<String>) {
//    vars("hello","word","!")
//    chengfa()
//    printPerson("kobe",age = 24)
//    println(getText())
//    scoreWhen()
    xunhuan();
}
fun add(a: Int,b: Int):Int = a+b

fun vars(vararg s: String){
    for ( a in s){
        System.out.println(a)
    }
    System.out.println(sum(1,5))

    val ages = age.toString()?:"23"
    System.out.println(ages+"")
}

val sum : (Int, Int) -> Int = {x,y -> x+y}

var age:String? =null;

fun chengfa() {
    for(i in 1..9) {
        for(j in 1..i)
            print("$j×$i=${i * j}\t")
        println()           //另起一行
    }
}

fun printPerson(name: String,sex:Boolean = true,age :Int){
    println("姓名："+name)
    println("性别："+ if (sex)"男" else "女")
    println("年龄："+age)
}

fun getText(): String {
//    return "  kobe"
    return """Kotlin is created by JetBrains
        #Kotlin is a Programming language running on JVM
        #We can use Kotlin to create Android apps""".trimMargin("#")
}

fun scoreWhen() {
    val score = 100
    val result = when (score) {
        97, 98, 99,100 -> "接近满分了"
        in 90..96, 100 -> {
            println("优")
            "优秀"
        }
        in 60..89 -> "及格"
        in 0..59 -> "不及格"
        else -> {
            println("成绩输入不正确")
            null
        }
    }
    print(result)
}

fun xunhuan(){
    for(i in 1..10){
        print(i)
    }
    val frute = listOf("apple","banana","orange");
    for(j in 1..frute.size){
        print(frute[j-1])
    }
}