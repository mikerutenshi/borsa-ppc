package com.android.borsappc.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "works")
data class Work(
    @PrimaryKey
    @SerializedName("work_id")
    val id: Int?,
    @SerializedName("spk_no")
    val spkNo: Int?,
    @SerializedName("product_id")
    val productId: Int?,
    @SerializedName("article_no")
    val articleNo: String?,
    @SerializedName("product_category_name")
    val productCategoryName: String?,
    @SerializedName("product_quantity")
    val qty: Int?,
    @SerializedName("is_drawn")
    val isDrawn: Boolean,
    @SerializedName("is_sewn")
    val isSewn: Boolean,
    @SerializedName("is_assembled")
    val isAssembled: Boolean,
    @SerializedName("is_sole_stitched")
    val isSoleStitched: Boolean,
    @SerializedName("is_lining_drawn")
    val isLiningDrawn: Boolean,
    @SerializedName("is_insole_stitched")
    val isInsoleStitched: Boolean,
    @SerializedName("drawing_cost")
    val drawingCost: Int,
    @SerializedName("sewing_cost")
    val sewingCost: Int,
    @SerializedName("assembling_cost")
    val assemblingCost: Int,
    @SerializedName("sole_stitching_cost")
    val soleStitchingCost: Int,
    @SerializedName("lining_drawing_cost")
    val liningDrawingCost: Int,
    @SerializedName("insole_stitching_cost")
    val insoleStitchingCost: Int,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("notes")
    val notes: String?,
    @Transient
    var isChecked: Boolean,
    @Transient
    var isSelected: Boolean
    )