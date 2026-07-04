package com.example.kycbank.ui.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.LocalActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

private enum class PermissionUiState {
    GRANTED, NOT_ASKED_OR_DENIED, PERMANENTLY_DENIED
}

@Composable
fun CameraPermissionHandler(
    onPermissionGranted: @Composable () -> Unit
) {
    val context = LocalContext.current
    val activity = LocalActivity.current

    var hasRequestedBefore by remember { mutableStateOf(false) }

    fun currentState(): PermissionUiState {
        val granted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (granted) return PermissionUiState.GRANTED

        val canShowRationale = activity?.let {
            ActivityCompat.shouldShowRequestPermissionRationale(it, Manifest.permission.CAMERA)
        } ?: true

        // If permission isn't granted AND we can no longer show a rationale,
        // AND this isn't the very first ask, the user has permanently denied it.
        return if (!canShowRationale && hasRequestedBefore) {
            PermissionUiState.PERMANENTLY_DENIED
        } else {
            PermissionUiState.NOT_ASKED_OR_DENIED
        }
    }

    var uiState by remember { mutableStateOf(currentState()) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasRequestedBefore = true
        uiState = if (granted) PermissionUiState.GRANTED else currentState()
    }
    when (uiState) {
        PermissionUiState.GRANTED -> {
            onPermissionGranted()
        }
        PermissionUiState.NOT_ASKED_OR_DENIED -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text("Camera permission is needed to capture your selfie for KYC verification.")
                Button(
                    onClick = {
                        hasRequestedBefore = true
                        launcher.launch(Manifest.permission.CAMERA)
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 12.dp)
                ) {
                    Text("Grant Camera Permission")
                }
            }
        }
        PermissionUiState.PERMANENTLY_DENIED -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text("Camera access was permanently denied. Please enable it from Settings to complete KYC.")
                Button(
                    onClick = {
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null)
                        )
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 12.dp)
                ) {
                    Text("Open App Settings")
                }
            }
        }
    }
}