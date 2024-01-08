package com.krakozaybr.todolist.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.krakozaybr.todolist.R
import com.krakozaybr.todolist.presentation.theme.AppTheme


@Composable
fun NameInput(
    text: String,
    error: Boolean,
    modifier: Modifier = Modifier,
    onTextChanged: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = text,
        singleLine = true,
        onValueChange = onTextChanged,
        isError = error,
        label = {
            Text(
                text = stringResource(id = R.string.name)
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium
    )
}

@Preview
@Composable
fun NameInputPreview() {
    AppTheme {
        Surface {

            var text by remember { mutableStateOf("") }

            Column(modifier = Modifier.padding(12.dp)) {
                NameInput(text = text, error = false) {
                    text = it
                }
                NameInput(text = text, error = true) {
                    text = it
                }
            }
        }
    }
}

@Composable
fun DescriptionInput(
    text: String,
    modifier: Modifier = Modifier,
    onTextChanged: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = text,
        minLines = 6,
        onValueChange = onTextChanged,
        label = {
            Text(
                text = stringResource(id = R.string.description)
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium
    )
}

@Preview
@Composable
fun DescriptionInputPreview() {
    AppTheme {
        Surface {

            var text by remember { mutableStateOf("") }

            Column(modifier = Modifier.padding(12.dp)) {
                DescriptionInput(text = text) {
                    text = it
                }
            }
        }
    }
}


@Composable
fun DateInput(
    modifier: Modifier = Modifier
) {

}
