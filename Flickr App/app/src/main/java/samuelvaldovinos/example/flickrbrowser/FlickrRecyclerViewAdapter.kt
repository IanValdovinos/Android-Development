package samuelvaldovinos.example.flickrbrowser

import android.annotation.SuppressLint
import android.media.Image
import android.util.Log
import android.util.Log.w
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class FlickrImageViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
    val thumbnail: ImageView = view.findViewById(R.id.thumbnail)
    val title: TextView = view.findViewById(R.id.title)
}

class FlickrRecyclerViewAdapter(private var photoList: List<Photo>): RecyclerView.Adapter<FlickrImageViewHolder>() {
    private val TAG = "FlickrRecyclerViewAdapt"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlickrImageViewHolder {
        Log.d(TAG, ".onCreateViewHolder: New view requested")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.browse, parent, false)
        return FlickrImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d(TAG, ".getItemCount called")
        return if(photoList.isNotEmpty()) photoList.size else 1
    }

    fun loadNewData(photos: List<Photo>){
        photoList = photos
        notifyDataSetChanged()
    }

    fun getPhoto(position: Int): Photo? {
        return if(photoList.isNotEmpty()) photoList[position] else null
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: FlickrImageViewHolder, position: Int) {

        if (photoList.isEmpty()){
            holder.thumbnail.setImageResource(R.drawable.placeholder)
            holder.title.setText(R.string.no_photo_match)
            holder.title.setTextColor(R.color.primary_dark)
        } else {
            val photoItem = photoList[position]

            Picasso.get()
                .load(photoItem.image)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.thumbnail)

            holder.title.text = photoItem.title
        }
    }
}