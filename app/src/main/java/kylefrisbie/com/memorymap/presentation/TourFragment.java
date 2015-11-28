package kylefrisbie.com.memorymap.presentation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kylefrisbie.com.memorymap.R;
import kylefrisbie.com.memorymap.model.Memory;

/**
 * A simple {@link Fragment} subclass.
 */
public class TourFragment extends Fragment {
    private int mLayoutid;

    private TourFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(int layoutId){
        TourFragment f = new TourFragment();
        f.mLayoutid = layoutId;

        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(mLayoutid, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
