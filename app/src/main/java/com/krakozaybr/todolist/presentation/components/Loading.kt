package com.krakozaybr.todolist.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.krakozaybr.todolist.R
import com.krakozaybr.todolist.presentation.theme.AppTheme

@Composable
fun Loading(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.loading),
                style = MaterialTheme.typography.titleLarge
            )
            CircularProgressIndicator(
                strokeCap = StrokeCap.Round
            )
        }
    }
}

@Preview
@Composable
fun LoadingPreview() {
    AppTheme {
        Surface(

        ) {
            Loading(modifier = Modifier.fillMaxSize())
        }
    }
}
