package com.example.android.dagger.di

import android.content.Context
import com.example.android.dagger.login.LoginComponent
import com.example.android.dagger.registration.RegistrationComponent
import com.example.android.dagger.splash.SplashComponent
import com.example.android.dagger.storage.StorageModule
import com.example.android.dagger.user.UserManager
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

// Definition of a Dagger component
@Singleton
@Component(modules = [StorageModule::class, AppSubcomponents::class])
interface AppComponent {

    // Factory to create instances of the AppComponent
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context): AppComponent
    }

    // Expose RegistrationComponent factory from the graph
    fun registrationComponent(): RegistrationComponent.Factory
    fun loginComponent(): LoginComponent.Factory
    fun splashComponent(): SplashComponent.Factory

    // Expose UserManager so that MainActivity and SettingsActivity
    // can access a particular instance of UserComponent
    fun userManager(): UserManager
}
