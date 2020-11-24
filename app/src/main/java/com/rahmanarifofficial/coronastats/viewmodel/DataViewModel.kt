package com.rahmanarifofficial.coronastats.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.rahmanarifofficial.coronastats.model.Attributes
import com.rahmanarifofficial.coronastats.repository.DataRepository

class DataViewModel(private val repository: DataRepository) : ViewModel() {

    val keyword = MutableLiveData<String>()

    init {
        this.keyword.value = ""
    }

    fun login(
        username: String,
        password: String,
        callback: (Boolean) -> Unit
    ) {
        repository.login(username, password, callback)
    }

    fun logout(callback: () -> Unit){
        repository.logout (callback)
    }

    fun getData(sortValue: Int, selectedList: ArrayList<Attributes>?) =
        Transformations.switchMap(keyword) {
            repository.getData(it, sortValue, selectedList)
        }

    fun updateSelected(fid: Int, isSelect : Boolean) {
        repository.updateSelected(fid, isSelect)
    }
}