package com.dms.tareosoft;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.dms.tareosoft.injection.ApplicationComponent;

import com.dms.tareosoft.injection.DaggerApplicationComponent;
import com.dms.tareosoft.injection.modules.ContextModule;
//import com.testfairy.TestFairy;

public class App extends MultiDexApplication {
    public static ApplicationComponent appComponent;
    public static App instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        buildDependecyInjection();
        //TestFairy.begin(this, "SDK-qVW6qOAe");
    }

    public static void buildDependecyInjection() {
        appComponent = DaggerApplicationComponent.builder().contextModule(new ContextModule(instance)).build();
    }

    public ApplicationComponent getAppComponent() {
        if (appComponent == null) {
            buildDependecyInjection();
        }
        return appComponent;
    }

    public static App getInstance() {
        if (instance == null)
            buildInstance();
        return instance;
    }

    public void clearDaggerComponent() {
        appComponent = null;
    }

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    private static void buildInstance() {
        instance = new App();
    }
}