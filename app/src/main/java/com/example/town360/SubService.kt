package com.example.town360


data class SubService(
    val name: String = "",
    val image: String = ""
) {
    // Add a no-argument constructor for Firebase
    constructor() : this("", "")
}
