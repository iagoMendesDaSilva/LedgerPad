package com.iago.ledgerbook.ui.previews

import com.iago.ledgerbook.data.Transaction
import com.iago.ledgerbook.data.TransactionCategory
import com.iago.ledgerbook.data.TransactionType

object PreviewDataTransaction {
    val transactionExpense = Transaction(
        id = 1,
        category = TransactionCategory.TRANSPORT,
        title = "Gasolina ",
        value = 159.99,
        type = TransactionType.EXPENSE
    )

    val transactionExpense2 = Transaction(
        id = 1,
        category = TransactionCategory.HEALTH,
        title = "Academia ",
        value = 70.0,
        type = TransactionType.EXPENSE
    )

    val transactionIncome = Transaction(
        id = 2,
        category = TransactionCategory.SALARY,
        title = "Salário",
        value = 1500.0,
        type = TransactionType.INCOME
    )

    val transactionIncome2 = Transaction(
        id = 2,
        category = TransactionCategory.BONUS,
        title = "Bônus",
        value = 150.0,
        type = TransactionType.INCOME
    )

    val transactionSaving = Transaction(
        id = 3,
        category = TransactionCategory.TRANSPORT,
        title = "Carro Novo",
        value = 250.0,
        type = TransactionType.SAVING
    )

    val transactionSaving2 = Transaction(
        id = 3,
        category = TransactionCategory.RETIREMENT,
        title = "Aposentadoria",
        value = 80.0,
        type = TransactionType.SAVING
    )

    val transactionList = listOf(
        transactionIncome,
        transactionIncome2,
        transactionExpense,
        transactionExpense2,
        transactionSaving,
        transactionSaving2
    )
}