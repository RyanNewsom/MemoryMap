package kylefrisbie.com.memorymap.model;

import android.net.Uri;

import com.orm.SugarRecord;

import java.net.URI;
import java.util.Calendar;

public class Memory extends SugarRecord<Memory> {
    private String title;
    private Calendar date;
    private String people;
    private String photoURI;
    private String description;
    private double latitude;
    private double longitude;
    private String placeName;

    public Memory() {}

    public Memory(String title, Calendar date, String people, String photoURI, String description, double latitude, double longitude, String placeName) {
        this.title = title;
        this.date = date;
        this.people = people;
        this.photoURI = photoURI;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeName = placeName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getPhotoURI() {
        return photoURI;
    }

    public void setPhotoURI(String photoURI) {
        this.photoURI = photoURI;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}