package com.example.top_10_or_25_downloadedappforandroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class FeedAdapter extends ArrayAdapter {

    private final int layoutResource;
    private List<EntryFeed> applications; //application data list

    public FeedAdapter(Context context, int resource, List listOfReeds_Rss) {
        super(context, resource, listOfReeds_Rss);
        this.layoutResource = resource;
        this.applications = listOfReeds_Rss;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        EntryFeed record_feed = applications.get(position);

        if( convertView == null ) {
            convertView = LayoutInflater.from(getContext()).inflate(layoutResource, parent, false); // R.layout.record_feed -> layoutResource
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.name);
        TextView tvArtist = (TextView) convertView.findViewById(R.id.artist);
        TextView tvReleaseDate = (TextView) convertView.findViewById(R.id.releaseDate);
        TextView tvSummary = (TextView) convertView.findViewById(R.id.summary);
        ImageView tvImage = (ImageView) convertView.findViewById(R.id.imageView);

        tvName.setText(record_feed.getName());
        tvArtist.setText(record_feed.getArtist());
        tvSummary.setText(record_feed.getSummary());
        tvReleaseDate.setText(record_feed.getReleaseDate());

        LoadImage loadImage = new LoadImage(tvImage); // adds displaying of Images using class LoadImage
        loadImage.execute(record_feed.getImage()); // adds photos from url link

        // or use library Picasso for displaying of images

        // Picasso.with(convertView.getContext()).load(record_feed.getImage()).into(tvImage); // adds displaying of Images using Picasso Library


        return convertView;
    }

    /**
     * class sharing photos from url link
     */
    private class LoadImage extends AsyncTask<String, Void, Bitmap> {

        ImageView imageView;

        public LoadImage(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String urlLink = strings[0];
            Bitmap bitmap = null;
            try {

                URL url = new URL(urlLink);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                bitmap = BitmapFactory.decodeStream(connection.getInputStream());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            this.imageView.setImageBitmap(bitmap);
        }
    }

}