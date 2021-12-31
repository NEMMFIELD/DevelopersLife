package com.example.developerslifesupreme.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Response(
	@SerializedName("result")
	val result: List<ResultItem>,

	@SerializedName("totalCount")
	val totalCount: Int? = null
) : Parcelable

@Parcelize
data class ResultItem(
	@SerializedName("date")
	val date: String? = null,

	@SerializedName("previewURL")
	val previewURL: String? = null,

	@SerializedName("author")
	val author: String? = null,

	@SerializedName("description")
	val description: String? = null,

	@SerializedName("type")
	val type: String? = null,

	@SerializedName("videoSize")
	val videoSize: Int? = null,

	@SerializedName("gifURL")
	val gifURL: String? = null,

	@SerializedName("videoPath")
	val videoPath: String? = null,

	@SerializedName("videoURL")
	val videoURL: String? = null,

	@SerializedName("fileSize")
	val fileSize: Int? = null,

	@SerializedName("gifSize")
	val gifSize: Int? = null,

	@SerializedName("commentsCount")
	val commentsCount: Int? = null,

	@SerializedName("width")
	val width: String? = null,

	@SerializedName("votes")
	val votes: Int? = null,

	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("height")
	val height: String? = null,

	@SerializedName("canVote")
	val canVote: Boolean? = null
) : Parcelable
