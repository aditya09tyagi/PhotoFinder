package com.example.photofinder.ui.home

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.photofinder.R
import com.example.photofinder.data.models.Photo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cell_image.view.*
import timber.log.Timber

class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var onItemClickListener: OnItemClickListener

    companion object {
        const val IMAGE_BASE_URL = "https://farm"
        const val IMAGE_STATIC_URL = ".staticflickr.com/"
    }

    fun setItem(photo: Photo, picasso: Picasso) {
        val url = createImageUrl(photo.farm, photo.server, photo.id, photo.secret)
        picasso.load(url)
            .placeholder(R.drawable.progress_animation)
            .error(R.drawable.ic_error)
            .into(itemView.ivPicHolder)


        itemView.setOnClickListener {
            if (::onItemClickListener.isInitialized)
                onItemClickListener.onItemClick(url,itemView.ivPicHolder,adapterPosition)
        }
    }

    private fun createImageUrl(
        farmId: Int,
        serverId: String,
        id: String,
        secretId: String
    ): String {
        val url = "$IMAGE_BASE_URL" +
                "$farmId" +
                "$IMAGE_STATIC_URL" +
                "$serverId" +
                "/" +
                "$id" +
                "_" +
                "$secretId.jpg"
        return url
    }

    interface OnItemClickListener {
        fun onItemClick(imageUrl: String, imageView: ImageView, position: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }
}