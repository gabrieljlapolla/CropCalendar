package com.example.cropcalendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class DetailsDialog extends Dialog implements DialogInterface.OnClickListener {

    public Activity activity;
    public Crop crop;

    public DetailsDialog(Activity activity, Crop crop) {
        super(activity);
        this.activity = activity;
        this.crop = crop;
    }

    // TODO: make this dialog prettier
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_dialog);
        Context context = getContext();

        TextView nameTextView = findViewById(R.id.textViewDetailsName);
        TextView typeTextView = findViewById(R.id.textViewDetailsType);
        TextView sunTextView = findViewById(R.id.textViewDetailsSun);
        TextView frostTextView = findViewById(R.id.textViewDetailsFrostDays);
        TextView germTextView = findViewById(R.id.textViewDetailsGermDays);
        TextView harvestTextView = findViewById(R.id.textViewDetailsHarvestDays);
        TextView notesTextView = findViewById(R.id.textViewDetailsNotes);

        nameTextView.setText(crop.getName());
        typeTextView.setText(crop.getTypeText());
        sunTextView.setText(crop.getSunText());
        frostTextView.setText(context.getString(R.string.details_frost_days,
                crop.getDaysToPlantBeforeLastFrost()));
        germTextView.setText(context.getString(R.string.details_germ_days,
                crop.getDaysToGerm()));
        harvestTextView.setText(context.getString(R.string.details_harvest_days,
                crop.getDaysToHarvest()));
        String notes = crop.getNotes();
        if (notes.equals("")) {
            // Remove notes textview because there are no notes
            ((ViewGroup) notesTextView.getParent()).removeView(notesTextView);
            // Reorganize layout to keep spacing at bottom
            ConstraintLayout layout = findViewById(R.id.ContraintLayoutDetailDialog);
            ConstraintSet cs = new ConstraintSet();
            cs.clone(layout);
            cs.connect(R.id.spaceBottomDetailsDialog, ConstraintSet.TOP,
                    R.id.textViewDetailsHarvestDays, ConstraintSet.BOTTOM);
            cs.applyTo(layout);
        } else {
            notes = "Notes:\n" + notes;
            notesTextView.setText(notes);
            notesTextView.setMovementMethod(new ScrollingMovementMethod());
        }

    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }
}
