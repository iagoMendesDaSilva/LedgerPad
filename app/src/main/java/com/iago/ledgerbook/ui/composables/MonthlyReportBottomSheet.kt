package com.iago.ledgerbook.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iago.ledgerbook.R
import com.iago.ledgerbook.data.MonthlyReportItem
import com.iago.ledgerbook.ui.theme.DarkBackground
import com.iago.ledgerbook.ui.theme.DarkTurquoise
import com.iago.ledgerbook.ui.theme.LedgerBookTheme
import com.iago.ledgerbook.ui.theme.Red
import java.util.Locale
import kotlin.math.abs

@Composable
fun MonthlyReportBottomSheet(
    item: MonthlyReportItem,
    includeFixed: Boolean = false,
    onIncludeFixedChange: ((Boolean) -> Unit)? = null
) {

    val isPositive = item.balance >= 0
    val previousAccumulated = item.accumulated - item.balance

    Column(
        modifier = Modifier
            .padding(24.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(R.string.financial_summary),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White.copy(alpha = .65f)
                )
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = String.format(
                        Locale("pt", "BR"),
                        "%02d/%d",
                        item.month,
                        item.year
                    ),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
            }

            if (onIncludeFixedChange != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onIncludeFixedChange(!includeFixed) }
                ) {
                    Text(
                        text = stringResource(R.string.include_fixed),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = .8f),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Switch(
                        checked = includeFixed,
                        onCheckedChange = onIncludeFixedChange,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = MaterialTheme.colorScheme.primary,
                            uncheckedThumbColor = Color.Gray,
                            uncheckedTrackColor = DarkBackground
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    DarkTurquoise,
                    MaterialTheme.shapes.large
                )
                .padding(20.dp)
        ) {

            Column {

                Text(
                    text = stringResource(if (isPositive) R.string.left_over else R.string.missing),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = .65f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text =
                        if (isPositive)
                            "+R$ ${
                                String.format(
                                    Locale("pt", "BR"),
                                    "%,.2f",
                                    item.balance
                                )
                            }"
                        else
                            "-R$ ${
                                String.format(
                                    Locale("pt", "BR"),
                                    "%,.2f",
                                    abs(item.balance)
                                )
                            }",
                    style = MaterialTheme.typography.displaySmall,
                    color = if (isPositive) MaterialTheme.colorScheme.primary else Red
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        ReportRow(
            title = R.string.incomes,
            value = item.income,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(14.dp))

        ReportRow(
            title = R.string.expenses,
            value = -item.expense,
            color = Red
        )

        Spacer(modifier = Modifier.height(14.dp))

        ReportRow(
            title = R.string.savings,
            value = item.saving,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Divider(
            color = Color.White.copy(alpha = .08f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.financial_history),
            style = MaterialTheme.typography.labelMedium,
            color = Color.White.copy(alpha = .6f)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text =
                if (previousAccumulated > 0)
                    stringResource(
                        R.string.monthly_report_positive_accumulated,
                        String.format(
                            Locale("pt", "BR"),
                            "%,.2f",
                            previousAccumulated
                        )
                    )
                else if (previousAccumulated < 0)
                    stringResource(
                        R.string.monthly_report_negative_accumulated,
                        String.format(
                            Locale("pt", "BR"),
                            "%,.2f",
                            abs(previousAccumulated)
                        )
                    )
                else stringResource(R.string.monthly_report_no_accumulated),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = .8f)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    DarkTurquoise,
                    MaterialTheme.shapes.large
                )
                .padding(18.dp)
        ) {

            Column {

                Text(
                    text = stringResource(R.string.final_balance),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = .65f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text =
                        if (item.accumulated >= 0)
                            "R$ ${
                                String.format(
                                    Locale("pt", "BR"),
                                    "%,.2f",
                                    item.accumulated
                                )
                            }"
                        else
                            "-R$ ${
                                String.format(
                                    Locale("pt", "BR"),
                                    "%,.2f",
                                    abs(item.accumulated)
                                )
                            }",
                    style = MaterialTheme.typography.headlineMedium,
                    color = if (item.accumulated >= 0) MaterialTheme.colorScheme.primary else Red
                )
            }
        }
    }
}

@Composable
private fun ReportRow(
    title: Int,
    value: Double,
    color: Color
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.7f)
        )
        Text(
            text =
                if (value > 0)
                    "+R$ ${
                        String.format(
                            Locale("pt", "BR"),
                            "%,.2f",
                            value
                        )
                    }"
                else
                    "-R$ ${
                        String.format(
                            Locale("pt", "BR"),
                            "%,.2f",
                            abs(value)
                        )
                    }",
            style = MaterialTheme.typography.bodyLarge,
            color = color
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MonthlyReportBottomSheetPreview() {
    LedgerBookTheme {
        Surface {
            MonthlyReportBottomSheet(
                MonthlyReportItem(
                    month = 1,
                    year = 2026,
                    income = 10000.0,
                    expense = 500.0,
                    saving = 500.0,
                    balance = 9000.0,
                    accumulated = 10000.0
                )
            )
        }
    }
}