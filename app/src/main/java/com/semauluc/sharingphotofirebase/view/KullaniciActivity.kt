package com.semauluc.sharingphotofirebase.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.emailText
import kotlinx.android.synthetic.main.activity_main.passwordText

class MainActivity : AppCompatActivity() {

    private  lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()

        val guncelKullanici = auth.currentUser
        if (guncelKullanici !=null) {
            val intent = Intent(this, HaberlerActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    fun girisYap(view : View) {
       auth.signInWithEmailAndPassword(emailText.text.toString(), passwordText.text.toString()).addOnCompleteListener{task ->
           if(task.isSuccessful){
               val guncelKullanici = auth.currentUser?.email.toString()
               Toast.makeText(this, "Hoşgeldin: ${guncelKullanici}", Toast.LENGTH_LONG).show()
               val intent = Intent(this, HaberlerActivity::class.java)
               startActivity(intent)
               finish()
           }
       }.addOnFailureListener{
           Toast.makeText(this, exception.locolizedMessage, Toast.LENGTH_LONG).show()
       }
    }
    fun kayitOl(view : View) {
     var email = emailText.text.toString()
     var password = passwordText.text.toString()
        auth.createUserWithEmailAndPassword(email , password).addOnCompleteListener{task ->
            if (task.isSuccessful) {
                //diğer activiteye git
                val intent = Intent(this, HaberlerActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener {
            Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
}