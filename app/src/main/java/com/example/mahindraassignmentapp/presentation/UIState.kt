package com.example.mahindraassignmentapp.presentation

import com.example.mahindraassignmentapp.data.model.Notification

sealed class UIState {
    object Idle : UIState()
    object Loading: UIState()
    data class Error(val message: String): UIState()
    data class Success(val notifications :List<Notification>,
        val isLoadingMore: Boolean=false):UIState()
}