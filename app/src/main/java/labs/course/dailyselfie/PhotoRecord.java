package labs.course.dailyselfie;

import android.graphics.Bitmap;

public class PhotoRecord {
    private String mName;
    private Bitmap mBitmap;

    public PhotoRecord(String mName, Bitmap mBitmap) {
        this.mName = mName;
        this.mBitmap = mBitmap;
    }

    public PhotoRecord() {
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

    @Override
    public String toString(){
        return mName;
    }

}
