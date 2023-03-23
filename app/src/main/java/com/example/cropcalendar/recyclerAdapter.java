package com.example.cropcalendar;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {
    private final ArrayList<Crop> cropList;

    public recyclerAdapter(ArrayList<Crop> cropList) {
        this.cropList = cropList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.crop_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Crop current = cropList.get(position);

        holder.current = current;
        holder.nameText.setText(current.getName());
        holder.plantDateText.setText(Integer.toString(current.getDaysToPlantBeforeLastFrost()));
        holder.germDateText.setText(Integer.toString(current.getDaysToGerm()));
        holder.harvestDateText.setText(Integer.toString(current.getDaysToHarvest()));
        // TODO: figure out why plant germ and harvest are mixed up - probs cdb
        // Sun level icon
        switch (current.getSun()) {
            case FULL_SUN:
                holder.sunIcon.setImageResource(R.drawable.ic_full_sun);
                break;
            case PART_SUN:
                holder.sunIcon.setImageResource(R.drawable.ic_part_sun);
                break;
            case PART_SHADE:
                holder.sunIcon.setImageResource(R.drawable.ic_part_shade);
                break;
            default:
                holder.sunIcon.setImageResource(R.drawable.ic_full_shade);
        }
    }

    @Override
    public int getItemCount() {
        return cropList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameText;
        private final TextView plantDateText;
        private final TextView germDateText;
        private final TextView harvestDateText;
        private final ImageView sunIcon;
        private Crop current;

        public MyViewHolder(View view) {
            super(view);
            nameText = view.findViewById(R.id.textViewListName);
            plantDateText = view.findViewById(R.id.textViewListPlantDate);
            germDateText = view.findViewById(R.id.textViewListGermDate);
            harvestDateText = view.findViewById(R.id.textViewListHarvestDate);
            sunIcon = view.findViewById(R.id.imageViewListSun);

            // View crop details
            view.findViewById(R.id.buttonListDetails).setOnClickListener(buttonView -> {
                Context context = buttonView.getContext();

                // Dialog to show details
                DetailsDialog detailsDialog = new DetailsDialog((Activity) context, current);
                detailsDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context,
                        R.drawable.dialog_rounded_corners_background)); // Rounded dialog corners
                detailsDialog.show();
            });

            // Edit crop
            view.findViewById(R.id.buttonListEdit).setOnClickListener(buttonView -> {
                Context context = buttonView.getContext();

                // Dialog to edit crop
                EditDialog editDialog = new EditDialog((Activity) context, current);
                editDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context,
                        R.drawable.dialog_rounded_corners_background)); // Rounded dialog corners
                editDialog.show(); // TODO: edit dialog
            });
        }
    }
}
