package com.example.android.doors

object Door {
     private val doorOrder = mutableListOf("door one", "door two")

    fun getDoor(): String {
        return doorOrder.random()
    }
}