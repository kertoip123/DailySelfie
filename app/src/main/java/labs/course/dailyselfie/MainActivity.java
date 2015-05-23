package labs.course.dailyselfie;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends ListActivity {

    private PhotoListAdapter mAdapter;
    private PhotoRecord currPhotoRecord;

    static final  String TAG = "Lab-DailySelfie";
    static final  String pathAttribute = "final_path";
    static final int REQUEST_TAKE_PHOTO = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.image_activity);

        ListView photosListView = getListView();

        mAdapter = new PhotoListAdapter(getApplicationContext());
        setListAdapter(mAdapter);
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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // @TODO Photo needs to be enlarged after clicking on the List Item
        PhotoRecord clickedPhoto = (PhotoRecord) getListAdapter().getItem(position);
        Bundle newBundle = new Bundle();
        newBundle.putString(pathAttribute, clickedPhoto.getCurrentPhotoPath());
        Intent intent = new Intent(MainActivity.this, ImageActivity.class);
        startActivity(intent, newBundle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_TAKE_PHOTO) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
             //   Bundle extras = data.getExtras();
               // Bitmap imageBitmap = (Bitmap) extras.get("data");
                //currPhotoRecord.setBitmap(imageBitmap);
                mAdapter.add(currPhotoRecord);
                // Do something with the contact here (bigger example below)
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
                currPhotoRecord = new PhotoRecord(getExternalFilesDir(null));
                photoFile = currPhotoRecord.createImageFile();
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
