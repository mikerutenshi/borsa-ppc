package com.android.borsappc.ui.screen.work

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
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
    
    LazyColumn {
        items(lazyWorkItems) { work ->
            WorkItem(work = work!!)
        }
    }
}

@Composable
fun WorkItem(work: WorkUiModel) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(1f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(stringResource(id = R.string.label_spk))
            Text(stringResource(id = R.string.label_date))
            Text(stringResource(id = R.string.label_article))
            Text(stringResource(id = R.string.label_category))
            Text(stringResource(id = R.string.label_quantity))
            Text(stringResource(id = R.string.label_status))
            if (work.notes != null) {
                Text(stringResource(id = R.string.label_notes))
            }
            if (work.updatedAt != null) {
                Text(stringResource(id = R.string.label_updated_at))
            }
        }
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(work.spkNo.toString())
            Text(LocalDateTime.parse(work.createdAt).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
            Text(work.articleNo)
            Text(work.productCategoryName)
            Text(work.quantity.toString())
            Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                WorkStatusText(title = R.string.status_draw, status = work.drawStatus)
                WorkStatusText(title = R.string.status_lining_draw, status = work.liningDrawStatus)
                WorkStatusText(title = R.string.status_sew, status = work.sewStatus)
                WorkStatusText(title = R.string.status_assemble, status = work.assembleStatus)
                WorkStatusText(title = R.string.status_sole_stitch, status = work.soleStitchStatus)
                WorkStatusText(title = R.string.status_insole_stitch, status = work.insoleStitchStatus)
            }
            if (work.notes != null) {
                Text(work.notes!!)
            }
            if (work.updatedAt != null) {
                Text(work.updatedAt!!)
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
            color = Color.Gray, modifier = modifier)
        WorkStatus.IN_PROGRESS -> Text(text = stringResource(id = title),
            color = Orange, modifier = modifier)
        WorkStatus.COMPLETED -> Text(text = stringResource(id = title),
            color = Green, modifier = modifier)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2)
@Composable
fun WorkItemPreview() {
    WorkItem(work = WorkUiModel())
}
