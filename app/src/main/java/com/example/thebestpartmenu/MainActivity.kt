package com.example.thebestpartmenu

import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thebestpartmenu.ui.theme.TheBestPartMenuTheme
import kotlinx.coroutines.internal.OpDescriptor
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheBestPartMenuTheme {
                MenuApp()
            }
        }
    }
}

@Composable
fun Logo(modifier :Modifier = Modifier) {
    val icon = R.drawable.the_best_part_icon
        Row(modifier = Modifier
            .padding(16.dp)
            .border(width = 3.dp, color = Color.LightGray),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center){
            //make it into icon maybe?
            Image(
                painter = painterResource(icon),
                //provide a message if logo cannot be loaded
                contentDescription =  R.drawable.the_best_part_icon.toString(),
                modifier
                    .padding(10.dp)
                    .width(150.dp)
                    .height(150.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Explore The Best Part coffee shop menu to always get the part of your favorite pastries!",
                modifier
                    .padding(10.dp)
                    .width(100.dp)
                    .height(150.dp),
                fontSize = 15.sp
            )
        }

}

@Composable
fun FoodItems(
//    quantity: Int,
    modifier :Modifier = Modifier){
    val food_names= stringArrayResource(id = R.array.food_names)
    val food_descriptions= stringArrayResource(id = R.array.food_descriptions)
    val food_prices= stringArrayResource(id = R.array.food_prices)
    //InitialMenuItem must be mutablestateof with lambda
    val InitialMenuItem = List(food_names.size){index ->
        MenuItem(
            food_names[index],
            food_descriptions[index],
            food_prices[index]
        )
    }
    Column (modifier = modifier
        .padding(50.dp)){
        for(food in InitialMenuItem){

            Text(
                text = "${food.Food_name}, ${food.Food_description}, ${food.Food_price}",
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
    }

}
data class MenuItem(
    var Food_name: String,
    var Food_description: String,
    var Food_price: String){
    fun menuItem(foodName: String, foodDescription : String, foodPrice: String){
        this.Food_name = foodName
        this.Food_description = foodDescription
        this.Food_price = foodPrice
    }
}
//for barcode
//generateEncoder()
//encodeBitMap(inputStr, BarcodeFormat.QR_CODE)

@Composable
fun MenuApp(){
    Column {
        Logo()
        FoodItems(
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MenuPreview() {
    TheBestPartMenuTheme {
        MenuApp()
    }
}