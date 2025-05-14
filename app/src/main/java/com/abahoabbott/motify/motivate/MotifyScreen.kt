package com.abahoabbott.motify.motivate

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import com.abahoabbott.motify.ui.theme.MotifyTheme

@Composable
fun MotifyScreen(
    modifier: Modifier = Modifier,
    viewModel: MotifyViewModel = hiltViewModel()
){

    MotifyScreenContents()
}

@Composable
private fun MotifyScreenContents(){


}

@Composable
@PreviewLightDark
private fun MotifyScreenContentsPreview(){
    MotifyTheme {
        Surface {
            MotifyScreenContents()
        }
    }
}