package pt.uevora.spacehub.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.uevora.spacehub.R
import pt.uevora.spacehub.ui.util.isValidIsoDate
import pt.uevora.spacehub.ui.util.toEpochMillis
import pt.uevora.spacehub.ui.util.toIsoDate

/**
 * Displays a read-only date field with a Material date picker dialog.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    dateText: String,
    onDateTextChange: (String) -> Unit,
    onConfirm: () -> Unit,
    confirmButtonText: String,
    modifier: Modifier = Modifier,
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            value = dateText,
            onValueChange = onDateTextChange,
            label = { Text(stringResource(id = R.string.date)) },
            placeholder = { Text(stringResource(R.string.date_format_hint)) },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_calendar),
                        contentDescription = stringResource(id = R.string.select_date)
                    )
                }
            },
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .clickable { showDialog = true }
        )
        Button(
            onClick = onConfirm,
            enabled = dateText.isValidIsoDate(),
            modifier = Modifier
                    .padding(top = 7.dp)
                    .height(56.dp),
        ) {
            Text(confirmButtonText)
        }
    }

    if (showDialog) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = dateText.toEpochMillis()
        )
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedDateMillis = datePickerState.selectedDateMillis

                        if (selectedDateMillis != null) {
                            onDateTextChange(selectedDateMillis.toIsoDate())
                        }

                        showDialog = false
                    }
                ) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}