package samuelvaldovinos.example.nasanews

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListAdapter
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL

val RSS_FEED_SOURCE = "https://www.nasa.gov/content/nasa-rss-feeds"

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    val nasa_breaking_news_rss_link = "https://www.nasa.gov/rss/dyn/breaking_news.rss"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val playButton: Button = findViewById(R.id.button_play_video)

        playButton.setOnClickListener(){
            val intent = Intent(this, NasaVideoActivity::class.java)
            startActivity(intent)
        }

        val downloader = Downloader(this, findViewById(R.id.listView_news))
        downloader.execute(nasa_breaking_news_rss_link)
    }

    companion object{
        class Downloader(val context: Context, val listView: ListView): AsyncTask<String, Void, String>() {
            val TAG = "Downloader"
            override fun doInBackground(vararg url: String): String {
                Log.d(TAG, "doInBackground called with: \b ${url[0]}")
                return URL(url[0]).readBytes().toString(Charsets.UTF_8).removePrefix("\uFEFF")
            }

            override fun onPostExecute(xml: String) {
                Log.d(TAG, "onPostExecute called with: \b $xml ")

                val nasaNewsXmlPullParser = XmlPullParser()
                nasaNewsXmlPullParser.parseXml(xml)

                val adapter = NasaNewsAdapter(context, R.layout.custom_news_layout, nasaNewsXmlPullParser.newsList)
                listView.adapter = adapter
            }
        }
    }

}
