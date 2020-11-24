package com.rahmanarifofficial.coronastats.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.rahmanarifofficial.coronastats.model.Attributes

@Dao
interface AttributesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMany(attributes: List<Attributes>)

    @Query("DELETE FROM ${Attributes.TABLE}")
    fun deleteAll()

    @Transaction
    fun deleteAndInsert(attributes: List<Attributes>) {
        deleteAll()
        insertMany(attributes)
    }

    @Query("UPDATE ${Attributes.TABLE} SET ${Attributes.IS_SELECTED} = :isSelect WHERE ${Attributes.FID_} = :FID")
    fun updateAttribute(FID: Int, isSelect: Boolean)

    @Query("SELECT * FROM ${Attributes.TABLE}")
    fun getAll(): LiveData<List<Attributes>>

    @Query("SELECT * FROM ${Attributes.TABLE} WHERE ${Attributes.PROVINSI} LIKE :keyword")
    fun getAllKeyword(keyword: String): LiveData<List<Attributes>>

    @RawQuery(observedEntities = [Attributes::class])
    fun getAll(query: SupportSQLiteQuery): LiveData<List<Attributes>>

}