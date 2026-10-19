package com.iago.ledgerbook.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iago.ledgerbook.R
import com.iago.ledgerbook.data.SummaryData
import com.iago.ledgerbook.data.TransactionType
import com.iago.ledgerbook.ui.theme.DarkBackground
import com.iago.ledgerbook.ui.theme.DarkTurquoise
import com.iago.ledgerbook.ui.theme.LedgerBookTheme
import com.iago.ledgerbook.ui.theme.Red
import java.util.Locale

@Composable
fun SummaryDisplay(
    summaryData: SummaryData,
    transactionType: TransactionType,
    onChangeTransactionType: (TransactionType) -> Unit
) {
    val remaining = summaryData.incomes - (summaryData.expenses + summaryData.savings)
    val formattedRemaining = String.format(Locale("pt", "BR"), "%,.2f", remaining)
    val (mainValue, decimals) = formattedRemaining.split(",")

    val expensePercentage =
        if (summaryData.incomes > 0) (summaryData.expenses / summaryData.incomes * 100) else 0.0
    val savingPercentage =
        if (summaryData.incomes > 0) (summaryData.savings / summaryData.incomes * 100) else 0.0

    Card(
        shape = MaterialTheme.shapes.extraLarge,
        contentColor = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .drawBehind {
                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                DarkTurquoise,
                                DarkBackground
                            ),
                            center = Offset(
                                x = size.width,
                                y = 0f
                            ),
                            radius = size.maxDimension
                        )
                    )
                }
                .padding(24.dp)
        ) {
            Column {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.income_spent),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray,
                            letterSpacing = 2.sp,
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = "${expensePercentage.toInt()}%",
                            style = MaterialTheme.typography.labelLarge,
                            color = Red,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.weight(1f))

                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.remaining),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray,
                            letterSpacing = 2.sp,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                maxLines = 1,
                                text = "R$$mainValue",
                                style = MaterialTheme.typography.displayMedium,
                                color = Color.White
                            )

                            Text(
                                maxLines = 1,
                                text = ",$decimals",
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color.Gray,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.income_saved),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray,
                            letterSpacing = 2.sp,
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = "${savingPercentage.toInt()}%",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.weight(1f))

                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Divider(color = Color.White.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SummaryItem(
                        summaryData,
                        transactionType,
                        TransactionType.INCOME
                    ) {
                        onChangeTransactionType(TransactionType.INCOME)
                    }
                    SummaryItem(
                        summaryData,
                        transactionType,
                        TransactionType.EXPENSE
                    ) {
                        onChangeTransactionType(TransactionType.EXPENSE)
                    }
                    SummaryItem(
                        summaryData,
                        transactionType,
                        TransactionType.SAVING
                    ) {
                        onChangeTransactionType(TransactionType.SAVING)
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryItem(
    summaryData: SummaryData,
    currentTransactionType: TransactionType,
    transactionType: TransactionType,
    onPress: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onPress() }
            .background(
                if (currentTransactionType == transactionType) MaterialTheme.colorScheme.primary.copy(
                    .15f
                ) else Color.Transparent,
                MaterialTheme.shapes.medium
            )
            .padding(  12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(
                when (transactionType) {
                    TransactionType.EXPENSE -> R.string.expense
                    TransactionType.INCOME -> R.string.income
                    TransactionType.SAVING -> R.string.saving
                }
            ),
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            maxLines = 1,
            text = "R$${
                String.format(
                    Locale("pt", "BR"), "%,.2f", when (transactionType) {
                        TransactionType.EXPENSE -> summaryData.expenses
                        TransactionType.INCOME -> summaryData.incomes
                        TransactionType.SAVING -> summaryData.savings
                    }
                )
            }",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SummaryDisplayPreview() {
    LedgerBookTheme {
        Surface {
            SummaryDisplay(
                SummaryData(
                    incomes = 4000.0,
                    expenses = 1200.99,
                    savings = 500.55,
                ),
                TransactionType.SAVING
            ) {}
        }
    }
}
