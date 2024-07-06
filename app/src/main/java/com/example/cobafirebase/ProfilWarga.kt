package com.example.cobafirebase

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cobafirebase.databinding.ActivityProfilWargaBinding
import com.example.cobafirebase.models.Contacts
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class ProfilWarga : AppCompatActivity() {
    private lateinit var binding: ActivityProfilWargaBinding
    private lateinit var firebaseRef: DatabaseReference
    private val TAG = "ProfilWarga"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilWargaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val contact = intent.getParcelableExtra<Contacts>("selected_contact")
        val userId = contact?.id

        firebaseRef = FirebaseDatabase.getInstance().getReference("kas")

        userId?.let { uid ->
            firebaseRef.orderByChild("userId").equalTo(uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var totalKas = 0.0
                        for (kasSnap in snapshot.children) {
                            val kasAmount = kasSnap.child("amount").getValue(Double::class.java) ?: 0.0
                            totalKas += kasAmount
                        }
                        binding.apply {
                            fullName.text = contact.name
                            nama.text = contact.name
                            email.text = contact.email
                            email2.text = contact.email
                            nomor.text = contact.phoneNumber
                            alamat.text = contact.alamat
                            jabatan.text = contact.jabatan
                            kasWarga.text = totalKas.toString()
                            Picasso.get().load(contact.imgUri).into(binding.imgProfile)

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "Gagal memuat jumlah kas: ${error.message}")
                        Toast.makeText(
                            this@ProfilWarga,
                            "Gagal memuat jumlah kas: ${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        } ?: run {
            Toast.makeText(this, "Gagal memuat informasi warga", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
