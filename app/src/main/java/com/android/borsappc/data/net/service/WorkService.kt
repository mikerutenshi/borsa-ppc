package com.android.borsappc.data.net.service

import com.android.borsappc.data.model.Work
import com.android.borsappc.data.net.response.GenericResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WorkService {
    @POST("v1/works")
    suspend fun storeWork(@Body work: Work): GenericResponse<Unit>

    @GET("v1-1/works")
    suspend fun getWorks(
        @Query("search_keyword") searchKey: String?,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("sort_by") sortBy: String,
        @Query("sort_direction") sortDirection: String
    ): GenericResponse<List<Work>>

//    @GET("v1-1/works/{id}/assigned-work")
//    fun getWorkDetailAssigned(
//        @Path("id") path: Int?,
//        @QueryMap options: Map<String?, String?>?
//    ): Single<GenericListResponseModel<CurrentWorkDetailAssignedModel?>?>?
//
//    @GET("v1-1/works/{id}/done-work")
//    fun getWorkDetailDone(
//        @Path("id") path: Int?,
//        @QueryMap options: Map<String?, String?>?
//    ): Single<GenericListResponseModel<CurrentWorkDetailDoneModel?>?>?
//
//    @GET("v1/works/{worker_id}")
//    fun getWorksPerWorker(
//        @Path("worker_id") workerId: Int?,
//        @Query("spk_no") spkNo: String?,
//        @Query("limit") limit: Int?,
//        @Query("page") page: Int?
//    ): Single<GenericPaginationResponse<List<Work?>?>?>?
//
//    @GET("v1/works/unassigned/{worker_id}")
//    fun getUnassignedWork(
//        @Path("worker_id") workerId: Int?,
//        @Query("position") position: String?,
//        @Query("spk_no") spkNo: String?,
//        @Query("limit") limit: Int?,
//        @Query("page") page: Int?,
//        @Query("start_date") startDate: String?,
//        @Query("end_date") endDate: String?,
//        @Query("sort_by") sortBy: String?,
//        @Query("sort_direction") sortDirection: String?
//    ): Single<GenericPaginationResponse<List<Work?>?>?>?
//
//    @DELETE("v1/works")
//    fun deleteWork(@Query("work_id") workId: List<Int?>?): Single<GenericResponse?>?
//
//    @PUT("v1/works/{work_id}")
//    fun updateWork(
//        @Path("work_id") workId: Int?,
//        @Body Work: WorkUpdateModel?
//    ): Single<GenericResponse?>?
}