package com.android.borsappc.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RetrofitWithAuth
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RetrofitWithoutAuth
