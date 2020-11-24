package com.rahmanarifofficial.coronastats.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.google.gson.Gson
import com.rahmanarifofficial.coronastats.database.AppDatabase
import com.rahmanarifofficial.coronastats.database.AttributesDao
import com.rahmanarifofficial.coronastats.datasource.*
import com.rahmanarifofficial.coronastats.model.Attributes
import com.rahmanarifofficial.coronastats.model.ResultData
import com.rahmanarifofficial.coronastats.utils.AppExecutors
import com.rahmanarifofficial.coronastats.utils.Preferences

class DataRepository private constructor(
    private val callApi: NetworkCall,
    private val attributesDao: AttributesDao,
    private val appExecutors: AppExecutors,
    private val prefs: Preferences

) {
    companion object {
        @Volatile
        private var INSTANCE: DataRepository? = null

        fun getInstance(
            callApi: NetworkCall,
            attributesDao: AttributesDao,
            appExecutors: AppExecutors,
            prefs: Preferences
        ) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: DataRepository(callApi, attributesDao, appExecutors, prefs)
            }
    }

    fun login(
        username: String,
        password: String,
        callback: (Boolean) -> Unit
    ) {
        Log.e("DATA LOGIN", "${username} ${password}")
        if (username.equals("admin") && password.equals("admin")) {
            Log.e("DATA LOGIN", "Pass")
            prefs.isLogin = true
            callback.invoke(true)
        } else {
            Log.e("DATA LOGIN", "Not Pass")
            prefs.isLogin = false
            callback.invoke(false)
        }
    }

    fun logout(
        callback: () -> Unit
    ) {
        prefs.isLogin = false
        callback.invoke()
    }

    fun getData(
        keyword: String?,
        sortValue: Int,
        selectedList: ArrayList<Attributes>?
    ): LiveData<Resource<List<Attributes>>> {
        return object : NetworkBoundResource<List<Attributes>, ResultData>(appExecutors) {
            override fun saveCallResult(item: ResultData) {
                val attributesList = ArrayList<Attributes>()
                val features = item.features
                features?.let {
                    for (attribute in features) {
                        val attributes = Attributes(
                            attribute.attributes?.Kode_Provi,
                            attribute.attributes?.Provinsi,
                            attribute.attributes?.Kasus_Meni,
                            attribute.attributes?.Kasus_Posi,
                            attribute.attributes?.Kasus_Semb
                        )
                        attributesList.add(attributes)
                    }
                }
                attributesDao.deleteAndInsert(attributesList)
            }

            override fun shouldFetch(data: List<Attributes>?): Boolean = data.isNullOrEmpty()

            override fun loadFromDb(): LiveData<List<Attributes>> {
                var query = "SELECT * FROM ${Attributes.TABLE} WHERE ${Attributes.PROVINSI} LIKE "
                var keywordCondition = ""
                var filterCondition = ""
                var sortCondition = ""
                if (!keyword.isNullOrEmpty()) {
                    keywordCondition = "\"%$keyword%\""
                } else {
                    keywordCondition = "\"%%\""
                }
                if (!selectedList.isNullOrEmpty()) {
                    filterCondition =
                        " AND ${Attributes.FID_} IN (SELECT ${Attributes.FID_} FROM ${Attributes.TABLE} WHERE"
                    var fidSelected = ""
                    for (index in selectedList.indices) {
                        fidSelected += " ${Attributes.FID_} = ${selectedList[index].FID}"
                        if (index == selectedList.size - 1) {
                            fidSelected += " )"
                        } else {
                            fidSelected += " OR "
                        }
                    }
                    filterCondition += fidSelected
                } else {
                    filterCondition = ""
                }
                when (sortValue) {
                    0 -> {
                        sortCondition = " ORDER BY ${Attributes.FID_} ASC"
                    }
                    1 -> {
                        sortCondition = " ORDER BY ${Attributes.PROVINSI} ASC"
                    }
                    2 -> {
                        sortCondition = " ORDER BY ${Attributes.PROVINSI} DESC"
                    }
                    3 -> {
                        sortCondition = " ORDER BY ${Attributes.KASUS_POSI} ASC"
                    }
                    4 -> {
                        sortCondition = " ORDER BY ${Attributes.KASUS_POSI} DESC"
                    }
                }
                query += keywordCondition + filterCondition + sortCondition
                return attributesDao.getAll(SimpleSQLiteQuery(query))
            }


            override fun createCall(): LiveData<ApiResponse<ResultData>> = callApi.getData()

        }.asLiveData()
    }

    fun updateSelected(fid: Int, isSelect: Boolean){
        attributesDao.updateAttribute(fid, isSelect)
    }
}