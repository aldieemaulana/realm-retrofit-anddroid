package com.ismealdi.lagu.fragment

import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.ismealdi.lagu.App
import com.ismealdi.lagu.R
import com.ismealdi.lagu.activity.MainActivity
import com.ismealdi.lagu.adapter.ArtistListAdapter
import com.ismealdi.lagu.base.BaseFragment
import com.ismealdi.lagu.logger.AlLog
import com.ismealdi.lagu.model.response.ArtistList
import com.ismealdi.lagu.model.response.Artists
import com.ismealdi.lagu.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_find.*
import com.ismealdi.lagu.api.Artist as API


class FindFragment : BaseFragment() {

    private val artist by lazy { API.create() }
    private var userLinearLayout = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
    private var nextPage : Boolean = false
    private var isSearch : Boolean = false
    private var totalPage : Long = 0
    private var currentPage : Int = 1
    private val perPage : Int = 100
    private var queryCurrent : String = ""
    private var adapter : ArtistListAdapter? = null
    private var datas: List<ArtistList?> = arrayListOf()

    private lateinit var mActivity : MainActivity
    private lateinit var artists: Artists

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mActivity = (activity as MainActivity)
        listener()
    }

    private fun listener() {
        buttonSearch.setOnClickListener {
            if(!textArtistName.text.toString().isEmpty()) {
                searchArtist(textArtistName.text.toString(), currentPage, perPage)
            }
        }

        mActivity.nestedScrollView.setOnScrollChangeListener { v: NestedScrollView?, _: Int, scrollY: Int, _: Int, oldScrollY: Int ->
            if (v != null) {
                if(v.getChildAt(v.childCount - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight)) &&
                            scrollY > oldScrollY) {
                        if(nextPage && fragmentManager?.findFragmentByTag(Constants.FRAGMENT.FIND.NAME)!!.isVisible) {
                            currentPage++
                            searchArtist(queryCurrent, currentPage, perPage)
                        }
                    }
                }
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_find, container, false)

    }

    private fun searchArtist(q: String, p: Int, pP: Int) {
        if(!isSearch) {
            isSearch = true
            mActivity.showProgress()
            mActivity.disposable = artist.search(q, pP, p, App.TOKEN)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { result ->
                                val count = result.message?.body?.artistList?.count()!!
                                if (count > 0) {
                                    totalPage += count
                                    initList(result.message.body.artistList, q)
                                    nextPage = (result.message.header?.available!! > totalPage && result.message.header.available > perPage)
                                    artists = result
                                    queryCurrent = q
                                }else{
                                    doClearList()
                                }

                                mActivity.hideProgress()
                                isSearch = false
                            },
                            { error ->
                                doClearList()
                                AlLog.e("${error.message}")
                                mActivity.hideProgress()
                                isSearch = false
                            }
                    )
        }
    }

    private fun doClearList() {
        if(adapter != null) {
            queryCurrent = ""
            totalPage = 0
            currentPage = 1
            adapter!!.clear()
            adapter!!.notifyItemRangeChanged(0, 0)
        }
    }

    private fun initList(artists: List<ArtistList?>, q: String) {
        datas = artists
        checkStateList()

        if(adapter == null) {
            adapter = ArtistListAdapter(artists as MutableList<ArtistList?>, this.context!!)

            recyclerView.layoutManager = userLinearLayout
            recyclerView.adapter = adapter

        }else{
            if(queryCurrent != q || (artists.count() == 1 && currentPage == 1)) {
                adapter!!.clear()
            }

            adapter!!.addAll(artists)
            adapter!!.notifyItemRangeChanged(0, adapter!!.itemCount)
        }

    }

    fun checkStateList() {
        datas.forEach {
            if (it != null) {
                it.artist?.selected = mActivity.isWishList(it.artist?.artistId.toString(), it.artist?.artistName.toString())
            }
        }

        if (adapter != null) {
            adapter!!.notifyDataSetChanged()
        }
    }


}
