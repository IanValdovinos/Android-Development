package samuelvaldovinos.example.nasanews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.util.zip.Inflater

class NasaNewsAdapter(context: Context, val resource: Int, val news: List<NasaNew>): ArrayAdapter<NasaNew>(context, resource, news) {
    private val inflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view = inflater.inflate(resource, parent, false)

        val textViewTitle: TextView = view.findViewById(R.id.textView_title)
        val textViewPublicationDate: TextView = view.findViewById(R.id.textView_publicationDate)
        val textViewDescription: TextView = view.findViewById(R.id.textView_description)
        val textViewLink: TextView = view.findViewById(R.id.textView_link)

        val currentNews = news[position]

        textViewTitle.text = currentNews.title
        textViewPublicationDate.text = currentNews.publicationDate
        textViewDescription.text = currentNews.description
        textViewLink.text = currentNews.link

        return view
    }

    override fun getCount(): Int {
        return news.size
    }
}