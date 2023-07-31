package com.example.stydyandroid2023.designSystem

import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


//USAGE
fun main() {
    /*
      val state: String by viewModel.state.collectAsStateWithLifecycle()
    when (result) {
        is Result.Success -> Item()
        is Result.Loading -> Item.Loading()
        is Result.Error -> Item.Error()
    }
    */

}

object PrimaryButton {

    @Composable
    operator fun invoke(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        text: String = "Primary Button",
    ) {
        Button(onClick = onClick, modifier = modifier) {
            Text(text = text)
        }
    }

    @Composable
    fun Large(onClick: () -> Unit) {
        PrimaryButton(
            text = "Primary Button Large",
            onClick = onClick,
            modifier = Modifier.width(200.dp)
        )
    }

    @Composable
    fun Small(onClick: () -> Unit) {
        PrimaryButton(
            text = "Primary Button Small",
            onClick = onClick,
            modifier = Modifier.width(100.dp)
        )
    }
}