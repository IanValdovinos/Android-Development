package samuelvaldovinos.example.flickrbrowser

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_photo_details.*
import kotlinx.android.synthetic.main.content_photo_details.*

class PhotoDetailsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_details)

        super.activateToolbar(true)

        val photo = intent.getParcelableExtra(PHOTO_TRANSFER) as Photo

//        photo_title.text = photo.title
        photo_title.text = resources.getString(R.string.photo_title_text, photo.title)
//        photo_tags.text = photo.tags
        photo_tags.text = resources.getString(R.string.photo_tags_text, photo.tags)
//        photo_author.text = photo.author
        photo_author.text = resources.getString(R.string.photo_author_text, photo.author)
        
        Picasso.get().load(photo.image).into(photo_image)
    }
}
