package com.example.cobafirebase

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cobafirebase.databinding.ActivityKasWargaBinding
import com.example.cobafirebase.models.Kas
import com.example.cobafirebase.models.Contacts
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class KasWarga : AppCompatActivity() {

    private lateinit var binding: ActivityKasWargaBinding
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var userRef: DatabaseReference
    private var userList: List<Contacts> = listOf()
    private var userNames: List<String> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKasWargaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseRef = FirebaseDatabase.getInstance().getReference("kas")
        userRef = FirebaseDatabase.getInstance().getReference("contacts")

        loadUserNames()

        binding.btnSend.setOnClickListener {
            saveKasData()
        }
    }

    private fun loadUserNames() {
        userRef.get().addOnSuccessListener { snapshot ->
            userList = snapshot.children.mapNotNull { it.getValue(Contacts::class.java) }
            userNames = userList.mapNotNull { it.name }

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, userNames)
            binding.spinnerName.adapter = adapter
        }
    }

    private fun saveKasData() {
        val selectedUserIndex = binding.spinnerName.selectedItemPosition
        if (selectedUserIndex < 0) {
            Toast.makeText(this, "Pilih warga", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedUser = userList[selectedUserIndex]
        val amount = binding.edtJumlah.text.toString().toDoubleOrNull()
        if (amount == null || amount <= 0) {
            Toast.makeText(this, "Masukkan jumlah kas yang valid", Toast.LENGTH_SHORT).show()
            return
        }

        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val kas = Kas(
            userId = selectedUser.id,
            amount = amount,
            date = currentDate
        )

        val kasId = firebaseRef.push().key ?: return
        firebaseRef.child(kasId).setValue(kas)
            .addOnCompleteListener {
                Toast.makeText(this, "Data kas berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ProfilWarga::class.java)
                intent.putExtra("selected_contact", selectedUser)
                intent.putExtra("kasId", kasId) // Pass kasId to ProfilWarga
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Gagal menambahkan data kas: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }

}
