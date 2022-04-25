package com.example.findit

import org.json.JSONArray
import org.json.JSONStringer
import org.json.JSONTokener

data class Cell(val isTraversable: Boolean, val isEndpoint: Boolean, val iconFileName: String)

class Cells(json: String) {
    private val cells: Array<Array<Cell>>

    init {
        // Parse outer json array
        val jsonMat = JSONTokener(json).nextValue() as JSONArray
        cells = Array(jsonMat.length()) { i ->

            // Parse inner json array
            val jsonRow = jsonMat.getJSONArray(i)
            Array(jsonRow.length()) { j ->

                // Parse json object
                val jsonObj = jsonRow.getJSONObject(j)
                val isTraversable: Boolean = jsonObj.getBoolean("isTraversable")
                val isEndpoint: Boolean = jsonObj.getBoolean("isEndpoint")
                val iconFileName: String = jsonObj.getString("iconFileName")

                // New object of Cell class
                Cell(isTraversable, isEndpoint, iconFileName)
            }
        }
    }

    fun getJson(): String {
        val jsonStringer = JSONStringer()

        // Write matrix
        jsonStringer.array()
        cells.forEach { row ->

            // Write row
            jsonStringer.array()
            row.forEach { cell ->
                jsonStringer.`object`()

                // Write object
                jsonStringer.key("isTraversable")
                jsonStringer.value(cell.isTraversable)
                jsonStringer.key("isEndpoint")
                jsonStringer.value(cell.isEndpoint)
                jsonStringer.key("iconFileName")
                jsonStringer.value(cell.iconFileName)

                jsonStringer.endObject()
            }
            jsonStringer.endArray()
        }
        jsonStringer.endArray()
        return jsonStringer.toString()
    }
}