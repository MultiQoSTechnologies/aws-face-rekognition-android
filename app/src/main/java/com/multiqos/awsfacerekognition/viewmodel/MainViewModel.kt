package com.multiqos.awsfacerekognition.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.rekognition.AmazonRekognition
import com.amazonaws.services.rekognition.AmazonRekognitionClient
import com.amazonaws.services.rekognition.model.CompareFacesRequest
import com.amazonaws.services.rekognition.model.Image
import com.amazonaws.util.IOUtils
import com.multiqos.awsfacerekognition.App.Companion.context
import com.multiqos.awsfacerekognition.R
import com.multiqos.awsfacerekognition.utils.getImagePath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.nio.ByteBuffer


class MainViewModel : ViewModel() {

    var imagePathOne: Uri? by mutableStateOf(null)
    var imagePathSecond: Uri? by mutableStateOf(null)

    var accessKey = "YOUR ACCESS KEY"
    var secretKey = "YOUR SECRET KEY"


    var similarityThreshold = 70f
    private var _courseList = MutableLiveData<String>()
    var matchResult: LiveData<String> = _courseList

    fun matchPhoto() {
        _courseList.value = context.getString(R.string.please_wait)

        try {
            awsFaceCompareRequest()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun awsFaceCompareRequest() {
        try {

            viewModelScope.launch {

                withContext(Dispatchers.IO) {

                    try {

                        val credentials: AWSCredentials = BasicAWSCredentials(accessKey, secretKey)

                        val rekognitionClient: AmazonRekognition =
                            AmazonRekognitionClient(credentials)
                        rekognitionClient.setRegion(Region.getRegion(Regions.US_EAST_1))

                        var sourceImageBytes: ByteBuffer?
                        var targetImageBytes: ByteBuffer?

                        FileInputStream(getImagePath(imagePathOne)?.let { File(it) }).use { inputStream ->
                            sourceImageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream))
                        }
                        FileInputStream(getImagePath(imagePathSecond)?.let { File(it) }).use { inputStream ->
                            targetImageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream))
                        }

                        val source = Image()
                            .withBytes(sourceImageBytes)
                        val target = Image()
                            .withBytes(targetImageBytes)

                        val request = CompareFacesRequest()
                            .withSourceImage(source)
                            .withTargetImage(target)
                            .withSimilarityThreshold(similarityThreshold)

                        val compareFacesResult = rekognitionClient.compareFaces(request)
                        val faceDetails = compareFacesResult.faceMatches

                        withContext(Dispatchers.Main) {
                            if (faceDetails.size == 0) {
                                _courseList.value = context.getString(R.string.face_not_matched)
                            } else {
                                for (match in faceDetails) {
                                    _courseList.value =
                                        "Wo hoo... Matched... \n Similarity: ${match.similarity}"
                                }
                            }
                        }
                    }catch (e: Exception){
                        withContext(Dispatchers.Main){
                            _courseList.value = context.getString(R.string.access_keys_message)
                        }
                        e.printStackTrace()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
