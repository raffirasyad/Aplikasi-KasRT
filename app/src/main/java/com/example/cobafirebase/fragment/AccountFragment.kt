package com.example.cobafirebase.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.cobafirebase.LoginActivity
import com.example.cobafirebase.R
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountFragment : Fragment() {

    private var fullName: String? = null
    private var email: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout fragment_account
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        // Mengatur listener untuk tombol logout
        view.findViewById<Button>(R.id.btn_logout).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }

        // Menampilkan data user jika sudah ada
        displayUserData(fullName, email, view)

        return view
    }

    // Fungsi untuk menampilkan data user di fragment
    fun displayUserData(fullName: String?, email: String?, view: View? = null) {
        this.fullName = fullName
        this.email = email
        // Menampilkan data user jika fragment sudah dibuat
        val actualView = view ?: getView()
        actualView?.findViewById<TextView>(R.id.full_name)?.text = fullName
        actualView?.findViewById<TextView>(R.id.email)?.text = email
    }

    companion object {
        @JvmStatic
        fun newInstance() = AccountFragment()
    }
}