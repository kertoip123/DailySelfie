package labs.course.dailyselfie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.File;
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

    public void setName (String name){
        this.mName = name;
    }

    public void setBitmap(Bitmap bitmap){
        this.mBitmap = bitmap;
    }

    public String getName(){
        return this.mName;
    }

    public Bitmap getBitmap(){
        return this.mBitmap;
    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mName = "JPEG_" + timeStamp + "_";
        //File storageDir = getExternalFilesDir(null);
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
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
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