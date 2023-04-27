package com.android.borsappc.data.repository

import Position
import androidx.datastore.core.DataStore
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.android.borsappc.Date
import com.android.borsappc.Order
import com.android.borsappc.UserPreferences
import com.android.borsappc.WorkFilterPrefs
import com.android.borsappc.data.db.AppDatabase
import com.android.borsappc.data.model.WorkQuery
import com.android.borsappc.data.repository.mediator.WorkRemoteMediator
import com.android.borsappc.di.RetrofitWithAuth
import com.android.borsappc.ui.model.WorkStatus
import com.android.borsappc.ui.model.WorkUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Retrofit
import javax.inject.Inject

class WorkRepository @Inject constructor(
    private val database: AppDatabase,
    @RetrofitWithAuth private val retrofit: Retrofit,
    private val userPrefs: DataStore<UserPreferences>
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
//        if (query.sortDirection == "asc") {
//            database.workDao().pagingSourceAsc(apiToRoomSortKey(query.sortBy))
//        } else  {
            database.workDao().getPagingSource(query)
//        }
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
                when  {
                    wd.position == Position.DRAWER && wd.doneQuantity == work.quantity ->
                        drawStatus = WorkStatus.COMPLETED
                    wd.position == Position.LINING_DRAWER && wd.doneQuantity == work.quantity ->
                        liningDrawStatus = WorkStatus.COMPLETED
                    wd.position == Position.SEWER && wd.doneQuantity == work.quantity ->
                        sewStatus = WorkStatus.COMPLETED
                    wd.position == Position.ASSEMBLER && wd.doneQuantity == work.quantity ->
                        assembleStatus = WorkStatus.COMPLETED
                    wd.position == Position.SOLE_STITCHER && wd.doneQuantity == work.quantity ->
                        soleStitchStatus = WorkStatus.COMPLETED
                    wd.position == Position.INSOLE_STITCHER && wd.doneQuantity == work.quantity ->
                        insoleStitchStatus = WorkStatus.COMPLETED
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

    fun getWorkFilterData(): Flow<WorkFilterPrefs> {
         return userPrefs.data.map { prefs ->
             prefs.workFilterPrefs
         }
    }

    suspend fun storeWorkFilterData(workQuery: WorkQuery) {
        userPrefs.updateData { prefs ->
            prefs.toBuilder()
                .setWorkFilterPrefs(
                    prefs.workFilterPrefs.toBuilder()
                        .setDate(Date.newBuilder()
                            .setStartDate(workQuery.startDate)
                            .setEndDate(workQuery.endDate)
                            .build())
                        .setOrder(
                            Order.newBuilder()
                                .setOrderByRemote(workQuery.orderBy)
                                .setOrderDirection(workQuery.orderDirection)
                                .build())
                        .build()
                )
                .build()

        }
    }
}