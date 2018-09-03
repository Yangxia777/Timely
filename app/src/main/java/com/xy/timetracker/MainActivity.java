package com.xy.timetracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.xy.timetracker.adapters.TrackerRecyclerViewAdapter;
import com.xy.timetracker.fragments.AddTrackersDialogFragment;
import com.xy.timetracker.fragments.TrackersRecyclerViewFragment;
import com.xy.timetracker.model.TrackerElement;
import com.xy.timetracker.model.TrackerElementsManager;
import com.xy.timetracker.util.Constants;
import com.xy.timetracker.util.IOUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AddTrackersDialogFragment.OnTrackerAddedListener, AddTrackersDialogFragment.ContextListProvider, NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String TRACKER_KEY = "tarckers";
    private TrackerElementsManager trackerElementsManager;
    private ArrayList<TrackerElement> trackers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Float action button -- add trackers
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_tracker_button);
        fab.setOnClickListener(this);

        // Navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        trackerElementsManager = TrackerElementsManager.getInstance();
        trackers = trackerElementsManager.getTrackers();
        if (savedInstanceState == null) {
            trackers = IOUtility.restoreTrackersFromInternal(this, Constants.INTERNAL_STORAGE_FILENAME);
            trackerElementsManager.setTrackers(trackers);

            TrackersRecyclerViewFragment trackersFragment = (TrackersRecyclerViewFragment) getSupportFragmentManager().findFragmentById(R.id.tracker_recycler_fragment);
            trackersFragment.setTrackersRecyclerViewAdapter(new TrackerRecyclerViewAdapter(trackerElementsManager.filterOutArchivedTrackerElements()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        trackers = savedInstanceState.<TrackerElement>getParcelableArrayList(TRACKER_KEY);
        trackerElementsManager.setTrackers(trackers);
        TrackersRecyclerViewFragment trackersFragment = (TrackersRecyclerViewFragment) getSupportFragmentManager().findFragmentById(R.id.tracker_recycler_fragment);
        trackersFragment.setTrackersRecyclerViewAdapter(new TrackerRecyclerViewAdapter(trackerElementsManager.filterOutArchivedTrackerElements()));
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(TRACKER_KEY, trackers);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        String jsonToSave = new Gson().toJson(trackers);
        IOUtility.saveTrackersToInternal(this, Constants.INTERNAL_STORAGE_FILENAME, jsonToSave);
    }

    //-------------------------------------------------------------------------
    // onclick callback
    //-------------------------------------------------------------------------
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_tracker_button:
                showAddTrackerDialog();
                break;
        }
    }

    private void showAddTrackerDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        AppCompatDialogFragment newFragment = new AddTrackersDialogFragment();
        newFragment.show(ft, "dialog");
    }

    //-------------------------------------------------------------------------
    // communication with add tracker fragment
    //-------------------------------------------------------------------------
    @Override
    public void onTrackerAddedListenr(TrackerElement trackerElement) {
        trackerElementsManager.addTracker(trackerElement);
        TrackersRecyclerViewFragment trackersFragment = (TrackersRecyclerViewFragment) getSupportFragmentManager().findFragmentById(R.id.tracker_recycler_fragment);
        TrackerRecyclerViewAdapter adapter = (TrackerRecyclerViewAdapter) trackersFragment.getTrackersRecyclerViewAdapter();
        adapter.notifyDataSetChanged();
    }

    @Override
    public String[] getExistedContexts() {
        Set<String> setContexts = new HashSet<>();
        setContexts.addAll(Arrays.asList(Constants.DEFAULT_CONTEXTS));
        for (TrackerElement trackerElement : trackers) {
            setContexts.add(trackerElement.getTrackerSubTitle());
        }

        String[] array = new String[setContexts.size()];
        return setContexts.toArray(array);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_calendar:
                Intent intent = new Intent(this, AnalysisActivity.class);
                startActivity(intent);
                break;
        }

        return true;
    }
}
