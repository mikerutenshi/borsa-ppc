package com.android.borsappc.data.model

data class PagedData<K, T> (
    val data: T,
    val nextKey: K?
    val prevKey: K?
) {
    constructor(data: T, nextKey: K?): this(data, nextKey, null)
}
