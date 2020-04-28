package com.tj.myandroid.kotlin

class Person (work: String){
    var name : String = "kobe"
    var age : Int = 40
    get() = field
    set(value) {field = if (value>0) value else 40}
    var work : String = work
    var sex :Boolean = true

    lateinit var child :String

    init {
        setup()
    }

    fun setup(){
        child = "GiGi"
    }

    constructor(work : String,name: String):this(name){

    }
}