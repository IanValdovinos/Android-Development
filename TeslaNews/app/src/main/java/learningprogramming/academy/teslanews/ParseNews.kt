package learningprogramming.academy.teslanews

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.lang.Exception

class ParseNews {
    val TAG = "ParseNews"
    val news = ArrayList<ItemFeed>()

    fun parse(xmlData: String): Boolean{
        Log.d(TAG, "parse() called with $xmlData")

        var status = true
        var inItem = false
        var textValue = ""

        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xmlPullParser = factory.newPullParser()
            xmlPullParser.setInput(xmlData.reader())
            var eventType = xmlPullParser.eventType
            var currentNew = ItemFeed()

            while(eventType != XmlPullParser.END_DOCUMENT){
                val tagName = xmlPullParser.name?.toLowerCase()
                when(eventType){
                    XmlPullParser.START_TAG -> {
                        Log.d(TAG, "parse: starting tag for $tagName")
                        if (tagName == "item"){
                            inItem = true
                        }
                    }

                    XmlPullParser.TEXT -> { textValue = xmlPullParser.text }

                    XmlPullParser.END_TAG -> {
                        Log.d(TAG, "parse: ending tag for $tagName")
                        if (inItem){
                            when(tagName){
                                "item" -> {
                                    news.add(currentNew)
                                    inItem = false
                                    currentNew = ItemFeed()
                                }

                                "title" -> currentNew.title = textValue
                                "description" -> currentNew.description = textValue
                                "link" -> currentNew.newsLink = textValue
                                "pubdate" -> currentNew.publicationDate = textValue
                            }
                        }
                    }
                }
                eventType = xmlPullParser.next()
            }

            for (new in news){
                Log.d(TAG, "********************")
                Log.d(TAG, new.toString())
            }

        } catch (e: Exception){
            Log.e(TAG, e.message)
            status = false
        }
        return status
    }
}