package com.example.merocofeee.model


data class UserModel(
    val userId:String =" ",
    val firstname:String =" ",
    val fullname:String=" ",
    val lastname:String =" ",
    val email:String =" ",
    val password:String =" ",
    val dob:String =" ",
    val gender:String =" ",
){
    fun toMap(): Map<String,Any?>{
        return mapOf(
            "userId" to userId,
            "firstname " to firstname,
            "fullname " to fullname,
            "lastname" to lastname,
            "email " to email,
            "dob" to dob,
            "gender" to gender,

            )

    }
}

