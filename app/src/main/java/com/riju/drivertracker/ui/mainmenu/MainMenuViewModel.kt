package com.riju.drivertracker.ui.mainmenu

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.riju.drivertracker.R
import com.riju.drivertracker.ui.BaseViewModel
import com.riju.drivertracker.ui.ScreenStatus
import com.riju.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainMenuViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context
) : BaseViewModel<Unit>(ScreenStatus.Success(Unit)) {
    private val _onLogoutSuccess = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val onLogoutSuccess = _onLogoutSuccess.asSharedFlow()

    val isUserLoggedIn = userRepository.getUserFlow()
        .map { it != null }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false
        )

    fun logout() {
        showLoadingDialog()
        try {
            userRepository.logout()
            _onLogoutSuccess.tryEmit(Unit)
        } catch (e: Exception) {
            showError(e.message ?: context.getString(R.string.common_unknown_error))
        } finally {
            hideLoadingDialog()
        }
    }
}
