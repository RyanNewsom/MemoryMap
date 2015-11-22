package kylefrisbie.com.memorymap.presentation;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import kylefrisbie.com.memorymap.controller.MemoryController;

/**
 * Created by Ryan on 11/21/2015.
 */
public class MemoryFragment extends Fragment {
    MemoryController mController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mController = MemoryController.getInstance(null);
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
