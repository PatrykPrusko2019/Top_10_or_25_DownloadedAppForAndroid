package com.example.top_10_or_25_downloadedappforandroid;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String STATE_CURRENT_URL = "state current url";
    private static final String STATE_CURRENT_LIMIT_OF_RECORDS = "10";
    private ListView listApps;
    private String urlRSSFeed = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml";
    private String tempUrl = "";
    private int limitOfRecords = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState); // restore 2 states (url and record limit) when screen position changes
        }

        setContentView(R.layout.activity_main);
        this.listApps = (ListView) findViewById(R.id.listXML);

            downloadUrl();
    }

    /**
     * check if the current url is different from the temporary url
     * @return
     */
    private boolean checkToUrlRSSFeedAndLimitOfRecords() {
        if ( ! this.urlRSSFeed.equals(this.tempUrl) ) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * saves 2 states while deleting an activity
     * @param outState
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(STATE_CURRENT_URL, this.urlRSSFeed);
        outState.putInt(STATE_CURRENT_LIMIT_OF_RECORDS, this.limitOfRecords);
        super.onSaveInstanceState(outState);
    }

    /**
     * after removing
     * given activity, restores 2 states of objects
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.urlRSSFeed = savedInstanceState.getString(STATE_CURRENT_URL);
        this.limitOfRecords = savedInstanceState.getInt(STATE_CURRENT_LIMIT_OF_RECORDS);
    }

    /**
     * adds a new menu option and sets the current limit of displayed applications / songs
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.feeds_option_menu, menu);

        MenuItem item10 = menu.findItem(R.id.limitTop10Apps);
        MenuItem item25 = menu.findItem(R.id.limitTop25Apps);

        if( this.limitOfRecords == 25 ) { // set to the current limit
            item25.setChecked(true);
        } else {
            item10.setChecked(true);
        }
        return true;
    }
    /**
     * the ability to display songs, paid, free applications and
     * the choice to display 10 or 25 app / songs
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        this.tempUrl = this.urlRSSFeed;

        switch (item.getItemId()) {
            case R.id.top10FreeApps :
                this.urlRSSFeed = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=" + this.limitOfRecords + "/xml";
                break;

            case R.id.topPaidApps :
                this.urlRSSFeed = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=" + this.limitOfRecords + "/xml";
                break;

            case R.id.topSongs :
                this.urlRSSFeed = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=" + this.limitOfRecords + "/xml";
                break;

            case R.id.limitTop10Apps :

                this.limitOfRecords = 10;
                this.urlRSSFeed = this.urlRSSFeed.replaceFirst("25", "10" ); // replaceFirst -> convert 25 to 10
                item.setChecked(true);
                break;

            case R.id.limitTop25Apps :

                this.limitOfRecords = 25;
                this.urlRSSFeed = this.urlRSSFeed.replaceFirst("10", "25" ); // replaceFirst -> convert 10 to 25
                item.setChecked(true);
                break;
        }

        if( checkToUrlRSSFeedAndLimitOfRecords() ) { // not to download the same url again
            downloadUrl();
        }

        return super.onOptionsItemSelected(item);
    }

    void downloadUrl() {
           Log.d(TAG, "downloadUrl : starting AsyncTask ");
           DownloadData downloadData = new DownloadData();
           downloadData.execute(urlRSSFeed);
           Log.d(TAG, "downloadUrl : done");
    }


    /**
     * a class using the AsyncTask class to be able to connect to a URL
     */
    private class DownloadData extends AsyncTask<String, Void, String> {

        public static final String TAG = "DownloadData";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

                Log.d(TAG, "onPostExecute: parameter is -> " + s);
                ParseApplication parseApplication = new ParseApplication();
                parseApplication.parse(s);

                FeedAdapter feedAdapter = new FeedAdapter(MainActivity.this, R.layout.record_feed, parseApplication.getListEntryFeeds() );
                listApps.setAdapter(feedAdapter);
        }

        @Override
        protected String doInBackground(String... strings) {

            Log.d(TAG, "doInBackground : starts with -> " + strings[0]);
            String rssFeed = downloadXML(strings[0]);
            if(rssFeed == null) {
                Log.d(TAG, "doInBackground : Error downloading !!!");
            }

            return rssFeed;
        }



        private String downloadXML(String currentUrl) {

            StringBuilder xmlResult = new StringBuilder();

            try {
                URL url = new URL(currentUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                int resultConnect = urlConnection.getResponseCode();

                Log.d(TAG, "downloadXML: result connection -> " + resultConnect);

                BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( urlConnection.getInputStream() )); // InputStream -> InputStreamReader -> BufferedReader

                int charsRead;
                char[] inputBuffer = new char[500];

                while(true) {
                    charsRead = bufferedReader.read(inputBuffer);
                    if (charsRead > 0) {
                        xmlResult.append(String.copyValueOf( inputBuffer, 0, charsRead ));
                    }
                    if (charsRead < 0) {
                        break;
                    }
                }

                bufferedReader.close();
                return xmlResult.toString();


            } catch(MalformedURLException e){
                Log.d(TAG, " -> MalformedURLException -> wrong address URL " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG , " -> IOException -> openConnection is error "  + e.getMessage());
            } catch (SecurityException e) {
                Log.d(TAG, " -> SecurityException -> needs permission " + e.getMessage());
            }

            return null;
        }


    }
}