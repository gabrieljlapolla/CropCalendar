package com.example.cropcalendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.snackbar.Snackbar;

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
        Context context = getContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_dialog);
        loadCropInfo();
        // Switch to plant after frost date
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
        // Exit dialog not saving
        Button cancelButton = findViewById(R.id.buttonEditCropCancel);
        cancelButton.setOnClickListener(view -> {
            cancel();
        });
        // Delete crop
        Button deleteButton = findViewById(R.id.buttonEditCropDelete);
        deleteButton.setOnClickListener(view -> {
            AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(new
                    ContextThemeWrapper(context, R.style.Dialog_RoundedDialog));
            confirmationDialog.setTitle(R.string.delete_confirmation)
                    .setMessage(context.getString(R.string.delete_confirmation_desc, crop.getName()))
                    .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                        if (CropDatabase.getInstance(context).deleteCrop(crop.getName())) {
                            // Success
                            Snackbar.make(view, context.getString(R.string.delete_success,
                                    crop.getName()), Snackbar.LENGTH_SHORT)
                                    .setAnchorView(findViewById(R.id.bottomNavView)).show();
                        } else {
                            Snackbar.make(view, context.getString(R.string.delete_failure,
                                    crop.getName()), Snackbar.LENGTH_SHORT)
                                    .setAnchorView(findViewById(R.id.bottomNavView)).show();
                        }
                        // Reload activity to update crop data
                        // TODO: reload to reflect updated crop list
                        /*Activity activity = getOwnerActivity();
                        getOwnerActivity().finish();
                        activity.overridePendingTransition(0, 0);
                        activity.startActivity(activity.getIntent());
                        activity.overridePendingTransition(0, 0);*/
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        });
        // Submit crop details
        Button submitButton = findViewById(R.id.buttonEditCropSubmit);
        submitButton.setOnClickListener(view -> {
            AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(new
                    ContextThemeWrapper(context, R.style.Dialog_RoundedDialog));
            confirmationDialog.setTitle(R.string.submit_confirmation)
                    .setMessage(context.getString(R.string.submit_confirmation_desc, crop.getName()))
                    .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                        // TODO: finish submit crop edits
                        // Reload activity to update crop data
                        Activity activity = getOwnerActivity();
                        activity.finish();
                        activity.overridePendingTransition(0, 0);
                        activity.startActivity(activity.getIntent());
                        activity.overridePendingTransition(0, 0);
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
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