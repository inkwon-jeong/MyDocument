package com.hariofspades.dagger2advanced.di.module;

import com.hariofspades.dagger2advanced.MainActivity;
import com.hariofspades.dagger2advanced.adapter.RandomUserAdapter;
import com.hariofspades.dagger2advanced.di.scope.MainActivityScope;
import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityModule {

    private final MainActivity mainActivity;

    public MainActivityModule(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Provides
    @MainActivityScope
    public RandomUserAdapter randomUserAdapter(Picasso picasso){
        return new RandomUserAdapter(mainActivity, picasso);
    }
}

