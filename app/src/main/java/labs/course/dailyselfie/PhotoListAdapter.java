package labs.course.dailyselfie;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoListAdapter extends BaseAdapter  {

    private ArrayList<PhotoRecord> list = new ArrayList<PhotoRecord>();
    private static LayoutInflater inflater = null;
    private Context mContext;

    public PhotoListAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View newView = convertView;
        ViewHolder holder;

        PhotoRecord curr = list.get(position);

        if (null == convertView) {
            holder = new ViewHolder();
            newView = inflater
                    .inflate(R.layout.my_selfie_view, parent, false);
            holder.photo = (ImageView) newView.findViewById(R.id.my_selfie);
            holder.name = (TextView) newView.findViewById(R.id.name);
            newView.setTag(holder);

        } else {
            holder = (ViewHolder) newView.getTag();
        }

        //holder.photo.setImageBitmap(curr.getBitmap());
        curr.setPic(holder.photo, -1, -1);
        holder.name.setText(curr.getName());

        return newView;
    }

    static class ViewHolder {

        ImageView photo;
        TextView name;

    }

    public void add(PhotoRecord listItem) {
        list.add(listItem);
        notifyDataSetChanged();
    }

    public void remove(String filePath) {
        Iterator<PhotoRecord> listIterator = list.iterator();
        while(listIterator.hasNext() ){
            PhotoRecord curr = listIterator.next();
            if(curr.getCurrentPhotoPath().equals(filePath))
                listIterator.remove();
        }
        notifyDataSetChanged();
    }

    public ArrayList<PhotoRecord> getList() {
        return list;
    }

    public void removeAllViews() {
        list.clear();
        this.notifyDataSetChanged();
    }
}
