package com.riju.drivertracker.ui.triphistory

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.riju.drivertracker.R
import com.riju.drivertracker.ui.BaseViewModel
import com.riju.drivertracker.ui.ScreenStatus
import com.riju.drivertracker.ui.triphistory.model.TripHistoryItemUIModel
import com.riju.repository.TrackingRepository
import com.riju.repository.TripHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripHistoryViewModel @Inject constructor(
    private val tripHistoryRepository: TripHistoryRepository,
    @ApplicationContext private val context: Context,
) : BaseViewModel<Unit>(ScreenStatus.Success(Unit)) {
    val isOrderAscending = tripHistoryRepository.isListAscending

    val tripHistoryList = tripHistoryRepository.tripHistoryList.map { tripHistory ->
        tripHistory.mapValues { (_, tripDetailsList) ->
            tripDetailsList.map { tripDetails ->
                TripHistoryItemUIModel(
                    tripId = tripDetails.tripId,
                    startTime = tripDetails.startTime,
                    endTime = tripDetails.endTime,
                    startLocation = tripDetails.startLocation,
                    endLocation = tripDetails.endLocation
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyMap()
    )

    fun toggleSortOrder() {
        tripHistoryRepository.toggleSortOrder()
    }
}
