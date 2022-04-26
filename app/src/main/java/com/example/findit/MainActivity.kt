package com.example.findit

import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.findit.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.math.min

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Draw maze on new bitmap
    private fun drawMaze(maze: Maze, w: Float, h: Float, drawGrid: Boolean = false): Bitmap {

        // Drawing data
        val bitmap = Bitmap.createBitmap(w.toInt(), h.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawRGB(255, 255, 255)

        // Resource images
        val chairBit = BitmapFactory.decodeResource(resources, R.drawable.chair)
        val tableBit = BitmapFactory.decodeResource(resources, R.drawable.table)
        val armchairLeftBit = BitmapFactory.decodeResource(resources, R.drawable.armchair_left)
        val armchairRightBit = BitmapFactory.decodeResource(resources, R.drawable.armchair_right)
        val tableLargeBit = BitmapFactory.decodeResource(resources, R.drawable.table_large)
        val deskRowBit = BitmapFactory.decodeResource(resources, R.drawable.desk_row)

        // Coordinate data
        val cellW = w / maze.w
        val cellH = h / maze.h

        // Iterate over rows
        var y = 0F
        maze.cells.forEach { row ->

            // Iterate over cells
            var x = 0F
            row.forEach { cell ->
                // Draw grid
                if (drawGrid) {
                    val bound = RectF(x, y, x + cellW, y + cellH)
                    val paint = Paint()
                    with(paint) {
                        color = Color.BLACK
                        strokeWidth = 2F
                        style = Paint.Style.STROKE
                    }
                    canvas.drawRect(bound, paint)
                }

                // Draw icons
                val rect = RectF(x, y, x + cellW * cell.cols, y + cellH * cell.rows)
                when (cell.iconName) {
                    "chair" -> canvas.drawBitmap(chairBit, null, rect, null)
                    "table" -> canvas.drawBitmap(tableBit, null, rect, null)
                    "armchair_left" -> canvas.drawBitmap(armchairLeftBit, null, rect, null)
                    "armchair_right" -> canvas.drawBitmap(armchairRightBit, null, rect, null)
                    "table_large" -> canvas.drawBitmap(tableLargeBit, null, rect, null)
                    "desk_row" -> canvas.drawBitmap(deskRowBit, null, rect, null)
                }
                x += cellW
            }
            y += cellH
        }
        return bitmap
    }

    // Draw maze and path on new bitmap
    private fun drawPath(
        maze: Maze,
        path: List<Pair<Int, Int>>,
        w: Float,
        h: Float,
        drawGrid: Boolean = false
    ): Bitmap {
        val bitmap = drawMaze(maze, w, h, drawGrid)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        with(paint) {
            color = Color.GREEN
            strokeWidth = 4F
        }
        val cellW = w / maze.w
        val cellH = h / maze.h
        for (i in 0 until path.size - 1) {
            val x1 = cellW * (path[i].first + .5F)
            val y1 = cellH * (path[i].second + .5F)
            val x2 = cellW * (path[i + 1].first + .5F)
            val y2 = cellH * (path[i + 1].second + .5F)
            canvas.drawLine(x1, y1, x2, y2, paint)
        }
        return bitmap
    }

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

        val json = assets.open("maze.json").bufferedReader().use {
            it.readText()
        }
        val maze = Maze(json)
        val path = maze.findPath(Pair(3, 4), Pair(7, 6))
        Log.i("PATH", path.joinToString(", "))
        val imgView = findViewById<ImageView>(R.id.imageView)
        val img = drawMaze(maze, 800F, 2000F)
        imgView.setImageBitmap(img)
//        imgView.setImageBitmap(drawPath(maze, path, 800F, 2000F, true))
    }
}