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

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Load icons from resources into dictionary
    private fun loadIcons(): Map<String, Bitmap> {

        // Map name of icon -> resource id
        val idMap = mapOf(
            "chair" to R.drawable.chair,
            "table" to R.drawable.table,
            "armchair_top" to R.drawable.armchair_top,
            "armchair_left" to R.drawable.armchair_left,
            "armchair_right" to R.drawable.armchair_right,
            "table_large" to R.drawable.table_large,
            "table_large_2" to R.drawable.table_large_2,
            "table_large_2_pc_left" to R.drawable.table_large_2_pc_left,
            "table_large_2_pc_right" to R.drawable.table_large_2_pc_right,
            "desk_row" to R.drawable.desk_row,
            "stall" to R.drawable.stall,
            "department" to R.drawable.department
        )

        // Map name of icon -> bitmap
        val imgMap = mutableMapOf<String, Bitmap>()
        idMap.forEach { imgMap[it.key] = BitmapFactory.decodeResource(resources, it.value) }
        return imgMap
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

        val icons = loadIcons()
        val json = assets.open("maze.json").bufferedReader().use {
            it.readText()
        }
        val maze = Maze(json, 800F, 1600F, icons)
        val path = maze.findPath(Pair(3, 4), Pair(7, 6))
        Log.i("PATH", path.joinToString(", "))
        val imgView = findViewById<ImageView>(R.id.imageView)
        val img = maze.drawMaze()
//        val img = maze.drawPath(path)
        imgView.setImageBitmap(img)
    }
}