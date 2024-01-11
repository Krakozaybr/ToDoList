package com.krakozaybr.todolist.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.krakozaybr.todolist.R
import com.krakozaybr.todolist.presentation.theme.AppTheme


@Composable
fun SaveButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        )
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.save),
            textAlign = TextAlign.Center
        )
    }
}


@Preview
@Composable
fun SaveButtonPreview() {
    Surface {
        Column (modifier = Modifier.width(200.dp)){
            AppTheme {
                SaveButton {

                }
            }
            AppTheme(darkTheme = true) {
                SaveButton {

                }
            }
        }
    }
}

@Composable
fun DeleteButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        )
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.delete),
            textAlign = TextAlign.Center
        )
    }
}


@Preview
@Composable
fun DeleteButtonPreview() {
    Surface {
        Column (modifier = Modifier.width(200.dp)){
            AppTheme {
                DeleteButton {

                }
            }
            AppTheme(darkTheme = true) {
                DeleteButton {

                }
            }
        }
    }
}
