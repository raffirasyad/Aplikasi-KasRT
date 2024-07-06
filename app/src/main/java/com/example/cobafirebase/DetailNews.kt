package com.example.cobafirebase

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cobafirebase.databinding.ActivityDetailNewsBinding
import com.example.cobafirebase.models.News
import com.squareup.picasso.Picasso

class DetailNews : AppCompatActivity() {
    private lateinit var binding: ActivityDetailNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val news = intent.getParcelableExtra<News>("selected_news")
        news?.let {
            binding.apply {
                title.text = it.title
                tvDesc.text = it.description
                Picasso.get().load(it.imgUri).into(img)
            }
        } ?: run {
            Toast.makeText(this, "Gagal memuat informasi berita", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
