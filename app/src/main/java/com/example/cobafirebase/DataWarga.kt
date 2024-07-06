package com.example.cobafirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cobafirebase.adapter.RvContactsAdapter
import com.example.cobafirebase.databinding.FragmentHomeBinding
import com.example.cobafirebase.models.Contacts
import com.google.firebase.database.*

class DataWarga : AppCompatActivity() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var contactsList: ArrayList<Contacts>
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var userRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this@DataWarga, TambahWarga::class.java)
            startActivity(intent)
        }

        firebaseRef = FirebaseDatabase.getInstance().getReference("contacts")
        contactsList = arrayListOf()
        userRef = FirebaseDatabase.getInstance().getReference("kas")

        fetchData()

        binding.rvContacts.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@DataWarga)
        }
    }

    private fun fetchData() {
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                contactsList.clear()
                if (snapshot.exists()) {
                    for (contactSnap in snapshot.children) {
                        val contacts = contactSnap.getValue(Contacts::class.java)
                        contacts?.let {
                            contactsList.add(it)
                        }
                    }
                }

                val rvAdapter = RvContactsAdapter(contactsList) { selectedContact ->
                    val intent = Intent(this@DataWarga, ProfilWarga::class.java).apply {
                        putExtra("selected_contact", selectedContact)

                    }
                    startActivity(intent)
                }
                // Set adapter to RecyclerView
                binding.rvContacts.adapter = rvAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DataWarga, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
