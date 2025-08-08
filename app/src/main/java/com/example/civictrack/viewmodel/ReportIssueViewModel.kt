package com.example.civictrack.viewmodel


import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.civictrack.data.model.IssueCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReportUiState(
    val title: String = "",
    val description: String = "",
    val category: IssueCategory = IssueCategory.ROADS,
    val photos: List<Uri> = emptyList(),
    val isSubmitting: Boolean = false,
    val submissionSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ReportIssueViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(ReportUiState())
    val uiState = _uiState.asStateFlow()

    fun onTitleChange(newTitle: String) {
        _uiState.update { it.copy(title = newTitle) }
    }

    fun onDescriptionChange(newDescription: String) {
        _uiState.update { it.copy(description = newDescription) }
    }

    fun onCategoryChange(newCategory: IssueCategory) {
        _uiState.update { it.copy(category = newCategory) }
    }

    fun onPhotosSelected(uris: List<Uri>) {
        _uiState.update {
            // Combine old and new photos, respecting the max limit of 3
            val currentPhotos = it.photos
            val newTotal = currentPhotos.size + uris.size
            if (newTotal > 3) {
                it.copy(error = "You can select a maximum of 3 photos.")
            } else {
                it.copy(photos = currentPhotos + uris, error = null)
            }
        }
    }

    fun onPhotoRemoved(uri: Uri) {
        _uiState.update {
            it.copy(photos = it.photos - uri)
        }
    }

    fun submitReport() {
        // In a real app, you would upload the photos from their URIs first,
        // get back URLs, and then submit those URLs with the report.
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
            delay(2000) // Simulate network delay
            _uiState.update { it.copy(isSubmitting = false, submissionSuccess = true) }
        }
    }
}

//    fun submitReport() {
//        viewModelScope.launch {
//            _uiState.update { it.copy(isSubmitting = true) }
//
//            // --- MOCK API CALL ---
//            // In a real app, you would call repository.reportIssue(...)
//            delay(2000) // Simulate network delay
//            val isSuccess = _uiState.value.title.isNotBlank() // Simulate success/fail
//
//            if (isSuccess) {
//                _uiState.update { it.copy(isSubmitting = false, submissionSuccess = true) }
//            } else {
//                _uiState.update { it.copy(isSubmitting = false, error = "Submission failed.") }
//            }
//        }
//    }
//}