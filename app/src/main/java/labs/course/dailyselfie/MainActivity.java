package labs.course.dailyselfie;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends ListActivity {

    private PhotoListAdapter mAdapter;

    final public String pathAttribute = "final_path";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.image_activity);

        ListView photosListView = getListView();

        mAdapter = new PhotoListAdapter(getApplicationContext());
        setListAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // @TODO Photo needs to be enlarged after clicking on the List Item
        PhotoRecord clickedPhoto = (PhotoRecord) getListAdapter().getItem(position);
        Bundle newBundle = new Bundle();
        newBundle.putString(pathAttribute, clickedPhoto.getCurrentPhotoPath());
        Intent intent = new Intent(MainActivity.this, ImageActivity.class);
        startActivity(intent, newBundle);
    }
}
