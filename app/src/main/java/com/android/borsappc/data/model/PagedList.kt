package com.android.borsappc.data.model

data class PagedList<K, T> (
    val data: ArrayList<T>,
    val nextKey: K?,
    val prevKey: K?
) {
    constructor(data: ArrayList<T>, nextKey: K?): this(data, nextKey, null)
}
