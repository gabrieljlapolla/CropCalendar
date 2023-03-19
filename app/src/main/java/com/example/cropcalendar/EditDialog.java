package com.example.cropcalendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
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
        Context context = getContext();


        EditText name = findViewById(R.id.editTextEditCropName);
        name.setText("sssssss");
        loadCropInfo();

    }

    private void loadCropInfo() {
        EditText name = findViewById(R.id.editTextEditCropName);
        EditText frost = findViewById(R.id.editTextEditCropFrost);
        EditText harvest = findViewById(R.id.editTextEditCropHarvest);
        EditText germ = findViewById(R.id.editTextEditCropGerm);
        EditText notes = findViewById(R.id.editTextEditCropNote);
        Spinner type = findViewById(R.id.spinnerEditCropType);
        Spinner sun = findViewById(R.id.spinnerEditCropSun);

        name.setText(crop.getName());
        frost.setText(Integer.toString(crop.getDaysToPlantBeforeLastFrost()));
        germ.setText(Integer.toString(crop.getDaysToGerm()));
        harvest.setText(Integer.toString(crop.getDaysToHarvest()));
        notes.setText(crop.getNotes());
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }
}