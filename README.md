# Top 10 or 25 Downloaded App For Android


The app downloads Apple RSS Feeds, which displays:
1. top Free Apps
2. top Paid Apps
3. top Songs

An app that displays the top free apps, the top paid apps, the top songs. Choice of 10 or 25 items to display from applications or songs.
When changing the position of the phone, you can continue with the current information.
The application requires internet access to download the url data. It uses downloading data from the site
https://www.apple.com/rss/

Each feed record has 5 values:
- name
- artist
- release Date
- summary
- image

which it downloads from the given url.
The releaseDate variable is formatted to be better displayed.

Additionally, the application works when user selects the same choice again, the application does not download this data again.

When sharing photo data from a URL, you can use the Picasso library:

1.use code in the FeedAdapter class:

  Picasso.with (convertView.getContext ()). Load (record_feed.getImage ()). Into (tvImage);
2.in a comment give:

  LoadImage loadImage = new LoadImage (tvImage);

  loadImage.execute (record_feed.getImage ());
