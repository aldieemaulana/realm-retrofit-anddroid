package com.ismealdi.lagu.fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.ismealdi.lagu.R
import com.ismealdi.lagu.activity.MainActivity
import com.ismealdi.lagu.adapter.WishListAdapter
import com.ismealdi.lagu.base.BaseFragment
import com.ismealdi.lagu.model.WishList
import io.realm.RealmResults
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_wish_list.*

class WishListFragment : BaseFragment() {

    private lateinit var mActivity : MainActivity
    private lateinit var adapter : WishListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initList()

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_wish_list, container, false)
    }

    private fun initList() {
        mActivity.showProgress()

        recyclerView.layoutManager = LinearLayoutManager(context,
                LinearLayout.VERTICAL, false)
        recyclerView.adapter = adapter
        mActivity.hideProgress()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        mActivity = (activity as MainActivity)
        adapter = WishListAdapter(mActivity.realm.where<com.ismealdi.lagu.model.WishList>().findAll(), this.context!!)

        mActivity.showProgress()
        adapter.addAll(mActivity.realm.where<WishList>().findAll())
        mActivity.hideProgress()
    }

    fun refreshWishList(data: RealmResults<WishList>) {
        adapter.addAll(data)
    }

}