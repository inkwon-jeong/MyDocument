package com.hariofspades.dagger2advanced.di.module;

import android.app.Activity;
import android.content.Context;

import com.hariofspades.dagger2advanced.di.qualifier.ActivityContext;
import com.hariofspades.dagger2advanced.di.scope.RandomUserApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final Context context;

    ActivityModule(Activity context){
        this.context = context;
    }

    @ActivityContext
    @RandomUserApplicationScope
    @Provides
    public Context context(){ return context; }
}

