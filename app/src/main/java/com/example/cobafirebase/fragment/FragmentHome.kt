package com.example.cobafirebase.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.cobafirebase.DataWarga
import com.example.cobafirebase.DetailNews
import com.example.cobafirebase.KasWarga
import com.example.cobafirebase.R
import com.example.cobafirebase.models.News
import java.util.Timer
import java.util.TimerTask

class FragmentHome : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var sliderAdapter: SliderAdapter
    private var timer: Timer? = null
    private var currentPage = 0
    private val DELAY_MS: Long = 2000 // Delay in milliseconds before the next page automatically slides.
    private val PERIOD_MS: Long = 4000 // Period between successive page slides.

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = view.findViewById(R.id.viewPager)
        sliderAdapter = SliderAdapter()
        viewPager.adapter = sliderAdapter

        val buttonDataWarga: CardView = view.findViewById(R.id.buttonDataWarga)
        buttonDataWarga.setOnClickListener {
            val intent = Intent(requireContext(), DataWarga::class.java)
            startActivity(intent)
        }

        val buttonPembayaran: CardView = view.findViewById(R.id.buttonPembayaran)
        buttonPembayaran.setOnClickListener {
            val intent = Intent(requireContext(), KasWarga::class.java)
            startActivity(intent)
        }

        val buttonInformasi: CardView = view.findViewById(R.id.buttonInformasi)
        buttonInformasi.setOnClickListener {
            val fragment = DashboardFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fr_container, fragment)
                .addToBackStack(null)  // Optional, untuk bisa kembali ke fragment sebelumnya
                .commit()
            // Ganti fragmen di bagian bawah
            replaceBottomMenuFragment(FragmentHome())
        }

        val buttonLaporan: CardView = view.findViewById(R.id.buttonLaporan)
        buttonLaporan.setOnClickListener {
            val fragment = NotifFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.rvNotifications, fragment)
                .addToBackStack(null)  // Optional, untuk bisa kembali ke fragment sebelumnya
                .commit()
        }


        // Start auto sliding.
        startAutoSlider()
    }

    private fun replaceBottomMenuFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.menu_fragment_container, fragment)
            .commit()
    }

    private fun startAutoSlider() {
        val handler = Handler()
        val update = Runnable {
            if (currentPage == sliderAdapter.itemCount) {
                currentPage = 0
            }
            viewPager.setCurrentItem(currentPage++, true)
        }

        // Start auto sliding.
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, DELAY_MS, PERIOD_MS)
    }

    private fun stopAutoSlider() {
        timer?.cancel()
        timer = null
    }

    inner class SliderAdapter : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

        private val images = intArrayOf(
            R.drawable.banner_welcome,
            R.drawable.gotong_royong2,
            R.drawable.agustusan
        )

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.home_fragment, parent, false)
            return SliderViewHolder(view)
        }

        override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
            val imageResId = images[position]
            holder.bind(imageResId)
            holder.itemView.setOnClickListener {
                // Handle click on image slider
                // You can fetch corresponding news data from Firebase and pass it to DetailNews activity
                val selectedNews = fetchNewsData(position) // Fetch the news data based on position
                selectedNews?.let {
                    val intent = Intent(holder.itemView.context, DetailNews::class.java).apply {
                        putExtra("selected_news", it)
                    }
                    holder.itemView.context.startActivity(intent)
                }
            }
        }

        override fun getItemCount(): Int {
            return images.size
        }

        inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val imageView1: ImageView = itemView.findViewById(R.id.sliderImage1)
            private val imageView2: ImageView = itemView.findViewById(R.id.sliderImage2)
            private val imageView3: ImageView = itemView.findViewById(R.id.sliderImage3)

            fun bind(imageResId: Int) {
                imageView1.setImageResource(imageResId)
                imageView2.setImageResource(imageResId)
                imageView3.setImageResource(imageResId)
            }
        }

        private fun fetchNewsData(position: Int): News? {
            // Replace this with your actual logic to fetch news data from Firebase or wherever you store it
            // For simplicity, returning hardcoded news data
            val newsList = arrayListOf(
                News("News 1 Title", "News 1 Description", "https://example.com/news1.jpg"),
                News("News 2 Title", "News 2 Description", "https://example.com/news2.jpg"),
                News("News 3 Title", "News 3 Description", "https://example.com/news3.jpg")
            )
            return if (position < newsList.size) newsList[position] else null
        }
    }

}
