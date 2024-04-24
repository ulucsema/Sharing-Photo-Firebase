package com.semauluc.sharingphotofirebase.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.semauluc.sharingphotofirebase.adapter.HaberRecyclerAdapter
import com.semauluc.sharingphotofirebase.model.Post

class HaberlerActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseFirestore
    private  lateinit var recyclerViewAdapter : HaberRecyclerAdapter

    var postListesi = ArrayList<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_haberler)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        verileriAl()

        var layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerViewAdapter = HaberRecyclerAdapter(postListesi)
        recyclerView.adapter = recyclerViewAdapter
    }

    fun verileriAl() {
        database.collection("post").orderBy("tarih", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
            if (exception != null ) {
                Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_LONG).show()
            }else {
                if(snapshot != null) {
                    if(snapshot.isEmpty == false) { // = !snapshot.isEmpty
                       val documents = snapshot.documents

                        postListesi.clear()

                        for (document in documents) {
                           val kullaniciEmail = document.get("kullaniciemail") as String
                            val kullaniciYorumu = document.get("kullaniciyorum") as String
                            val gorselUrl = document.get("gorselurl") as String

                            val indirilenPost = Post(kullaniciEmail, kullaniciYorumu, gorselUrl)
                            postListesi.add(indirilenPost)
                        }
                        recyclerViewAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.secenekler_menu,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.fotograf_paylas) {
            //fotoğraf paylaşma aktivitesine gidilecek
            val intent = Intent(this, SharePhotoActivity::class.java)
            startActivity(intent)
        }else if (item.itemId == R.id.cikis_yap) {
            auth.signOut()
            val intent = Intent(this, KullaniciActivity::class.java)
            startActivity(intent)
            finish() //geri tuşuyla dönmemek için
        }
        return super.onOptionsItemSelected(item)
    }
    }