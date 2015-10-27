package drawer.lukmarr.pl.dynamicdrawer;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private DrawerLayout mDrawerLayout;
    private RecyclerView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerAdapter drawerAdapter;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private List<String> menuOptions;
    private boolean isDrawerOpened;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();
        menuOptions = new ArrayList<>();
        menuOptions.add("Adaś");
        menuOptions.add("Matje");
        menuOptions.add("Łukasz");

//        if (getIntent().hasExtra("ARRAY")) {
//            menuOptions = getIntent().getStringArrayExtra("ARRAY");
//        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (RecyclerView) findViewById(R.id.left_drawer);
        mDrawerList.setLayoutManager(new LinearLayoutManager(this));
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        drawerAdapter = new DrawerAdapter(this, menuOptions);
        mDrawerList.setAdapter(drawerAdapter);

//        mDrawerList.
//                setAdapter(new ArrayAdapter<>(this,
//                R.layout.drawer_list_item1, menuOptions));

//        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
//
        // enable ActionBar app icon to behave as action to toggle nav drawer
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                Log.d(TAG, "onDrawerClosed ");
                //getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                Log.d(TAG, "onDrawerOpened ");
                //getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content_frame, FirstFragment.newInstance())
                    .commitAllowingStateLoss();//.commit();
        }
        mDrawerLayout.openDrawer(mDrawerList);

    }

    private void selectItem(int position) {
        Log.d(TAG, "selectItem " + position);
        Fragment fragment;
        switch (position) {
            case 0: {
                fragment = FirstFragment.newInstance();
                break;
            }
            default: {
                fragment = ThirdFragment.newInstance();
            }
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        setTitle(menuOptions.get(position));
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    public void switchTo(String comName) {
        Log.d(TAG, "switchTo ");
        //switching to comFragment:
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, ComFragment.newInstance(comName))
                .commit();

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

    public void closeDrawer() {
        Log.d(TAG, "closeDrawer ");
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    public void addNewItem(String string) {
        Log.d(TAG, "addNewItem ");

        drawerAdapter.addNewItem(string);
        Log.d(TAG, "addNewItem ");
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        if (getActionBar() != null)
            getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


}
