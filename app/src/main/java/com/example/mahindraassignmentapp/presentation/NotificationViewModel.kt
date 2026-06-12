package com.example.mahindraassignmentapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mahindraassignmentapp.Constants
import com.example.mahindraassignmentapp.data.model.Notification
import com.example.mahindraassignmentapp.domain.use_case.GetNotificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val useCase: GetNotificationUseCase
) :ViewModel(){

    private val _uiState= MutableStateFlow<UIState>( UIState.Idle)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    private var currentPage = 1

    private var isLoadingMore = false

    private var hasMore = true

    private val notifications = mutableListOf<Notification>()

    fun getNotifications(pageNo:Int=1){

        viewModelScope.launch {
            try{
                currentPage = pageNo
                hasMore = true
                notifications.clear()
                _uiState.value=UIState.Loading
                val response = useCase.execute(pageNo)
                notifications.addAll(
                    mapData(response)
                )
                hasMore = response.isNotEmpty()
                _uiState.value = UIState.Success(notifications.toList())
            }catch (e:Exception){
                _uiState.value=UIState.Error(Constants.ERROR_MSG)
            }
        }

    }
    fun loadMoreNotifications() {

        if (isLoadingMore || !hasMore) return

        viewModelScope.launch {

            isLoadingMore = true

            _uiState.value = UIState.Success(
                notifications = notifications.toList(),
                isLoadingMore = true
            )
            try {
                val nextPage = currentPage + 1
                val response = useCase.execute(nextPage)

                if (response.isEmpty()) {
                    hasMore = false
                    return@launch
                }

                currentPage = nextPage

                notifications.addAll(mapData(response))

                _uiState.value = UIState.Success(
                    notifications = notifications.toList(),
                    isLoadingMore = false
                )

            } catch (e: Exception) {

                _uiState.value = UIState.Success(
                    notifications = notifications.toList(),
                    isLoadingMore = false
                )

            } finally {
                isLoadingMore = false
            }
        }
    }
}


private fun mapData(list: List<Notification>): List<Notification> {
    return list.mapIndexed { index, item ->
        item.copy(
            tag = if (index % 2 == 0) "Info" else "Critical"
        )
    }
}