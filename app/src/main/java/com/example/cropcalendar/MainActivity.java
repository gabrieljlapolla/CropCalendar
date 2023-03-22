package com.example.cropcalendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cropcalendar.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    final public static String FROST_SETTING = "firstFrostDate";

    // TODO: change switch color
    // TODO: Reminder to plant on day of planting
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: Implement dark mode theme and clean theme in general
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        com.example.cropcalendar.databinding.ActivityMainBinding binding =
                ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_calendar, R.id.navigation_add, R.id.navigation_settings)
                .build();
        NavController navController =
                Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Settings
        SharedPreferences sp = getSharedPreferences("settings", 0);
        Crop.setFrostDate(sp.getString(FROST_SETTING, "1/1"));

    }

    /**
     * Create a crop and adds it to the database if information is correct
     *
     * @param view View
     */
    public void createCrop(View view) {
        boolean allFieldsFilled = true;

        EditText nameEditText = findViewById(R.id.editTextFrostDate);
        Spinner typeSpinner = findViewById(R.id.spinnerCropType);
        EditText frostEditText = findViewById(R.id.editTextCropFrost);
        Switch sowAfterFrostSwitch = findViewById(R.id.switchSowAfterFrost);
        EditText germEditText = findViewById(R.id.editTextGermination);
        EditText harvestEditText = findViewById(R.id.editTextHarvest);
        Spinner sunSpinner = findViewById(R.id.spinnerCropSunLevels);
        EditText notesEditText = findViewById(R.id.editTextMultiLineCropNotes);

        String name = nameEditText.getText().toString().trim();
        String typeString = typeSpinner.getSelectedItem().toString();
        typeString = typeString.substring(3); // Chop off emoji
        Crop.cropType type = Crop.cropType.valueOf(typeString.toUpperCase());
        String daysToPlantBeforeLastFrost;
        if (sowAfterFrostSwitch.isChecked()) {
            daysToPlantBeforeLastFrost = "0";
        } else {
            daysToPlantBeforeLastFrost = frostEditText.getText().toString();
        }
        String daysUntilGerm = germEditText.getText().toString();
        String daysUntilHarvest = harvestEditText.getText().toString();
        String sunString = sunSpinner.getSelectedItem().toString();
        Crop.sunLevel sunLevel = Crop.getSunLevelFromText(sunString);
        String notes = notesEditText.getText().toString();

        if (TextUtils.isEmpty(name)) {
            nameEditText.setError(getString(R.string.empty_field));
            allFieldsFilled = false;
        }
        if (TextUtils.isEmpty(daysToPlantBeforeLastFrost) && !sowAfterFrostSwitch.isChecked()) {
            frostEditText.setError(getString(R.string.empty_field));
            allFieldsFilled = false;
        }
        if (TextUtils.isEmpty(daysUntilGerm)) {
            germEditText.setError(getString(R.string.empty_field));
            allFieldsFilled = false;
        }
        if (TextUtils.isEmpty(daysUntilHarvest)) {
            harvestEditText.setError(getString(R.string.empty_field));
            allFieldsFilled = false;
        }

        // Error checking for filled in fields and duplicate entries
        if (!allFieldsFilled) {
            // Not all fields filled
            Snackbar.make(view, R.string.complete_all_fields, Snackbar.LENGTH_SHORT)
                    .setAnchorView(findViewById(R.id.nav_view)).show();
        } else {
            Crop crop = new Crop(name, type, Integer.parseInt(daysToPlantBeforeLastFrost),
                    Integer.parseInt(daysUntilGerm), Integer.parseInt(daysUntilHarvest),
                    sunLevel, notes);
            // TODO: async me
            CropDatabase cdb = CropDatabase.getInstance(this);
            if (cdb.addCrop(crop)) {
                // Successfully added
                Snackbar.make(view, getString(R.string.crop_added, name), Snackbar.LENGTH_LONG)
                        .setAnchorView(findViewById(R.id.nav_view)).show();
                // Clear/reset all fields
                nameEditText.getText().clear();
                typeSpinner.setSelection(0);
                frostEditText.getText().clear();
                germEditText.getText().clear();
                harvestEditText.getText().clear();
                sunSpinner.setSelection(0);
                notesEditText.getText().clear();
                // Hide keyboard
                View focus = this.getCurrentFocus();
                if (focus != null) {
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(focus.getWindowToken(), 0);
                }
            } else {
                // Not all fields filled
                Snackbar.make(view, R.string.complete_all_fields, Snackbar.LENGTH_SHORT)
                        .setAnchorView(findViewById(R.id.nav_view)).show();
            }
        }
    }

    public void saveSettings(View view) {
        // Dialog to confirm selection
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(true);
        builder.setTitle(R.string.save_changes);

        builder.setPositiveButton(R.string.save, (dialogInterface, i) -> {

            DatePicker frostDatePicker = findViewById(R.id.lastFrostDatePicker);

            String selectedLastFrostDate = (frostDatePicker.getMonth() + 1) +
                    "/" + frostDatePicker.getDayOfMonth();

            // Save new settings
            SharedPreferences sp = getSharedPreferences("settings", 0);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(FROST_SETTING, selectedLastFrostDate);
            editor.apply(); // Save settings

            Crop.setFrostDate(selectedLastFrostDate);
            TextView currentLastFrostDate = findViewById(R.id.textViewCurrentLastFrostDate);
            currentLastFrostDate.setText(
                    getString(R.string.current_frost_date, selectedLastFrostDate));

            // Confirmation message
            Snackbar.make(view, R.string.done, Snackbar.LENGTH_SHORT)
                    .setAnchorView(findViewById(R.id.nav_view)).show();
        });
        // User selects cancel
        builder.setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
            Snackbar.make(view, R.string.no_changes, Snackbar.LENGTH_SHORT)
                    .setAnchorView(findViewById(R.id.nav_view)).show();
        });
        builder.create();
        builder.show();
    }
}