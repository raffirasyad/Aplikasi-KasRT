package com.example.cobafirebase.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.example.cobafirebase.databinding.RvNewsBinding
import com.example.cobafirebase.models.News
import com.squareup.picasso.Picasso

class RvNewsAdapter(private val newsList: ArrayList<News>, private val onItemClicked: (News) -> Unit) : RecyclerView.Adapter<RvNewsAdapter.ViewHolder>() {

    class ViewHolder(val binding: RvNewsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RvNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = newsList[position]
        holder.apply {
            binding.apply {
                tvJudulItem.text = currentItem.title
                tvDesc.text = currentItem.description
                tvTglTerbit.text = currentItem.date

                Picasso.get().load(currentItem.imgUri).into(imgItem)

                rvContainer.setOnClickListener {
                    onItemClicked(currentItem)
                }

                rvContainer.setOnLongClickListener {
                    currentItem.id?.let { id ->
                        MaterialAlertDialogBuilder(holder.itemView.context)
                            .setTitle("Delete item permanently")
                            .setMessage("Are you sure you want to delete this item?")
                            .setPositiveButton("Yes") { _, _ ->
                                val firebaseRef = FirebaseDatabase.getInstance().getReference("news")
                                val storageRef = FirebaseStorage.getInstance().getReference("Images")
                                // storage
                                storageRef.child(id).delete()

                                // realtime database
                                firebaseRef.child(id).removeValue()
                                    .addOnSuccessListener {
                                        Toast.makeText(holder.itemView.context, "Item removed successfully", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener { error ->
                                        Toast.makeText(holder.itemView.context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                            .setNegativeButton("No") { _, _ ->
                                Toast.makeText(holder.itemView.context, "Canceled", Toast.LENGTH_SHORT).show()
                            }
                            .show()
                    }

                    return@setOnLongClickListener true
                }
            }
        }
    }
}
