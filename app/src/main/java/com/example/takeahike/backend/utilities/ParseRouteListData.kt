package com.example.takeahike.backend.utilities

import android.os.Parcel
import com.example.takeahike.backend.data.RouteSaveData
import java.io.FileOutputStream

class ParseRouteListData {
    fun parseData(data: ByteArray) : RouteSaveData? {
        val saveData = Parcel.obtain()
        saveData.unmarshall(data, 0, data.size)
        saveData.setDataPosition(0)
        val loaded = saveData.readParcelable<RouteSaveData>(RouteSaveData::class.java.classLoader)
        saveData.recycle()
        return loaded
    }

    fun writeData(
        fos: FileOutputStream?,
        data: RouteSaveData
    ) {
        val parcel = Parcel.obtain()
        parcel.writeParcelable(data, 0)
        val byteArray = parcel.marshall()
        if (byteArray != null) {
            fos?.write(byteArray)
        }
        else {
            // TODO: Error handling
        }
        parcel.recycle()
    }
}