package labs.course.dailyselfie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by KAMIL on 2015-05-21.
 */
public class ImageActivity extends Activity {

    String photoCurrentPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_activity);

        //@TODO Passing Photo record from MainActivity
        Intent intent = getIntent();
        photoCurrentPath = intent.getExtras().getString(MainActivity.pathAttribute);

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
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
