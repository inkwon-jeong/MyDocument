package com.example.realmwordsample

class WordRepository(
    private val wordDao: WordDao
) {

//    val allWords = wordDao.getAlphabetizedWords()
    val words = wordDao.getWords()

    fun insert(word: Word) {
        wordDao.insert(word)
    }

    fun delete(content: String) {
        wordDao.delete(content)
    }

    fun deleteAll() {
        wordDao.deleteAll()
    }
}