package com.example.thebestpartmenu

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thebestpartmenu.ui.theme.TheBestPartMenuTheme
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder


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
    var foodPrice = 0.0
    val foodNames= stringArrayResource(id = R.array.food_names)
    val foodDescriptions= stringArrayResource(id = R.array.food_descriptions)
    val foodPrices= stringArrayResource(id = R.array.food_prices)
    var orderPlaced by remember { mutableStateOf(false) }
    val initialMenuItems = remember {
        MutableList(foodNames.size) { i ->
            MenuItem(foodNames[i], foodDescriptions[i], foodPrices[i].toDouble(), mutableStateOf(0))
        }
    }
    FoodItems(modifier, initialMenuItems)
    for (food in initialMenuItems){
        foodPrice +=  food.food_quantity.value * food.food_price
//        Clear(food)
    }
    DisplayTotal(foodPrice)
    val jsonData = getMenuItems(initialMenuItems)
    Button(onClick = {orderPlaced = true} ){
        Text("Place Order")
    }

    Button(onClick = { Clear(initialMenuItems) }) {
        Text("Clear")
    }
    if (orderPlaced == true){
        QRCodeDisplay(modifier, jsonData)
    }
}

@Composable
fun FoodItems(modifier: Modifier, initialMenuItems : MutableList<MenuItem>){
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
                text = "Price: ${roundToTwoDecimals(food.food_price)}$",
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
                Button(onClick =  {
                    if(food.food_quantity.value>0){
                        food.food_quantity.value -= 1
                    }
                }){
                    Text("-")
                }
            }
        }

    }
}

fun roundToTwoDecimals(number: Double): String{
    return String.format("%.2f", number)
}

fun Clear(initialMenuItems : MutableList<MenuItem> ){
    for (food in initialMenuItems){
        food.food_quantity.value = 0
    }
}

@Composable
fun QRCodeDisplay(modifier: Modifier = Modifier, jsonData : String) {
    val bitmap = generateQRCode(jsonData, 512, 512)

    // Display the QR code
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        Image(bitmap!!.asImageBitmap(), contentDescription = null)
    }
}
@Composable
fun getMenuItems(initialMenuItems : MutableList<MenuItem>): String {
    var quanityIsZero = 0;
    var jsonMenuItems = """"""
    for(food in initialMenuItems){
        if(food.food_quantity.value > 0){
            jsonMenuItems += """
                ${food}
                    """.trimIndent()
        }else{
            quanityIsZero++
        }
    }
    if(quanityIsZero == initialMenuItems.size){
        jsonMenuItems = "Your cart is empty"
    }
    return jsonMenuItems
}

@Composable
fun generateQRCode(inputStr: String, codeWidth: Int, codeHeight: Int): Bitmap? {
    try{
        val barcodeEncoder = BarcodeEncoder()
        return barcodeEncoder.encodeBitmap(inputStr, BarcodeFormat.QR_CODE, codeWidth, codeHeight)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }

}

//for barcode
//generateEncoder()
//encodeBitMap(inputStr, BarcodeFormat.QR_CODE)

@Composable
fun DisplayTotal(foodPrice : Double){
    val gst = 0.05 * foodPrice
    val qst = 0.09975 * foodPrice
    var net_total = gst + qst + foodPrice
    net_total = net_total
    Column (modifier = Modifier
        .padding(16.dp)
    ){
        Text(
            text = "Total: $${roundToTwoDecimals(foodPrice)}"
        )
        Text(
            text = "GST (5%): $${roundToTwoDecimals(gst)}"
        )
        Text(
            text = "QST (9.975%): $${roundToTwoDecimals(qst)}"
        )
        Text(
            text = "Total (tax included): $${roundToTwoDecimals(net_total)}"
        )
    }
}


data class MenuItem(
    var food_name: String,
    var food_description: String,
    var food_price: Double,
    var food_quantity: MutableState<Int>)

@Composable
fun MenuApp(){
    Column (modifier = Modifier
        .verticalScroll(rememberScrollState())){
        Logo()
        CreateInitialMenuItems(
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