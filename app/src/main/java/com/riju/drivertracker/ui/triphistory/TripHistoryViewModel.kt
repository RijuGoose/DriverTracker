package com.riju.drivertracker.ui.triphistory

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.riju.drivertracker.R
import com.riju.drivertracker.repository.TripHistoryRepository
import com.riju.drivertracker.repository.model.DatabaseConstants
import com.riju.drivertracker.repository.model.TripDetails
import com.riju.drivertracker.ui.BaseViewModel
import com.riju.drivertracker.ui.ScreenStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripHistoryViewModel @Inject constructor(
    private val tripHistoryRepository: TripHistoryRepository,
    @ApplicationContext private val context: Context
) : BaseViewModel<Unit>() {
    private val _tripHistory: MutableStateFlow<List<TripDetails>> = MutableStateFlow(emptyList())
    val tripHistory = _tripHistory.asStateFlow()

    init {
        getTripHistoryByStartTime()
    }

    private fun getTripHistoryByStartTime() {
        viewModelScope.launch {
            _screenStatus.value = ScreenStatus.LoadingFullScreen
            try {
                _tripHistory.value =
                    tripHistoryRepository.getAllTripHistory(DatabaseConstants.FIELD_START_TIME) ?: emptyList()
                _screenStatus.value = ScreenStatus.Success(Unit)
            } catch (e: Exception) {
                _screenStatus.value = ScreenStatus.ErrorFullScreen(
                    error = e.message ?: context.getString(R.string.common_unknown_error)
                )
            }
        }
    }
}
