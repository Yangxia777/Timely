package com.xy.timetracker;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.xy.timetracker.fragments.AnalysisDetailFragment;

import java.util.Calendar;

public class AnalysisActivity extends AppCompatActivity {


    private Button pickerButton;
    private AnalysisDetailFragment mDetailViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_analysis_view);

        mDetailViewFragment = (AnalysisDetailFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment);

        //Setup up action
        Toolbar analysisToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(analysisToolBar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.analysis_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_calendar_picker:
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog(
                        this,
                        mDetailViewFragment,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show();
                break;
            case R.id.action_pick_today:
                mDetailViewFragment.gotoToday();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }


}
