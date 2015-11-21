package kylefrisbie.com.memorymap.Model;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kylel on 11/21/2015.
 */
public class Memory extends SugarRecord<Memory> {
    String title;
    Date date;
    ArrayList<String> people;
    String photoURI;
    String description;
    double[] location;

    public Memory() {}

    public Memory(String title, Date date, ArrayList<String> people,
                  String photoURI, String description, double[] location) {
        this.title = title;
        this.date = date;
        this.people = people;
        this.photoURI = photoURI;
        this.description = description;
        this.location = location;
    }
}