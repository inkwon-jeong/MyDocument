package com.hariofspades.dagger2advanced.di.component;

import com.hariofspades.dagger2advanced.di.module.PicassoModule;
import com.hariofspades.dagger2advanced.di.scope.RandomUserApplicationScope;
import com.hariofspades.dagger2advanced.di.module.RandomUserModule;
import com.hariofspades.dagger2advanced.interfaces.RandomUsersApi;
import com.squareup.picasso.Picasso;

import dagger.Component;

@RandomUserApplicationScope
@Component(modules = {RandomUserModule.class, PicassoModule.class})
public interface RandomUserComponent {
    RandomUsersApi getRandomUserService();
    Picasso getPicasso();
}
