package labs.course.dailyselfie;

import android.app.Activity;
import android.os.Bundle;
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
        String photoCurrentPath = savedInstanceState.getString(MainActivity.pathAttribute);
        PhotoRecord photoRecord = new PhotoRecord(photoCurrentPath);
        photoRecord.setPic(imageView);
    }
}
