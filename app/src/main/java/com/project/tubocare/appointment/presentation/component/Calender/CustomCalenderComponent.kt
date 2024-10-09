package com.project.tubocare.appointment.presentation.component.Calender

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.project.tubocare.appointment.domain.model.Appointment
import com.project.tubocare.core.util.toLocalDate
import com.project.tubocare.ui.theme.TuboCareTheme
import io.github.boguszpawlowski.composecalendar.StaticCalendar
import io.github.boguszpawlowski.composecalendar.day.DayState
import io.github.boguszpawlowski.composecalendar.header.MonthState
import io.github.boguszpawlowski.composecalendar.rememberCalendarState
import io.github.boguszpawlowski.composecalendar.selection.EmptySelectionState
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

@Composable
fun CustomCalender(appointments: List<Appointment>) {

    val calendarState = rememberCalendarState()

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(15.dp)
    ) {
        StaticCalendar(
            modifier = Modifier.animateContentSize(),
            firstDayOfWeek = DayOfWeek.SUNDAY,
            calendarState = calendarState,
            today = LocalDate.now(),
            showAdjacentMonths = false,
            monthContainer = { MonthContainer(it) },
            dayContent = { DayContent(state = it, appointments = appointments) },
            daysOfWeekHeader = { WeekHeader(daysOfWeek = it) },
            monthHeader = { MonthHeader(monthState = it) },
        )
    }
}

@Composable
private fun DayContent(
    state: DayState<EmptySelectionState>,
    appointments: List<Appointment>,
    modifier: Modifier = Modifier,
    selectionColor: Color = MaterialTheme.colorScheme.primary,
    unSelectionColor: Color = MaterialTheme.colorScheme.onPrimary,
    currentDayColor: Color = MaterialTheme.colorScheme.secondary,
    onClick: (LocalDate) -> Unit = {}
) {

    val date = state.date
    val selectionState = state.selectionState
    val isSelected = selectionState.isDateSelected(date)
    val hasAppointment = appointments.any { it.date?.toLocalDate() == date}

    Box(
        modifier = Modifier
            .aspectRatio(1.5f)
            .clip(CircleShape)
            .background(
                color = (if (isSelected) selectionColor else unSelectionColor)
            )
            .border(
                width = if (state.isCurrentDay) 1.dp else 0.dp,
                color = if (state.isCurrentDay) currentDayColor else unSelectionColor,
                shape = CircleShape
            )
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
        if (hasAppointment){
            Box(modifier = Modifier
                .offset(x = 10.dp, y = (-8).dp)
                .size(6.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)

            )
        }
    }
}

@Composable
private fun WeekHeader(daysOfWeek: List<DayOfWeek>) {

    Row(modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)) {
        daysOfWeek.forEach { dayOfWeek ->
            Text(
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ROOT),
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
private fun CustomCalenderPreview() {
    TuboCareTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            CustomCalender(appointments = listOf(
                Appointment(
                    appointmentId = "",
                    userId = "",
                    name = "",
                    date = Date(),
                    time = LocalTime.now(),
                    location = "",
                    note = ""
                )
            ))
        }
    }
}