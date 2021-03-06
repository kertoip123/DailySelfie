package labs.course.dailyselfie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
        //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                mName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void setPic(ImageView mImageView, int tarWidth, int tarHeight) {
        // Get the dimensions of the View
        //mImageView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        //Works only with fixed dps values
        int targetW, targetH;
        if(tarWidth == -1) {
            targetW = mImageView.getLayoutParams().width;
            targetH = mImageView.getLayoutParams().height;
        }else {
            targetW = tarWidth;
            targetH = tarHeight;
        }

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bmOptions.inJustDecodeBounds = true;

        Bitmap tempBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }

    @Override
    public String toString(){
        return mName;
    }

}
