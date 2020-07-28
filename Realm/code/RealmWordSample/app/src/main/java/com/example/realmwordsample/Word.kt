package com.example.realmwordsample

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Word (
    @PrimaryKey
    var word: String = ""
) : RealmObject()

