package com.example.cobafirebase.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cobafirebase.adapter.RvKasAdapter
import com.example.cobafirebase.databinding.FragmentNotifBinding
import com.example.cobafirebase.models.Kas
import com.google.firebase.database.*

class NotifFragment : Fragment() {

    private lateinit var binding: FragmentNotifBinding
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var kasList: MutableList<Kas>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotifBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi DatabaseReference untuk kas
        firebaseRef = FirebaseDatabase.getInstance().getReference("kas")
        kasList = mutableListOf()

        // Setup RecyclerView
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rvNotifications.layoutManager = LinearLayoutManager(requireContext())
        val adapter = RvKasAdapter(kasList)
        binding.rvNotifications.adapter = adapter

        // Fetch data from Firebase
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                kasList.clear()
                for (postSnapshot in snapshot.children) {
                    val kas = postSnapshot.getValue(Kas::class.java)
                    kas?.let {
                        kasList.add(it)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}
