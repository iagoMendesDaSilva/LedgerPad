package com.iago.ledgerbook.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iago.ledgerbook.data.Transaction
import com.iago.ledgerbook.data.TransactionType

@Composable
fun TransactionsGrid(
    transactions: List<Transaction>,
    currentScreen: TransactionType,
    onEdit: (Transaction) -> Unit,
    onDelete: (Transaction) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(
            if (currentScreen == TransactionType.SAVING) 2 else 1
        ),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(12.dp)
    ) {

        when (currentScreen) {

            TransactionType.INCOME,
            TransactionType.EXPENSE -> {
                items(transactions) { transaction ->
                    TransactionCard(
                        transaction,
                        onLongPress = { onDelete(transaction) }
                    ) {
                        onEdit(transaction)
                    }
                }
            }

            TransactionType.SAVING -> {
                items(transactions) { saving ->
                    SavingCard(
                        saving,
                        onLongPress = { onDelete(saving) }
                    ) {
                        onEdit(saving)
                    }
                }
            }
        }
    }
}