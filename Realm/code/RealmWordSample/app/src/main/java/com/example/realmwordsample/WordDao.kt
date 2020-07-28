package com.example.realmwordsample

import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import io.realm.kotlin.where

class WordDao(private val realm: Realm) {

    fun getWords(): RealmResults<Word> = realm.where<Word>()
        .sort("word", Sort.ASCENDING)
        .findAll()

//    fun getAlphabetizedWords() = realm.where<Word>()
//        .sort("word", Sort.ASCENDING)
//        .findAllAsync()
//        .asLiveData()

    fun insert(word: Word) {
        realm.executeTransactionAsync {
            it.insert(word)
        }
    }

    fun delete(content: String) {
        realm.executeTransaction {
            it.where<Word>().equalTo("word", content).findAll().deleteAllFromRealm()
        }
    }

    fun deleteAll() {
        realm.executeTransaction {
            it.where<Word>().findAll().deleteAllFromRealm()
        }
    }
}