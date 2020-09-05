package samuelvaldovinos.example.nasanews

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.lang.Exception

class XmlPullParser {
    private val TAG = "XmlPullParser"

    val newsList = mutableListOf<NasaNew>()


    fun parseXml(xml: String){
        Log.d(TAG, "parseXml called")

        var currentNew:NasaNew = NasaNew()
        var isItem = false
        var text: String = ""

        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()

            xpp.setInput(xml.reader())
            var eventType = xpp.eventType

            while (eventType != XmlPullParser.END_DOCUMENT){
                when(eventType){
                    XmlPullParser.START_TAG -> {
                        Log.d(TAG, "Starting tag of ${xpp.name}")
                        if (xpp.name == "item"){
                            isItem = true
                        }
                    }

                    XmlPullParser.TEXT -> {
                        text = xpp.text
                    }

                    XmlPullParser.END_TAG -> {
                        Log.d(TAG, "Ending tag of ${xpp.name}")
                        if (isItem){
                            when(xpp.name.toLowerCase()){
                                "item" -> {
                                    isItem = false
                                    newsList.add(currentNew)
                                    currentNew = NasaNew()
                                }
                                "title" -> currentNew.title = text
                                "link" -> currentNew.link = text
                                "description" -> currentNew.description = text
                                "pubdate" -> currentNew.publicationDate = text
                            }
                        }
                    }
                }
                eventType = xpp.next()
            }

            Log.d(TAG, "parseXml: End of the document reached")
            Log.d(TAG, """
                There are ${newsList.size} news.
                The first news is this:
                ${newsList[0].toString()}
                """.trimIndent())

        } catch (e: Exception){
            Log.e(TAG, "parseXml: Error parsing the xml feed")
        }
    }
}