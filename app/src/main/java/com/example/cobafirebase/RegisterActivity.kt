package com.example.cobafirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest

class RegisterActivity : AppCompatActivity() {
    private lateinit var editFullName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var editPasswordConf: EditText
    private lateinit var btnRegister: Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        editFullName = findViewById(R.id.full_name)
        editEmail = findViewById(R.id.email)
        editPassword = findViewById(R.id.password)
        editPasswordConf = findViewById(R.id.password_conf)
        btnRegister = findViewById(R.id.button_register)

        firebaseAuth = FirebaseAuth.getInstance()

        btnRegister.setOnClickListener {
            val fullName = editFullName.text.toString().trim()
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString().trim()
            val passwordConf = editPasswordConf.text.toString().trim()

            if (fullName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                if (password == passwordConf) {
                    // LAUNCH REGISTER
                    processRegister(fullName, email, password)
                } else {
                    showToast("Konfirmasi kata sandi harus sama")
                }
            } else {
                showToast("Silahkan isi dulu semua data")
            }
        }
    }

    private fun processRegister(fullName: String, email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val UserUpdateProfile = userProfileChangeRequest {
                        displayName = fullName
                    }

                    val user = task.result.user
                    user!!.updateProfile(UserUpdateProfile)
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                                finish()
                            } else {
                                showToast(task.exception?.localizedMessage ?: "Gagal mengupdate profil")
                            }
                        }
                } else {
                    showToast(task.exception?.localizedMessage ?: "Gagal mendaftar")
                }
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
    }
}
