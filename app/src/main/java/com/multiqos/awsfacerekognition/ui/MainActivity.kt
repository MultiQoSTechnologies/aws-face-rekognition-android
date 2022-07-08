package com.multiqos.awsfacerekognition.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.multiqos.awsfacerekognition.ui.screens.MainScreen
import com.multiqos.awsfacerekognition.ui.theme.AWSFaceRekognitionTheme
import com.multiqos.awsfacerekognition.viewmodel.MainViewModel


class MainActivity : ComponentActivity() {

    private var context: Context? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Manifest.permission.READ_EXTERNAL_STORAGE.checkPermission(1023)
        val viewModel = MainViewModel()

        context = this
        setContent {
            AWSFaceRekognitionTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen(viewModel)
                }
            }
        }
    }

    private fun String.checkPermission(requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                this
            ) == PackageManager.PERMISSION_DENIED
        ) {
            // Requesting the permission
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(this), requestCode)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AWSFaceRekognitionTheme {
        MainScreen(null)
    }
}


