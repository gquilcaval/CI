package com.dms.tareosoft.injection.modules;

import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.injection.scope.ApplicationScope;
import com.dms.tareosoft.data.source.remote.WebService;
import com.dms.tareosoft.util.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = NetworkModule.class)
public class WebServiceModule {

    public WebServiceModule(){}

    @Provides
    @ApplicationScope
    public WebService webService(Retrofit retrofit){
        return retrofit.create(WebService.class);
    }

    @Provides
    public Gson gson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }

    @Provides
    @ApplicationScope
    public Retrofit retrofit(Gson gson, OkHttpClient okhttpClient, PreferenceManager preferenceManager){
        return new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okhttpClient)
                .baseUrl(Constants.URL_SECURITY + preferenceManager.getWebService())
                .build();
    }
}