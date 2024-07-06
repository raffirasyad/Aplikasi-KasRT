package com.example.cobafirebase

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.cobafirebase.fragment.AccountFragment
import com.example.cobafirebase.fragment.DashboardFragment
import com.example.cobafirebase.fragment.FragmentHome
import com.example.cobafirebase.fragment.NotifFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.example.cobafirebase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    // Deklarasi variabel
    lateinit var textFullName: TextView
    lateinit var textEmail: TextView
    lateinit var btnLogout: Button
    val firebaseAuth = FirebaseAuth.getInstance()
    var accountFragment: AccountFragment? = null

    private lateinit var binding : ActivityMainBinding



    private lateinit var rv_users: RecyclerView
//    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.addFragment))

        // Setup BottomNavigationView
        setupBottomNavigationView()

        // Mengecek apakah pengguna sudah login
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            // Menampilkan fragment HomeFragment jika sudah login
            loadFragment(FragmentHome())
            // Menampilkan data user di fragment AccountFragment
            accountFragment = AccountFragment.newInstance()
            accountFragment?.let {
                it.displayUserData(firebaseUser.displayName, firebaseUser.email)
            }
        } else {
            // Jika belum login, arahkan ke halaman login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }

    private fun setupBottomNavigationView() {
        val navigationView: BottomNavigationView = findViewById(R.id.nav_bottom)
        navigationView.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(@NonNull item: MenuItem): Boolean {
        var fragment: Fragment? = null
        when (item.itemId) {
            R.id.fr_home -> fragment = FragmentHome()
            R.id.fr_dashboard -> fragment = DashboardFragment()
            R.id.fr_notif -> fragment = NotifFragment()
            R.id.fr_account -> fragment = accountFragment ?: AccountFragment().also {
                accountFragment = it
            }
        }
        return loadFragment(fragment)
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fr_container, fragment, fragment.javaClass.simpleName)
                .commit()
            return true
        }
        return false
    }

    // Fungsi untuk menampilkan data user di fragment AccountFragment
    fun displayUserData(fullName: String?, email: String?) {
        accountFragment?.displayUserData(fullName, email)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_bottom, menu)
        return true
    }
}
