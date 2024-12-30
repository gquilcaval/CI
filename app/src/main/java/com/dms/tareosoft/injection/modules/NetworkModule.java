package com.dms.tareosoft.injection.modules;

import android.content.Context;


import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.injection.scope.ApplicationScope;

import java.io.File;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@Module
public class NetworkModule {

    @Provides
    @ApplicationScope
    public HttpLoggingInterceptor httpLoggingInterceptor(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    @Provides
    @ApplicationScope
    public Cache cache(File cacheFile){
        return new Cache(cacheFile, 10 * 1000 * 1000);
    }

    @Provides
    @ApplicationScope
    public File cacheFile(Context context){
        return new File(context.getCacheDir(), "okhttp_cache");
    }

    @Provides
    @ApplicationScope
    public OkHttpClient okhttpClient(HttpLoggingInterceptor httpLoggingInterceptor, Cache cache, PreferenceManager preferenceManager){
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(preferenceManager.getTimeOut(), TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
               // .cache(cache)
                .build();
    }
}