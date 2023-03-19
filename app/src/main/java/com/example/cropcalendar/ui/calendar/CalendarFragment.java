package com.example.cropcalendar.ui.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cropcalendar.Crop;
import com.example.cropcalendar.CropDatabase;
import com.example.cropcalendar.R;
import com.example.cropcalendar.databinding.FragmentCalendarBinding;
import com.example.cropcalendar.recyclerAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class CalendarFragment extends Fragment {

    final private HashMap<String, String> filters = new HashMap<>();
    private FragmentCalendarBinding binding;
    private RecyclerView recyclerView;
    private final ArrayList<Crop> crops = new ArrayList<>();
    private TextView noResults;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CalendarViewModel calendarViewModel = new ViewModelProvider(this)
                .get(CalendarViewModel.class);

        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        noResults = root.findViewById(R.id.textViewNoResults);

        setAdapter(root);
        viewAllCrops(root); // By default show all crops
        setRecyclerViewVisibility();

        // TODO: DatePicker dateFilter = root.findViewById(R.id.lastFrostDatePicker);
        //dateFilter.setOnDateChangedListener(this::selectDate);
        Button viewAllButton = root.findViewById(R.id.buttonAllCrops);
        viewAllButton.setOnClickListener(this::viewAllCrops);
        Button filterButton = root.findViewById(R.id.buttonFilters);
        filterButton.setOnClickListener(this::selectFilters);

        resetFilters();
        return root;
    }

    /**
     * Creates recycler adapter and adds it to the recycler view
     */
    private void setAdapter(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewCropResults);
        recyclerAdapter adapter = new recyclerAdapter(crops);
        LinearLayoutManager layoutManager = new LinearLayoutManager((getContext()));
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividers = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividers);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
    // TODO: ASYNC when reading from cdb

    private void setRecyclerViewVisibility() {
        if (crops.isEmpty()) {
            // If there are no results, hide recyclerview and show text
            recyclerView.setAlpha(0);
            noResults.setAlpha(1);
        } else {
            // Results exist to display in recyclerview
            recyclerView.setAlpha(1);
            noResults.setAlpha(0);
        }
    }

    /**
     * Sets all filters to empty
     */
    private void resetFilters() {
        filters.put("date", "");
        filters.put("name", "");
        filters.put("cropType", "");
        filters.put("sunLevel", "");
        filters.put("today", "");
        filters.put("nextWeek", "");
        filters.put("nextMonth", "");
        filters.put("onlyPlant", "");
        filters.put("onlyGerm", "");
        filters.put("onlyHarvest", "");
    }

    /**
     * Search database for crops that match the applied filters
     *
     * @param view
     */
    public void selectFilters(View view) {
        // TODO: filters
        crops.clear();
        recyclerView.getAdapter().notifyDataSetChanged();
        setRecyclerViewVisibility();
        return;
    }

    /**
     * Get date from date picker and apply with filter
     *
     * @param datePicker Spinner date picker
     * @param year       Selected year
     * @param month      Selected month
     * @param day        Selected day
     */
    private void selectDate(DatePicker datePicker, int year, int month, int day) {
        // TODO: select date filter
    }

    /**
     * Reloads recyclerView to show all available crops from database
     *
     * @param view
     */
    public void viewAllCrops(View view) {
        // TODO: async me
        crops.clear(); // Clear old list
        CropDatabase cdb = CropDatabase.getInstance(getContext());
        crops.addAll(cdb.readAllCrops()); // Load list with all crops from database
        recyclerView.getAdapter().notifyDataSetChanged();
        setRecyclerViewVisibility();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}