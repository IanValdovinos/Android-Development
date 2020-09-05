package learningprogramming.academy.teslanews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.util.zip.Inflater

class NewsAdapter(context: Context, private val view: Int, private val newsList: ArrayList<ItemFeed> ): ArrayAdapter<ItemFeed>(context, view) {
    val inflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = if(convertView == null){
            inflater.inflate(view, parent, false)
        } else {
            convertView
        }

        val title = view.findViewById<TextView>(R.id.tv_title)
        val date = view.findViewById<TextView>(R.id.tv_date)
        val description = view.findViewById<TextView>(R.id.tv_description)
        val link = view.findViewById<TextView>(R.id.tv_link)

        val new = newsList[position]

        title.text = new.title
        date.text = new.publicationDate
        description.text = new.description
        link.text = new.newsLink

        return view
    }

    override fun getCount(): Int {
        return newsList.size
    }
}