package com.riju.drivertracker.ui.triphistory

import androidx.lifecycle.viewModelScope
import com.riju.drivertracker.repository.TripHistoryRepository
import com.riju.drivertracker.repository.model.TripDetails
import com.riju.drivertracker.ui.BaseViewModel
import com.riju.drivertracker.ui.ScreenStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripHistoryViewModel @Inject constructor(
    private val tripHistoryRepository: TripHistoryRepository
) : BaseViewModel() {
    private val _tripHistory: MutableStateFlow<List<TripDetails>> = MutableStateFlow(emptyList())
    val tripHistory = _tripHistory.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                _tripHistory.value = tripHistoryRepository.getAllTripHistory() ?: emptyList()
                _screenStatus.value = ScreenStatus.Success
            } catch (e: Exception) {
                _screenStatus.value = ScreenStatus.Failure(
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }
}