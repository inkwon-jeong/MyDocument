package com.hariofspades.dagger2advanced.di.module;

import android.content.Context;

import com.hariofspades.dagger2advanced.di.qualifier.ApplicationContext;
import com.hariofspades.dagger2advanced.di.scope.RandomUserApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {

    Context context;

    public ContextModule(Context context){
        this.context = context;
    }

    @ApplicationContext
    @RandomUserApplicationScope
    @Provides
    public Context context(){ return context.getApplicationContext(); }
}

