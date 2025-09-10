package com.example.thebestpartmenu

import androidx.compose.runtime.MutableState

data class MenuItem (
        var food_name: String,
        var food_description: String,
        var food_price: Double,
        var food_quantity: MutableState<Int>
)