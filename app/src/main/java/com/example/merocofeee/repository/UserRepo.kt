package com.example.merocofeee.repository

import com.example.merocofeee.UserModel
import com.google.firebase.auth.FirebaseUser

interface UserRepo {
    //    {
//        "Success ":false
//        "message":invalid login
//    }
//
    fun login (email:String,password: String,
               callback:(Boolean, String)-> Unit)
    //    {
//        "Success ":true
//        "message":now you can change password
//    }
//
    fun forgetPassword( email:String,
                        callback:(Boolean, String)-> Unit)
    fun updateProfile (userId: String, model:
    UserModel, callback:(Boolean, String)-> Unit)
    fun getCurrentUser() : FirebaseUser?
    fun logout (callback:(Boolean, String)-> Unit)
    fun deleteAccount ( userId: String,
                        callback: (Boolean, String) -> Unit
    )
    fun getAllUser (callback: (Boolean, String, List<UserModel?>?) -> Unit)

    fun getUserById ( userId: String,  callback:(Boolean, String, UserModel?)-> Unit)
    fun register (email: String,password: String,
                  callback: (Boolean, String, String) -> Unit)
    fun addUserTODatabase(userId: String,
                          model: UserModel,callback: (Boolean, String) -> Unit
    )
}
