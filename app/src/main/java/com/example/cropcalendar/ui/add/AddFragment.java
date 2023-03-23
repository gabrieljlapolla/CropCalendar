package com.example.cropcalendar.ui.add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cropcalendar.R;
import com.example.cropcalendar.databinding.FragmentAddBinding;

public class AddFragment extends Fragment {

    private FragmentAddBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddViewModel dashboardViewModel =
                new ViewModelProvider(this).get(AddViewModel.class);

        binding = FragmentAddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textViewAddFrost;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Set listener for switch to disable editText if enabled
        Switch sowAfterFrostSwitch = root.findViewById(R.id.switchAddSowAfterFrost);
        sowAfterFrostSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            EditText frostEditText = root.findViewById(R.id.editTextAddFrost);
            if (isChecked) {
                frostEditText.setHint(R.string.sow_after_frost);
                frostEditText.setError(null); // Clear any error
                frostEditText.setEnabled(false);
            } else {
                frostEditText.setHint(R.string.enter_text);
                frostEditText.setEnabled(true);
            }
        });

        // Custom spinner style
        Spinner cropTypeSpinner = root.findViewById(R.id.spinnerAddType);
        ArrayAdapter<CharSequence> cropTypeSpinnerAdapter =
                ArrayAdapter.createFromResource(root.getContext(), R.array.crop_types,
                        R.layout.spinner_crop_style);
        cropTypeSpinner.setAdapter(cropTypeSpinnerAdapter);

        Spinner sunLevelSpinner = root.findViewById(R.id.spinnerAddSun);
        ArrayAdapter<CharSequence> sunLevelSpinnerAdapter =
                ArrayAdapter.createFromResource(root.getContext(), R.array.crop_sun_levels,
                        R.layout.spinner_crop_style);
        sunLevelSpinner.setAdapter(sunLevelSpinnerAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}