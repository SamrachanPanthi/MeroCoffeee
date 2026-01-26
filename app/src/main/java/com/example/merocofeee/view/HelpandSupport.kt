package com.example.merocofeee.view



import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


private val CreamBackground = Color(0xFFFDF5E6)
private val DarkBrown = Color(0xFF4E342E)
private val SoftGray = Color(0xFF757575)

data class FaqItem(val question: String, val answer: String)

@Composable
fun HelpSupportScreen(onBack: () -> Unit) {
    val faqs = listOf(
        FaqItem("How do I reset my password?", "You can reset your password from the login screen by tapping the 'Forgot Password' link. An email will be sent to you with further instructions."),
        FaqItem("How can I track my order?", "Once your order is placed, you will receive a confirmation. You can view the status of your order in the 'My Orders' section of your profile."),
        FaqItem("What payment methods are accepted?", "We accept all major credit cards, as well as digital wallets like Google Pay and Apple Pay.")
    )

    Scaffold(containerColor = CreamBackground) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
//            ProfileSubScreenHeader("Help & Support", onBack)
//            Spacer(Modifier.height(24.dp))

            Text("Frequently Asked Questions", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = DarkBrown))
            Spacer(Modifier.height(16.dp))
            faqs.forEach { faq ->
                FaqCard(faq)
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(Modifier.height(32.dp))

            Text("Contact Us", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = DarkBrown))
            Spacer(Modifier.height(8.dp))
            Text("For any further assistance, please do not hesitate to contact our support team at:",
                style = MaterialTheme.typography.bodyLarge, color = SoftGray, lineHeight = 24.sp)
            Text("support@merocoffee.com", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = DarkBrown))
        }
    }
}

@Composable
fun FaqCard(faq: FaqItem) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp).animateContentSize()) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Text(faq.question, modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold, color = DarkBrown)
                Icon(if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, null, tint = SoftGray)
            }
            if (isExpanded) {
                Spacer(Modifier.height(12.dp))
                Text(faq.answer, color = SoftGray, lineHeight = 22.sp)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFDF5E6)
@Composable
fun HelpSupportScreenPreview() {
    HelpSupportScreen(onBack = {})
}
