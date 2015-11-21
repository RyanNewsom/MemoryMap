package kylefrisbie.com.memorymap;

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

    public Memory() {}

    
}