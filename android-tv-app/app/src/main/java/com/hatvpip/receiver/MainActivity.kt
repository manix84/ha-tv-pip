package com.hatvpip.receiver

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    private var compatibility by mutableStateOf<DeviceCompatibility?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppLog.activityCreated("MainActivity")
        refreshCompatibility()

        setContent {
            HaTvTheme {
                compatibility?.let { currentCompatibility ->
                    MainScreen(
                        compatibility = currentCompatibility,
                        onRequestOverlayPermission = ::openOverlayPermissionSettings,
                        onStopOverlay = ::stopOverlayFallback,
                        onPlayTestVideo = {
                            startActivity(Intent(this, PlayerActivity::class.java))
                        }
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshCompatibility()
    }

    private fun refreshCompatibility() {
        compatibility = DeviceCompatibilityEvaluator.from(this)
    }

    private fun openOverlayPermissionSettings() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return

        val settingsIntent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        runCatching {
            startActivity(settingsIntent)
        }.onFailure { error ->
            AppLog.error("Unable to open overlay permission settings", error)
            startActivity(Intent(Settings.ACTION_SETTINGS))
        }
    }

    private fun stopOverlayFallback() {
        stopService(
            Intent(this, OverlayPlayerService::class.java)
                .setAction(OverlayPlayerService.ACTION_STOP)
        )
    }
}

@Composable
private fun MainScreen(
    compatibility: DeviceCompatibility,
    onRequestOverlayPermission: () -> Unit,
    onStopOverlay: () -> Unit,
    onPlayTestVideo: () -> Unit
) {
    val playButtonFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        playButtonFocusRequester.requestFocus()
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 72.dp, vertical = 56.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "HA TV PiP Receiver",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Phase 1 MVP: test HLS playback, native PiP where supported, and safe fallback handling.",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(28.dp))
            CompatibilityStatus(compatibility = compatibility)
            Spacer(modifier = Modifier.height(32.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = onPlayTestVideo,
                    modifier = Modifier
                        .widthIn(min = 220.dp)
                        .focusRequester(playButtonFocusRequester)
                        // Explicit focusability makes D-pad startup focus predictable on TV launchers.
                        .focusable()
                ) {
                    Text(text = "Play Test Video", fontSize = 18.sp)
                }
                if (compatibility.canRequestOverlayPermission) {
                    Button(
                        onClick = onRequestOverlayPermission,
                        modifier = Modifier.widthIn(min = 260.dp)
                    ) {
                        Text(text = "Open Overlay Settings", fontSize = 18.sp)
                    }
                }
                if (compatibility.overlayPermission == CompatibilityState.Granted) {
                    Button(
                        onClick = onStopOverlay,
                        modifier = Modifier.widthIn(min = 180.dp)
                    ) {
                        Text(text = "Stop Overlay", fontSize = 18.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun CompatibilityStatus(compatibility: DeviceCompatibility) {
    Column(
        modifier = Modifier.widthIn(max = 760.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Device compatibility",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = compatibility.androidVersionLabel,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 17.sp
        )
        Text(
            text = "Native PiP: ${compatibility.nativePictureInPicture.label}",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 17.sp
        )
        Text(
            text = "Overlay permission: ${compatibility.overlayPermission.label}",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 17.sp
        )
        Text(
            text = "Recommended mode: ${compatibility.recommendedMode.label}",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = compatibility.statusText,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 17.sp
        )
    }
}
