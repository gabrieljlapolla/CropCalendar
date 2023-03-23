package com.example.cropcalendar.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cropcalendar.Crop;
import com.example.cropcalendar.MainActivity;
import com.example.cropcalendar.R;
import com.example.cropcalendar.databinding.FragmentSettingsBinding;

import java.util.Calendar;
import java.util.Date;

public class SettingsFragment extends Fragment {

    private String selectedLastFrostDate;

    private FragmentSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SharedPreferences sp = getActivity().getSharedPreferences("settings", 0);
        TextView currentLastFrostDate = root.findViewById(R.id.textViewCurrentLastFrostDate);
        currentLastFrostDate.setText(
                getString(R.string.current_frost_date, sp.getString(MainActivity.FROST_SETTING,"1/1")));

        return root;
    }

    public String getSelectedLastFrostDate() {
        return getSelectedLastFrostDate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}