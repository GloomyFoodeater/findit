package com.example.findit

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class Maze(private val bitmap: Bitmap) {
    private val mat: IntArray
    private val w: Int = bitmap.width
    private val h: Int = bitmap.height

    // Init maze matrix
    init {
        mat = IntArray(w * h)
        bitmap.getPixels(mat, 0, w, 0, 0, w, h)
    }

    // Check if (x, y) tile is traversable
    private fun isTraversable(x: Int, y: Int, thresh: Int = 100): Boolean {
        val color = mat[y * w + x] and 0x00FFFFFF
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        val grayscale: Int = (0.299 * red + 0.587 * green + 0.114 * blue).toInt()
        return grayscale > thresh
    }

    // Use bfs to find shortest path between start and end points
    fun findPath(start: Pair<Int, Int>, end: Pair<Int, Int>): List<Pair<Int, Int>> {

        // Init data
        val path = arrayListOf<Pair<Int, Int>>()
        val isVisited = Array(h) { BooleanArray(w) { false } }
        val parent = Array(h) {
            Array(w) { Pair(-1, -1) }
        }

        // Find path
        var isFound = false
        val queue = ArrayDeque<Pair<Int, Int>>()
        queue.add(start)
        while (!queue.isEmpty()) {

            // Visit tile
            val (x, y) = queue.removeFirst()
            isFound = Pair(x, y) == end
            if (isFound)
                break

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
                        isTraversable(it.first, it.second) &&
                        !isVisited[it.second][it.first]
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

    // Draw path on copy of passed bitmap
    fun drawPath(path: List<Pair<Int, Int>>): Bitmap {
        val bitmapCopy = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(bitmapCopy)
        val paint = Paint().apply {
            color = Color.GREEN
            strokeWidth = 10F
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.SQUARE
            strokeMiter = 20F
        }
        for (i in 1 until path.size) {
            canvas.drawLine(
                path[i - 1].first.toFloat(), path[i - 1].second.toFloat(),
                path[i].first.toFloat(), path[i - 1].second.toFloat(),
                paint
            )
        }
        return bitmapCopy
    }
}