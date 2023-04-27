package com.android.borsappc.data.model

import Position
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

@Entity(tableName = "works")
data class Work(
    @PrimaryKey
    @SerializedName("work_id")
    var id: Int = 123,
    @ColumnInfo(name = "spk_no")
    @SerializedName("spk_no")
    var spkNo: Int = 1234,
    @SerializedName("product_id")
    var productId: Int = 123,
    @ColumnInfo(name = "article_no")
    @SerializedName("article_no")
    var articleNo: String = "A12345",
    @SerializedName("product_category_name")
    var productCategoryName: String = "Men Shoes",
    @SerializedName("product_quantity")
    var quantity: Int = 12,
    @SerializedName("work_in_progress")
    var workInProgress: List<WorkInProgress> = listOf(
        WorkInProgress(Position.DRAWER),
        WorkInProgress(Position.ASSEMBLER),
        WorkInProgress(Position.INSOLE_STITCHER)
    ),
    @SerializedName("work_done")
    var workDone: List<WorkDone> = listOf(
        WorkDone(Position.DRAWER, 5),
        WorkDone(Position.ASSEMBLER, 5),
        WorkDone(Position.INSOLE_STITCHER, 5)
    ),
    @SerializedName("drawing_cost")
    var drawingCost: Int = 1234,
    @SerializedName("sewing_cost")
    var sewingCost: Int = 1234,
    @SerializedName("assembling_cost")
    var assemblingCost: Int = 1234,
    @SerializedName("sole_stitching_cost")
    var soleStitchingCost: Int = 1234,
    @SerializedName("lining_drawing_cost")
    var liningDrawingCost: Int = 1234,
    @SerializedName("insole_stitching_cost")
    var insoleStitchingCost: Int = 0,
    @ColumnInfo(name = "created_at")
    @SerializedName("created_at")
    var createdAt: String = LocalDateTime.now().toString(),
    @SerializedName("updated_at")
    var updatedAt: String? = null,
    @SerializedName("notes")
    var notes: String? = "abcdefg",
)

data class WorkInProgress(
    var position: String
)

data class WorkDone(
    @SerializedName("position")
    var position: String,
    @SerializedName("sum")
    var doneQuantity: Int
)
