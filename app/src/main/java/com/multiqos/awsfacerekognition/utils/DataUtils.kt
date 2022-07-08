package com.multiqos.awsfacerekognition.utils

import android.annotation.SuppressLint
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.multiqos.awsfacerekognition.App.Companion.context


@SuppressLint("Range")
fun getImagePath(uri: Uri?): String? {
    var cursor: Cursor? = uri?.let { context.contentResolver.query(it, null, null, null, null) }
    cursor?.moveToFirst()
    var documentId: String? = cursor?.getString(0)
    documentId = documentId?.substring(documentId.lastIndexOf(":") + 1)
    cursor?.close()
    cursor = context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        null, MediaStore.Images.Media._ID + " = ? ", arrayOf(documentId), null
    )
    cursor?.moveToFirst()
    val path: String? = cursor?.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
    cursor?.close()
    return path
}