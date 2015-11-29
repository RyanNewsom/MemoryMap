package kylefrisbie.com.memorymap.presentation;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import kylefrisbie.com.memorymap.R;
import kylefrisbie.com.memorymap.controller.MemoryController;
import kylefrisbie.com.memorymap.model.Memory;

public class MemoryFragment extends Fragment {

    MemoryController mController;

    // request code values for intents
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int RESULT_OK = -1;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int GALLERY_ACTIVITY_REQUEST_CODE = 200;

    // member variables
    private Uri mPhotoUri;
    private long mMemoryID;
    private String mMemoryMarkerID;
    private Memory mMemory;
    private EditText mMemoryTitle;
    private EditText mMemoryPlace;
    private CalendarView mMemoryDate;
    private EditText mPeopleList;
    private EditText mMemoryDescription;
    private ImageButton mCameraButton;
    private ImageButton mGalleryButton;
    private Button mSaveButton;
    private Button mCancelButton;
    private Button mDeleteButton;
    private Location mMemoryLocation;
    private ImageView mMemoryImage;

    // populate the memory in the fragment view
    private void populateMemory() {
        mMemoryTitle.setText(mMemory.getTitle());
        mMemoryPlace.setText(mMemory.getPlaceName());
        mMemoryDate.setDate(mMemory.getDate().getTimeInMillis());
        mPeopleList.setText(mMemory.getPeople());
        mMemoryDescription.setText(mMemory.getDescription());
        if (mMemory.getPhotoURI() != null) {
            mPhotoUri = Uri.parse(mMemory.getPhotoURI());
            mMemoryImage.setImageURI(mPhotoUri);
            setImageView(mMemoryImage);
        }
    }

    // store changed values so they persist in the database
    public void populateMemoryOnSave() {
        mMemory.setTitle(mMemoryTitle.getText().toString());
        mMemory.setPlaceName(mMemoryPlace.getText().toString());
        Calendar date = generateCalendarObject();
        date.setTimeInMillis(mMemoryDate.getDate());
        mMemory.setDate(date);
        mMemory.setPeople(mPeopleList.getText().toString());
        mMemory.setDescription(mMemoryDescription.getText().toString());
        mMemory.setLatitude(mMemoryLocation.getLatitude());
        mMemory.setLongitude(mMemoryLocation.getLongitude());
        if (mPhotoUri != null) {
            if (mPhotoUri.toString().contains("content")) {
                mMemory.setPhotoURI("content://media" + mPhotoUri.getEncodedPath());
            } else {
                mMemory.setPhotoURI(mPhotoUri.getEncodedPath());
            }
        }
    }

    // add on click listeners to buttons and text fields
    public void addListeners() {
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create Intent to take a picture and return control to the calling application
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                mPhotoUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri); // set the image file name

                // start the image capture Intent
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        mGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, GALLERY_ACTIVITY_REQUEST_CODE);
            }
        });

        mMemoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                String filePath;
                if (mPhotoUri.toString().contains("content")) {
                    filePath = "content://media" + mPhotoUri.getEncodedPath();
                } else {
                    filePath = "file://" + mPhotoUri.getEncodedPath();
                }
                intent.setDataAndType(Uri.parse(filePath), "image/*");
                startActivity(intent);
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMemory == null) {
                    mMemory = new Memory();
                    populateMemoryOnSave();
                    mController.createMemory(mMemory);
                } else {
                    populateMemoryOnSave();
                    mController.updateMemory(mMemory);
                }

                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.deleteMemory(mMemory, mMemoryMarkerID);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    // initialize member variables with their respective view items
    private void linkUpViewItems() {
        mMemoryTitle = (EditText) getView().findViewById(R.id.titleEditText);
        mMemoryPlace = (EditText) getView().findViewById(R.id.placeEditText);
        mMemoryDate = (CalendarView) getView().findViewById(R.id.calendarView);
        mPeopleList = (EditText) getView().findViewById(R.id.peopleEditText);
        mCameraButton = (ImageButton) getView().findViewById(R.id.cameraButton);
        mGalleryButton = (ImageButton) getView().findViewById(R.id.galleryButton);
        mMemoryDescription = (EditText) getView().findViewById(R.id.memoryEditText);
        mSaveButton = (Button) getView().findViewById(R.id.saveButton);
        mCancelButton = (Button) getView().findViewById(R.id.cancelButton);
        mDeleteButton = (Button) getView().findViewById(R.id.deleteButton);
        mDeleteButton.setVisibility(View.INVISIBLE);
        mMemoryImage = (ImageView) getView().findViewById(R.id.imageView);
    }

    private Calendar generateCalendarObject() {
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

    // get the orientation of images from the gallery
    public static int getOrientation(Context context, Uri photoUri) {
        /* it's on the external media. */
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);

        if (cursor == null || cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    // set the image view with an image with proper orientation
    public void setImageView(ImageView imageView) {
        try {
            ExifInterface ei = new ExifInterface(mPhotoUri.getPath());
            int orientation;
            int rotate = 0;

            if (mPhotoUri.toString().contains("content")) {
                rotate = getOrientation(this.getContext(), mPhotoUri);
            } else {
                orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotate = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotate = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotate = 270;
                        break;
                    default:
                        break;
                }
            }

            Bitmap bitmap;

            if (mPhotoUri.toString().contains("content")) {
                String path = getRealPathFromURI(mPhotoUri);
                bitmap = BitmapFactory.decodeFile(path);
            } else if (mPhotoUri.toString().contains("file")) {
                String path = mPhotoUri.toString().replace("file://", "");
                bitmap = BitmapFactory.decodeFile(path);
            } else {
                bitmap = BitmapFactory.decodeFile(mPhotoUri.toString());
            }

            if (rotate != 0) {
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();

                Matrix mtx = new Matrix();
                mtx.preRotate(rotate);

                // Rotating Bitmap & convert to ARGB_8888, required by tess
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
                bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                imageView.setImageBitmap(bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // get the true file path for images stored in the gallery
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContext().getContentResolver().query(contentUri, proj, null, null, null);
        assert cursor != null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    /**
     * Create a file Uri for saving an image or video
     */
    private Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MemoryMap");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MemoryMap", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
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
            mMemoryMarkerID = bundle.getString(MapActivity.MARKER_ID);
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
            mDeleteButton.setVisibility(View.VISIBLE);
            populateMemory();
        }
    }

    @Override
    // get results from intent to camera or gallery
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GALLERY_ACTIVITY_REQUEST_CODE:
                    if (null != data) {
                        mPhotoUri = data.getData();
                        mMemoryImage.setImageURI(mPhotoUri);
                        setImageView(mMemoryImage);
                    }
                    break;
                case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                    if (mPhotoUri != null) {
                        setImageView(mMemoryImage);
                    }
                default:
                    break;
            }
        }
    }

}