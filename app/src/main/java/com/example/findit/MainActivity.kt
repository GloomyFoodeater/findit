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
        // Resource images
        val chairBit = BitmapFactory.decodeResource(resources, R.drawable.chair)
        val tableBit = BitmapFactory.decodeResource(resources, R.drawable.table)
        val armchairLeftBit = BitmapFactory.decodeResource(resources, R.drawable.armchair_left)
        val armchairRightBit = BitmapFactory.decodeResource(resources, R.drawable.armchair_right)
        val tableLargeBit = BitmapFactory.decodeResource(resources, R.drawable.table_large)
        val deskRowBit = BitmapFactory.decodeResource(resources, R.drawable.desk_row)

        return mapOf(
            "chair" to chairBit,
            "table" to tableBit,
            "armchair_left" to armchairLeftBit,
            "armchair_right" to armchairRightBit,
            "table_large" to tableLargeBit,
            "desk_row" to deskRowBit
        )
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
        val maze = Maze(json, 800F, 2000F, icons)
        val path = maze.findPath(Pair(3, 4), Pair(7, 6))
        Log.i("PATH", path.joinToString(", "))
        val imgView = findViewById<ImageView>(R.id.imageView)
//        val img = maze.drawMaze()
        val img = maze.drawPath(path)
        imgView.setImageBitmap(img)
    }
}