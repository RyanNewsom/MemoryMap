package kylefrisbie.com.memorymap.presentation;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import kylefrisbie.com.memorymap.R;
import kylefrisbie.com.memorymap.model.Memory;

/**
 * Created by Ryan on 11/24/2015.
 */
public class SearchListAdapter extends ArrayAdapter<Memory> {
    public SearchListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public SearchListAdapter(Context context, int resource, List<Memory> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(v == null){
            LayoutInflater inflater;
            inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.itemlistrow, null);
        }

        Memory selectedMem = getItem(position);

        if(selectedMem != null) {
            TextView title = (TextView) v.findViewById(R.id.title);
            TextView date = (TextView) v.findViewById(R.id.date);


            if (title != null){
                title.setText(selectedMem.getTitle());
            }
            if(date != null){
                date.setText(selectedMem.getTitle());
            }
        }

        return v;
    }
}
