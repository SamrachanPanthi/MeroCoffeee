package com.example.merocofeee.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.FlowRow
import androidx.core.view.WindowCompat.enableEdgeToEdge
import com.example.merocofeee.R

class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LatteScreen()
        }
    }
}

@Composable
fun LatteScreen(
    onCancel: () -> Unit = {},
    onAdd: (Double) -> Unit = {}
) {
    var selectedSize by remember { mutableStateOf("Medium") }
    val selectedCustomizations = remember { mutableStateListOf<String>() }

    val basePrice = 350.0
    val customizationPrice = selectedCustomizations.size * 0.50
    val totalPrice = basePrice + customizationPrice

    val customOptions = listOf(
        "Extra Shot", "Oat Milk", "Almond Milk",
        "Vanilla", "Caramel", "Whipped Cream"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        // Image
        Image(
            painter = painterResource(R.drawable.lattee),
            contentDescription = "Latte",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Title + Price
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Latte", fontSize = 24.sp, fontWeight = FontWeight.Bold, color=Color(0xFF8B4513))
            Text(
                "Rs${String.format("%.2f", basePrice)}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B4513)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            "Smooth espresso with steamed milk and foam",
            fontSize = 16.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Size Selector
        Text("Size", fontSize = 18.sp, fontWeight = FontWeight.Medium)

        Row(modifier = Modifier.padding(top = 8.dp)) {
            listOf("Small", "Medium", "Large").forEach { size ->
                SizeButton(
                    label = size,
                    selected = (selectedSize == size),
                    onClick = { selectedSize = size }
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Customization Chips
        Text("Customizations (+Rs80 each)", fontSize = 18.sp, fontWeight = FontWeight.Medium)

        FlowRow(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            customOptions.forEach { option ->
                CustomChip(
                    label = option,
                    selected = selectedCustomizations.contains(option),
                    onClick = {
                        if (selectedCustomizations.contains(option))
                            selectedCustomizations.remove(option)
                        else
                            selectedCustomizations.add(option)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Bottom Buttons
        Row(modifier = Modifier.fillMaxWidth()) {

            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
            ) {
                Text("Cancel")
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = { onAdd(totalPrice) },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD27C1D)
                )
            ) {
                Text("Add Rs${String.format("%.2f", totalPrice)}", color = Color.White)
            }
        }
    }
}

@Composable
fun SizeButton(label: String, selected: Boolean, onClick: () -> Unit) {
    val bg = if (selected) Color(0xFFFFE9C7) else Color.White
    val border = if (selected) Color(0xFFFFA62B) else Color.LightGray

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .border(1.dp, border, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun CustomChip(label: String, selected: Boolean, onClick: () -> Unit) {
    val bg = if (selected) Color(0xFFFFE9C7) else Color(0xFFF4F4F4)
    val border = if (selected) Color(0xFFD27C1D) else Color.LightGray

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .border(1.dp, border, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(label, fontSize = 14.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun LatteScreenPreview() {
    LatteScreen(
        onCancel = {},
        onAdd = {})
}