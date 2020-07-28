package com.example.realmwordsample

import android.app.Application
import io.realm.Realm

class WordApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}