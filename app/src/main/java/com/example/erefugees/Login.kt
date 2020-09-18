package com.example.erefugees

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        create.setOnClickListener {
            startActivity(Intent(this, CreateAccount::class.java))
            finish()
        }

        login.setOnClickListener {
               performLogin()
        }
    }

    private fun performLogin(){

        val email = email.text.toString()

        val password = password.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Enter all details", Toast.LENGTH_LONG).show()
            return
        }

        Log.d("Login","Attempt a login with email/pw:$email")

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {

                //if not susccessfully sigined in//
                if (!it.isSuccessful)
                    return@addOnCompleteListener

                // if suceessfully signed in//
                // else if successful//
                Toast.makeText(this,"Signed In", Toast.LENGTH_LONG).show()

                Log.d("Login","Successfully Signed In user with uid:${it.result?.user?.uid}")
                val intent = Intent(this,MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)


            }
            .addOnFailureListener {

                Log.d("Main","Failed to create user:${it.message}")
                Toast.makeText(this,"Failed to Sign In user:${it.message}",
                    Toast.LENGTH_LONG).show()

            }


    }
}
