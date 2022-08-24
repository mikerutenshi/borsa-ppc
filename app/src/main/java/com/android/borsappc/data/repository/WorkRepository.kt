package com.android.borsappc.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.android.borsappc.data.db.AppDatabase
import com.android.borsappc.data.model.Position
import com.android.borsappc.data.model.WorkQuery
import com.android.borsappc.di.RetrofitWithAuth
import com.android.borsappc.ui.model.WorkStatus
import com.android.borsappc.ui.model.WorkUiModel
import kotlinx.coroutines.flow.map
import retrofit2.Retrofit
import javax.inject.Inject

class WorkRepository @Inject constructor(
    private val database: AppDatabase,
    @RetrofitWithAuth private val retrofit: Retrofit
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getWorks(query: WorkQuery) = Pager(
        config = PagingConfig(pageSize = 10),
        remoteMediator = WorkRemoteMediator(
            query,
            database,
            retrofit
        )
    ) {
        database.workDao().pagingSource()
    }.flow.map { pagingData ->
        pagingData.map { work ->
            var drawStatus = WorkStatus.AVAILABLE
            var liningDrawStatus = WorkStatus.AVAILABLE
            var sewStatus = WorkStatus.AVAILABLE
            var assembleStatus = WorkStatus.AVAILABLE
            var soleStitchStatus = if (work.soleStitchingCost != 0) WorkStatus.AVAILABLE
                else WorkStatus.NOT_AVAILABLE
            var insoleStitchStatus = if (work.insoleStitchingCost != 0) WorkStatus.AVAILABLE
                else WorkStatus.NOT_AVAILABLE

            for (wip in work.workInProgress) {
                when (wip.position) {
                    Position.DRAWER -> drawStatus = WorkStatus.IN_PROGRESS
                    Position.LINING_DRAWER -> liningDrawStatus = WorkStatus.IN_PROGRESS
                    Position.SEWER -> sewStatus = WorkStatus.IN_PROGRESS
                    Position.ASSEMBLER -> assembleStatus = WorkStatus.IN_PROGRESS
                    Position.SOLE_STITCHER -> soleStitchStatus = WorkStatus.IN_PROGRESS
                    Position.INSOLE_STITCHER -> insoleStitchStatus = WorkStatus.IN_PROGRESS
                }
            }

            for (wd in work.workDone) {
                when (wd.position) {
                    Position.DRAWER -> drawStatus = WorkStatus.COMPLETED
                    Position.LINING_DRAWER -> liningDrawStatus = WorkStatus.COMPLETED
                    Position.SEWER -> sewStatus = WorkStatus.COMPLETED
                    Position.ASSEMBLER -> assembleStatus = WorkStatus.COMPLETED
                    Position.SOLE_STITCHER -> soleStitchStatus = WorkStatus.COMPLETED
                    Position.INSOLE_STITCHER -> insoleStitchStatus = WorkStatus.COMPLETED
                }
            }

            WorkUiModel(
                id = work.id,
                spkNo = work.spkNo,
                productId = work.productId,
                articleNo = work.articleNo,
                productCategoryName = work.productCategoryName,
                quantity = work.quantity,
                drawStatus = drawStatus,
                liningDrawStatus = liningDrawStatus,
                sewStatus = sewStatus,
                assembleStatus = assembleStatus,
                soleStitchStatus = soleStitchStatus,
                insoleStitchStatus = insoleStitchStatus,
                createdAt = work.createdAt,
                updatedAt = work.updatedAt,
                notes = work.notes
            )
        }
    }
}