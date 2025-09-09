package com.example.thebestpartmenu

import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.Menu
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.style.TextAlign
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
    Row(modifier = Modifier,
//        .border(width = 5.dp, Color = Color.DarkGray,  Shape = RectangleShape),
        verticalAlignment = Alignment.CenterVertically){
        //make it into icon maybe?
        Image(
            painter = painterResource(icon),
            //provide a message if logo cannot be loaded
            contentDescription =  R.drawable.the_best_part_icon.toString(),
            modifier
                .width(150.dp)
                .height(150.dp)
        )
        Text(
            text = "Explore The Best Part coffee shop menu to always get the part of your favorite pastries!",
            modifier
                .padding(top = 55.dp)
                .height(150.dp),
            textAlign = TextAlign.Center
        )
    }

}

@Composable
fun CreateInitialMenuItems(modifier :Modifier = Modifier) : MutableList<MenuItem>{
    var quantity by remember { mutableStateOf(0)}
    val food_names= stringArrayResource(id = R.array.food_names)
    val food_descriptions= stringArrayResource(id = R.array.food_descriptions)
    val food_prices= stringArrayResource(id = R.array.food_prices)
    //InitialMenuItem must be mutablestateof with lambda
    for (i in 0..food_names.size-1){
        val InitialMenuItems = remember { mutableListOf(
            MenuItem(
                food_names[i],
                food_descriptions[i],
                food_prices[i],
                mutableStateOf(0)
            )
        )}
        FoodItems(modifier, InitialMenuItems, i)
        if(i == food_names.size-1){
            return InitialMenuItems
        }
    }
    throw error("The mutable list of MenuItems was not created")
}

@Composable
fun FoodItems(modifier: Modifier, initialMenuItems : MutableList<MenuItem>, index: Int){
    Column (modifier = modifier
        .padding(16.dp)){
        for(food in initialMenuItems){

//            clear(food)
            Text(
                text = "${food.food_name}. ${food.food_description}",
                modifier.padding(10.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Price: ${food.food_price}$",
                modifier.padding(10.dp),
                textAlign = TextAlign.Center
            )
            Row (modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center){
                Button(onClick =  {
                    food.food_quantity.value += 1
                }){
                    Text("+")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "${food.food_quantity.value}",
                    modifier.padding(top = 13.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick =  {food.food_quantity.value -= 1}){
                    Text("-");
                }
            }
        }
    }
}

@Composable fun CalculateTotal( initialMenuItems : MutableList<MenuItem>) {
    var brut_total : MutableState<() -> Double>
    brut_total = remember{ mutableStateOf({
        for(food in initialMenuItems){
            food.food_price.toDouble() * food.food_quantity.value }
    })}
    DisplayTotal(brut_total.value)
}

@Composable
fun DisplayTotal(brut_total : () -> Unit){
    val gst = 0.05
    val qst = 0.09975
    var net_total by remember { mutableStateOf(0)}
    Column (modifier = Modifier
        .padding(16.dp)
    ){
        Text(
            text = "Total: $$brut_total"
        )
        Text(
            text = "GST (5%): $$gst"
        )
        Text(
            text = "QST (9.975%): $$qst"
        )
        Text(
            text = "Total (tax included): $$net_total"
        )
    }
}

//fun Clear(){
//    Button(onClick = {}) {
//        "Clear"
//    }
//}

data class MenuItem(
    var food_name: String,
    var food_description: String,
    var food_price: String,
    var food_quantity: MutableState<Int>)

//for barcode
//generateEncoder()
//encodeBitMap(inputStr, BarcodeFormat.QR_CODE)

@Composable
fun MenuApp(){
    Column (modifier = Modifier
        .verticalScroll(rememberScrollState())){
        Logo()
        var initialMenuItems = CreateInitialMenuItems(
            modifier = Modifier
        )
        CalculateTotal(initialMenuItems)
    }
}

@Preview(showBackground = true)
@Composable
fun MenuPreview() {
    TheBestPartMenuTheme {
        MenuApp()
    }
}