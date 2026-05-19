package com.virasat.nammaguide.ui.qr

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.virasat.nammaguide.R
import com.virasat.nammaguide.databinding.FragmentQrScannerBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * QRScannerFragment — scans a QR code encoding a siteId (e.g. "KA001").
 *
 * Key implementation details:
 * 1. Camera permission is requested at runtime.
 * 2. CameraX ImageAnalysis processes each frame via ML Kit Barcode Scanner.
 * 3. ImageProxy is ALWAYS closed in addOnCompleteListener (not onSuccessListener)
 *    to prevent camera freeze.
 * 4. A boolean flag (isNavigating) prevents duplicate navigation on rapid scans.
 */
class QRScannerFragment : Fragment() {

    private var _binding: FragmentQrScannerBinding? = null
    private val binding get() = _binding!!

    private lateinit var cameraExecutor: ExecutorService

    /** Debounce flag: prevents firing multiple navigations for the same QR scan */
    @Volatile
    private var isNavigating = false

    // ─── Permission launcher ─────────────────────────────────────────────────

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) startCamera()
        else showPermissionDenied()
    }

    // ─── Lifecycle ───────────────────────────────────────────────────────────

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQrScannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()

        if (hasCameraPermission()) {
            startCamera()
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(
        requireContext(), Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    // ─── CameraX setup ───────────────────────────────────────────────────────

    private fun startCamera() {
        binding.textScanStatus.setText(R.string.label_scan_qr_instruction)
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, QRCodeAnalyzer { siteId ->
                        onQrCodeDetected(siteId)
                    })
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner, cameraSelector, preview, imageAnalyzer
                )
            } catch (e: Exception) {
                Log.e(TAG, "Camera binding failed: ${e.message}")
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    // ─── QR code result handling ─────────────────────────────────────────────

    private fun onQrCodeDetected(siteId: String) {
        if (isNavigating) return // debounce
        isNavigating = true

        requireActivity().runOnUiThread {
            binding.textScanStatus.text = getString(R.string.label_qr_found, siteId)
        }

        // Navigate on main thread
        requireView().post {
            if (findNavController().currentDestination?.id == R.id.qrScannerFragment) {
                val action = QRScannerFragmentDirections
                    .actionQrScannerFragmentToSiteDetailFragment(siteId)
                findNavController().navigate(action)
            }
        }
    }

    private fun showPermissionDenied() {
        binding.textScanStatus.setText(R.string.label_camera_permission_denied)
        binding.previewView.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
        _binding = null
    }

    // ─── ML Kit Barcode Analyzer ─────────────────────────────────────────────

    /**
     * ImageAnalysis.Analyzer that uses ML Kit to detect QR codes.
     *
     * CRITICAL: ImageProxy is closed in addOnCompleteListener to ensure it's
     * always closed regardless of success or failure — prevents camera freeze.
     */
    private class QRCodeAnalyzer(
        private val onQrDetected: (siteId: String) -> Unit
    ) : ImageAnalysis.Analyzer {

        private val scanner = BarcodeScanning.getClient()

        @androidx.camera.core.ExperimentalGetImage
        override fun analyze(imageProxy: ImageProxy) {
            val mediaImage = imageProxy.image
            if (mediaImage == null) {
                imageProxy.close()
                return
            }

            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

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
                .addOnFailureListener { e ->
                    Log.e(TAG, "Barcode scan failed: ${e.message}")
                }
                .addOnCompleteListener {
                    // ALWAYS close ImageProxy here — in addOnCompleteListener
                    // NOT in onSuccessListener — to prevent camera freeze
                    imageProxy.close()
                }
        }
    }

    companion object {
        private const val TAG = "QRScannerFragment"
    }
}
