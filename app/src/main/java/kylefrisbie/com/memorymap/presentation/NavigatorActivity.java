package kylefrisbie.com.memorymap.presentation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import kylefrisbie.com.memorymap.R;

public class NavigatorActivity extends AppCompatActivity {

    private boolean mTakeToTutorial;
    private SharedPreferences mPrefs;
    public static String PREFS_NAME = "MEMORYPREFS";
    public static String TAKE_TO_TUTORIAL = "TUTORIAL DONE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator);
        mPrefs = getSharedPreferences(PREFS_NAME, 0);
        mTakeToTutorial = mPrefs.getBoolean(TAKE_TO_TUTORIAL, true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mTakeToTutorial){
            Intent intent = new Intent(this, TabbedActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mPrefs.edit().putBoolean(TAKE_TO_TUTORIAL, false).apply();

            startActivity(intent);
            finish();
        } else{
            Intent intent = new Intent(this, MapActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}
