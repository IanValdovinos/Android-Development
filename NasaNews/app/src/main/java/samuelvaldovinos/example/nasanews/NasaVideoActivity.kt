package samuelvaldovinos.example.nasanews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView

class NasaVideoActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {
    val videoLink = "gg5Ncc9GODY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = layoutInflater.inflate(R.layout.activity_nasa_video, null) as ConstraintLayout
        setContentView(layout)

        val youTubePlayerView = YouTubePlayerView(this)
        youTubePlayerView.layoutParams = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        layout.addView(youTubePlayerView)

        youTubePlayerView.initialize(getString(R.string.api_key), this)

    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider?,
        youTubePlayer: YouTubePlayer?,
        restored: Boolean
    ) {

        if(!restored){
            youTubePlayer?.loadVideo(videoLink)
        } else{
            youTubePlayer?.play()
        }
    }

    override fun onInitializationFailure(
        provider: YouTubePlayer.Provider?,
        result: YouTubeInitializationResult?
    ) {
        val REQUEST_CODE = 0

        if (result?.isUserRecoverableError == true) {
            result.getErrorDialog(this, REQUEST_CODE).show()
        } else {
            val message = "There was an error in YoutubePlayer $result"
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }
}
