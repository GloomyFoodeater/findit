package com.example.findit

import org.json.JSONArray
import org.json.JSONStringer
import org.json.JSONTokener

data class Cell(
    val isTraversable: Boolean, // Can be passed through irl
    val isEndpoint: Boolean, // Can be used as source/destination point on map
    val iconFileName: String,

    // Offsets of left top angle cell of large decoration
    val offsetX: Int,
    val offsetY: Int
)

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
                val isTraversable = jsonObj.getBoolean("isTraversable")
                val isEndpoint = jsonObj.getBoolean("isEndpoint")
                val iconFileName = jsonObj.getString("iconFileName")
                val offsetX = jsonObj.getInt("offsetX")
                val offsetY = jsonObj.getInt("offsetY")

                // New object of Cell class
                Cell(isTraversable, isEndpoint, iconFileName, offsetX, offsetY)
            }
        }
    }

    // Convert 2D matrix of cells into json string
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
                jsonStringer.key("offsetX")
                jsonStringer.value(cell.offsetX)
                jsonStringer.key("offsetY")
                jsonStringer.value(cell.offsetY)

                jsonStringer.endObject()
            }
            jsonStringer.endArray()
        }
        jsonStringer.endArray()
        return jsonStringer.toString()
    }
}