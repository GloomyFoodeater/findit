package com.example.findit

import android.graphics.*
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.findit.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Draw maze on new bitmap
    private fun drawCells(maze: Cells, w: Int, h: Int): Bitmap {

        // Drawing data
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawRGB(255,255,255)

        // Resource images
        val chairBitmap = BitmapFactory.decodeResource(resources, R.drawable.chair)
        val tableBitmap = BitmapFactory.decodeResource(resources, R.drawable.table)

        // Coordinate data
        var y = 0F
        val xStep = w.toFloat() / maze.cells[0].size
        val yStep = h.toFloat() / maze.cells.size

        // Iterate over cells
        maze.cells.forEach { row ->
            var x = 0F
            row.forEach { cell ->
                val rect = RectF(x, y, x + xStep, y + yStep)
                when (cell.iconName) {
                    "chair" -> {
                        canvas.drawBitmap(chairBitmap, null, rect, null)
                    }
                    "table" -> {
                        rect.right += xStep
                        canvas.drawBitmap(tableBitmap, null, rect, null)
                    }
                }
                x += xStep
            }
            y += yStep
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
        val cells = Cells(json)
        val imgView = findViewById<ImageView>(R.id.imageView)
        imgView.setImageBitmap(drawCells(cells, 2000, 4000))
    }
}