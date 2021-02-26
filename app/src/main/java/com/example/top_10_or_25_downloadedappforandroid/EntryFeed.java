package com.example.top_10_or_25_downloadedappforandroid;

/**
 * class that creates new RSS feeds
 */
public class EntryFeed {

    private String name;
    private String artist;
    private String summary;
    private String releaseDate;
    private String image;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = changeOfDate(releaseDate);
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // changes the date and time format
    private String changeOfDate(String releaseDate) {
        return "Date: " + releaseDate.replaceFirst("T", "\nTime: ");
    }

    @Override
    public String toString() {
        return  "name = " + this.name + "\n" +
                "artist = " + this.artist + "\n" +
                "summary = \n " + this.summary + "\n" +
                "releaseDate = " + this.releaseDate + "\n" +
                "image='" + this.image + "\n";
    }
}
