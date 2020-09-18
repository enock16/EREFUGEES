package com.example.erefugees

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.erefugees.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create_account.*

class CreateAccount : AppCompatActivity() {
    var firstName:String ?= null
    var lastName:String ?= null
    var email:String ?= null
    var password :String ?= null
    var confirmPassword:String ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        login.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }

        create.setOnClickListener {
            performRegister()
        }


    }

    fun performRegister(){
       firstName = first.text.toString()

       lastName = last.text.toString()

       email = createEmail.text.toString()

       password = createPassword.text.toString()

       confirmPassword = passwordConfirm.text.toString()

        if(email!!.isEmpty() || password!!.isEmpty() || firstName!!.isEmpty() || lastName!!.isEmpty() || confirmPassword!!.isEmpty()){

            Toast.makeText(this,"Enter all details", Toast.LENGTH_LONG).show()

            return
        }

        if(password != confirmPassword){
            Toast.makeText(this,"Passwords Don't match", Toast.LENGTH_LONG).show()
        }

        // firebase authentication //

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener {

                if (!it.isSuccessful)
                    return@addOnCompleteListener

                Toast.makeText(this,"Successfully Registered ", Toast.LENGTH_LONG).show()

               saveUserData()

            }


            .addOnFailureListener {
                Toast.makeText(this,"Failed to create user:${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    fun saveUserData(){

    val uid = FirebaseAuth.getInstance().uid?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(
            uid,
            "$firstName $lastName",
            email!!,
            password!!
        )


        ref.setValue(user)

            .addOnSuccessListener {

               //  intent to the next activity

                             val intent = Intent(this, MainActivity::class.java)
                               //  intent.flags  helps to not to  go back to the register activity rather the phone screen
                               intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                               startActivity(intent)
            }
            .addOnFailureListener {
               Toast.makeText(this, "Faild to save User Data", Toast.LENGTH_LONG).show()


            }
    }


}
