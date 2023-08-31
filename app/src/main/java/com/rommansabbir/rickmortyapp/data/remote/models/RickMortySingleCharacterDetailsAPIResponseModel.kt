package com.rommansabbir.rickmortyapp.data.remote.models

import androidx.annotation.Keep
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import com.rommansabbir.rickmortyapp.utils.extensions.writeString


@Keep
class RickMortySingleCharacterDetailsAPIResponseModel {
    @SerializedName("id")
    @Expose
     var id: Int? = null

    @SerializedName("name")
    @Expose
     var name: String? = null
        set(value) {
            field = if (value == null) null else writeString { it.append(value) }
        }

    @SerializedName("status")
    @Expose
     var status: String? = null
        set(value) {
            field = if (value == null) null else writeString { it.append(value) }
        }

    @SerializedName("species")
    @Expose
     var species: String? = null
        set(value) {
            field = if (value == null) null else writeString { it.append(value) }
        }

    @SerializedName("type")
    @Expose
     var type: String? = null
        set(value) {
            field = if (value == null) null else writeString { it.append(value) }
        }

    @SerializedName("gender")
    @Expose
     var gender: String? = null
        set(value) {
            field = if (value == null) null else writeString { it.append(value) }
        }

    @SerializedName("origin")
    @Expose
     var origin: Origin? = null


    @SerializedName("location")
    @Expose
     var location: Location? = null

    @SerializedName("image")
    @Expose
     var image: String? = null
        set(value) {
            field = if (value == null) null else writeString { it.append(value) }
        }

    @SerializedName("episode")
    @Expose
     var episode: MutableList<String> = mutableListOf()

    @SerializedName("url")
    @Expose
     var url: String? = null
        set(value) {
            field = if (value == null) null else writeString { it.append(value) }
        }

    @SerializedName("created")
    @Expose
     var created: String? = null
        set(value) {
            field = if (value == null) null else writeString { it.append(value) }
        }

    companion object {
        @Keep
        class Origin {
            @SerializedName("name")
            @Expose
            var name: String? = null
                set(value) {
                    field = if (value == null) null else writeString { it.append(value) }
                }

            @SerializedName("url")
            @Expose
            var url: String? = null
                set(value) {
                    field = if (value == null) null else writeString { it.append(value) }
                }
        }

        @Keep
        class Location {
            @SerializedName("name")
            @Expose
            var name: String? = null
                set(value) {
                    field = if (value == null) null else writeString { it.append(value) }
                }

            @SerializedName("url")
            @Expose
            var url: String? = null
                set(value) {
                    field = if (value == null) null else writeString { it.append(value) }
                }
        }
    }
}