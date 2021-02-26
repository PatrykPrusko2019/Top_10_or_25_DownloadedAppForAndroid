package com.example.top_10_or_25_downloadedappforandroid;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * the ParseApplication class using -> XmlPullParser is the interface that specifies
 * the ability to analyze connections in the API
 * Event Driven XMLPULL V1 Parser.
 */
public class ParseApplication {
    public static final String TAG = "ParseApplication";

    private ArrayList<EntryFeed> listEntryFeeds;

    public ParseApplication() {
        this.listEntryFeeds = new <EntryFeed>ArrayList();
    }

    public ArrayList<EntryFeed> getListEntryFeeds() {
        return listEntryFeeds;
    }

    public void setListEntryFeeds(ArrayList<EntryFeed> listEntryFeeds) {
        this.listEntryFeeds = listEntryFeeds;
    }

    /**
     * Th following event types are seen by next()
     * START_DOCUMENT
     *
     * START_TAG
     * An XML start tag was read.
     * TEXT
     * Text content was read; the text content can be retrieved using the getText() method. (when in validating mode next()
     * will not report ignorable whitespace, use nextToken() instead)
     * END_TAG
     * An end tag was read
     * END_DOCUMENT
     * @param record
     * @return
     */
    public boolean parse(String record) {

        boolean status = true;
        boolean isEntry = false;
        EntryFeed currentRecord = null;
        String currentText = "";

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);

            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new StringReader(record));

            int eventType = xpp.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT) {

                String tagName = xpp.getName();
                switch (eventType) {

                    case XmlPullParser.START_TAG :
                        if(tagName.equalsIgnoreCase("entry")) {
                            currentRecord = new EntryFeed();
                            isEntry = true;
                        }
                        break;

                    case XmlPullParser.TEXT :
                        currentText = xpp.getText(); //add text from the current tag
                        break;

                    case XmlPullParser.END_TAG :
                        if(isEntry) { // if isEntry is still true, then complete the rest of the name, artist, releaseDate, summary, image values
                            if ("entry".equalsIgnoreCase(tagName)) { //end of next record
                                isEntry = false; // change the control variable
                                this.listEntryFeeds.add(currentRecord); // new record adds
                            } else if ("name".equalsIgnoreCase(tagName)) {
                                currentRecord.setName(currentText);
                            } else if ("artist".equalsIgnoreCase(tagName)) {
                                currentRecord.setArtist(currentText);
                            } else if ("releaseDate".equalsIgnoreCase(tagName)) {
                                currentRecord.setReleaseDate(currentText);
                            } else if ("summary".equalsIgnoreCase(tagName)) {
                                currentRecord.setSummary(currentText);
                            } else if ("image".equalsIgnoreCase(tagName)) {
                                currentRecord.setImage(currentText);
                            }
                        }
                        break;
                }

                eventType = xpp.next(); // next tagName

            } //end document


        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            status = false;
        }


        // display 10 feeds from url on the console
//        for(EntryFeed entryFeed : listEntryFeeds) {
//            Log.d(TAG, "**********************");
//            Log.d(TAG, entryFeed.toString());
//        }

        return status;
    }



}
