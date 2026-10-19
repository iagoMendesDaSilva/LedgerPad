package com.iago.ledgerbook.data

data class MonthlyReportItem(
    val month: Int,
    val year: Int,
    val income: Double,
    val expense: Double,
    val saving: Double,
    val balance: Double,
    val accumulated: Double
)