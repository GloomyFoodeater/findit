package com.example.findit

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.findit.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class Maze(bitmap: Bitmap) {
    private val maze: IntArray
    private val w: Int = bitmap.width
    private val h: Int = bitmap.height

    init {
        maze = IntArray(w * h)
        bitmap.getPixels(maze, 0, 0, 0, 0, w, h)
    }

    private fun isBlocked(x: Int, y: Int, thresh: Int = 100): Boolean {
        val color = maze[y * w + x]
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        val grayscale: Int = (0.299 * red + 0.587 * green + 0.114 * blue).toInt()
        return grayscale < thresh
    }

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

            //Visit
            val (x, y) = queue.removeFirst()
            isFound = Pair(x, y) == end
            if (isFound)
                break
            val neighbours = arrayOf(
                Pair(x + 1, y),
                Pair(x, y + 1),
                Pair(x - 1, y),
                Pair(x, y - 1)
            )
            // Check neighbours
            neighbours.forEach {
                val canVisit =
                    it.first in 0..w && it.second in 0..h && isBlocked(
                        it.first,
                        it.second
                    ) && !isVisited[it.second][it.first]
                if (canVisit) {
                    isVisited[it.second][it.first] = true
                    parent[it.second][it.first] = it
                    queue.add(it)
                }
            }
        }

        // Restore path
        if (isFound) {
            var current = end
            while (current != end) {
                path.add(current)
                current = parent[current.second][current.first]
            }
            path.add(start)
            path.reverse()
        }

        return path
    }

}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Load maze from resource
//        val img = BitmapFactory.decodeResource(resources, R.drawable.map_back)
//        val maze = Maze(img)
    }
}