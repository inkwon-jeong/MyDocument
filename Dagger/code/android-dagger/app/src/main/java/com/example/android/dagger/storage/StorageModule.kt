package com.example.android.dagger.storage

import android.content.Context
import com.example.android.dagger.storage.SharedPreferencesStorage
import com.example.android.dagger.storage.Storage
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class RegistrationStorage

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class LoginStorage

// Tells Dagger this is a Dagger module
// Because of @Binds, StorageModule needs to be an abstract class
@Module
abstract class StorageModule {

    // Makes Dagger provide SharedPreferencesStorage when a Storage type is requested
    @Binds
    abstract fun provideStorage(storage: SharedPreferencesStorage): Storage

//    @RegistrationStorage
//    @Provides
//    fun provideRegistrationStorage(context: Context): Storage {
//        return SharedPreferencesStorage(context)
//    }
//
//    @LoginStorage
//    @Provides
//    fun provideLoginStorage(context: Context): Storage {
//        return SharedPreferencesStorage(context)
//    }
}

