package com.example.thebestpartmenu

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thebestpartmenu.ui.theme.TheBestPartMenuTheme
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.draw.clip

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

//This function creates the logo section
@Composable
fun LogoSection(modifier :Modifier = Modifier) {
    val icon = R.drawable.the_best_part_icon
    Box(modifier
        .border(21.dp, Color.White, RectangleShape)){
        Row(
            modifier = Modifier
                .background(Color.LightGray),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = R.drawable.the_best_part_icon.toString(),
                modifier
                    .width(150.dp)
                    .height(150.dp)
            )
            Text(
                text = stringResource(R.string.app_description),
                modifier
                    .padding(top = 60.dp)
                    .padding(end = 30.dp)
                    .height(150.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }
    }

}

//this sections creates the mutableList of menu items with the string array resources
//and calls the method that displays the items
@Composable
fun createFoodSection(modifier :Modifier = Modifier) : MutableList<MenuItem>{
    val foodNames= stringArrayResource(id = R.array.food_names)
    val foodDescriptions= stringArrayResource(id = R.array.food_descriptions)
    val foodPrices= stringArrayResource(id = R.array.food_prices)
    val initialMenuItems = rememberSaveable {
        MutableList(foodNames.size) { i ->
            MenuItem(foodNames[i], foodDescriptions[i], foodPrices[i].toDouble(), mutableIntStateOf(0))
        }
    }
    FoodItemsSection(modifier, initialMenuItems)
    return initialMenuItems
}

//this creates the total section with the clear button that clears the quantity of foods
//with the place order button that only generates the qr code with the receipt
//if the button is pressed
@Composable
fun CreateTotalSection(modifier :Modifier, initialMenuItems : MutableList<MenuItem>){
    var foodPricesTotal = 0.0
    var orderPlaced by rememberSaveable { mutableStateOf(false) }
    for (food in initialMenuItems){
        foodPricesTotal +=  food.food_quantity.value * food.food_price
    }
    DisplayTotal(foodPricesTotal)
    val jsonData = getMenuItems(initialMenuItems)
    Column(modifier = modifier
        .padding(16.dp)
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally){
        Button(onClick = {orderPlaced = true} ){
            Text("Place Order")
        }
        Button(onClick = { clear(initialMenuItems) }) {
            Text("Clear")
        }
    }
    if (orderPlaced == true){
        QRCodeDisplay(modifier, jsonData)
    }
}

//this is the method that displays the food items nicely
@Composable
fun FoodItemsSection(modifier: Modifier, initialMenuItems : MutableList<MenuItem>){
    Column (modifier = modifier
        .padding(16.dp)){
        for(food in initialMenuItems){
            Column(modifier
                .border(5.dp, Color.White, RectangleShape)
                .background(Color.LightGray)
                .padding(16.dp)
            ){
                Text(
                    text = "${food.food_name}. ${food.food_description}",
                    modifier.padding(10.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Price: ${roundToTwoDecimals(food.food_price)}$",
                    modifier.padding(10.dp),
                    textAlign = TextAlign.Center,
                )
                AddQuantitySection(modifier, food)
            }
        }
    }
}

//this method creates the buttons to change the quantity of foods
@Composable
fun AddQuantitySection(modifier: Modifier, food : MenuItem){
    Row (modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center){
        Button(onClick =  {
            if(food.food_quantity.value>0){
                food.food_quantity.value -= 1
            }
        }){
            Text("-")
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "${food.food_quantity.value}",
            modifier.padding(top = 13.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Button(onClick =  {
            food.food_quantity.value += 1
        }){
            Text("+")
        }
    }
}

//Method that rounds any double to have only two decimals
fun roundToTwoDecimals(number: Double): String{
    return String.format( "%.2f", number)
}


//method that clears the quantity of food that a user has added
fun clear(initialMenuItems : MutableList<MenuItem> ){
    for (food in initialMenuItems){
        food.food_quantity.value = 0
    }
}


//this method displays the generated qr code
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

//this method makes the food list into a json if they order a quantity higher than 0
//if they don't order anything it displays that their cart is empty
fun getMenuItems(initialMenuItems : MutableList<MenuItem>): String {
    var quantityIsZero = 0
    var jsonMenuItems = """"""
    for(food in initialMenuItems){
        if(food.food_quantity.value > 0){
            jsonMenuItems += """
                Food: ${food.food_name}, Description: ${food.food_description}, Price: ${food.food_price}, Quantity: ${food.food_quantity.value} 
                    """.trimIndent()
        }else{
            quantityIsZero++
        }
    }
    if(quantityIsZero == initialMenuItems.size){
        jsonMenuItems = "Your cart is empty"
    }
    return jsonMenuItems
}

//this method creates the QR code
fun generateQRCode(inputStr: String, codeWidth: Int, codeHeight: Int): Bitmap? {
    try{
        val barcodeEncoder = BarcodeEncoder()
        return barcodeEncoder.encodeBitmap(inputStr, BarcodeFormat.QR_CODE, codeWidth, codeHeight)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }

}

//this method calculates and displays the total with the taxes
@Composable
fun DisplayTotal(foodPricesTotal : Double){
    val gst = 0.05 * foodPricesTotal
    val qst = 0.09975 * foodPricesTotal
    var netTotal = gst + qst + foodPricesTotal
    Column (modifier = Modifier
        .padding(16.dp)
    ){
        Text(
            text = "Total: $${roundToTwoDecimals(foodPricesTotal)}"
        )
        Text(
            text = "GST (5%): $${roundToTwoDecimals(gst)}"
        )
        Text(
            text = "QST (9.975%): $${roundToTwoDecimals(qst)}"
        )
        Text(
            text = "Total (tax included): $${roundToTwoDecimals(netTotal)}"
        )
    }
}

//this is the function that calls all of the composable functions at the right place
@Composable
fun MenuApp(){
    Column (modifier = Modifier
        .verticalScroll(rememberScrollState())
        .statusBarsPadding()){
        LogoSection()
        val initialMenuItems = createFoodSection(
            modifier = Modifier
        )
        CreateTotalSection(modifier = Modifier, initialMenuItems)
    }
}

@Preview(showBackground = true)
@Composable
fun MenuPreview() {
    TheBestPartMenuTheme {
        MenuApp()
    }
}