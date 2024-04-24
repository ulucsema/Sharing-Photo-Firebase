package com.semauluc.sharingphotofirebase.adapter

import android.os.Build.VERSION_CODES.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class HaberRecyclerAdapter (val postList : ArrayList<Post>): RecyclerView.Adapter<HaberRecyclerAdapter.PostHolder>(){
    class PostHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
          val inflater = LayoutInflater.from(parent.context)
          val view = inflater.inflate(R.layout.recycler_row,parent, false)
          return  PostHolder(view)
    }

    override fun getItemCount(): Int {
      return postList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
       holder.itemView.recycler_row_kullanici_email.text = postList[position].kullainiEmail
        holder.itemView.recycler_row_yorum_text.text = postList[position].kullainiYorum
        holder.itemView.recycler_row_kullanici_email.text = postList[position].kullainiEmail

          Picasso.get().load(postList[position].gorselUrl).into(holder.itemView.recycler_row_imageview)
    }
}