package com.riju.drivertracker.ui.triphistory

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.riju.drivertracker.R
import com.riju.drivertracker.repository.TripHistoryRepository
import com.riju.drivertracker.repository.model.DatabaseConstants
import com.riju.drivertracker.ui.BaseViewModel
import com.riju.drivertracker.ui.ScreenStatus
import com.riju.drivertracker.ui.triphistory.model.TripHistoryItemUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TripHistoryViewModel @Inject constructor(
    private val tripHistoryRepository: TripHistoryRepository,
    @ApplicationContext private val context: Context
) : BaseViewModel<List<TripHistoryItemUIModel>>() {
    init {
        getTripHistoryByStartTime()
    }

    private fun getTripHistoryByStartTime() {
        viewModelScope.launch {
            _screenStatus.value = ScreenStatus.LoadingFullScreen
            try {
                val tripHistory =
                    tripHistoryRepository.getAllTripHistory(DatabaseConstants.FIELD_START_TIME)?.map {
                        TripHistoryItemUIModel(
                            tripId = it.tripId,
                            startTime = LocalDateTime.parse(it.startTime),
                            endTime = it.endTime?.let { endTime -> LocalDateTime.parse(endTime) },
                            startLocation = it.startLocation,
                            endLocation = it.endLocation
                        )
                    } ?: emptyList()
                _screenStatus.value = ScreenStatus.Success(tripHistory)
            } catch (e: Exception) {
                _screenStatus.value = ScreenStatus.ErrorFullScreen(
                    error = e.message ?: context.getString(R.string.common_unknown_error)
                )
            }
        }
    }
}
