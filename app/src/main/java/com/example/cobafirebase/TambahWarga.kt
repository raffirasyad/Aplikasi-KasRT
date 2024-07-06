package com.example.cobafirebase

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.example.cobafirebase.databinding.FragmentAddBinding
import com.example.cobafirebase.models.Contacts

class TambahWarga : AppCompatActivity() {
    private lateinit var binding: FragmentAddBinding
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var storageRef: StorageReference
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseRef = FirebaseDatabase.getInstance().getReference("contacts")
        storageRef = FirebaseStorage.getInstance().getReference("Images")

        binding.btnSend.setOnClickListener {
            saveData()
        }

        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { selectedUri ->
            selectedUri?.let {
                binding.imgAdd.setImageURI(it)
                uri = it
            }
        }

        binding.btnPickImage.setOnClickListener {
            pickImage.launch("image/*")
        }
    }

    private fun saveData() {
        val name = binding.edtName.text.toString()
        val phone = binding.edtPhone.text.toString()
        val email = binding.edtEmail.text.toString()
        val alamat = binding.edtAlamat.text.toString()
        val jabatan = binding.edtJabatan.text.toString()

        if (name.isEmpty()) {
            binding.edtName.error = "write a name"
            return
        }
        if (phone.isEmpty()) {
            binding.edtPhone.error = "write a phone number"
            return
        }
        if (email.isEmpty()) {
            binding.edtEmail.error = "write a email"
            return
        }
        if (alamat.isEmpty()) {
            binding.edtAlamat.error = "write a address"
            return
        }
        if (jabatan.isEmpty()) {
            binding.edtJabatan.error = "write a jabatan"
            return
        }

        val contactId = firebaseRef.push().key ?: ""
        uri?.let { selectedUri ->
            storageRef.child(contactId).putFile(selectedUri)
                .addOnSuccessListener { task ->
                    task.metadata?.reference?.downloadUrl
                        ?.addOnSuccessListener { url ->
                            val imgUrl = url.toString()

                            val contacts = Contacts(contactId, name, phone, email, alamat, jabatan, imgUrl)

                            firebaseRef.child(contactId).setValue(contacts)
                                .addOnCompleteListener {
                                    Toast.makeText(this@TambahWarga, "Data stored successfully", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { error ->
                                    Toast.makeText(this@TambahWarga, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                }
        } ?: run {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
        }
    }
}
