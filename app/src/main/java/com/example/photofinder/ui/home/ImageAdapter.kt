package com.example.photofinder.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.photofinder.R
import com.example.photofinder.data.models.Photo
import com.squareup.picasso.Picasso

class ImageAdapter(private val picasso: Picasso) : RecyclerView.Adapter<ImageViewHolder>(),
    ImageViewHolder.OnItemClickListener {

    private lateinit var onItemClickListener: OnItemClickListener
    private var imageList: ArrayList<Photo> = ArrayList()

    fun addImages(list: ArrayList<Photo>) {
        imageList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.cell_image,parent,false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (imageList.isNotEmpty())
            return imageList.size
        return 0
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        if (imageList.isNotEmpty()) {
            holder.setItem(imageList[position], picasso)
            holder.setOnItemClickListener(this)
        }
    }

    fun getCurrentList(): ArrayList<Photo> {
        return imageList
    }

    fun clearList(){
        imageList.clear()
    }

    interface OnItemClickListener{
        fun onItemClick(imageUrl: String, imageView: ImageView, position: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }

    override fun onItemClick(imageUrl: String, imageView: ImageView, position: Int) {
        if (::onItemClickListener.isInitialized)
            onItemClickListener.onItemClick(imageUrl, imageView, position)
    }
}