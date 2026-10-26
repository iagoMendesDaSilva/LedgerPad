package com.iago.ledgerbook.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iago.ledgerbook.data.FirestoreTransactionRepository
import com.iago.ledgerbook.data.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val repository: FirestoreTransactionRepository,
) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.checkAndMigrateLocalData()
        }
    }

    val transactions = repository.getTransactions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addTransaction(transaction: Transaction) =
        viewModelScope.launch {
            repository.insertTransaction(transaction)
        }

    fun updateTransaction(transaction: Transaction) =
        viewModelScope.launch {
            repository.updateTransaction(transaction)
        }

    fun deleteTransaction(transaction: Transaction) =
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
}
