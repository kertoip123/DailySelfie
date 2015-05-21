package labs.course.dailyselfie;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoListAdapter extends BaseAdapter  {

    private ArrayList<PlaceRecord> list = new ArrayList<PlaceRecord>();
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

        PlaceRecord curr = list.get(position);

        if (null == convertView) {
            holder = new ViewHolder();
            newView = inflater
                    .inflate(R.layout.place_badge_view, parent, false);
            holder.photo = (ImageView) newView.findViewById(R.id.flag);
            holder.name = (TextView) newView.findViewById(R.id.place_name);
            newView.setTag(holder);

        } else {
            holder = (ViewHolder) newView.getTag();
        }

        holder.photo.setImageBitmap(curr.getFlagBitmap());
        holder.name.setText("Place: " + curr.getPlace());

        return newView;
    }

    static class ViewHolder {

        ImageView photo;
        TextView name;

    }

    public void add(PlaceRecord listItem) {
        list.add(listItem);
        notifyDataSetChanged();
    }

    public ArrayList<PlaceRecord> getList() {
        return list;
    }

    public void removeAllViews() {
        list.clear();
        this.notifyDataSetChanged();
    }
}
