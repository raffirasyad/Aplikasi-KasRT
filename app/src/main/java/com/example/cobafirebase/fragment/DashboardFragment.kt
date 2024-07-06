package com.example.cobafirebase.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cobafirebase.DetailNews
import com.example.cobafirebase.ProfilWarga
import com.example.cobafirebase.TambahNews
import com.example.cobafirebase.adapter.RvNewsAdapter
import com.example.cobafirebase.databinding.FragmentDashboardBinding
import com.example.cobafirebase.models.News
import com.google.firebase.database.*

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var newsList: ArrayList<News>
    private lateinit var firebaseRef: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAdd.setOnClickListener {
            val intent = Intent(activity, TambahNews::class.java)
            startActivity(intent)
        }

        firebaseRef = FirebaseDatabase.getInstance().getReference("news")
        newsList = arrayListOf()

        fetchData()

        binding.rvNews.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun fetchData() {
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                newsList.clear()
                if (snapshot.exists()) {
                    for (contactSnap in snapshot.children) {
                        val news = contactSnap.getValue(News::class.java)
                        news?.let {
                            newsList.add(it)
                        }
                    }
                }
                val rvAdapter = RvNewsAdapter(newsList) { selectedNews ->
                    val intent = Intent(activity, DetailNews::class.java).apply {
                        putExtra("selected_news", selectedNews)
                    }
                    startActivity(intent)
                }
                // Set adapter to RecyclerView
                binding.rvNews.adapter = rvAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Error: $error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
