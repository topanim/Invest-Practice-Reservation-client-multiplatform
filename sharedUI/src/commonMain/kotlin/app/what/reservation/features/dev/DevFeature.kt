package app.what.reservation.features.dev

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import app.what.reservation.core.foundation.ui.Gap
import app.what.reservation.core.foundation.ui.SegmentTab
import app.what.reservation.core.foundation.ui.useState
import app.what.reservation.core.foundation.utils.freeze
import app.what.reservation.features.dev.presentation.FeaturePane
import app.what.reservation.features.dev.presentation.LogsPane
import app.what.reservation.features.dev.presentation.NetworksPane
import app.what.reservation.ui.theme.icons.WHATIcons
import app.what.reservation.ui.theme.icons.filled.Features
import app.what.reservation.ui.theme.icons.filled.Logs
import app.what.reservation.ui.theme.icons.filled.Network
import kotlinx.coroutines.launch

enum class DevToolsTab(
    val title: String, val icon: ImageVector
) {
    LOGS("Логи", WHATIcons.Logs),
    NETWORK("Сеть", WHATIcons.Network),
    FEATURES("Фичи", WHATIcons.Features);

    companion object {
        fun all() = listOf(LOGS, NETWORK, FEATURES)
    }
}

@Composable
fun DevFeature(
    modifier: Modifier = Modifier
) = Column(
    Modifier.statusBarsPadding()
) {
    var selectedTabIndex by useState(0)
    val devToolsTabs = DevToolsTab.all()
        .dropLast(1)
        .freeze()
    val pagerState = rememberPagerState { devToolsTabs.size }
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        selectedTabIndex = pagerState.currentPage
    }

    Gap(8)

    SingleChoiceSegmentedButtonRow(
        space = (-4).dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        devToolsTabs.forEachIndexed { index, it ->
            val selected = selectedTabIndex == index

            SegmentTab(
                selected = selected,
                index = index,
                count = devToolsTabs.size,
                icon = it.icon,
                label = null
            ) { scope.launch { pagerState.animateScrollToPage(index) } }
        }
    }

    HorizontalPager(pagerState) {
        when (devToolsTabs[it]) {
            DevToolsTab.LOGS -> LogsPane()
            DevToolsTab.NETWORK -> NetworksPane()
            DevToolsTab.FEATURES -> FeaturePane()
        }
    }
}