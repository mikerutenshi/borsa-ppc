package com.android.borsappc.ui.screen.work

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.hilt.getViewModel
import com.android.borsappc.R
import com.android.borsappc.ui.BorsaPpcTheme
import com.android.borsappc.ui.Green
import com.android.borsappc.ui.Orange
import com.android.borsappc.ui.model.WorkStatus
import com.android.borsappc.ui.model.WorkUiModel
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class WorkScreen : AndroidScreen() {

    override val key: ScreenKey = "WorkScreen"

    @Composable
    override fun Content() {
        val viewModel = getViewModel<WorkViewModel>()
        WorkScreenContent(viewModel)
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WorkScreenContent(viewModel: WorkViewModel) {

    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        WorkList(works = viewModel.works)
    }
}

@Composable
fun WorkList(works: Flow<PagingData<WorkUiModel>>) {
    val lazyWorkItems: LazyPagingItems<WorkUiModel> = works.collectAsLazyPagingItems()
    
    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        items(lazyWorkItems) { work ->
            WorkItem(work = work!!)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WorkItem(work: WorkUiModel) {
    Card(onClick = { /*TODO*/ },
        modifier = Modifier.padding(bottom = 16.dp)) {
        Column(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()) {
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(id = R.string.label_spk))
                Text(work.spkNo.toString())
            }
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(id = R.string.label_date))
                Text(LocalDateTime.parse(work.createdAt).format(DateTimeFormatter
                    .ofLocalizedDate(FormatStyle.MEDIUM)))
            }
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(id = R.string.label_article))
                Text(work.articleNo)
            }
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(id = R.string.label_category))
                Text(work.productCategoryName)
            }
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(id = R.string.label_quantity))
                Text(work.quantity.toString())
            }
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(id = R.string.label_status))
                Row(horizontalArrangement = Arrangement.SpaceAround) {
                    WorkStatusText(title = R.string.status_draw, status = work.drawStatus)
                    WorkStatusText(title = R.string.status_lining_draw, status = work.liningDrawStatus)
                    WorkStatusText(title = R.string.status_sew, status = work.sewStatus)
                    WorkStatusText(title = R.string.status_assemble, status = work.assembleStatus)
                }
            }
            Row(horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()) {
                WorkStatusText(title = R.string.status_sole_stitch, status = work.soleStitchStatus)
                WorkStatusText(title = R.string.status_insole_stitch, status = work.insoleStitchStatus)
            }
            if (work.notes != null) {
                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(id = R.string.label_notes))
                    Text(work.notes!!)
                }
            }
            if (work.updatedAt != null) {
                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(id = R.string.label_updated_at))
                    Text(LocalDateTime.parse(work.updatedAt!!).format(DateTimeFormatter
                        .ofLocalizedDateTime(FormatStyle.MEDIUM)))
                }
            }
        }
    }
}

@Composable
fun WorkStatusText(@StringRes title: Int, status: WorkStatus) {
    val modifier = Modifier.padding(start = 8.dp)
    when (status) {
        WorkStatus.NOT_AVAILABLE -> {}
        WorkStatus.AVAILABLE -> Text(text = stringResource(id = title),
            color = Color.Gray, modifier = modifier, overflow = TextOverflow.Visible)
        WorkStatus.IN_PROGRESS -> Text(text = stringResource(id = title),
            color = Orange, modifier = modifier, overflow = TextOverflow.Visible)
        WorkStatus.COMPLETED -> Text(text = stringResource(id = title),
            color = Green, modifier = modifier, overflow = TextOverflow.Visible)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun WorkItemPreview() {
    BorsaPpcTheme() {
        WorkItem(work = WorkUiModel())
    }
}
