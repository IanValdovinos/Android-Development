package samuelvaldovinos.example.flickrbrowser

import android.content.Intent
import android.location.Criteria
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.lang.Exception
import androidx.preference.PreferenceManager

private const val TAG = "MainActivity"

class MainActivity : BaseActivity(), GetRawData.OnDownloadComplete,
    RecyclerItemClickListener.OnRecyclerClickListener, GetFlickrJsonData.OnDataAvailable {

    private val JSONlink: String = "https://api.flickr.com/services/feeds/photos_public.gne?tags=android,oreo&format=json&nojsoncallback=1"

    private val flickrRecyclerViewAdapter = FlickrRecyclerViewAdapter(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        super.activateToolbar(false)

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addOnItemTouchListener(RecyclerItemClickListener(this, recycler_view, this))
        recycler_view.adapter = flickrRecyclerViewAdapter

        val url = createUrl("https://api.flickr.com/services/feeds/photos_public.gne", "android, oreo", "en-us", true)
        val getRawData = GetRawData(this)
        getRawData.execute(url)

        Log.d(TAG, "onCreate ends")
    }

    override fun OnItemClick(view: View, position: Int) {
        Log.d(TAG, ".onItemClick starts")
        Toast.makeText(this, "Normal tap at position $position", Toast.LENGTH_LONG).show()
    }

    override fun OnItemLongClick(view: View, position: Int) {
        Log.d(TAG, ".onItemLongClick starts")
//        Toast.makeText(this, "Long tap at position $position", Toast.LENGTH_SHORT).show()
        val photo = flickrRecyclerViewAdapter.getPhoto(position)
        photo?.let {
            val intent = Intent(this, PhotoDetailsActivity::class.java)
            intent.putExtra(PHOTO_TRANSFER, photo)
            startActivity(intent)
        }
    }

    private fun createUrl(baseUrl: String, searchCriteria: String, lang: String, matchAll: Boolean): String {
        Log.d(TAG, ".createUrl starts")
        var uri = Uri.parse(baseUrl)
        var builder = uri.buildUpon()
        builder = builder.appendQueryParameter("tags", searchCriteria)
        builder = builder.appendQueryParameter("tagmode", if (matchAll) "ALL" else "ANY")
        builder = builder.appendQueryParameter("lang", lang)
        builder = builder.appendQueryParameter("format", "json")
        builder = builder.appendQueryParameter("nojsoncallback", "1")

        uri = builder.build()
        Log.d(TAG, ".createUrl ends with $uri")
        return uri.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d(TAG, "onCreateOptionsMenu called")
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.d(TAG, "onOptionsItemSelected called")
        return when (item.itemId) {
            R.id.action_search -> {
                startActivity(Intent(this, SearchActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // This function is the callback function for the GetRawData class
    override fun onDownloadComplete(data: String, status: DownloadStatus){
        if(status == DownloadStatus.OK){
            // Download succeed
            Log.d(TAG, "onDownloadComplete called, the data is $data")
            val getFlickrJsonData = GetFlickrJsonData(this)
            getFlickrJsonData.execute(data)
        } else {
            // Download failed
            Log.d(TAG, "onDownloadComplete failed, the error is $data")
        }
    }

    // These functions are the callback functions that belong to the GetFlickrJsonData class
    override fun onDataAvailable(data: List<Photo>) {
        Log.d(TAG, ".onData Available: The data is $data")
        flickrRecyclerViewAdapter.loadNewData(data)
        Log.d(TAG, ".onDataAvailable ends")
    }

    override fun onError(exception: Exception) {
        Log.e(TAG, ".onError: The error is ${exception.message}")
    }

    override fun onResume() {
        super.onResume()

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val queryResult = sharedPref.getString(FLICKR_QUERY, "")

        if (queryResult != null && queryResult.isNotEmpty()) {
            val url = createUrl("https://api.flickr.com/services/feeds/photos_public.gne", queryResult,"en-us", true)
            val getRawData = GetRawData(this)
            getRawData.execute(url)
        }
    }
}
