package com.ismealdi.lagu.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ismealdi.lagu.R
import com.ismealdi.lagu.activity.MainActivity
import com.ismealdi.lagu.model.WishList
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.item_artist_list.view.*

class WishListAdapter(private var artists: RealmResults<WishList>, private val context: Context) : RecyclerView.Adapter<WishListAdapter.ViewHolder>() {

    val realm = Realm.getDefaultInstance()!!

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fullName: AppCompatTextView = itemView.textArtistName
        val rating: AppCompatTextView = itemView.textRating
        val wishList: AppCompatTextView = itemView.textWishList
        val layoutParent: ConstraintLayout = itemView.parentLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_artist_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val artist = artists[position]

        holder.fullName.text = artist?.artistName
        holder.rating.text = ((artist?.artistRating!! / 10).toDouble()).toString()
        holder.wishList.visibility = View.INVISIBLE

        holder.layoutParent.setOnClickListener{
            notifyItemRemoved(holder.adapterPosition)

            val results = this.realm.where<WishList>().equalTo("mbId", artist?.mbId).findFirst()
            if(results?.isValid!!) {
                this.realm.executeTransaction { _ ->
                    results.deleteFromRealm()
                }
            }

            (context as MainActivity).checkStateList()
        }

    }

    override fun getItemCount(): Int {
        return artists.size
    }

    fun addAll(artist : RealmResults<WishList>) {
        this.artists = artist
        notifyDataSetChanged()
    }
}
