package com.example.realmwordsample

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WordViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WordRepository
    private val realm: Realm = Realm.getDefaultInstance()

//    val allWords: LiveData<List<Word>>
    val words: RealmResults<Word>

    init {
        repository = WordRepository(
            WordDao(realm)
        )
//        allWords = Transformations.map(repository.allWords) { it }
        words = repository.words
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(word: Word) {
        repository.insert(word)
    }

    fun delete(content: String) {
        repository.delete(content)
    }
    fun deleteAll() {
        repository.deleteAll()
    }

    override fun onCleared() {
        super.onCleared()
        realm.close()
    }
}