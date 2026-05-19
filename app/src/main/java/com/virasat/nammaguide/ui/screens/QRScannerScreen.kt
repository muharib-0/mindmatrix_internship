package com.virasat.nammaguide.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.virasat.nammaguide.R
import com.virasat.nammaguide.ui.theme.AgedParchment
import com.virasat.nammaguide.ui.theme.SandstoneGold
import com.virasat.nammaguide.ui.theme.TempleMaroon
import java.util.concurrent.Executors

/**
 * QRScannerScreen — CameraX preview via AndroidView + ML Kit barcode analysis.
 *
 * Key safety rules:
 * 1. ImageProxy ALWAYS closed in addOnCompleteListener (not onSuccessListener) → no camera freeze.
 * 2. isNavigating MutableState flag debounces duplicate navigation.
 * 3. Permission requested via rememberLauncherForActivityResult.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRScannerScreen(onSiteFound: (String) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED
        )
    }
    var statusText by remember { mutableStateOf("") }
    // Debounce: once a QR is detected, prevent duplicate navigation
    val isNavigating = remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.title_qr_scanner),
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TempleMaroon
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.Black)
        ) {
            if (hasCameraPermission) {
                // ── CameraX preview via AndroidView ─────────────────────────
                AndroidView(
                    factory = { ctx ->
                        val previewView = PreviewView(ctx)
                        val executor = Executors.newSingleThreadExecutor()
                        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                        cameraProviderFuture.addListener({
                            val cameraProvider = cameraProviderFuture.get()

                            val preview = Preview.Builder().build().also {
                                it.setSurfaceProvider(previewView.surfaceProvider)
                            }

                            val analyzer = ImageAnalysis.Builder()
                                .setBackpressureStrategy(
                                    ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
                                )
                                .build()
                                .also { analysis ->
                                    analysis.setAnalyzer(executor, QRCodeAnalyzer { siteId ->
                                        if (!isNavigating.value) {
                                            isNavigating.value = true
                                            statusText = "✓ Found: $siteId"
                                            onSiteFound(siteId)
                                        }
                                    })
                                }

                            try {
                                cameraProvider.unbindAll()
                                cameraProvider.bindToLifecycle(
                                    lifecycleOwner,
                                    CameraSelector.DEFAULT_BACK_CAMERA,
                                    preview,
                                    analyzer
                                )
                            } catch (e: Exception) {
                                Log.e("QRScanner", "Camera bind failed: ${e.message}")
                            }
                        }, ContextCompat.getMainExecutor(ctx))

                        previewView
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // ── Scan frame overlay ──────────────────────────────────────
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .align(Alignment.Center)
                        .offset(y = (-20).dp),
                    contentAlignment = Alignment.Center
                ) {
                    ScanFrameOverlay()
                }

                // ── Status text at bottom ───────────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(Color.Black.copy(alpha = 0.65f))
                        .padding(20.dp)
                ) {
                    Text(
                        text = statusText.ifBlank {
                            stringResource(R.string.label_scan_qr_instruction)
                        },
                        color = Color.White,
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Serif,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

            } else {
                // ── Permission denied state ─────────────────────────────────
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AgedParchment)
                        .padding(40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("📷", fontSize = 56.sp)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.label_camera_permission_denied),
                        color = TempleMaroon,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily.Serif
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                        colors = ButtonDefaults.buttonColors(containerColor = TempleMaroon)
                    ) {
                        Text("Grant Permission")
                    }
                }
            }
        }
    }
}

// ── Compose scan-frame corner brackets drawn with Canvas ─────────────────────
@Composable
private fun ScanFrameOverlay() {
    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
        val strokeWidth = 6f
        val cornerLen = 50f
        val paint = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
        val color = androidx.compose.ui.graphics.Color.White
        val w = size.width; val h = size.height

        // Top-left
        drawLine(color, start = androidx.compose.ui.geometry.Offset(0f, cornerLen),
            end = androidx.compose.ui.geometry.Offset(0f, 0f), strokeWidth = strokeWidth)
        drawLine(color, start = androidx.compose.ui.geometry.Offset(0f, 0f),
            end = androidx.compose.ui.geometry.Offset(cornerLen, 0f), strokeWidth = strokeWidth)
        // Top-right
        drawLine(color, start = androidx.compose.ui.geometry.Offset(w - cornerLen, 0f),
            end = androidx.compose.ui.geometry.Offset(w, 0f), strokeWidth = strokeWidth)
        drawLine(color, start = androidx.compose.ui.geometry.Offset(w, 0f),
            end = androidx.compose.ui.geometry.Offset(w, cornerLen), strokeWidth = strokeWidth)
        // Bottom-left
        drawLine(color, start = androidx.compose.ui.geometry.Offset(0f, h - cornerLen),
            end = androidx.compose.ui.geometry.Offset(0f, h), strokeWidth = strokeWidth)
        drawLine(color, start = androidx.compose.ui.geometry.Offset(0f, h),
            end = androidx.compose.ui.geometry.Offset(cornerLen, h), strokeWidth = strokeWidth)
        // Bottom-right
        drawLine(color, start = androidx.compose.ui.geometry.Offset(w - cornerLen, h),
            end = androidx.compose.ui.geometry.Offset(w, h), strokeWidth = strokeWidth)
        drawLine(color, start = androidx.compose.ui.geometry.Offset(w, h - cornerLen),
            end = androidx.compose.ui.geometry.Offset(w, h), strokeWidth = strokeWidth)
    }
}

// ── ML Kit Barcode Analyzer ───────────────────────────────────────────────────
private class QRCodeAnalyzer(
    private val onQrDetected: (String) -> Unit
) : ImageAnalysis.Analyzer {

    private val scanner = BarcodeScanning.getClient()

    @androidx.camera.core.ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            imageProxy.close()
            return
        }
        val image = InputImage.fromMediaImage(
            mediaImage, imageProxy.imageInfo.rotationDegrees
        )
        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    if (barcode.format == Barcode.FORMAT_QR_CODE) {
                        val raw = barcode.rawValue
                        if (!raw.isNullOrBlank()) {
                            onQrDetected(raw.trim())
                            break
                        }
                    }
                }
            }
            .addOnFailureListener { Log.e("QRAnalyzer", it.message ?: "scan error") }
            .addOnCompleteListener {
                // ALWAYS close in addOnCompleteListener — prevents camera freeze
                imageProxy.close()
            }
    }
}
