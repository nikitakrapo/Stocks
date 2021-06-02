package com.nikitakrapo.android.stocks

import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import org.junit.Test

class GsonTest {

    @Test
    fun stringList(){
        var list = listOf("a", "b", "cd", "ef")
        var jsonList = Gson().toJson(list)
        var fromJsonList = Gson().fromJson(jsonList, List::class.javaObjectType)
        assertEquals(fromJsonList, list)
    }
}