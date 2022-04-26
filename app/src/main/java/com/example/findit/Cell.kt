package com.example.findit

data class Cell(
    val isRoad: Boolean, // Can be passed through irl
    val isEnd: Boolean, // Can be used as source/destination point on map
    val iconName: String,

    // Offsets of left top angle cell of large decoration
    val offX: Int,
    val offY: Int
)
