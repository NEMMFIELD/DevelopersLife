package com.example.developerslife.data

import com.google.gson.annotations.SerializedName

data class Model (
    @SerializedName("gifURL") var url:String? = null,
    @SerializedName("description")var description:String? = null
)