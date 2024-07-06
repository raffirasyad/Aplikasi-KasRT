package com.example.cobafirebase

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cobafirebase.databinding.ActivityTambahNewsBinding
import com.example.cobafirebase.models.News
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class TambahNews : AppCompatActivity() {
    private lateinit var binding: ActivityTambahNewsBinding
    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAddImage.setOnClickListener {
            selectImage()
        }

        binding.btnSubmit.setOnClickListener {
            uploadNews()
        }
    }

    private fun selectImage() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                imageUri = it
                binding.ivImage.setImageURI(it)
            }
        }
    }

    private fun uploadNews() {
        val title = binding.etTitle.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val date = binding.etDate.text.toString().trim()

        if (title.isEmpty() || description.isEmpty() || date.isEmpty() || !::imageUri.isInitialized) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show()
            return
        }

        val storageRef = FirebaseStorage.getInstance().reference.child("Images/${UUID.randomUUID()}")
        storageRef.putFile(imageUri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                saveNewsToDatabase(title, description, date, uri.toString())
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Image upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveNewsToDatabase(title: String, description: String, date: String, imgUri: String) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("news")
        val newsId = databaseRef.push().key!!
        val news = News(newsId, title, description, date, imgUri)

        databaseRef.child(newsId).setValue(news).addOnSuccessListener {
            Toast.makeText(this, "News added successfully", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to add news: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
