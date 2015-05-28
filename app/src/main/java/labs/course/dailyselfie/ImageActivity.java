package labs.course.dailyselfie;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by KAMIL on 2015-05-21.
 */
public class ImageActivity extends Activity {

    String photoCurrentPath;
    String photoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_activity);

        //@TODO Passing Photo record from MainActivity
        Intent intent = getIntent();
        photoCurrentPath = intent.getExtras().getString(MainActivity.pathAttribute);
        photoName = intent.getExtras().getString(MainActivity.nameAttribute);

        ImageView imageView = (ImageView) findViewById(R.id.enlarged_selfie);
        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new MyGlobalListenerClass());

    }

    class MyGlobalListenerClass implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            ImageView imageView = (ImageView) findViewById(R.id.enlarged_selfie);
            //show ImageView width and height
            PhotoRecord photoRecord = new PhotoRecord(photoCurrentPath);
            photoRecord.setPic(imageView, imageView.getWidth(), imageView.getHeight());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.delete) {
            SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            Set<String> paths = new LinkedHashSet<String>();
            Set<String> names = new LinkedHashSet<String>();

            Set<String> currPaths = sharedPreferences.getStringSet(MainActivity.pathAttributeSet, null);
            Set<String> currNames = sharedPreferences.getStringSet(MainActivity.nameAttributeSet, null);

            for(String s : currNames) {
                if(s != photoName)
                    names.add(s);
            }
            for(String s: currPaths) {
                if(s != photoCurrentPath)
                    paths.add(s);
            }

            paths.add(photoCurrentPath);
            names.add(photoName);
            editor.putStringSet(MainActivity.pathAttributeSet, paths);
            editor.putStringSet(MainActivity.nameAttributeSet, names);
            editor.commit();

            File tempFile = new File(photoCurrentPath);
            tempFile.delete();

        }

        return super.onOptionsItemSelected(item);
    }
}
