package com.lahacks.fyp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lahacks.fyp.R;
import com.lahacks.fyp.models.App;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder>{

    Context context;
    List<App> apps;
    Set<String> selectedApps;

    public AppAdapter(Context context, List<App> apps) {
        this.context = context;
        this.apps = apps;
        selectedApps = new HashSet<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_app, parent, false);
        return new ViewHolder(movieView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
//        Log.d("MovieAdapter", "onBindViewHolder " + position);
        // Get movie at the passed in position
        App app = apps.get(position);
        // Bind the movie data into the VH
        holder.bind(app);
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout container;
        TextView appName;
        ImageView appIcon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            appName = itemView.findViewById(R.id.appName);
            appIcon = itemView.findViewById(R.id.appIcon);
            container = itemView.findViewById(R.id.container);
        }

        public void bind(final App app) {

            // check if item is selected
            if(selectedApps.contains(app.getName())) {
                container.setBackgroundColor(Color.parseColor("#AFDCFF"));
            }

            appName.setText(app.getName());
            appIcon.setImageDrawable(app.getIcon());

            // 1. Register click listener on the whole row
            // 2. Navigate to a new activity on tap
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectedApps.contains(app.getName())) {
                        container.setBackgroundColor(0x00000000);
                        selectedApps.remove(appName.getText().toString());
                    } else {
                        container.setBackgroundColor(Color.parseColor("#AFDCFF"));
                        selectedApps.add(appName.getText().toString());
                    }
                }
            });

        }
    }
}
