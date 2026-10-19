package com.iago.ledgerbook.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iago.ledgerbook.R
import com.iago.ledgerbook.data.TransactionCategory
import com.iago.ledgerbook.data.TransactionType
import com.iago.ledgerbook.ui.theme.LedgerBookTheme

enum class BottomSheetAction {
    EDIT,
    CREATE,
    CLOSE,
    REPORT
}

@Composable
fun TransactionBottomSheetContent(
    type: TransactionType,
    isEditing: Boolean,
    amount: Double? = null,
    description: String = "",
    selectedCategory: TransactionCategory?,
    onSubmit: (value: Double, category: TransactionCategory, title: String) -> Unit
) {

    val title = remember { mutableStateOf(description) }
    val value = remember { mutableStateOf(amount) }
    val moneyText = remember {
        mutableStateOf(amount?.let { String.format("%.2f", it).replace(".", ",") } ?: "")
    }
    val category = remember { mutableStateOf(selectedCategory) }

    val typeName = stringResource(
        when (type) {
            TransactionType.EXPENSE -> R.string.expense
            TransactionType.INCOME -> R.string.income
            TransactionType.SAVING -> R.string.saving
        }
    ).lowercase().replaceFirstChar { it.uppercase() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "R$",
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1.5f)
            )
            TextField(
                value = moneyText.value,
                onValueChange = { input ->
                    val filtered = input.filter { it.isDigit() || it == ',' || it == '.' }
                    val normalized = filtered.replace(",", ".")
                    val parts = normalized.split(".")

                    val result = when (parts.size) {
                        1 -> {
                            parts[0].take(6)
                        }

                        2 -> {
                            val integer = parts[0].take(6)
                            val decimals = parts[1].take(2)
                            "$integer.$decimals"
                        }

                        else -> moneyText.value
                    }

                    moneyText.value = result
                    value.value = result.toDoubleOrNull()
                },
                placeholder = {
                    Text(
                        "0,00",
                        style = MaterialTheme.typography.displayMedium,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                textStyle = MaterialTheme.typography.displayMedium.copy(
                    textAlign = TextAlign.Start
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                singleLine = true,
                modifier = Modifier.weight(2f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }
        TextField(
            value = title.value,
            onValueChange = { title.value = it },
            placeholder = { Text(stringResource(R.string.description)) },
            modifier = Modifier.fillMaxWidth(),
            shape = CircleShape,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White.copy(.05f),
                focusedContainerColor = Color.White.copy(.05f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words
            ),
            singleLine = true
        )
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = stringResource(R.string.category),
                style = MaterialTheme.typography.labelMedium
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(30.dp),
            ) {
                items(TransactionCategory.entries.toTypedArray()) { categoryItem ->
                    CategoryItem(
                        category = categoryItem,
                        selected = category.value,
                        onClick = { category.value = categoryItem }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            enabled = category.value != null && value.value != null && title.value.isNotEmpty(),
            onClick = {
                onSubmit(value.value!!, category.value!!, title.value)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.background)
        ) {
            Text(
                if (isEditing) stringResource(
                    R.string.edit_type,
                    typeName
                ) else stringResource(
                    R.string.add_type, typeName
                )
            )
        }
    }
}

@Composable
fun CategoryItem(
    category: TransactionCategory,
    selected: TransactionCategory?,
    onClick: () -> Unit
) {
    val backgroundColor =
        if (selected == null) category.color else if (category == selected) category.color else category.color.copy(
            .5f
        )
    val labelColor =
        if (selected == null) Color.White.copy(.75f) else if (category == selected) category.color else Color.White.copy(
            .5f
        )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier
                .size(75.dp)
                .background(backgroundColor, MaterialTheme.shapes.small)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = null,
                tint = Color.Black.copy(alpha = 0.85f),
                modifier = Modifier.fillMaxSize(.5f)
            )

        }
        Spacer(Modifier.height(10.dp))
        Text(text = stringResource(category.title), style = MaterialTheme.typography.labelMedium, color = labelColor)
    }
}


@Preview(showBackground = true)
@Composable
fun TransactionBottomSheetPreview() {
    LedgerBookTheme {
        Surface {
            LedgerBookTheme {
                val amount = remember { mutableStateOf(150.0) }
                val description = remember { mutableStateOf("") }
                val category =
                    remember { mutableStateOf<TransactionCategory?>(TransactionCategory.HOUSE) }

                TransactionBottomSheetContent(
                    type = TransactionType.EXPENSE,
                    isEditing = false,
                    amount = amount.value,
                    description = description.value,
                    selectedCategory = category.value,
                    onSubmit = { _, _, _ -> },
                )
            }
        }
    }
}
