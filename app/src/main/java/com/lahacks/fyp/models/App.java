package com.lahacks.fyp.models;

import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

public class App {
    String name;
    String packageName;
    Drawable icon;

    public App(String name, String packageName, Drawable icon) {
            this.name = name;
            this.packageName = packageName;
            this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return packageName;
    }

    public Drawable getIcon() {
        return icon;
    }
}
