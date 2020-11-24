package com.rahmanarifofficial.coronastats.datasource

import androidx.lifecycle.LiveData
import com.rahmanarifofficial.coronastats.model.ResultData
import retrofit2.http.GET

interface ApiService {

    @GET("VS6HdKS0VfIhv8Ct/arcgis/rest/services/COVID19_Indonesia_per_Provinsi/FeatureServer/0/query?where=1%3D1&outFields=*&outSR=4326&f=json")
    fun getData(): LiveData<ApiResponse<ResultData>>
}