package com.rahmanarifofficial.coronastats.model

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    tableName = Attributes.TABLE,
    indices = [Index(
        value = [
            Attributes.FID_
        ]
    )]
)

data class Attributes(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = FID_)
    val FID: Int? = null,

    @ColumnInfo(name = KODE_PROVI)
    val Kode_Provi: Int? = null,

    @ColumnInfo(name = PROVINSI)
    val Provinsi: String? = null,

    @ColumnInfo(name = KASUS_MENI)
    val Kasus_Meni: Int? = null,

    @ColumnInfo(name = KASUS_POSI)
    val Kasus_Posi: Int? = null,

    @ColumnInfo(name = KASUS_SEMB)
    val Kasus_Semb: Int? = null,

    @ColumnInfo(name = IS_SELECTED)
    var isSelected: Boolean = false

) : Parcelable {

    constructor(
        _kode_prov: Int?,
        _prov: String?,
        _kasus_meni: Int?,
        _kasus_posi: Int?,
        _kasus_semb: Int?
    ) : this(
        null,
        _kode_prov,
        _prov,
        _kasus_meni,
        _kasus_posi,
        _kasus_semb
    )

    companion object {
        const val TABLE = "Attributes"
        const val FID_ = "FID"
        const val KODE_PROVI = "Kode_Provi"
        const val PROVINSI = "Provinsi"
        const val KASUS_MENI = "Kasus_Meni"
        const val KASUS_POSI = "Kasus_Posi"
        const val KASUS_SEMB = "Kasus_Semb"
        const val IS_SELECTED = "IS_SELECTED"
    }
}