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
fun CreateInitialMenuItems(modifier :Modifier = Modifier){
    var quantity by remember { mutableStateOf(0)}
    val food_names= stringArrayResource(id = R.array.food_names)
    val food_descriptions= stringArrayResource(id = R.array.food_descriptions)
    val food_prices= stringArrayResource(id = R.array.food_prices)
    val food_quantities = integerArrayResource(id = R.array.food_quantitites)
    //InitialMenuItem must be mutablestateof with lambda
    var InitialMenuItems = remember { mutableListOf(List(food_names.size)
        {index ->
            MenuItem(
                food_names[index],
                food_descriptions[index],
                food_prices[index],
                food_quantities[index]
            )
    })}
    for (i in 0..InitialMenuItems.size){
        FoodItems(modifier, InitialMenuItems, i)
    }
}
@Composable
fun FoodItems(modifier: Modifier, initialMenuItems : MutableList<List<MenuItem>>, index: Int){
    Column (modifier = modifier
        .padding(16.dp)){
        var i by remember { mutableStateOf({})}
        for(food in initialMenuItems){
//            clear(food)
            Text(
                text = "${food[index].Food_name}. ${food[index].Food_description}",
                modifier.padding(10.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Price: ${food[index].Food_price}$",
                modifier.padding(10.dp),
                textAlign = TextAlign.Center
            )
            Row (modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center){
                Button(onClick =  { food[index].Food_quantity += 1}){
                    Text("+")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "${food[index].Food_quantity}",
                    modifier.padding(top = 13.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick =  {food[index].Food_quantity-= 1}){
                    Text("-");
                }
            }
        }
    }
}

@Composable
fun TotalPrices(
//    quantity : Int, food: MenuItem
){
    var brut_total by remember { mutableStateOf(0)}
    var gst = 0.05
    var qst = 0.09975
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
    var Food_name: String,
    var Food_description: String,
    var Food_price: String,
    var Food_quantity: Int)

//for barcode
//generateEncoder()
//encodeBitMap(inputStr, BarcodeFormat.QR_CODE)

@Composable
fun MenuApp(){
    Column (modifier = Modifier
        .verticalScroll(rememberScrollState())){
        Logo()
        CreateInitialMenuItems(
            modifier = Modifier
        )
        TotalPrices()
    }
}

@Preview(showBackground = true)
@Composable
fun MenuPreview() {
    TheBestPartMenuTheme {
        MenuApp()
    }
}