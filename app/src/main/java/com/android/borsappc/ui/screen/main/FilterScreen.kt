package com.android.borsappc.ui.screen.main

import android.app.DatePickerDialog
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.borsappc.ui.BorsaPpcTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterBottomSheet() {

    val startDate = remember { mutableStateOf(LocalDate.now().minusWeeks(1)) }
    val endDate = remember { mutableStateOf(LocalDate.now()) }
    val startDatePicker = datePicker(startDate.value) {
        startDate.value = it
    }
    val endDatePicker = datePicker(endDate.value) {
        endDate.value = it
    }
    Column(modifier = Modifier.padding(24.dp)) {
        Text(text = "Filter")
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            OutlinedTextField(
                value = startDate.value.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                , label = { Text(text = "Start Date") }
                , onValueChange = {}
                , enabled = false, modifier = Modifier.clickable { startDatePicker.show() }
            )
            OutlinedTextField(
                value = endDate.value.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                , label = { Text(text = "End Date") }
                , onValueChange = {}
                , enabled = false, modifier = Modifier.clickable { endDatePicker.show() }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Sort")
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
        }, year, month, day
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun WorkItemPreview() {
    BorsaPpcTheme {
        FilterBottomSheet()
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