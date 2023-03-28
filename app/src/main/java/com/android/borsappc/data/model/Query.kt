package com.android.borsappc.data.model

interface Query  {
    var limit: Int
    var index: String?
    var orderBy: Pair<String, String>
    var orderDirection : String
    var searchKey : String?
}