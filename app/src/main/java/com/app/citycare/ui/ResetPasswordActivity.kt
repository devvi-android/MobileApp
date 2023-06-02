package com.app.citycare.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.citycare.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        val resetBtn = findViewById<MaterialButton>(R.id.resetPasswordWithEmailBtn)
        val resetField = findViewById<TextInputEditText>(R.id.resetPasswordEd)
        auth = FirebaseAuth.getInstance()

        resetBtn.setOnClickListener {
            val resetPassword = resetField.text.toString()

            auth.sendPasswordResetEmail(resetPassword)
                .addOnSuccessListener {
                    Toast.makeText(this, "Successfully submitted!", Toast.LENGTH_SHORT).show()

                }
                .addOnFailureListener {
                    Toast.makeText(this, "Sending Error!", Toast.LENGTH_SHORT).show()
                }
        }
    }
}