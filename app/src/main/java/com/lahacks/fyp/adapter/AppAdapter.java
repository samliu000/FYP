package com.lahacks.fyp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lahacks.fyp.R;
import com.lahacks.fyp.models.App;

import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder>{

    Context context;
    List<App> apps;
    List<String> selectedApps;

    public AppAdapter(Context context, List<App> apps) {
        this.context = context;
        this.apps = apps;
        selectedApps = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_app1, parent, false);
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

            appName.setText(app.getName());
            appIcon.setImageDrawable(app.getIcon());

            // 1. Register click listener on the whole row
            // 2. Navigate to a new activity on tap
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("AppAdapter", "Changing background");
                    container.setBackgroundColor(Color.parseColor("#AFDCFF"));
                    selectedApps.add(appName.getText().toString());
                }
            });

        }
    }
}
