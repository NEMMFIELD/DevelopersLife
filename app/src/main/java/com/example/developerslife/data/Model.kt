package com.example.developerslife.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Model (
    @SerializedName("gifURL") var url:String? = null,
    @SerializedName("description")var description:String? = null
) : Parcelable