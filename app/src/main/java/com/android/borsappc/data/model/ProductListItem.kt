package com.android.borsappc.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

@Entity(tableName = "products")
data class ProductListItem(
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("code")
    var code: String = "11011-bk/br",
    @SerializedName("name")
    var name: String = "Tazia",
    @SerializedName("sub_category")
    var subCategory: String = "Mid-cut Boot",
    @SerializedName("created_at")
    var createdAt: String = LocalDateTime.now().toString()
)
