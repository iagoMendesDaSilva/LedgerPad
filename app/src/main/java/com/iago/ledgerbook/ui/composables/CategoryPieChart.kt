package com.iago.ledgerbook.ui.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iago.ledgerbook.data.TransactionCategory
import com.iago.ledgerbook.data.TransactionType
import com.iago.ledgerbook.ui.previews.PreviewDataTransaction
import com.iago.ledgerbook.ui.screens.categoryTotals
import com.iago.ledgerbook.ui.theme.LedgerBookTheme
import java.util.Locale


@Composable
fun CategoryPieChart(
    data: Map<TransactionCategory, Double>,
    modifier: Modifier = Modifier
) {
    val total = data.values.sum()
    val biggestEntry = data.maxByOrNull { it.value }
    val biggestPercentage =
        if (total > 0 && biggestEntry != null)
            (biggestEntry.value / total * 100)
        else 0.0
    Row(
        Modifier
            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        CategoryLegend(data, modifier = Modifier.weight(1f))
        Box(
            modifier = modifier
                .weight(2f)
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                var startAngle = -90f

                data.entries.forEach { (category, amount) ->
                    val sweep =
                        if (total == 0.0) 0f
                        else (amount / total * 360f).toFloat()

                    drawArc(
                        color = category.color,
                        startAngle = startAngle,
                        sweepAngle = sweep,
                        useCenter = false,
                        style = Stroke(
                            width = 30f,
                            cap = StrokeCap.Round
                        )
                    )

                    startAngle += sweep
                }
            }

            if (biggestEntry != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = biggestEntry.key.icon,
                        contentDescription = null,
                        tint = biggestEntry.key.color,
                        modifier = Modifier.size(32.dp)
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = stringResource(biggestEntry.key.title),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = String.format(
                            Locale("pt", "BR"),
                            "%.1f%%",
                            biggestPercentage
                        ),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        CategoryLegendValues(data, modifier = Modifier.weight(1f))
    }
}


@Composable
fun CategoryLegend(
    data: Map<TransactionCategory, Double>,
    modifier: Modifier,
) {
    val total = data.values.sum()
    Column(
        modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        data.entries.sortedByDescending { it.value }.forEach { (category, amount) ->
            val percentage = if (total > 0) (amount / total * 100) else 0.0
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = null,
                    tint = category.color,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = String.format(Locale("pt", "BR"), "%.1f%%", percentage),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun CategoryLegendValues(
    data: Map<TransactionCategory, Double>,
    modifier: Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        data.entries.sortedByDescending { it.value }.forEach { (category, amount) ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = "R$ ${String.format(Locale("pt", "BR"), "%,.2f", amount)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryPieChartPreview() {
    LedgerBookTheme {
        Surface {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CategoryPieChart(
                    categoryTotals(
                        PreviewDataTransaction.transactionList,
                        TransactionType.EXPENSE
                    )
                )
            }
        }
    }
}