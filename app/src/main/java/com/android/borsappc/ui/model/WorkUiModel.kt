package com.android.borsappc.ui.model

import java.time.LocalDateTime

enum class WorkStatus {
    NOT_AVAILABLE, AVAILABLE, IN_PROGRESS, COMPLETED
}

data class WorkUiModel(
    var id: Int = 123,
    var spkNo: Int = 1234,
    var productId: Int = 123,
    var articleNo: String = "A12345",
    var productCategoryName: String = "Men Shoes",
    var quantity: Int = 12,
    var drawStatus: WorkStatus = WorkStatus.AVAILABLE,
    var liningDrawStatus: WorkStatus = WorkStatus.AVAILABLE,
    var sewStatus: WorkStatus = WorkStatus.AVAILABLE,
    var assembleStatus: WorkStatus = WorkStatus.AVAILABLE,
    var soleStitchStatus: WorkStatus = WorkStatus.NOT_AVAILABLE,
    var insoleStitchStatus: WorkStatus = WorkStatus.NOT_AVAILABLE,
    var createdAt: String = LocalDateTime.now().toString(),
    var updatedAt: String? = null,
    var notes: String? = null,
    @Transient
    var isSelected: Boolean = false
)
