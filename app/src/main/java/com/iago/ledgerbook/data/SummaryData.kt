package com.iago.ledgerbook.data


data class SummaryData(
    val incomes: Double,
    val expenses: Double,
    val savings: Double,
    val checkedIncomes: Double = 0.0,
    val checkedExpenses: Double = 0.0,
    val checkedSavings: Double = 0.0,
) {}