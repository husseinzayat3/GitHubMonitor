package com.example.githubmonitor

class Users {
    //    var id: Int = 0
    lateinit var num_commiters: String
    lateinit var date: String
//    val committer:String
    constructor()
    constructor(num_commiters:String,date:String) {
        this.num_commiters = num_commiters
        this.date = date
    }

}