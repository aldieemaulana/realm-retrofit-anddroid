package com.ismealdi.lagu.api

import com.ismealdi.lagu.model.response.Artists
import com.ismealdi.lagu.utils.Networks
import io.reactivex.Observable
import retrofit2.http.*

/**
 * Created by Al on 28/09/2018
 */

interface Artist {

    @GET("artist.search")
    @Headers("Content-Type: application/json")
    fun search(@Query("q_artist") keyword: String,
               @Query("page_size") perPage: Int,
               @Query("page") page: Int,
               @Query("apikey") apiKey: String): Observable<Artists>

    companion object {
        fun create(): Artist {
            return Networks().retrofit.create(Artist::class.java)
        }
    }

}