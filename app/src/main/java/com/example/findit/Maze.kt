package com.example.findit

import org.json.JSONArray
import org.json.JSONStringer
import org.json.JSONTokener

class Maze(json: String) {
    val cells: Array<Array<Cell>>
    val h: Int
    val w: Int

    // Deserialize maze from json
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
        h = cells.size
        w = cells[0].size
    }

    // Serialize maze to json
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

    // Find shortest path via bfs
    fun findPath(start: Pair<Int, Int>, end: Pair<Int, Int>): List<Pair<Int, Int>> {

        // Init data
        val path = arrayListOf<Pair<Int, Int>>()
        val isVisited = Array(h) { BooleanArray(w) { false } }
        val parent = Array(h) { Array(w) { Pair(-1, -1) } }

        // Find path
        var isFound = false
        val queue = ArrayDeque<Pair<Int, Int>>()
        queue.add(start)
        while (!queue.isEmpty()) {

            // Visit tile
            val (x, y) = queue.removeFirst()
            isFound = Pair(x, y) == end
            if (isFound) {
                queue.clear()
                break
            }
            // Enqueue neighbours
            val neighbours = arrayOf(
                Pair(x + 1, y),
                Pair(x, y + 1),
                Pair(x - 1, y),
                Pair(x, y - 1)
            )
            neighbours.forEach {
                // Not out of range & is traversable & is not visited
                val canVisit = it.first in 0 until w &&
                        it.second in 0 until h &&
                        (cells[it.second][it.first].isRoad && !isVisited[it.second][it.first]
                                || it == end)
                if (canVisit) {
                    // Enqueueing
                    isVisited[it.second][it.first] = true
                    parent[it.second][it.first] = Pair(x, y)
                    queue.add(it)
                }
            }
        }

        // Restore path
        if (isFound) {
            var current = end
            while (current != start) {
                path.add(current)
                current = parent[current.second][current.first]
            }
            path.add(start)
            path.reverse()
        }

        return path
    }

}
