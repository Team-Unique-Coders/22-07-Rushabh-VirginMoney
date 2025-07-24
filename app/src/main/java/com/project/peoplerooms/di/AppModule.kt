package com.project.peoplerooms.di

import com.project.peoplerooms.data.repository.Repository
import com.project.peoplerooms.data.repository.RepositoryImpl
import com.project.peoplerooms.data.service.ApiDetails
import com.project.peoplerooms.data.service.UserService
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun providesGson(): Gson{
        return Gson()
    }

    @Provides
    fun provideGsonConverter(gson: Gson): GsonConverterFactory{
        return GsonConverterFactory.create(gson)
    }

    @Provides
    fun provideOkHttp(): OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })//loggin Interceptor
            .build()
    }

    @Provides
    fun provideRetrofit(
        converterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit{
        return Retrofit.Builder()
            .baseUrl(ApiDetails.BASE_URL)
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideApiServices(retrofit: Retrofit): UserService{
        return retrofit.create(UserService::class.java)
    }

    @Provides
    fun providesRepository(
        userService: UserService
    ): Repository{
        return RepositoryImpl(
            userService
        )
    }
}