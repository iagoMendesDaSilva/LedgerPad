package com.iago.ledgerbook.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.iago.ledgerbook.R
import com.iago.ledgerbook.data.*
import com.iago.ledgerbook.ui.composables.*
import com.iago.ledgerbook.ui.previews.PreviewDataTransaction
import com.iago.ledgerbook.ui.theme.LedgerBookTheme
import com.iago.ledgerbook.utils.DevicePreviews
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun TransactionsScreen() {
    val context = LocalContext.current
    val activity = context as Activity

    ViewCompat.getWindowInsetsController(activity.window.decorView)?.apply {
        hide(WindowInsetsCompat.Type.systemBars())
        systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    val viewModel = hiltViewModel<TransactionsViewModel>()
    val transactions by viewModel.transactions.collectAsState()
    val currentScreen = remember { mutableStateOf(TransactionType.EXPENSE) }

    TransactionsScreenUI(
        transactions = transactions,
        currentScreen = currentScreen.value,
        addTransaction = { viewModel.addTransaction(it) },
        updateTransaction = { viewModel.updateTransaction(it) },
        deleteTransaction = { viewModel.deleteTransaction(it) },
        onChangeTransactionType = { currentScreen.value = it }
    )
}

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreenUI(
    transactions: List<Transaction>,
    currentScreen: TransactionType,
    addTransaction: (Transaction) -> Unit,
    updateTransaction: (Transaction) -> Unit,
    deleteTransaction: (Transaction) -> Unit,
    onChangeTransactionType: (TransactionType) -> Unit,
) {

    val scope = rememberCoroutineScope()
    val appMode = remember { mutableStateOf(AppMode.FIXED) }
    val includeFixed = remember { mutableStateOf(false) }

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = 500)

    var showBottomSheet by remember { mutableStateOf(BottomSheetAction.CLOSE) }
    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
    )

    val editItem = remember { mutableStateOf<Transaction?>(null) }
    val deleteItem = remember { mutableStateOf<Transaction?>(null) }

    val visibleIndex by remember { derivedStateOf { listState.firstVisibleItemIndex } }
    val (currentMonth, currentYear) = getMonthFromIndex(visibleIndex)

    BackHandler(enabled = showBottomSheet != BottomSheetAction.CLOSE) {
        scope.launch {
            sheetState.bottomSheetState.hide()
            showBottomSheet = BottomSheetAction.CLOSE
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    showBottomSheet = BottomSheetAction.CREATE
                    editItem.value = null
                    scope.launch { sheetState.bottomSheetState.expand() }
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "")
            }
        }
    ) { paddingValues ->

        LazyRow(
            state = listState,
            flingBehavior = rememberSnapFlingBehavior(
                listState,
                snapPosition = SnapPosition.Start
            ),
            userScrollEnabled = appMode.value == AppMode.MONTHLY,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            items(Int.MAX_VALUE) { index ->

                val (month, year) = getMonthFromIndex(index)

                val baseList = when (appMode.value) {

                    AppMode.FIXED -> transactions.filter { it.date == null }

                    AppMode.MONTHLY -> transactions.filter {
                        if (it.date == null) return@filter false

                        val cal = Calendar.getInstance().apply {
                            timeInMillis = it.date
                        }

                        cal.get(Calendar.MONTH) + 1 == month && cal.get(Calendar.YEAR) == year
                    }
                }

                val summaryData = getSummaryData(baseList)

                val filteredTransactions = baseList.filter {
                    it.type == currentScreen
                }

                val categoryData = categoryTotals(filteredTransactions, currentScreen)

                Column(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(15.dp)
                ) {

                    ModeHeader(
                        currentMonthYear = if (appMode.value == AppMode.MONTHLY)
                            String.format("%02d/%d", month, year)
                        else null,
                        onToggle = { appMode.value = it },
                        onReportClick ={
                            showBottomSheet = BottomSheetAction.REPORT
                            scope.launch {
                                sheetState.bottomSheetState.expand()
                            }
                        }
                    )

                    SummaryDisplay(
                        summaryData = summaryData,
                        transactionType = currentScreen,
                        isMonthlyMode = (appMode.value == AppMode.MONTHLY)
                    ) {
                        onChangeTransactionType(it)
                    }

                    if (categoryData.isNotEmpty()) {
                        Box(
                            Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CategoryPieChart(categoryData)
                        }
                    }

                    Spacer(Modifier.height(15.dp))

                    if (filteredTransactions.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = when (currentScreen) {
                                    TransactionType.INCOME -> stringResource(R.string.empty_income)
                                    TransactionType.EXPENSE -> stringResource(R.string.empty_expense)
                                    TransactionType.SAVING -> stringResource(R.string.empty_saving)
                                },
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        TransactionsGrid(
                            transactions = filteredTransactions,
                            currentScreen = currentScreen,
                            isMonthlyMode = (appMode.value == AppMode.MONTHLY),
                            onToggleCheck = { transaction ->
                                updateTransaction(transaction.copy(isChecked = !transaction.isChecked))
                            },
                            onEdit = {
                                showBottomSheet = BottomSheetAction.EDIT
                                editItem.value = it
                                scope.launch { sheetState.bottomSheetState.expand() }
                            },
                            onDelete = {
                                deleteItem.value = it
                            }
                        )
                    }
                }
            }
        }
    }

    if (showBottomSheet != BottomSheetAction.CLOSE) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    scope.launch {
                        sheetState.bottomSheetState.hide()
                        showBottomSheet = BottomSheetAction.CLOSE
                    }
                }
        )
    }

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetPeekHeight = 0.dp,
        sheetSwipeEnabled = false,
        sheetContent = {

            when (showBottomSheet) {

                BottomSheetAction.CREATE,
                BottomSheetAction.EDIT -> {

                    TransactionBottomSheetContent(
                        type = currentScreen,
                        isEditing = showBottomSheet == BottomSheetAction.EDIT,
                        amount = editItem.value?.value,
                        description = editItem.value?.title ?: "",
                        selectedCategory = editItem.value?.category,
                        onSubmit = { value, category, title ->

                            if (
                                showBottomSheet == BottomSheetAction.EDIT &&
                                editItem.value != null
                            ) {

                                updateTransaction(
                                    editItem.value!!.copy(
                                        category = category,
                                        title = title,
                                        value = value
                                    )
                                )

                            } else {

                                val (currentMonth, currentYear) =
                                    getMonthFromIndex(visibleIndex)

                                addTransaction(
                                    Transaction(
                                        category = category,
                                        title = title,
                                        value = value,
                                        type = currentScreen,
                                        date = if (appMode.value == AppMode.MONTHLY)
                                            getMillisFromMonthYear(
                                                currentMonth,
                                                currentYear
                                            )
                                        else null
                                    )
                                )
                            }

                            scope.launch {
                                sheetState.bottomSheetState.hide()
                            }

                            showBottomSheet = BottomSheetAction.CLOSE
                        }
                    )
                }

                BottomSheetAction.REPORT -> {

                    MonthlyReportBottomSheet(
                        item = generateMonthlyReport(
                            transactions = transactions,
                            currentMonth = currentMonth,
                            currentYear = currentYear,
                            includeFixed = includeFixed.value
                        ),
                        includeFixed = includeFixed.value,
                        onIncludeFixedChange = { includeFixed.value = it }
                    )
                }

                BottomSheetAction.CLOSE -> {

                    Box(
                        modifier = Modifier.height(1.dp)
                    )
                }
            }
        },
        content = {}
    )

    if (deleteItem.value != null) {
        AlertDialog(
            onDismissRequest = { deleteItem.value = null },
            title = { Text(stringResource(R.string.delete_confirmation_title)) },
            text = {
                Text(
                    stringResource(
                        R.string.delete_confirmation_desc,
                        deleteItem.value!!.title
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    deleteTransaction(deleteItem.value!!)
                    deleteItem.value = null
                }) {
                    Text(stringResource(R.string.delete), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteItem.value = null }) {
                    Text(
                        stringResource(R.string.cancel),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )
    }
}

fun generateMonthlyReport(
    transactions: List<Transaction>,
    currentMonth: Int,
    currentYear: Int,
    includeFixed: Boolean = false
): MonthlyReportItem {

    val fixedList = if (includeFixed) transactions.filter { it.date == null } else emptyList()
    var fixedIncome = 0.0
    var fixedExpense = 0.0
    var fixedSaving = 0.0

    fixedList.forEach {
        when (it.type) {
            TransactionType.INCOME -> fixedIncome += it.value
            TransactionType.EXPENSE -> fixedExpense += it.value
            TransactionType.SAVING -> fixedSaving += it.value
        }
    }

    val grouped = transactions
        .filter { it.date != null }
        .groupBy {

            val cal = Calendar.getInstance().apply {
                timeInMillis = it.date!!
            }

            Pair(
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.YEAR)
            )
        }
        .toSortedMap(
            compareBy<Pair<Int, Int>> { it.second }
                .thenBy { it.first }
        )

    var accumulated = 0.0

    if (grouped.isEmpty()) {
        val balance = fixedIncome - (fixedExpense + fixedSaving)
        return MonthlyReportItem(
            month = currentMonth,
            year = currentYear,
            income = fixedIncome,
            expense = fixedExpense,
            saving = fixedSaving,
            balance = balance,
            accumulated = balance
        )
    }

    grouped.forEach { (date, list) ->

        var income = fixedIncome
        var expense = fixedExpense
        var saving = fixedSaving

        list.forEach {

            when (it.type) {

                TransactionType.INCOME ->
                    income += it.value

                TransactionType.EXPENSE ->
                    expense += it.value

                TransactionType.SAVING ->
                    saving += it.value
            }
        }

        val balance = income - (expense + saving)

        accumulated += balance

        if (
            date.first == currentMonth &&
            date.second == currentYear
        ) {

            return MonthlyReportItem(
                month = currentMonth,
                year = currentYear,
                income = income,
                expense = expense,
                saving = saving,
                balance = balance,
                accumulated = accumulated
            )
        }
    }

    val currentBalance = fixedIncome - (fixedExpense + fixedSaving)
    return MonthlyReportItem(
        month = currentMonth,
        year = currentYear,
        income = fixedIncome,
        expense = fixedExpense,
        saving = fixedSaving,
        balance = currentBalance,
        accumulated = accumulated + currentBalance
    )
}

fun getMillisFromMonthYear(month: Int, year: Int): Long {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month - 1)
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 12)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return calendar.timeInMillis
}

fun getMonthFromIndex(index: Int, center: Int = 500): Pair<Int, Int> {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.MONTH, index - center)
    return (calendar.get(Calendar.MONTH) + 1) to calendar.get(Calendar.YEAR)
}

fun categoryTotals(
    transactions: List<Transaction>,
    type: TransactionType
): Map<TransactionCategory, Double> {
    return transactions
        .filter { it.type == type }
        .groupBy { it.category }
        .mapValues { it.value.sumOf { tx -> tx.value } }
}

fun getSummaryData(transactions: List<Transaction>): SummaryData {
    var income = 0.0
    var expense = 0.0
    var saving = 0.0
    var checkedIncome = 0.0
    var checkedExpense = 0.0
    var checkedSaving = 0.0

    transactions.forEach {
        when (it.type) {
            TransactionType.INCOME -> {
                income += it.value
                if (it.isChecked) checkedIncome += it.value
            }
            TransactionType.EXPENSE -> {
                expense += it.value
                if (it.isChecked) checkedExpense += it.value
            }
            TransactionType.SAVING -> {
                saving += it.value
                if (it.isChecked) checkedSaving += it.value
            }
        }
    }

    return SummaryData(
        incomes = income,
        expenses = expense,
        savings = saving,
        checkedIncomes = checkedIncome,
        checkedExpenses = checkedExpense,
        checkedSavings = checkedSaving
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@DevicePreviews
@Composable
fun TransactionsPreview() {
    LedgerBookTheme {
        Scaffold {
            TransactionsScreenUI(
                transactions = PreviewDataTransaction.transactionList,
                currentScreen = TransactionType.EXPENSE,
                addTransaction = {},
                updateTransaction = {},
                deleteTransaction = {},
                onChangeTransactionType = {}
            )
        }
    }
}