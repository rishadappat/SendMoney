package com.appat.sendmoney.ui.customviews

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomSheetDefaults.DragHandle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T>SingleSelectionBottomSheet(openBottomSheet: Boolean,
                                     title: String,
                                     items: List<T> = listOf(),
                                     itemText: (T) -> String,
                                     subText: (T) -> String = {""},
                                     didSelectItem: (T)-> Unit,
                                     onDismissRequest: () -> Unit) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedIndex by rememberSaveable { mutableIntStateOf(-1) }
    if (openBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing),
            onDismissRequest = {
                selectedIndex = -1
                onDismissRequest()
            },
            sheetState = bottomSheetState,
            dragHandle = {},
            containerColor = MaterialTheme.colorScheme.background
        ) {
            DragHandle(modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
            Box(modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center) {
                Text(title, style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary)
            }
            SheetContent<T>(items, itemText, subText, didSelectItem, onDismissRequest)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T>SheetContent(items: List<T> = listOf(),
                            itemText: (T) -> String,
                            subText: (T) -> String? = {""},
                            didSelectItem: (T)-> Unit,
                            onDismissRequest: () -> Unit) {

    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedIndex by rememberSaveable { mutableIntStateOf(-1) }

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = this.maxHeight * 0.8f)
            .padding(20.dp)
            .clip(RoundedCornerShape(10.dp))) {
            items(items.size) {
                if(it != 0) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.tertiary)
                }
                ListItem(
                    modifier = Modifier
                        .height(70.dp)
                        .clickable(onClick = {
                            scope.launch {
                                selectedIndex = it
                                didSelectItem(items[it])
                                bottomSheetState.hide()
                                onDismissRequest()
                                selectedIndex = -1
                            }
                        }),
                    colors = ListItemDefaults.colors(headlineColor = MaterialTheme.colorScheme.onPrimary,
                        supportingColor  = MaterialTheme.colorScheme.onTertiary,
                        containerColor = Color.White),
                    headlineContent = { Text(itemText(items[it])) },
                    supportingContent = {
                        if(!subText(items[it]).isNullOrEmpty()) {
                            Text(subText(items[it]) ?: "")
                        }
                    },
                    leadingContent = {
                        Icon(
                            Icons.Default.Done,
                            contentDescription = "Selected",
                            tint = if(selectedIndex == it)  Color.Black else Color.Transparent
                        )
                    }
                )
            }
        }
    }
}
