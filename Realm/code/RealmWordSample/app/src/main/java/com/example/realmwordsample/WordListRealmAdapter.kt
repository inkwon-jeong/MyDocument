package com.example.realmwordsample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter

class WordListRealmAdapter(
    data: OrderedRealmCollection<Word>,
    autoUpdate: Boolean = true,
    private val click: (String) -> Unit
) : RealmRecyclerViewAdapter<Word, WordListRealmAdapter.WordViewHolder>(data, autoUpdate) {

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val wordItemView = itemView.findViewById<TextView>(R.id.textView)

        fun onBind(item: Word) {
            wordItemView.text = item.word
            wordItemView.setOnClickListener {
                click(item.word)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        getItem(position)?.let {
            holder.onBind(it)
        }
    }
}