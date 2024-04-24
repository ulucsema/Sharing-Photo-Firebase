package com.semauluc.sharingphotofirebase.view

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class SharePhotoActivity : AppCompatActivity() {
    
    var secilenGorsel : Uri? = null
    var secilenBitmap : Bitmap? =null
    private lateinit var storage : FirebaseStorage
    private lateinit var auth : FirebaseAuth
    private lateinit var databse : FirebaseFireStore
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_share_photo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
    }
    fun paylas(view: View) {
       //depo işlemleri
        //UUID ->universal uniqe id
        val UUID = UUID.randomUUID()
        val gorselIsmi = "${UUID}"

        val reference = storage.reference
        val gorselReference = reference.child("images").child(gorselIsmi)
          if(secilenGorsel != null){
              gorselReference.putFile(secilenGorsel).addOnCompleteListener({taskSnapshot ->
                  val yuklenenGorselReference = FirebaseStorage.getInstance().reference.child("images").child(gorselIsmi)
                  yuklenenGorselReference.downloadUrl.addOnSuccessListener { uri ->
                      val downloadUrl = uri.toString()
                      val guncelKullaniciEmaili = auth.currentUser!!.email.toString()
                      val kullaniciYorumu = yorumText.text.toStrin()
                      val tarih = Timestamp.now()

                      // veritabanı işlemleri

                      val postHashMap = hashMapOf<String, Any>()
                      postHashMap.put("gorselurl",downloadUrl)
                      postHashMap.put("kullaniciemail",guncelKullaniciEmaili)
                      postHashMap.put("kullaniciyorum",kullaniciYorumu)
                      postHashMap.put("tarih",tarih)
                      databse.collection("Post").add(postHashMap).addOnCompleteListener{task ->
                          if(task.isSuccesful) {
                              finish()
                          }
                      }.addOnFailureListener{ exception ->
                          Toast.makeText(application.Context, exception.localizedMessage, Toast.LENGTH_LONG).show()
                      }.addOnFailureListener { exception ->
                          Toast.makeText(application.Context, exception.localizedMessage, Toast.LENGTH_LONG).show()}

                  }
              })
          }
    }
    fun gorselSec(view: View) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STOAGE) != PackageManager.PERMISION_GANTED) {
            //İzin alınmadı
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }else {
            val galeriIntent(Intent.ACTION_PICK,Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriIntent,2)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode ==1) {
            if (grantResults.size >0 && grantResults[0] == packageManager.PERMISSION){
                //izin verilince yapılacaklar
                val galeriIntent =Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent,2)
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            secilenGorsel = data.data
            if (secilenGorsel != null) {
                if (Build.VERSION.SDK_INT >= 28) {
                    val source == ImageDecoder.createSource(this.contentResolver, secilenGorsel)
                    secilenBitmap = ImageDecoder.decodeBitmap(source)
                    imageView.setImageBitmap(secilenBitmap)
                } else {
                    secilenBitmap =
                        MediaStore.Images.Media.getBitmap(this.contentResolver, secilenBitmap)

                }
            }

            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}