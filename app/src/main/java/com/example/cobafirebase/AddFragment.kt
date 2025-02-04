package com.example.cobafirebase

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.example.cobafirebase.databinding.FragmentAddBinding
import com.example.cobafirebase.databinding.FragmentHomeBinding
import com.example.cobafirebase.models.Contacts


class AddFragment : Fragment() {
    private var _binding : FragmentAddBinding? = null
    private  val binding get() = _binding!!

    private lateinit var firebaseRef : DatabaseReference
    private lateinit var storageRef : StorageReference

    private var uri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater , container, false)
        firebaseRef = FirebaseDatabase.getInstance().getReference("contacts")
        storageRef = FirebaseStorage.getInstance().getReference("Images")

        binding.btnSend.setOnClickListener {
            saveData()
        }

        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()){
            binding.imgAdd.setImageURI(it)
            if (it != null){
                uri = it
            }
        }

        binding.btnPickImage.setOnClickListener {
            pickImage.launch("image/*")
        }


        return binding.root
    }

    private fun saveData() {
        val name = binding.edtName.text.toString()
        val phone = binding.edtPhone.text.toString()
        val email = binding.edtEmail.text.toString()
        val alamat = binding.edtAlamat.text.toString()
        val jabatan = binding.edtJabatan.text.toString()

        if (name.isEmpty()) binding.edtName.error = "write a name"
        if (phone.isEmpty()) binding.edtPhone.error = "write a phone number"
        if (email.isEmpty()) binding.edtEmail.error = "write a email"
        if (alamat.isEmpty()) binding.edtAlamat.error = "write a address"
        if (jabatan.isEmpty()) binding.edtJabatan.error = "write a jabatan"


        val contactId = firebaseRef.push().key!!
        var contacts : Contacts

        uri?.let{
            storageRef.child(contactId).putFile(it)
                .addOnSuccessListener { task->
                    task.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener { url ->
                            Toast.makeText(context, " Image stored successfully",Toast.LENGTH_SHORT).show()
                            val imgUrl = url.toString()


                            contacts = Contacts(contactId,name , phone , email, alamat, jabatan, imgUrl)

                            firebaseRef.child(contactId).setValue(contacts)
                                .addOnCompleteListener{
                                    Toast.makeText(context, " data stored successfully",Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener{error ->
                                    Toast.makeText(context, "error ${error.message}",Toast.LENGTH_SHORT).show()
                                }


                        }
                }
        }


    }


}