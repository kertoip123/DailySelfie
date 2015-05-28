package labs.course.dailyselfie;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class MainActivity extends ListActivity {

    private PhotoListAdapter mAdapter;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    //private PhotoRecord currPhotoRecord;

    private AlarmManager mAlarmManager;
    private Intent mNotificationReceiverIntent;
    private PendingIntent mNotificationReceiverPendingIntent;
    private static final long INITIAL_ALARM_DELAY = 15*1000L;
    private static final long ALARM_DELAY = AlarmManager.INTERVAL_DAY;

    static final  String TAG = "Lab-DailySelfie";
    static final  String pathAttribute = "final_path";
    static final  String nameAttribute = "curr_name";
    static final  String pathAttributeSet = "final_path_set";
    static final  String nameAttributeSet = "curr_name_set";
    static final String PREF_FILE_NAME = "preferences";
    static final int REQUEST_TAKE_PHOTO = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.image_activity);

        ListView photosListView = getListView();

        sharedPreferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if(sharedPreferences.getStringSet(pathAttributeSet, null)==null)
            editor.putStringSet(pathAttributeSet, new LinkedHashSet<String>());

        if(sharedPreferences.getStringSet(nameAttributeSet, null)==null)
            editor.putStringSet(nameAttributeSet, new LinkedHashSet<String>());

        editor.commit();

        mAdapter = new PhotoListAdapter(getApplicationContext());
        setListAdapter(mAdapter);

        //notification
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        mNotificationReceiverIntent = new Intent(MainActivity.this,
                AlarmNotificationReceiver.class);

        mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(
                MainActivity.this, 0, mNotificationReceiverIntent, 0);
        mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + INITIAL_ALARM_DELAY,
                ALARM_DELAY,
                mNotificationReceiverPendingIntent);
    }

    @Override
    protected void onResume(){
        super.onResume();

        Set<String> photoNameSet = sharedPreferences.getStringSet(nameAttributeSet, null);
        Iterator<String> photoNameIterator = photoNameSet.iterator();
        Set<String> photoPathSet = sharedPreferences.getStringSet(pathAttributeSet, null);
        Iterator<String> photoPathIterator = photoPathSet.iterator();
        while(photoNameIterator.hasNext() && photoPathIterator.hasNext()){
            String name = photoNameIterator.next();
            String path = photoPathIterator.next();
            if(!new File(path).exists()){
                photoNameIterator.remove();
                photoPathIterator.remove();
            }
            else
                mAdapter.add(new PhotoRecord(name, path));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_camera) {
            dispatchTakePictureIntent();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // @TODO Photo needs to be enlarged after clicking on the List Item
        PhotoRecord clickedPhoto = (PhotoRecord) getListAdapter().getItem(position);
        Intent intent = new Intent(MainActivity.this, ImageActivity.class);
        intent.putExtra(pathAttribute, clickedPhoto.getCurrentPhotoPath());
        intent.putExtra(nameAttribute, clickedPhoto.getName());
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_TAKE_PHOTO) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                String mName = sharedPreferences.getString(nameAttribute, "No such items");
                String mCurrentPhotoPath = sharedPreferences.getString(pathAttribute, "No such items");
                PhotoRecord currPhotoRecord = new PhotoRecord(mName, mCurrentPhotoPath);
                mAdapter.add(currPhotoRecord);

                Set<String> paths = new LinkedHashSet<String>();
                Set<String> names = new LinkedHashSet<String>();

                Set<String> currPaths = sharedPreferences.getStringSet(pathAttributeSet, null);
                Set<String> currNames = sharedPreferences.getStringSet(nameAttributeSet, null);

                for(String s : currNames)
                    names.add(s);
                for(String s: currPaths)
                    paths.add(s);

                paths.add(mCurrentPhotoPath);
                names.add(mName);
                editor.putStringSet(pathAttributeSet, paths);
                editor.putStringSet(nameAttributeSet, names);
                editor.commit();
                // Do something with the contact here (bigger example below)
            }else {
                String mCurrentPhotoPath = sharedPreferences.getString(pathAttribute, "No such items");
                File emptyImage = new File(mCurrentPhotoPath);
                emptyImage.delete();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                PhotoRecord currPhotoRecord = new PhotoRecord(getExternalFilesDir(null));
                //PhotoRecord currPhotoRecord = new PhotoRecord(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
                Log.e(TAG, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString());
                photoFile = currPhotoRecord.createImageFile();
               // if(photoFile.exists())
               //   Log.e(TAG, "true");

                editor.putString(nameAttribute, currPhotoRecord.getName());
                editor.putString(pathAttribute, currPhotoRecord.getCurrentPhotoPath());

                editor.commit();

            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(TAG, "createImageFile() failed !!!");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
}
