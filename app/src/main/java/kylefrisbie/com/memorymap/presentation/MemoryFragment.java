package kylefrisbie.com.memorymap.presentation;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Date;

import kylefrisbie.com.memorymap.R;
import kylefrisbie.com.memorymap.controller.MemoryController;
import kylefrisbie.com.memorymap.model.Memory;

public class MemoryFragment extends Fragment {
    MemoryController mController;
    private Memory mMemory;
    private EditText memoryTitle;
    private DatePicker memoryDate;
    private EditText peopleList;
    private EditText memoryDescription;

    private void populateMemory() {
        memoryTitle.setText(mMemory.getTitle());
        Date date = mMemory.getDate();
        memoryDate.init(date.getYear(), date.getMonth(), date.getDay(), null);
        peopleList.setText(mMemory.getPeople().toString());
        memoryDescription.setText(mMemory.getDescription());
    }

    private void linkUpTextFields() {
        memoryTitle = (EditText) getView().findViewById(R.id.titleEditText);
        memoryDate = (DatePicker) getView().findViewById(R.id.datePicker);
        peopleList = (EditText) getView().findViewById(R.id.peopleEditText);
        memoryDescription = (EditText) getView().findViewById(R.id.memoryEditText);
    }

    public MemoryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mController = MemoryController.getInstance(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_memory, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        linkUpTextFields();

        long memoryID = savedInstanceState.getLong(MapActivity.MEMORY_ID);
        if (memoryID != -1) {
            mMemory = mController.findMemoryByID(memoryID);
            populateMemory();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}