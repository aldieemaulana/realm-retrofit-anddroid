package com.ismealdi.lagu.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ismealdi.lagu.R
import com.ismealdi.lagu.model.Event
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.item_event_list.view.*

class EventAdapter(private var events: RealmResults<Event>, private val context: Context) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    val realm = Realm.getDefaultInstance()!!

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: AppCompatTextView = itemView.textName
        val layoutParent: ConstraintLayout = itemView.parentLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = events[position]

        holder.name.text = event?.name
    }

    override fun getItemCount(): Int {
        return events.size
    }

}

