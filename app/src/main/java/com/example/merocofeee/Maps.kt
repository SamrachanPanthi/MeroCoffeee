//package com.example.merocofeee
//
//import android.Manifest
//import android.content.pm.PackageManager
//import android.location.Geocoder
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.text.BasicTextField
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.LocationOn
//import androidx.compose.material3.Button
//import androidx.compose.material3.FloatingActionButton
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.runtime.snapshotFlow
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.core.content.ContextCompat
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.maps.model.CameraPosition
//import com.google.android.gms.maps.model.LatLng
//import com.google.maps.android.compose.GoogleMap
//import com.google.maps.android.compose.MapProperties
//import com.google.maps.android.compose.MapType
//import com.google.maps.android.compose.MapUiSettings
//import com.google.maps.android.compose.Marker
//import com.google.maps.android.compose.rememberCameraPositionState
//import com.google.maps.android.compose.rememberMarkerState
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.distinctUntilChanged
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import java.util.Locale
//
//@Composable
//fun LocationScreen() {
//
//    val context = LocalContext.current
//    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//    val coroutineScope = rememberCoroutineScope()
//
//    var hasLocationPermission by remember {
//        mutableStateOf(
//            ContextCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        )
//    }
//
//    val permissionLauncher = rememberLauncherForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { granted ->
//        hasLocationPermission = granted
//    }
//
//    LaunchedEffect(Unit) {
//        if (!hasLocationPermission) {
//            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//        }
//    }
//
//    var userLocation by remember { mutableStateOf(LatLng(27.7172, 85.3240)) }
//    var markerPosition by remember { mutableStateOf(userLocation) }
//    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition.fromLatLngZoom(userLocation, 14f)
//    }
//
//    // Last known location
//    LaunchedEffect(hasLocationPermission) {
//        if (hasLocationPermission) {
//            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
//                location?.let {
//                    val latLng = LatLng(it.latitude, it.longitude)
//                    userLocation = latLng
//                    markerPosition = latLng
//                    cameraPositionState.position =
//                        CameraPosition.fromLatLngZoom(latLng, 15f)
//                }
//            }
//        }
//    }
//
//    var searchQuery by remember { mutableStateOf("") }
//    var mapType by remember { mutableStateOf(MapType.NORMAL) } // Toggle Normal/Satellite
//
//    Column {
//        // Search Bar
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(8.dp)
//                .height(50.dp)
//                .background(Color.White, MaterialTheme.shapes.small)
//        ) {
//            BasicTextField(
//                value = searchQuery,
//                onValueChange = { searchQuery = it },
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(8.dp),
//                singleLine = true
//            )
//            if (searchQuery.isEmpty()) {
//                Text(
//                    text = "Search location",
//                    color = Color.Gray,
//                    modifier = Modifier.padding(8.dp)
//                )
//            }
//        }
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 8.dp, vertical = 4.dp),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Button(
//                onClick = {
//                    coroutineScope.launch {
//                        try {
//                            val geocoder = Geocoder(context, Locale.getDefault())
//                            val addresses = withContext(Dispatchers.IO) {
//                                geocoder.getFromLocationName(searchQuery, 1)
//                            }
//                            if (!addresses.isNullOrEmpty()) {
//                                val addr = addresses[0]
//                                val latLng = LatLng(addr.latitude, addr.longitude)
//                                markerPosition = latLng
//                                cameraPositionState.position =
//                                    CameraPosition.fromLatLngZoom(latLng, 15f)
//                            }
//                        } catch (e: Exception) {
//                            e.printStackTrace()
//                        }
//                    }
//                },
//                modifier = Modifier.weight(1f)
//            ) { Text("Go") }
//
//            Spacer(modifier = Modifier.width(8.dp))
//
//            Button(
//                onClick = {
//                    mapType =
//                        if (mapType == MapType.NORMAL) MapType.SATELLITE else MapType.NORMAL
//                },
//                modifier = Modifier.weight(1f)
//            ) { Text(if (mapType == MapType.NORMAL) "Satellite" else "Normal") }
//        }
//
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(top = 8.dp) // Space for search bar
//        ) {
//
//            val markerState = rememberMarkerState(position = markerPosition)
//
//            GoogleMap(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(bottom = 80.dp), // Push Google zoom/location buttons above FAB
//                cameraPositionState = cameraPositionState,
//                properties = MapProperties(
//                    isMyLocationEnabled = hasLocationPermission,
//                    mapType = mapType
//                ),
//                uiSettings = MapUiSettings(
//                    myLocationButtonEnabled = true,
//                    zoomControlsEnabled = true,
//                    zoomGesturesEnabled = true,
//                    mapToolbarEnabled = false
//                )
//            ) {
//                Marker(
//                    state = markerState,
//                    draggable = true,
//                    title = "Selected Location"
//                )
//            }
//
//            // Update markerPosition when dragged
//            LaunchedEffect(markerState.position) {
//                snapshotFlow { markerState.position }
//                    .distinctUntilChanged()
//                    .collect { newPos ->
//                        markerPosition = newPos
//                    }
//            }
//
//            // Recenter FAB -chnages the cam
//            if (hasLocationPermission) {
//                FloatingActionButton(
//                    onClick = {
//                        cameraPositionState.position =
//                            CameraPosition.fromLatLngZoom(userLocation, 15f)
//                        markerPosition = userLocation
//                    },
//                    containerColor = Color.Black,
//                    modifier = Modifier
//                        .align(Alignment.BottomEnd)
//                        .padding(16.dp)
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.LocationOn,
//                        contentDescription = "Recenter",
//                        tint = Color.White
//                    )
//                }
//            }
//        }
//    }
//}