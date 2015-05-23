package labs.course.dailyselfie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

/**
 * Created by KAMIL on 2015-05-21.
 */
public class ImageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_activity);

        ImageView imageView = (ImageView) findViewById(R.id.enlarged_selfie);

        //@TODO Passing Photo record from MainActivity
        Intent intent = getIntent();
        String photoCurrentPath = intent.getExtras().getString(MainActivity.pathAttribute);
        PhotoRecord photoRecord = new PhotoRecord(photoCurrentPath);
        photoRecord.setPic(imageView);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
