package com.example.chattingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception

private lateinit var auth: FirebaseAuth
private lateinit var databaseReference: DatabaseReference
private lateinit var btnSignup: Button
private lateinit var btnLogin: Button
private lateinit var etName:EditText
private lateinit var etEmail:EditText
private lateinit var etPassword:EditText
private lateinit var etConfirmPassword:EditText

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        btnSignup = findViewById(R.id.btnSignup)
        btnLogin = findViewById(R.id.btnLogin)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)

        auth = FirebaseAuth.getInstance()

        btnSignup.setOnClickListener{
          try {
              val userName = etName.text.toString()
              val email = etEmail.text.toString()
              val password = etPassword.text.toString()
              val confirmPassword = etConfirmPassword.text.toString()

              if(TextUtils.isEmpty(userName)){
                  Toast.makeText(applicationContext,"username is required",Toast.LENGTH_SHORT).show()
              }
              if(TextUtils.isEmpty(email)){
                  Toast.makeText(applicationContext,"email is required",Toast.LENGTH_SHORT).show()
              }
              if(TextUtils.isEmpty(password)){
                  Toast.makeText(applicationContext,"password is required",Toast.LENGTH_SHORT).show()
              }
              if(TextUtils.isEmpty(confirmPassword)){
                  Toast.makeText(applicationContext,"confirm password is required",Toast.LENGTH_SHORT).show()
              }
              if(password != confirmPassword){
                  Toast.makeText(applicationContext,"password does not match",Toast.LENGTH_SHORT).show()
              }
              registerUser(userName,email,password)
          }catch (e:Exception){
              Toast.makeText(applicationContext,"Some error occurred",Toast.LENGTH_SHORT).show()
          }
        }

    }

    private fun registerUser(userName: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    val userId: String = user!!.uid

                    databaseReference =
                        FirebaseDatabase.getInstance().getReference("Users").child(userId)

                    val hashMap: HashMap<String, String> = HashMap()

                    hashMap.put("userId", userId)
                    hashMap.put("userName", userName)
                    hashMap.put("profileImage", "")

                    databaseReference.setValue(hashMap).addOnCompleteListener(this) {
                        if (it.isSuccessful) {

//                            Intent
                            val intent = Intent(this@SignUpActivity, HomeActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
    }
}