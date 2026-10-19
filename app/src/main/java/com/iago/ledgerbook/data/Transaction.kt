package com.iago.ledgerbook.data

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson


@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: TransactionCategory,
    val type: TransactionType,
    val title: String,
    var value: Double,
    val date: Long? = null
) {
    override fun toString(): String = Uri.encode(Gson().toJson(this))

}