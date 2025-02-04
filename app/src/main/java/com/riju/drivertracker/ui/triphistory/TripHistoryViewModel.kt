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
    private val trackingRepository: TrackingRepository,
    @ApplicationContext private val context: Context,
) : BaseViewModel<Unit>(ScreenStatus.Success(Unit)) {
    val isOrderAscending = tripHistoryRepository.isListAscending

    @Suppress("VariableNaming")
    val _emptyTripDialog = MutableStateFlow<String?>(null)
    val emptyTripDialog = _emptyTripDialog.asStateFlow()

    val tripHistoryList = tripHistoryRepository.tripHistoryList.map { tripHistory ->
        tripHistory.mapValues { (_, tripDetailsList) ->
            tripDetailsList.map { tripDetails ->
                TripHistoryItemUIModel(
                    tripId = tripDetails.tripId,
                    inProgress = tripDetails.tripId == trackingRepository.currentTripId.firstOrNull(),
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

    fun setEmptyTripDialog(tripId: String?) {
        _emptyTripDialog.value = tripId
    }

    suspend fun isTripEmpty(tripId: String): Boolean {
        return try {
            tripHistoryRepository.getTripRouteById(tripId)?.isEmpty() == true
        } catch (e: Exception) {
            true
        }
    }

    fun deleteTrip(tripId: String) {
        viewModelScope.launch {
            try {
                tripHistoryRepository.deleteTrip(tripId)
            } catch (e: Exception) {
                _screenStatus.value = ScreenStatus.ErrorFullScreen(
                    error = e.message ?: context.getString(R.string.common_unknown_error)
                )
            }
        }
    }

    fun toggleSortOrder() {
        tripHistoryRepository.toggleSortOrder()
    }
}
