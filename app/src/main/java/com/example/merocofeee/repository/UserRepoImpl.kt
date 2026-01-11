package com.example.merocofeee.repository

import androidx.compose.animation.core.snap

import com.example.merocofeee.model.UserModel

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserRepoImpl: UserRepo {

    val auth : FirebaseAuth= FirebaseAuth.getInstance()
    val database: FirebaseDatabase= FirebaseDatabase.getInstance()
    val ref: DatabaseReference=database.getReference("users")


    override fun login(
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful){
                callback(true,"login sucessfully")
            }else {
                callback(false ,"${it.exception?.message}")
            }
        }
    }

    override fun forgetPassword(
        email: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    callback(true,"Reset link sent to $email ")
                }else {
                    callback(false ,"${it.exception?.message}")
                }
            }
    }

    override fun updateProfile(
        userId: String,
        model: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(userId).updateChildren(model.toMap()).addOnCompleteListener {
            if (it.isSuccessful){
                callback(true,"Profile added successfully")
            } else{
                callback(false,"${it.exception?.message}")
            }
        }


    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun logout(callback: (Boolean, String) -> Unit) {
        try {
            auth.signOut()
            callback(true,"logut")
        } catch (e: Exception){
            callback(false,e.message.toString())
        }
    }

    override fun deleteAccount(
        userId: String,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(userId).removeValue().addOnCompleteListener {
            if (it.isSuccessful){
                callback(true,"Account Deleted")
            } else{
                callback(false,"${it.exception?.message}")
            }
        }

    }







    override fun getAllUser(callback: (Boolean, String, List<UserModel?>?) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val  allUsers = mutableListOf<UserModel>()
                    for (data in snapshot.children){
                        val user = data.getValue(UserModel::class.java)
                        if(user!= null ){
                            allUsers.add(user)
                        }
                    }
                    callback(true,"User fetched",allUsers)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false,error.message,null)
            }
        })
    }

    override fun getUserById(
        userId: String,
        callback: (Boolean, String, UserModel?) -> Unit
    ) {
        ref.child(userId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val users = snapshot.getValue(UserModel::class.java)
                    if (users != null ){
                        callback(true,"profile fetched",users)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false,error.message,null)
            }
        })
    }

    override fun register(
        email: String,
        password: String,
        callback: (Boolean, String, String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful){
                callback(true,"Registration Successful ","${auth.currentUser?.uid}")
            }else {
                callback(false ,"${it.exception?.message}","")
            }
        }

    }

    override fun addUserTODatabase(
        userId: String,
        model: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(userId).setValue(model).addOnCompleteListener {
            if(it.isSuccessful){
                callback(true,"Registration Sucessfull ")
            }else {
                callback(false ,"${it.exception?.message}")
            }
        }
    }
}