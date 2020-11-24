package com.rahmanarifofficial.coronastats.utils

import android.content.Context
import com.rahmanarifofficial.coronastats.database.AppDatabase
import com.rahmanarifofficial.coronastats.datasource.NetworkCall
import com.rahmanarifofficial.coronastats.repository.DataRepository

object Injection {
    fun provideDataRepository(context: Context): DataRepository {
        val database = AppDatabase.getInstance(context)!!
        return DataRepository.getInstance(NetworkCall(), database.attributesDao(), AppExecutors(), Preferences(context))
    }
}