package com.android.borsappc.ui.screen.main

import API_DATE_FORMAT
import Filter
import android.app.DatePickerDialog
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.borsappc.R
import com.android.borsappc.ui.BorsaPpcTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun FilterBottomSheet(viewModel: MainViewModel) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
//    val startDate = remember { mutableStateOf(LocalDate.now().minusWeeks(1)) }
//    val endDate = remember { mutableStateOf(LocalDate.now()) }
    val startDate = LocalDate
        .parse(uiState.value.workQuery.startDate, DateTimeFormatter
            .ofPattern(API_DATE_FORMAT))
    val endDate = LocalDate
        .parse(uiState.value.workQuery.endDate, DateTimeFormatter
            .ofPattern(API_DATE_FORMAT))
    val startDatePicker = datePicker(startDate) { selectedDate ->
        viewModel.onEvent(MainUIEvent.StartDateChanged(selectedDate))
    }
    val endDatePicker = datePicker(endDate) { selectedDate ->
        viewModel.onEvent(MainUIEvent.EndDateChanged(selectedDate))
    }
    var expanded by remember {
        mutableStateOf(false)
    }
    var textFieldSize by remember { mutableStateOf(Size.Zero)}
    val sortOptions = mapOf(
        Pair(Filter.BY_SPK_NO, stringResource(id = R.string.label_spk)),
        Pair(Filter.BY_ARTICLE_NO, stringResource(id = R.string.label_article)),
        Pair(Filter.BY_CREATED_AT, stringResource(id = R.string.label_date)),
    )
    val sortDirections = mapOf(
        Pair(Filter.DIRECTION_DESC, true),
        Pair(Filter.DIRECTION_ASC, false),
    )
//    var selectedSort by remember {
//        mutableStateOf(sortOptions.getValue(Sort.BY_SPK_NO))
//    }
//    var checked by remember {
//        mutableStateOf(false)
//    }
//    var sortDirection by remember { mutableStateOf(sortDirections.getValue(false)) }
//     val interactionSource = remember { MutableInteractionSource() }

    Column(modifier = Modifier
        .padding(24.dp)
    ) {
        Text(text = "Filter")
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Box(modifier = Modifier.weight(0.5f)) {
                OutlinedTextField(
                    value = uiState.value.workQuery.startDate
                        .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
                    label = { Text(text = "From") },
                    onValueChange = {}
                )

                Box(modifier = Modifier
                    .clickable {
                        startDatePicker.show()
                    }
                    .matchParentSize())
            }
            Spacer(modifier = Modifier.width(12.dp))
            Box(modifier = Modifier.weight(0.5f)) {
                OutlinedTextField(
                    value = uiState.value.workQuery.endDate
                        .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
                    label = { Text(text = "Until") },
                    onValueChange = {}
                )
                Box(modifier = Modifier
                    .clickable {
                        endDatePicker.show()
                    }
                    .matchParentSize())
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Sort")
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Box {
                    OutlinedTextField(
                        value = sortOptions.getValue(uiState.value.workQuery.orderBy),
//                        value = sortOptions.getValue(Filter.BY_CREATED_AT),
                        label = { Text(text = "Sort by") },
                        onValueChange = {},
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .onGloballyPositioned { layoutCoordinates ->
                                textFieldSize = layoutCoordinates.size.toSize()
                            }
                    )
                    Box(modifier = Modifier
                        .clickable {
                            expanded = !expanded
                        }
                        .matchParentSize())
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .width(with(LocalDensity.current){textFieldSize.width.toDp()})
                ) {
                    sortOptions.forEach { option ->
                        DropdownMenuItem(onClick = {
                            viewModel.onEvent(MainUIEvent.SortKeyChanged(option.key))
                            expanded = false
                        }) {
                            Text(text = option.value)
                        }
                    }
                }
            }
            IconToggleButton(
                checked = sortDirections.getValue(uiState.value.workQuery.orderDirection),
                onCheckedChange = { isChecked ->
                    val sortDirection = if (isChecked) Filter.DIRECTION_DESC else Filter.DIRECTION_ASC
                    viewModel.onEvent(
                        MainUIEvent.SortDirectionChanged(sortDirection))
            }) {
                Row {
                    Icon(imageVector = Icons.Filled.SortByAlpha,
                        contentDescription = "Sort Alphabetically")
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = if (uiState.value.workQuery.orderDirection == Filter.DIRECTION_DESC) "Desc" else "Asc")
                }
            }
        }
    }
}

@Composable
fun datePicker(date: LocalDate, onDatePicked: (date: LocalDate) -> Unit): DatePickerDialog {
    val year = date.year
    val month = date.monthValue
    val day = date.dayOfMonth
    val context = LocalContext.current

    return DatePickerDialog(
        context, { _: android.widget.DatePicker, y: Int, M: Int, d: Int ->
            val chosenDate = LocalDate.of(y, M + 1, d)
            onDatePicked(chosenDate)
        }, year, month - 1, day
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun WorkItemPreview() {
    BorsaPpcTheme {
//        FilterBottomSheet()
    }
}
//@Composable
//fun rememberDatePickerDialog(
//    title: String,
//    selection: Long? = null,
//    bounds: CalendarConstraints? = null,
//    onDateSelected: (Long) -> Unit = {},
//): DatePicker {
//    val fm = rememberFragmentManager()
//    val tag = "date_picker"
//    val datePicker = remember {
//        @Suppress("UNCHECKED_CAST")
//        fm.findFragmentByTag(tag) as? MaterialDatePicker<Long> ?: createMaterialDatePicker(
//            selection = selection,
//            bounds = bounds,
//            title = title
//        )
//    }
//
//    DisposableEffect(datePicker) {
//        val listener = MaterialPickerOnPositiveButtonClickListener<Long> {
//            if (it != null) onDateSelected(it)
//        }
//        datePicker.addOnPositiveButtonClickListener(listener)
//        datePicker.addOnNegativeButtonClickListener { datePicker.dismiss() }
//        onDispose {
//            datePicker.removeOnPositiveButtonClickListener(listener)
//        }
//    }
//
//    return DatePicker(datePicker, fm, tag)
//}
//
//class DatePicker(
//    private val materialDatePicker: MaterialDatePicker<Long>,
//    private val fragmentManager: FragmentManager,
//    private val tag: String,
//    ) {
//
//        fun show() = materialDatePicker.show(fragmentManager, tag)
//
//        fun hide() = materialDatePicker.dismiss()
//}
//
//private fun createMaterialDatePicker(
//    title: String,
//    selection: Long?,
//    bounds: CalendarConstraints?,
//) = MaterialDatePicker.Builder.datePicker()
//    .setSelection(selection)
//    .setCalendarConstraints(bounds)
//    .setTitleText(title)
//    .build()
//
//
//@Composable
//fun rememberFragmentManager(): FragmentManager {
//    val context = LocalContext.current
//    return remember(context) {
//        (context as FragmentActivity).supportFragmentManager
//    }
//}


//@Composable
//fun CustomCalendarView() {
//    AndroidView(
//        { CalendarView(it) },
//        modifier = Modifier.wrapContentWidth(),
//        update = { views ->
//            views.setOnDateChangeListener { calendarView, i, i2, i3 ->
//            }
//        }
//    )
//}