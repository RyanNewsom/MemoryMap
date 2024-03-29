package kylefrisbie.com.memorymap.presentation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kylefrisbie.com.memorymap.R;
import kylefrisbie.com.memorymap.model.Memory;

/**
 * Created by Ryan on 11/24/2015.
 */
public class SearchListAdapter extends ArrayAdapter<Memory> {
    private LayoutInflater layoutInflater;
    List<Memory> mMemories;

    private Filter mFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            return ((Memory) resultValue).getTitle();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null) {
                ArrayList<Memory> suggestions = new ArrayList<Memory>();
                for (Memory memory : mMemories) {
                    // Note: change the "contains" to "startsWith" if you only want starting matches
                    if (memory.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(memory);
                    }
                }

                results.values = suggestions;
                results.count = suggestions.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            if (results != null && results.count > 0) {
                // we have filtered results
                addAll((ArrayList<Memory>) results.values);
            } else {
                // no filter, add entire original list back in
                addAll(mMemories);
            }
            notifyDataSetChanged();
        }
    };

    public SearchListAdapter(Context context, int textViewResourceId, List<Memory> memories) {
        super(context, textViewResourceId, memories);
        // copy all the memories into a master list
        if(memories == null) {
            memories = new ArrayList<>();
        }
            mMemories = new ArrayList<>(memories.size());
            mMemories.addAll(memories);
        layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.itemlistrow, null);
        }

        Memory memory = getItem(position);
        Calendar calendar = memory.getDate();
        TextView title = (TextView) view.findViewById(R.id.thetitle);
        title.setText(memory.getTitle());
        TextView date = (TextView) view.findViewById(R.id.date);
        String theDate = getFormattedTime(calendar);
        date.setText(theDate);

        return view;
    }

    private String getFormattedTime(Calendar date) {
        Date newDate = new Date(date.getTimeInMillis());
        DateFormat df = new SimpleDateFormat("MM/dd/yy");
        return df.format(newDate);
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }
}