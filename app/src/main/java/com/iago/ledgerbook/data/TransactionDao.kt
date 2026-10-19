package com.iago.ledgerbook.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.iago.ledgerbook.data.Transaction
import kotlinx.coroutines.flow.Flow


@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions WHERE id=:id")
    suspend fun getTransaction(id: Int): Transaction

    @Query("SELECT * FROM transactions")
    fun getTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions")
    suspend fun getTransactionsSync(): List<Transaction>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

}