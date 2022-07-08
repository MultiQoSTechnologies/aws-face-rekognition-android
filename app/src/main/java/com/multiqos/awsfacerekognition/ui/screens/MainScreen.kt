package com.multiqos.awsfacerekognition.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.multiqos.awsfacerekognition.viewmodel.MainViewModel
import com.multiqos.awsfacerekognition.R

@Composable
fun MainScreen(viewModel: MainViewModel?) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.face_reko))
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.Menu,"")
                    }
                },
                backgroundColor = MaterialTheme.colors.primary
            )
        }, content = {
        })

    Column(
        modifier = Modifier.fillMaxSize().padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.aws_face_reko),
            modifier = Modifier.padding(top = 50.dp),
            style = TextStyle(fontWeight = FontWeight(500), fontSize = 20.sp)
        )
        viewModel?.imagePathOne = userImage()
        viewModel?.imagePathSecond = userImage()

        Button(onClick = {
            if (viewModel?.imagePathOne == null || viewModel.imagePathSecond == null){
                Toast.makeText(context, "Please select both photo first.", Toast.LENGTH_SHORT).show()
                return@Button
            }
            viewModel.matchPhoto()
        }, modifier = Modifier.padding(top = 50.dp)) {
            Text(
                text = "Compare",
                fontSize = 15.sp,
            )
        }
        val resultOfMatch: String by viewModel?.matchResult!!.observeAsState("")
        Text(
            text = " $resultOfMatch",
            fontSize = 15.sp,
            modifier = Modifier.padding(10.dp)
        )
    }
}

@Composable
fun userImage(): Uri? {
    var pickedImage by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        pickedImage = uri
    }
    Image(
        modifier = Modifier
            .padding(top = 10.dp)
            .height(100.dp)
            .clip(CircleShape) // clip to the circle shape
            .width(100.dp)
            .clickable {
                launcher.launch("image/*")
            },
        painter = (pickedImage?.let {
            rememberImagePainter(it)
        } ?: painterResource(id = R.drawable.user_avtar)),
        contentDescription = "First Image",
        contentScale = ContentScale.Crop
    )
    return pickedImage
}