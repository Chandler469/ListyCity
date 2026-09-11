package com.demingqian.listycity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.items
import com.demingqian.listycity.ui.theme.ListyCityTheme
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Color

class CityRepository {
    private val _cities = mutableStateListOf(
        "Edmonton", "Vancouver", "Moscow", "Sydney", "Berlin", "Vienna", "Tokyo", "Beijing", "Osaka", "New Delhi"
    )

    val cities: List<String>
        get() = _cities

    fun addCity(city: String) {
        _cities.add(city)
    }

    fun deleteCity(city: String) {
        _cities.remove(city)
    }
}

class CardUiState {
    var selectedCity by mutableStateOf("")

    fun updateSelection(city: String) {
        if (city == selectedCity) {
            selectedCity = ""
        } else {
            selectedCity = city
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val cityRepo = CityRepository()
        val cardUiState = CardUiState()
        setContent {
            ListyCityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CityListScreen(
                        cities = cityRepo.cities,
                        onAddCity = { cityRepo.addCity(it) },
                        onDeleteCity = { cityRepo.deleteCity(it) },
                        cardUiState = cardUiState,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun CityListScreen(
    modifier: Modifier = Modifier,
    cities: List<String>,
    cardUiState: CardUiState,
    onAddCity: (String) -> Unit,
    onDeleteCity: (String) -> Unit
) {
    var newCityName by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        OutlinedTextField(
            value = newCityName,
            onValueChange = { newCityName = it },
            label = { Text(text = "City Name:") },
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Button(
                onClick = {
                    if (newCityName.isNotBlank()) {
                        onAddCity(newCityName)
                        newCityName = ""
                    }
                }
            ) {
                Text(
                    text = "Add City"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (cardUiState.selectedCity.isNotBlank()) {
                        onDeleteCity(cardUiState.selectedCity)
                        cardUiState.selectedCity = ""
                    }
                }
            ) {
                Text(
                    text = "Delete City"
                )
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(cities) { city ->
                CityRow(
                    city = city,
                    cardUiState = cardUiState
                )
            }
        }
    }
}

@Composable
fun CityRow(
    city: String,
    cardUiState: CardUiState
) {
    Card(
        onClick = {
            cardUiState.updateSelection(city = city)
        },
        colors = CardDefaults.cardColors(
            containerColor = if (city == cardUiState.selectedCity) {
                Color(0xFF9E9E9E)
            } else {
                Color(0xFFFFFFFF)
            }
        )
    ) {
        Text(
            text = city,
            fontSize = 28.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp)
        )
    }
}