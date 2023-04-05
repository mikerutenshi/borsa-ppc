package com.android.borsappc.data.repository.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.android.borsappc.data.db.AppDatabase
import com.android.borsappc.data.model.RemoteKey
import com.android.borsappc.data.model.Work
import com.android.borsappc.data.model.WorkQuery
import com.android.borsappc.data.net.service.WorkService
import retrofit2.HttpException
import retrofit2.Retrofit
import java.io.IOException

const val WORK_REMOTE_KEY_QUERY: String = "work_remote_key_query"

@OptIn(ExperimentalPagingApi::class)
class WorkRemoteMediator constructor(
    private val query: WorkQuery,
    private val database: AppDatabase,
    private val retrofit: Retrofit
) : RemoteMediator<Int, Work>() {


    private val workDao = database.workDao()
    private val remoteKeyDao = database.remoteKeyDao()

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Work>): MediatorResult {
        return try {
            val loadKey = when (/**/loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(true)
                LoadType.APPEND -> {
                    val remoteKey = database.withTransaction {
                        remoteKeyDao.remoteKeyByQuery(WORK_REMOTE_KEY_QUERY)
                    }

                    if (remoteKey.nextKey == null) {
                        return MediatorResult.Success(true)
                    }

                    remoteKey.nextKey
                }
            }
            val response = retrofit.create(WorkService::class.java).getWorks(
                query.searchKeyword,
                loadKey,
                query.limit,
                query.startDate,
                query.endDate,
                query.orderBy,
                query.orderDirection
            )
            val nextKey = if (response.meta!!.totalPage == loadKey) null else loadKey.plus(1)

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.deleteByQuery(WORK_REMOTE_KEY_QUERY)
                    workDao.clearAll()
                }

                remoteKeyDao.insertOrReplace(RemoteKey(
                    WORK_REMOTE_KEY_QUERY,
                    nextKey
                ))
                workDao.insertAll(response.data)
            }

            MediatorResult.Success(
                endOfPaginationReached = nextKey == null
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}