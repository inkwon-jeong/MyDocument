package com.hariofspades.dagger2advanced.di.component;

import com.hariofspades.dagger2advanced.MainActivity;
import com.hariofspades.dagger2advanced.adapter.RandomUserAdapter;
import com.hariofspades.dagger2advanced.di.module.MainActivityModule;
import com.hariofspades.dagger2advanced.di.scope.MainActivityScope;
import com.hariofspades.dagger2advanced.interfaces.RandomUsersApi;

import dagger.Component;

@MainActivityScope
@Component(modules = MainActivityModule.class, dependencies = RandomUserComponent.class)
public interface MainActivityComponent {
    void injectMainActivity(MainActivity activity);
}
