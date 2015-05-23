package labs.course.dailyselfie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoRecord {
    private String mName;
    private String mCurrentPhotoPath;
    private File storageDir;
    private Bitmap mBitmap;

    public PhotoRecord(String mName, Bitmap mBitmap) {
        this.mName = mName;
        this.mBitmap = mBitmap;
    }

    public PhotoRecord(File storageDir) {
        this.storageDir = storageDir;
    }

    public PhotoRecord(String mCurrentPhotoPath) {
        this.mCurrentPhotoPath = mCurrentPhotoPath;
    }

    public PhotoRecord(String mName, String mCurrentPhotoPath) {
        this.mName = mName;
        this.mCurrentPhotoPath = mCurrentPhotoPath;
    }

    public void setName (String name){
        this.mName = name;
    }

    public void setBitmap(Bitmap bitmap){
        this.mBitmap = bitmap;
    }

    public String getName(){
        return this.mName;
    }

    public String getCurrentPhotoPath(){
        return this.mCurrentPhotoPath;
    }


    public Bitmap getBitmap(){
        return this.mBitmap;
    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                mName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    public void setPic(ImageView mImageView) {
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        try ( InputStream is = new URL( mCurrentPhotoPath ).openStream() ) {
            Bitmap bitmap = BitmapFactory.decodeStream( is );
            //Bitmap tempBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            Bitmap tempBitmap  = BitmapFactory.decodeStream( is, null, bmOptions);
        }
        catch(Exception e){
            Log.e(MainActivity.TAG, "setPic() Exception !");
        }
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        try ( InputStream is = new URL( mCurrentPhotoPath ).openStream() ) {
            //Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            Bitmap bitmap  = BitmapFactory.decodeStream( is, null, bmOptions);
            mImageView.setImageBitmap(bitmap);
        }
        catch(Exception e){
            Log.e(MainActivity.TAG, "setPic() Exception !");
        }
    }

    @Override
    public String toString(){
        return mName;
    }

}
