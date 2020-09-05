package learningprogramming.academy.teslanews

import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import java.net.URL
import kotlinx.android.synthetic.main.activity_main.*

private val CURRENT_URL = "CurrentUrl"
private var currentUrl = "https://carbuzz.com/rss/cars/tesla/model-x"


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private val modelXUrl: String = "https://carbuzz.com/rss/cars/tesla/model-x"
    private val cybertruckUlr: String = "https://carbuzz.com/rss/cars/tesla/cybertruck"
    private val roadsterUrl: String = "https://carbuzz.com/rss/cars/tesla/roadster"


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "MainActivity: onCreate() called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        downloadData(currentUrl)
    }

    private fun updateNewsTitle(){
        when(currentUrl){

            modelXUrl ->{
                newsTitlePage.text =  "Model X News"
                newsTitlePage.setTextColor(Color.DKGRAY)
            }
            cybertruckUlr ->{
                newsTitlePage.text =  "Cybertruck News"
                newsTitlePage.setTextColor(Color.BLUE)
            }
            roadsterUrl -> {
                newsTitlePage.text =  "Roadster News"
                newsTitlePage.setTextColor(Color.RED)
            }
            else -> ""
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        when(currentUrl){
            modelXUrl -> menu?.findItem(R.id.modelx_mnu)?.isChecked = true
            cybertruckUlr -> menu?.findItem(R.id.cybertruck_mnu)?.isChecked = true
            roadsterUrl -> menu?.findItem(R.id.roadster_mnu)?.isChecked = true
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        item.isChecked = true

        val newUrl = when(item.itemId){
            R.id.modelx_mnu -> modelXUrl
            R.id.cybertruck_mnu -> cybertruckUlr
            R.id.roadster_mnu -> roadsterUrl
            else -> ""
        }

        currentUrl = newUrl
        updateNewsTitle()
        downloadData(newUrl)
        return true
    }

    private fun downloadData(url: String){
        updateNewsTitle()
        val downloadData: DownloadData = DownloadData(this, listView_news)
        downloadData.execute(url)
    }

    companion object{
        class DownloadData(private val context: Context, private val listView: ListView): AsyncTask<String, Void, String>(){
            val TAG = "DownloadData"
            override fun onPostExecute(result: String) {
                Log.d(TAG, "DownloadData: onPostExecute() called")
                super.onPostExecute(result)
                val parseNews = ParseNews()
                parseNews.parse(result)

//                val arrayAdapter = ArrayAdapter(context, R.layout.news_textview, parseNews.news )
                val arrayAdapter = NewsAdapter(context, R.layout.news_layout, parseNews.news)
                listView.adapter = arrayAdapter
            }

            override fun doInBackground(vararg url: String): String {
                return URL(url[0]).readBytes().toString(Charsets.UTF_8).removePrefix("\uFEFF")
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(CURRENT_URL, currentUrl)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        currentUrl = savedInstanceState.getString(CURRENT_URL, "https://carbuzz.com/rss/cars/tesla/model-x")
    }
}
