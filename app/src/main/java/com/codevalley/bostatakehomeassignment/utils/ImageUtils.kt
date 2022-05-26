package com.codevalley.bostatakehomeassignment.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

open class ImageUtils(val context: Context) {

    fun getLocalBitmapUri(bmp: Bitmap): Uri? {
        var bmpUri: Uri? = null
        try {
            val file = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "Bosta${System.currentTimeMillis()}.png"
            )
            val out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()
            bmpUri = FileProvider.getUriForFile(
                context,
                "com.codevalley.bostatakehomeassignment.fileprovider",
                file
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bmpUri
    }

    suspend fun convertUrlIntoBitmap(imageUrl:String?): Bitmap?{
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .allowHardware(false) // Disable hardware bitmaps.
            .build()
        val result = (loader.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }
}