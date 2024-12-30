package com.dms.tareosoft.injection.modules;

import android.content.Context;

import com.dms.tareosoft.injection.scope.ApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {
    private Context context;

    public ContextModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    @ApplicationScope
    public Context provideContext() {
        return this.context;
    }
}