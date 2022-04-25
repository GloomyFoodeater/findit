package com.example.findit

import org.json.JSONArray
import org.json.JSONStringer
import org.json.JSONTokener

data class Cell(
    val isRoad: Boolean, // Can be passed through irl
    val isEnd: Boolean, // Can be used as source/destination point on map
    val iconName: String,

    // Offsets of left top angle cell of large decoration
    val offX: Int,
    val offY: Int
)

class Cells(json: String) {
    val cells: Array<Array<Cell>>

    init {
        // Parse outer json array
        val jsonMat = JSONTokener(json).nextValue() as JSONArray
        cells = Array(jsonMat.length()) { i ->

            // Parse inner json array
            val jsonRow = jsonMat.getJSONArray(i)
            Array(jsonRow.length()) { j ->

                // Parse json object
                val jsonObj = jsonRow.getJSONObject(j)
                val isRoad = jsonObj.getBoolean("isRoad")
                val isEnd = jsonObj.getBoolean("isEnd")
                val iconName = jsonObj.getString("iconName")
                val offX = jsonObj.getInt("offX")
                val offY = jsonObj.getInt("offY")

                // New object of Cell class
                Cell(isRoad, isEnd, iconName, offX, offY)
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
                jsonStringer.key("isRoad")
                jsonStringer.value(cell.isRoad)
                jsonStringer.key("isEnd")
                jsonStringer.value(cell.isEnd)
                jsonStringer.key("iconName")
                jsonStringer.value(cell.iconName)
                jsonStringer.key("offX")
                jsonStringer.value(cell.offX)
                jsonStringer.key("offY")
                jsonStringer.value(cell.offY)

                jsonStringer.endObject()
            }
            jsonStringer.endArray()
        }
        jsonStringer.endArray()
        return jsonStringer.toString()
    }

}