package com.iago.ledgerbook.data

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreTransactionRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val localDao: TransactionDao,
    @ApplicationContext private val context: Context
) {
    private fun getUserId(): String? = auth.currentUser?.uid

    private fun getCollection() = firestore.collection("users")
        .document(getUserId() ?: "anonymous")
        .collection("transactions")

    suspend fun checkAndMigrateLocalData() {
        val uid = getUserId() ?: return
        val prefs = context.getSharedPreferences("ledger_migration_prefs", Context.MODE_PRIVATE)
        val isMigrated = prefs.getBoolean("migrated_$uid", false)
        if (isMigrated) return

        try {
            val snapshot = getCollection().get().await()
            if (snapshot.isEmpty) {
                val localTransactions = localDao.getTransactionsSync()
                if (localTransactions.isNotEmpty()) {
                    val batch = firestore.batch()
                    for (tx in localTransactions) {
                        val docRef = getCollection().document()
                        val txMap = mapOf(
                            "id" to tx.id,
                            "category" to tx.category.name,
                            "type" to tx.type.name,
                            "title" to tx.title,
                            "value" to tx.value,
                            "date" to (tx.date ?: System.currentTimeMillis()),
                            "isChecked" to tx.isChecked
                        )
                        batch.set(docRef, txMap)
                    }
                    batch.commit().await()
                }
            }
            prefs.edit().putBoolean("migrated_$uid", true).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getTransactions(): Flow<List<Transaction>> {
        return localDao.getTransactions()
    }

    suspend fun insertTransaction(transaction: Transaction) {
        val uid = getUserId()
        if (uid != null) {
            try {
                val txMap = mapOf(
                    "id" to transaction.id,
                    "category" to transaction.category.name,
                    "type" to transaction.type.name,
                    "title" to transaction.title,
                    "value" to transaction.value,
                    "date" to (transaction.date ?: System.currentTimeMillis()),
                    "isChecked" to transaction.isChecked
                )
                getCollection().document().set(txMap).await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        localDao.insertTransaction(transaction)
    }

    suspend fun updateTransaction(transaction: Transaction) {
        val uid = getUserId()
        if (uid != null) {
            try {
                val query = getCollection().whereEqualTo("id", transaction.id).get().await()
                val txMap = mapOf(
                    "id" to transaction.id,
                    "category" to transaction.category.name,
                    "type" to transaction.type.name,
                    "title" to transaction.title,
                    "value" to transaction.value,
                    "date" to (transaction.date ?: System.currentTimeMillis()),
                    "isChecked" to transaction.isChecked
                )
                if (!query.isEmpty) {
                    for (doc in query.documents) {
                        doc.reference.set(txMap).await()
                    }
                } else {
                    getCollection().document().set(txMap).await()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        localDao.updateTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction: Transaction) {
        val uid = getUserId()
        if (uid != null) {
            try {
                val query = getCollection().whereEqualTo("id", transaction.id).get().await()
                for (doc in query.documents) {
                    doc.reference.delete().await()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        localDao.deleteTransaction(transaction)
    }
}
