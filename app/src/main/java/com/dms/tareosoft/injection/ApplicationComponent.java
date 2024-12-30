package com.dms.tareosoft.injection;

import android.content.Context;

import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.injection.modules.ApplicationModule;
import com.dms.tareosoft.injection.modules.ContextModule;
import com.dms.tareosoft.injection.modules.NetworkModule;
import com.dms.tareosoft.injection.modules.SqliteModule;
import com.dms.tareosoft.injection.modules.WebServiceModule;
import com.dms.tareosoft.injection.scope.ApplicationScope;
import com.dms.tareosoft.data.source.local.DbTareo;
import com.dms.tareosoft.data.source.remote.WebService;
import com.dms.tareosoft.workmanager.SendDataWorker;

import javax.inject.Named;
import dagger.Component;
import io.reactivex.Scheduler;

@ApplicationScope
@Component(modules = {ContextModule.class, SqliteModule.class, NetworkModule.class, WebServiceModule.class, ApplicationModule.class})
public interface ApplicationComponent {

    Context context();
    PreferenceManager preferenceManager();
    DbTareo dbTareo();
    WebService webService();

    @Named("executor_thread")
    Scheduler executorThread();

    @Named("ui_thread")
    Scheduler uiThread();

    void inject(SendDataWorker worker);
}
