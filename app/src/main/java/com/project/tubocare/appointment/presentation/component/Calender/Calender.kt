package com.project.tubocare.appointment.presentation.component.Calender

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.tubocare.ui.theme.TuboCareTheme
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.StaticCalendar
import io.github.boguszpawlowski.composecalendar.day.DayState
import io.github.boguszpawlowski.composecalendar.header.MonthState
import io.github.boguszpawlowski.composecalendar.rememberCalendarState
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import io.github.boguszpawlowski.composecalendar.selection.EmptySelectionState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import java.time.DayOfWeek
import java.time.DayOfWeek.SUNDAY
import java.time.LocalDate
import java.time.format.TextStyle.SHORT
import java.util.Locale

@Composable
fun CustomComponentsSample() {

    val calendarState = rememberCalendarState()

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(15.dp)
    ) {
        StaticCalendar(
            modifier = Modifier.animateContentSize(),
            firstDayOfWeek = SUNDAY,
            calendarState = calendarState,
            today = LocalDate.now(),
            showAdjacentMonths = false,
            monthContainer = { MonthContainer(it) },
            dayContent = { DayContent(state = it) },
            daysOfWeekHeader = { WeekHeader(daysOfWeek = it) },
            monthHeader = { MonthHeader(monthState = it) },
        )
    }
}

@Composable
private fun <T : EmptySelectionState> DayContent(
    state: DayState<T>,
    modifier: Modifier = Modifier,
    selectionColor: Color = MaterialTheme.colorScheme.primary,
    unselectionColor: Color = MaterialTheme.colorScheme.onPrimary,
    currentDayColor: Color = MaterialTheme.colorScheme.secondary,
    onClick: (LocalDate) -> Unit = {}
) {

    val date = state.date
    val selectionState = state.selectionState

    val isSelected = selectionState.isDateSelected(date)

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(
                color = (if (isSelected) selectionColor else unselectionColor)
            )
            .border(
                width = if (state.isCurrentDay) 1.dp else 0.dp,
                color = if (state.isCurrentDay) currentDayColor else unselectionColor,
                shape = CircleShape)
            .clickable {
                onClick(date)
                selectionState.onDateSelected(date)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            textAlign = TextAlign.Center,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.labelSmall,
        )

    }

    /*Card(
        modifier = modifier
            .aspectRatio(1f)
            .padding(2.dp),
        elevation = CardDefaults.elevatedCardElevation(
            if (state.isFromCurrentMonth) 4.dp else 0.dp
        ),
        border = if (state.isCurrentDay) BorderStroke(1.dp, currentDayColor) else null,
        colors = CardDefaults.cardColors(if (isSelected) selectionColor else contentColorFor(
            backgroundColor = Color.White)
        )
    ) {


    }*/
}

@Composable
private fun WeekHeader(daysOfWeek: List<DayOfWeek>) {

    Row(modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)) {
        daysOfWeek.forEach { dayOfWeek ->
            Text(
                text = dayOfWeek.getDisplayName(SHORT, Locale.ROOT),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
            )
        }
    }
}

@Composable
private fun MonthHeader(monthState: MonthState) {
    val monthText = monthState.currentMonth.month.name

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { monthState.currentMonth = monthState.currentMonth.plusMonths(-1) },
            modifier = Modifier
                .clip(CircleShape)
                .size(30.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Image(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary),
                contentDescription = "Prev",
                modifier = Modifier.size(14.dp)
            )
        }
        Row {
            Text(
                text = monthText
                    .lowercase()
                    .replaceFirstChar { it.titlecase() },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = monthState.currentMonth.year.toString(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )

        }
        IconButton(
            onClick = { monthState.currentMonth = monthState.currentMonth.plusMonths(1) },
            modifier = Modifier
                .clip(CircleShape)
                .size(30.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Image(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary),
                contentDescription = "Next",
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
private fun MonthContainer(content: @Composable (PaddingValues) -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(10.dp),
        content = { content(PaddingValues(4.dp)) },
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary)
    )
}

@Preview
@Composable
private fun CustomComponentsPreview() {
    TuboCareTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            CustomComponentsSample()
        }
    }
}

//////////////////////

@Composable
fun SelectableCalendarSample() {
    val calendarState = rememberSelectableCalendarState()

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(15.dp)
        ) {
            SelectableCalendar(
                calendarState = calendarState,
            )
        }

//        SelectionControls(selectionState = calendarState.selectionState)
    }
}

@Composable
private fun SelectionControls(
    selectionState: DynamicSelectionState,
) {
    Text(
        text = "Calendar Selection Mode",
        style = MaterialTheme.typography.headlineLarge,
    )
    SelectionMode.values().forEach { selectionMode ->
        Row(modifier = Modifier.fillMaxWidth()) {
            RadioButton(
                selected = selectionState.selectionMode == selectionMode,
                onClick = { selectionState.selectionMode = selectionMode }
            )
            Text(text = selectionMode.name)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }

    Text(
        text = "Selection: ${selectionState.selection.joinToString { it.toString() }}",
        style = MaterialTheme.typography.headlineLarge,
    )
}

@Preview(showBackground = true)
@Composable
private fun SelectableCalendarSamplePreview() {
    TuboCareTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SelectableCalendarSample()
        }
    }
}

