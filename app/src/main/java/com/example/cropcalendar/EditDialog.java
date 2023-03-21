package com.example.cropcalendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class EditDialog extends Dialog implements DialogInterface.OnClickListener {

    public Activity activity;
    public Crop crop;

    public EditDialog(Activity activity, Crop crop) {
        super(activity);
        this.activity = activity;
        this.crop = crop;
    }

    // TODO: make this dialog prettier
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_dialog);
        loadCropInfo();
        Switch sowAfterFrostSwitch = findViewById(R.id.switchEditCropSowAfterFrost);
        sowAfterFrostSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            EditText frostEditText = findViewById(R.id.editTextEditCropFrost);
            if (isChecked) {
                frostEditText.setHint(R.string.sow_after_frost);
                frostEditText.setError(null); // Clear any error
                frostEditText.setEnabled(false);
            } else {
                frostEditText.setHint(R.string.enter_text);
                frostEditText.setEnabled(true);
            }
        });
    }

    private void loadCropInfo() {
        EditText name = findViewById(R.id.editTextEditCropName);
        EditText frost = findViewById(R.id.editTextEditCropFrost);
        EditText harvest = findViewById(R.id.editTextEditCropHarvest);
        EditText germ = findViewById(R.id.editTextEditCropGerm);
        EditText notes = findViewById(R.id.editTextEditCropNote);
        Switch sowAfterFrostSwitch = findViewById(R.id.switchEditCropSowAfterFrost);

        // Custom spinner style
        Spinner typeSpinner = findViewById(R.id.spinnerEditCropType);
        ArrayAdapter<CharSequence> cropTypeSpinnerAdapter =
                ArrayAdapter.createFromResource(getContext(), R.array.crop_types,
                        R.layout.spinner_crop_style);
        typeSpinner.setAdapter(cropTypeSpinnerAdapter);

        Spinner sunSpinner = findViewById(R.id.spinnerEditCropSun);
        ArrayAdapter<CharSequence> sunLevelSpinnerAdapter =
                ArrayAdapter.createFromResource(getContext(), R.array.crop_sun_levels,
                        R.layout.spinner_crop_style);
        sunSpinner.setAdapter(sunLevelSpinnerAdapter);

        // Set spinners to correct selections
        for (int i = 0; i < cropTypeSpinnerAdapter.getCount(); i++) {
            String selection = (String) typeSpinner.getItemAtPosition(i);
            if (selection.contains(crop.getTypeText())) {
                typeSpinner.setSelection(i);
                break;
            }
        }

        for (int i = 0; i < sunLevelSpinnerAdapter.getCount(); i++) {
            String selection = (String) sunSpinner.getItemAtPosition(i);
            if (selection.contains(crop.getSunText())) {
                sunSpinner.setSelection(i);
                break;
            }
        }

        name.setText(crop.getName());
        germ.setText(Integer.toString(crop.getDaysToGerm()));
        harvest.setText(Integer.toString(crop.getDaysToHarvest()));
        notes.setText(crop.getNotes());

        if (crop.getDaysToPlantBeforeLastFrost() == 0) {
            sowAfterFrostSwitch.setChecked(true);
            frost.setHint(R.string.sow_after_frost);
        } else {
            frost.setText(Integer.toString(crop.getDaysToPlantBeforeLastFrost()));
        }

    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }
}