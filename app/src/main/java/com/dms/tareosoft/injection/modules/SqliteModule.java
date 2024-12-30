package com.dms.tareosoft.injection.modules;

import android.content.Context;

import com.dms.tareosoft.injection.scope.ApplicationScope;
import com.dms.tareosoft.data.source.local.DbTareo;

import dagger.Module;
import dagger.Provides;

@Module
public class SqliteModule {

    @Provides
    @ApplicationScope
    public DbTareo provideDBApplication(Context context){
        return DbTareo.getDatabase(context);
    }

}