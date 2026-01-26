package com.example.merocofeee.repository

import ProductRepo
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.example.merocofeee.model.ProductModel


import com.google.firebase.database.*
import java.io.InputStream
import java.util.concurrent.Executors

class ProductRepoImpl : ProductRepo {

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val ref: DatabaseReference = database.getReference("products")

    override fun addProduct(
        product: ProductModel,
        callback: (Boolean, String, String?) -> Unit
    ) {
        val newRef = ref.push()
        val productId = newRef.key

        if (productId == null) {
            callback(false, "Failed to create a new product ID.", null)
            return
        }

        newRef.setValue(product.copy(productId = productId)).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, "Product added successfully", productId)
            } else {
                callback(false, task.exception?.message ?: "Unknown error while adding product", null)
            }
        }
    }

    override fun updateProduct(
        productId: String,
        product: ProductModel,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(productId).updateChildren(product.toMap()).addOnCompleteListener { //setValue() affects the whole node unlike updateChildren which only affects
            // specific attributes. Thus, we use a .toMap() fn in the updateUser because
            // we usually only update some fields.
            if (it.isSuccessful) {
                callback(true, "Product updated")
            } else {
                callback(false, "Failed to update product")
            }
        }
    }

    override fun deleteProduct(
        productId: String,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(productId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Product deleted")
            } else {
                callback(false, "Failed to delete product")
            }
        }
    }

    override fun getProductById(
        productId: String,
        callback: (Boolean, String, ProductModel?) -> Unit
    ) {
        ref.child(productId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val product = snapshot.getValue(ProductModel::class.java)
                if (product != null) {
                    callback(true, "Product fetched", product)
                } else {
                    callback(false, "Product not found", null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }

    override fun getAllProducts(callback: (Boolean, String, List<ProductModel>) -> Unit) {
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = mutableListOf<ProductModel>()
                for (data in snapshot.children) {
                    data.getValue(ProductModel::class.java)?.let { products.add(it) }
                }
                callback(true, "Products fetched", products)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, emptyList())
            }
        })
    }

    override fun getAllProductsByCategory(
        category: String,
        callback: (Boolean, String, List<ProductModel>?) -> Unit
    ) {
        ref.orderByChild("category").equalTo(category)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val products = mutableListOf<ProductModel>()
                    for (data in snapshot.children) {
                        data.getValue(ProductModel::class.java)?.let { products.add(it) }
                    }
                    callback(true, "Products fetched", products)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, emptyList())
                }
            })
    }

    override fun getAvailableProducts(callback: (Boolean, String, List<ProductModel>) -> Unit) {
        ref.orderByChild("availability").equalTo(true)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val products = mutableListOf<ProductModel>()
                    for (data in snapshot.children) {
                        data.getValue(ProductModel::class.java)?.let { products.add(it) }
                    }
                    callback(true, "Available products fetched", products)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, emptyList())
                }
            })
    }

    override fun getAllProductsByUser(
        userId: String,
        callback: (Boolean, String, List<ProductModel>) -> Unit
    ) {
        ref.orderByChild("listedBy").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val products = mutableListOf<ProductModel>()
                    for (data in snapshot.children) {
                        data.getValue(ProductModel::class.java)?.let { products.add(it) }
                    }
                    callback(true, "User's products fetched", products)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, emptyList())
                }
            })
    }

    override fun updateAvailability(
        productId: String,
        available: Boolean,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(productId).child("availability").setValue(available).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Availability updated")
            } else {
                callback(false, "Failed to update availability")
            }
        }
    }
    private val cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to "dkviawapl",
            "api_key" to "518886281689279",
            "api_secret" to "_76fV2cVa2pDTxNSV1Dp9s-8ltQ"
        )
    )



    override fun uploadImage(
        context: Context,
        imageUri: Uri,
        callback: (String?) -> Unit
    ) {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
                var fileName = getFileNameFromUri(context, imageUri)

                fileName = fileName?.substringBeforeLast(".") ?: "uploaded_image"

                val response = cloudinary.uploader().upload(
                    inputStream, ObjectUtils.asMap(
                        "public_id", fileName,
                        "resource_type", "image"
                    )
                )

                var imageUrl = response["url"] as String?

                imageUrl = imageUrl?.replace("http://", "https://")

                Handler(Looper.getMainLooper()).post {
                    callback(imageUrl)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Handler(Looper.getMainLooper()).post {
                    callback(null)
                }
            }
        }
    }

    override fun getFileNameFromUri(
        context: Context,
        uri: Uri
    ): String? {
        var fileName: String? = null
        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName

    }


    override fun updateQuantity(
        productId: String,
        quantity: Int,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(productId).child("quantity").setValue(quantity).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Quantity updated")
            } else {
                callback(false, "Failed to update quantity")
            }
        }
    }
}