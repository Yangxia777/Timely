package com.xy.timetracker.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.xy.timetracker.R;
import com.xy.timetracker.adapters.ColorPaletteAdapter;
import com.xy.timetracker.model.TrackerElement;
import com.xy.timetracker.util.Constants;

import java.util.UUID;

public class AddTrackersDialogFragment extends AppCompatDialogFragment implements View.OnClickListener {
    EditText mTitleEditText = null;
    EditText mSubTitleEditText = null;
    GridView mColorPickerGridView = null;

    OnTrackerAddedListener mOnTrackerAddedListener;
    ContextListProvider mContextListProvider;


    public interface OnTrackerAddedListener {
        void onTrackerAddedListenr(TrackerElement trackerElement);
    }

    public interface ContextListProvider {
        String[] getExistedContexts();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_tracker_fragment, container, false);
        mTitleEditText = (EditText) v.findViewById(R.id.editText_title);
        mSubTitleEditText = (EditText) v.findViewById(R.id.editText_subtitle);
        mColorPickerGridView = (GridView) v.findViewById(R.id.color_picker_gridview);

        // set up color picker
        mColorPickerGridView.setAdapter(new ColorPaletteAdapter(getActivity()));
        mColorPickerGridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
        mColorPickerGridView.setItemChecked(0, true);

        // autocomplete for context (subtitle)
        AutoCompleteTextView contextView = (AutoCompleteTextView) v.findViewById(R.id.editText_subtitle);
        String[] contextNames = mContextListProvider.getExistedContexts();
        ArrayAdapter<String> contextAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, contextNames);
        contextView.setAdapter(contextAdapter);

        // on click listener for cancel and ok
        Button cancelButton = (Button) v.findViewById(R.id.button_cancel);
        Button okButton = (Button) v.findViewById(R.id.button_ok);
        cancelButton.setOnClickListener(this);
        okButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_cancel:
                dismiss();
                break;
            case R.id.button_ok:
                int colorPosition = mColorPickerGridView.getCheckedItemPosition();
                TrackerElement trackerElement = new TrackerElement(UUID.randomUUID(), mTitleEditText.getText().toString(),
                        mSubTitleEditText.getText().toString(), Constants.MATERIAL_COLORS_PRIMARY[colorPosition]);
                dismiss();
                mOnTrackerAddedListener.onTrackerAddedListenr(trackerElement);
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mOnTrackerAddedListener = (OnTrackerAddedListener) activity;
        mContextListProvider = (ContextListProvider) activity;
    }
}
