package com.rahmanarifofficial.coronastats.datasource

import android.util.Log
import com.rahmanarifofficial.coronastats.BuildConfig
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DataSource {
    companion object {
        private fun service(): Retrofit {
            val service: Retrofit =
                Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(LiveDataCallAdapterFactory())
                    .build()
            return service
        }
    }

    open class Service {
        companion object {
            val api: ApiService by lazy {
                service().create(ApiService::class.java)
            }
        }
    }
}