package com.example.kycbank.ui.camera

import androidx.camera.core.ImageCapture
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kycbank.data.local.SelfieFileStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CaptureState {
    data object Idle : CaptureState
    data object Capturing : CaptureState
    data class Captured(val path: String) : CaptureState
    data class Failed(val message: String) : CaptureState
}

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val selfieFileStore: SelfieFileStore
) : ViewModel() {

    private val _captureState = MutableStateFlow<CaptureState>(CaptureState.Idle)
    val captureState: StateFlow<CaptureState> = _captureState

    fun captureSelfie(imageCapture: ImageCapture) {
        viewModelScope.launch {
            _captureState.value = CaptureState.Capturing
            try {
                val path = selfieFileStore.captureSelfie(imageCapture)
                _captureState.value = CaptureState.Captured(path)
            } catch (e: Exception) {
                _captureState.value = CaptureState.Failed(e.message ?: "Capture failed")
            }
        }
    }
}