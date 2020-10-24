package com.example.takeahike.backend.utilities

import android.app.Application
import android.content.Context
import com.example.takeahike.backend.data.CurrentHike
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.ObjectOutputStream

fun saveCurrentHike(currentHike: CurrentHike, context: Context, fileName: String) {
    context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
        it.write(Json.encodeToString(currentHike).toByteArray())
    }
}

fun loadCurrentHike(context: Context, fileName: String) : CurrentHike? {
    return if (fileExists(context, fileName)) {
        context.openFileInput(fileName)?.bufferedReader()?.useLines {
            try {
                Json.decodeFromString<CurrentHike>(it.joinToString("\n"))
            }
            catch (e: Exception) {
                null
            }
        }
    }
    else {
        null
    }
}

fun fileExists(context: Context, fileName: String) : Boolean {
    val file = context.getFileStreamPath(fileName)
    return file != null && file.exists()
}