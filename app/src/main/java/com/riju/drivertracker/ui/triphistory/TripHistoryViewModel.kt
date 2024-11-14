package com.riju.drivertracker.ui.triphistory

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.riju.domain.model.common.DatabaseConstants
import com.riju.drivertracker.R
import com.riju.drivertracker.ui.BaseViewModel
import com.riju.drivertracker.ui.ScreenStatus
import com.riju.drivertracker.ui.triphistory.model.TripHistoryItemUIModel
import com.riju.repository.TripHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TripHistoryViewModel @Inject constructor(
    private val tripHistoryRepository: TripHistoryRepository,
    @ApplicationContext private val context: Context
) : BaseViewModel<List<TripHistoryItemUIModel>>() {
    private val orderBy = DatabaseConstants.Field.StartTime
    private val _isOrderAscending = MutableStateFlow(false)
    val isOrderAscending = _isOrderAscending.asStateFlow()

    init {
        getTripHistory()
    }

    fun getTripHistory() {
        viewModelScope.launch {
            showPullToRefresh()
            try {
                val tripHistory =
                    tripHistoryRepository.getAllTripHistory(orderBy, _isOrderAscending.value).map {
                        TripHistoryItemUIModel(
                            tripId = it.tripId,
                            startTime = LocalDateTime.parse(it.startTime),
                            endTime = it.endTime?.let { endTime -> LocalDateTime.parse(endTime) },
                            startLocation = it.startLocation,
                            endLocation = it.endLocation
                        )
                    }
                _screenStatus.value = ScreenStatus.Success(tripHistory)
                hidePullToRefresh()
            } catch (e: Exception) {
                _screenStatus.value = ScreenStatus.ErrorFullScreen(
                    error = e.message ?: context.getString(R.string.common_unknown_error)
                )
            }
        }
    }

    fun changeSortOrder() {
        _isOrderAscending.value = !isOrderAscending.value
        getTripHistory()
    }
}
