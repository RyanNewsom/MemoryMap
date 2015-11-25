package kylefrisbie.com.memorymap.presentation;


import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

import kylefrisbie.com.memorymap.R;
import kylefrisbie.com.memorymap.controller.MemoryController;
import kylefrisbie.com.memorymap.model.Memory;

public class MemoryFragment extends Fragment {
    MemoryController mController;
    private long mMemoryID;
    private Memory mMemory;
    private EditText mMemoryTitle;
    private EditText mMemoryPlace;
    private CalendarView mMemoryDate;
    private EditText mPeopleList;
    private EditText mMemoryDescription;
    private Button mSaveButton;
    private Button mCancelButton;
    private Location mMemoryLocation;

    private void populateMemory() {
        mMemoryTitle.setText(mMemory.getTitle());
        mMemoryPlace.setText(mMemory.getPlaceName());
        mMemoryDate.setDate(mMemory.getDate().getTimeInMillis());
        mPeopleList.setText(mMemory.getPeople().toString());
        mMemoryDescription.setText(mMemory.getDescription());
    }

    private void linkUpViewItems() {
        mMemoryTitle = (EditText) getView().findViewById(R.id.titleEditText);
        mMemoryPlace = (EditText) getView().findViewById(R.id.placeEditText);
        mMemoryDate = (CalendarView) getView().findViewById(R.id.calendarView);
        mPeopleList = (EditText) getView().findViewById(R.id.peopleEditText);
        mMemoryDescription = (EditText) getView().findViewById(R.id.memoryEditText);
        mSaveButton = (Button) getView().findViewById(R.id.saveButton);
        mCancelButton = (Button) getView().findViewById(R.id.cancelButton);
    }

    private Calendar generateMemoryDate() {
        return new Calendar() {
            @Override
            public void add(int field, int value) {

            }

            @Override
            protected void computeFields() {

            }

            @Override
            protected void computeTime() {

            }

            @Override
            public int getGreatestMinimum(int field) {
                return 0;
            }

            @Override
            public int getLeastMaximum(int field) {
                return 0;
            }

            @Override
            public int getMaximum(int field) {
                return 0;
            }

            @Override
            public int getMinimum(int field) {
                return 0;
            }

            @Override
            public void roll(int field, boolean increment) {

            }
        };
    }

    public void addListeners() {
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Memory memory = new Memory();
                memory.setTitle(mMemoryTitle.getText().toString());
                memory.setPlaceName(mMemoryPlace.getText().toString());
                Calendar date = generateMemoryDate();
                date.setTimeInMillis(mMemoryDate.getDate());
                memory.setDate(date);
                memory.setPeople(mPeopleList.getText().toString());
                memory.setDescription(mMemoryDescription.getText().toString());
                memory.setLatitude(mMemoryLocation.getLatitude());
                memory.setLongitude(mMemoryLocation.getLongitude());
                mController.createMemory(memory);

                // use interface to notify MapActivity
                mController.createMemory(memory);

                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    public MemoryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mController = MemoryController.getInstance(null);
        Bundle bundle = getArguments();

        mMemoryID = bundle.getLong(MapActivity.MEMORY_ID);
        mMemoryLocation = new Location("MemoryLocation");

        if (mMemoryID != -1) {
            mMemory = mController.findMemoryByID(mMemoryID);
            mMemoryLocation.setLatitude(mMemory.getLatitude());
            mMemoryLocation.setLongitude(mMemory.getLongitude());
        } else {
            double[] latlng = bundle.getDoubleArray(MapActivity.LATLNGARRAY);
            mMemoryLocation.setLatitude(latlng[0]);
            mMemoryLocation.setLongitude(latlng[1]);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_memory, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        linkUpViewItems();
        addListeners();

        if (mMemoryID != -1) {
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