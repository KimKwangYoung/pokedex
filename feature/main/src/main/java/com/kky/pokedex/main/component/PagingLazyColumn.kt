package com.kky.pokedex.main.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PagingLazyColumn(
    reachedEnd: Boolean,
    loadAction: () -> Unit,
    content: LazyListScope.() -> Unit
) {
    var pagingState = remember { PagingState(false) }

    if (pagingState.isLoading) {
        pagingState = PagingState(false)
    }

    PagingLazyColumnContent(
        reachedEnd = reachedEnd,
        loadAction = {
            pagingState = PagingState(true)
            loadAction()
        },
        pagingState = pagingState,
        content = content,
    )
}

@Composable
private fun PagingLazyColumnContent(
    reachedEnd: Boolean,
    state: LazyListState = rememberLazyListState(),
    pagingState: PagingState,
    loadAction: () -> Unit,
    content: LazyListScope.() -> Unit
) {
    state.onLoadMore(action = loadAction, isLoading = pagingState.isLoading)

    LazyColumn(
        state = state,
    ) {
        content()
        if (!reachedEnd) item {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth(),
            ) {
                CircularProgressIndicator(modifier = Modifier.padding(vertical = 10.dp))
            }
        }
    }
}

private fun LazyListState.reachedBottom(
    limitCount: Int = 6,
    triggerOnEnd: Boolean = false,
): Boolean {
    val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
    return ((triggerOnEnd && lastVisibleItem?.index == layoutInfo.totalItemsCount - 1) ||
            lastVisibleItem?.index != null && lastVisibleItem.index >= layoutInfo.totalItemsCount - (limitCount + 2))
}

@SuppressLint("ComposableNaming")
@Composable
private fun LazyListState.onLoadMore(
    limitCount: Int = 4,
    loadOnBottom: Boolean = true,
    isLoading: Boolean,
    action: () -> Unit,
) {
    val reached by remember {
        derivedStateOf {
            reachedBottom(
                limitCount = limitCount,
                triggerOnEnd = loadOnBottom
            )
        }
    }

    LaunchedEffect(reached) {
        if (reached && !isLoading) {
            action()
        }
    }
}

data class PagingState(val isLoading: Boolean)
