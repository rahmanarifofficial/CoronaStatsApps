package com.rahmanarifofficial.coronastats.datasource

class NetworkCall {
    private val api = DataSource.Service.api

    fun getData() = api.getData()
}