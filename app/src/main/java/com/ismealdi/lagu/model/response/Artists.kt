package com.ismealdi.lagu.model.response
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import java.io.Serializable


/**
 * Created by Al on 28/09/2018
 */

data class Artists(
    val message: Message?
) : Serializable

data class Message(
    val header: Header?,
    val body: Body?
)

data class Header(
    @SerializedName("status_code")
    val statusCode: Int?,
    @SerializedName("execute_time")
    val executeTime: Double?,
    val available: Int?
)

data class Body(
    @SerializedName("artist_list")
    val artistList: List<ArtistList?>?
)

data class ArtistList(
    val artist: ArtistDetail?
)

open class ArtistDetail (
        @SerializedName("artist_id")
    val artistId: Long?,
        @SerializedName("artist_mbid")
    val artistMbid: String?,
        @SerializedName("artist_name")
    val artistName: String?,
        @SerializedName("artist_name_translation_list")
    val artistNameTranslationList: List<Any?>?,
        @SerializedName("artist_comment")
    val artistComment: String?,
        @SerializedName("artist_country")
    val artistCountry: String?,
        @SerializedName("artist_alias_list")
    val artistAliasList: List<Any?>?,
        @SerializedName("artist_rating")
    val artistRating: Int?,
        @SerializedName("primary_genres")
    val primaryGenres: PrimaryGenres?,
        @SerializedName("secondary_genres")
    val secondaryGenres: SecondaryGenres?,
        @SerializedName("artist_twitter_url")
    val artistTwitterUrl: String?,
        @SerializedName("artist_vanity_id")
    val artistVanityId: String?,
        @SerializedName("artist_edit_url")
    val artistEditUrl: String?,
        @SerializedName("artist_share_url")
    val artistShareUrl: String?,
        @SerializedName("artist_credits")
    val artistCredits: ArtistCredits?,
        val restricted: Int?,
        val managed: Int?,
        @SerializedName("updated_time")
    val updatedTime: String? = "",
        var selected: Boolean = false
) {
}

data class ArtistCredits(
    @SerializedName("artist_list")
    val artistList: List<ArtistList?>?
)

data class PrimaryGenres(
    @SerializedName("music_genre_list")
    val musicGenreList: List<MusicGenreList?>?
)

data class MusicGenreList(
    @SerializedName("music_genre")
    val musicGenre: MusicGenre?
)

data class MusicGenre(
    @SerializedName("music_genre_id")
    val musicGenreId: Int?,
    @SerializedName("music_genre_parent_id")
    val musicGenreParentId: Int?,
    @SerializedName("music_genre_name")
    val musicGenreName: String?,
    @SerializedName("music_genre_name_extended")
    val musicGenreNameExtended: String?,
    @SerializedName("music_genre_vanity")
    val musicGenreVanity: String?
)

data class ArtistNameTranslation(
    @SerializedName("artist_name_translation")
    val artistNameTranslation: ArtistNameTranslationDetail?
)

data class ArtistNameTranslationDetail(
    val language: String?,
    val translation: String?
)

data class SecondaryGenres(
    @SerializedName("music_genre_list")
    val musicGenreList: List<MusicGenreList?>
)

data class ArtistAlias(
    @SerializedName("artist_alias")
    val artistAlias: String?
)