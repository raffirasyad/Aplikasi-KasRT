package com.example.cobafirebase.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cobafirebase.databinding.RvKasBinding
import com.example.cobafirebase.models.Kas
import com.example.cobafirebase.models.Contacts
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.example.cobafirebase.R

class RvKasAdapter(private val kasList: List<Kas>) : RecyclerView.Adapter<RvKasAdapter.KasViewHolder>() {

    private val userRef = FirebaseDatabase.getInstance().getReference("contacts")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KasViewHolder {
        val binding = RvKasBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KasViewHolder(binding)
    }

    override fun onBindViewHolder(holder: KasViewHolder, position: Int) {
        val kas = kasList[position]
        holder.bind(kas, userRef)
    }

    override fun getItemCount() = kasList.size

    class KasViewHolder(private val binding: RvKasBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(kas: Kas, userRef: DatabaseReference) {
            binding.tvKas.text = kas.amount.toString()
            binding.tvTgl.text = kas.date

            kas.userId?.let {
                userRef.child(it).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val contact = snapshot.getValue(Contacts::class.java)
                        contact?.let {
                            binding.tvNama.text = it.name

                            // Load image from Firebase Storage using imgUri
                            val imgUri = it.imgUri
                            if (!imgUri.isNullOrEmpty()) {
                                val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imgUri)
                                storageRef.downloadUrl.addOnSuccessListener { uri ->
                                    Picasso.get().load(uri).into(binding.imgItem)
                                }.addOnFailureListener { e ->
                                    Log.e("RvKasAdapter", "Failed to load image", e)
                                    // Load placeholder image if fail to load
                                    Picasso.get().load(R.drawable.image_placeholder).into(binding.imgItem)
                                }
                            } else {
                                // Load placeholder image if imgUri is null or empty
                                Picasso.get().load(R.drawable.image_placeholder).into(binding.imgItem)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("RvKasAdapter", "Failed to fetch contact", error.toException())
                    }
                })
            }
        }
    }
}
