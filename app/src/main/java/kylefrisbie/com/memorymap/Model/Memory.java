package kylefrisbie.com.memorymap.model;

import android.location.Location;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Date;

public class Memory extends SugarRecord<Memory> {
    private String title;
    private Date date;
    private ArrayList<String> people;
    private String photoURI;
    private String description;
    private Location location;
    private String placeName;

    public Memory() {}

    public Memory(String title, Date date, ArrayList<String> people, String photoURI, String description, Location location, String placeName) {
        this.title = title;
        this.date = date;
        this.people = people;
        this.photoURI = photoURI;
        this.description = description;
        this.location = location;
        this.placeName = placeName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<String> getPeople() {
        return people;
    }

    public void setPeople(ArrayList<String> people) {
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }
}