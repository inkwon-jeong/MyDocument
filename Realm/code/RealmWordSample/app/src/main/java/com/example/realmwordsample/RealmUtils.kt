package com.example.realmwordsample

import io.realm.RealmModel
import io.realm.RealmResults

fun <T: RealmModel> RealmResults<T>.asLiveData() =
    RealmLiveData(this)