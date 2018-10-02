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
import com.ismealdi.lagu.model.response.ArtistDetail
import com.ismealdi.lagu.model.response.ArtistList
import io.realm.Realm
import io.realm.kotlin.createObject
import kotlinx.android.synthetic.main.item_artist_list.view.*

class ArtistListAdapter(private var artists: MutableList<ArtistList?>, private val context: Context) : RecyclerView.Adapter<ArtistListAdapter.ViewHolder>() {
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
        (context as MainActivity)

        val artist = artists[holder.adapterPosition]?.artist

        holder.fullName.text = artist?.artistName
        holder.rating.text = ((artist?.artistRating!! / 10).toDouble()).toString()

        if(artist.selected){
            holder.wishList.setBackgroundDrawable(context.resources.getDrawable(R.drawable.ic_wishing_enable))
        }else{
            holder.wishList.setBackgroundDrawable(context.resources.getDrawable(R.drawable.ic_wishing_disable))

            holder.layoutParent.setOnClickListener{
                artist.selected = !artist.selected
                addWishlist(artist, context, holder.adapterPosition)
                context.refreshWishList()
            }

        }
    }

    private fun addWishlist(artist: ArtistDetail?, activity: MainActivity, position: Int) {
        val results = activity.getWishList(artist?.artistId!!.toString(), artist.artistName!!)
        this.realm.executeTransaction { _ ->
            if (results != null) {
                results.deleteFromRealm()

            }else{
                val shared= realm.createObject<WishList>(artist?.artistId.toString())
                shared.mbId = artist.artistId.toString()!!
                shared.wishList = true
                shared.artistName = artist.artistName!!
                shared.artistRating = artist.artistRating!!
            }

            notifyItemChanged(position)
        }

    }

    override fun getItemCount(): Int {
        return artists.size
    }

    fun addAll(artist: List<ArtistList?>) {
        artists.addAll(artist)
        notifyDataSetChanged()
    }

    fun clear() {
        artists.clear()
        notifyDataSetChanged()
    }

}
