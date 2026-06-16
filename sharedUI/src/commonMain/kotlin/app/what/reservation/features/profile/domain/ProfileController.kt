package app.what.reservation.features.profile.domain

import androidx.compose.ui.util.fastFilteredMap
import androidx.lifecycle.viewModelScope
import app.what.reservation.core.foundation.core.UIController
import app.what.reservation.core.foundation.data.RemoteState
import app.what.reservation.core.foundation.services.AppLogger.Companion.Auditor
import app.what.reservation.core.foundation.utils.launchSafe
import app.what.reservation.data.local.AuthSessionManager
import app.what.reservation.data.local.AuthStorage
import app.what.reservation.data.remote.BookingsService
import app.what.reservation.data.remote.UsersService
import app.what.reservation.domain.models.BookingDomain
import app.what.reservation.domain.models.UserDomain
import app.what.reservation.features.profile.domain.models.ProfileAction
import app.what.reservation.features.profile.domain.models.ProfileEvent
import app.what.reservation.features.profile.domain.models.ProfileInfo
import app.what.reservation.features.profile.domain.models.ProfileState
import kotlin.uuid.Uuid

class ProfileController(
    private val authStorage: AuthStorage,
    private val usersService: UsersService,
    private val apiSessionManager: AuthSessionManager,
    private val bookings: BookingsService,
    private val users: UsersService
) : UIController<ProfileState, ProfileAction, ProfileEvent>(
    ProfileState()
) {
    init {
        load()
    }

    override fun obtainEvent(viewEvent: ProfileEvent) = when (viewEvent) {
        ProfileEvent.OnLogout -> logout()
        ProfileEvent.OnRefresh -> load()
        ProfileEvent.OnOpen -> checkAccountSwitch()
        is ProfileEvent.OnCancelBookingAsUser -> cancelBooking(viewEvent.id)
    }

    private fun cancelBooking(id: Uuid) {
        viewModelScope.launchSafe(
            onFailure = { Auditor.err("d", it.message ?: "", it) }
        ) { bookings.cancel(id, null); loadMyReservations() }
    }

    private fun load() {
        getProfileInfo()
        loadMyReservations()
    }

    private fun loadMyReservations() {
        viewModelScope.launchSafe(
            onFailure = {
                Auditor.err("d", it.message ?: "", it)
            }
        ) {
            val reservations = bookings.my().map {
                BookingDomain(
                    it.id,
                    it.userId,
                    it.serviceId,
                    it.startsAt,
                    it.endsAt,
                    it.status,
                    it.cancelReason
                )
            }.sortedBy { it.startsAt }

            reservations
                .fastFilteredMap({ it.userId !in viewState.userCacheInfo.map { it.id } }) { it.userId }
                .toSet()
                .forEach {
                    loadUserInfo(it)
                }

            updateState { copy(myReservations = reservations) }
        }
    }

    private fun loadUserInfo(userId: Uuid) {
        viewModelScope.launchSafe(
            onFailure = {
                Auditor.err("d", it.message ?: "", it)
            }
        ) {
            val user = users.byId(userId).let {
                UserDomain(
                    it.id,
                    it.name,
                    it.email,
                    it.picture
                )
            }

            updateState { copy(userCacheInfo = userCacheInfo + user) }
        }
    }

    private fun checkAccountSwitch() {
        if (authStorage.userId.get() == viewState.info?.id) return
        load()
    }

    private fun logout() {
        apiSessionManager.clearCredentials()
        setAction(ProfileAction.NavigateToAuth)
    }

    private fun getProfileInfo() {
        updateState { copy(infoState = RemoteState.Loading, info = null) }
        viewModelScope.launchSafe(
            debug = true,
            onFailure = {
                updateState { copy(infoState = RemoteState.Error(it)) }
            }
        ) {
            val data = usersService.me()

            updateState {
                copy(
                    infoState = RemoteState.Success,
                    info = ProfileInfo(data.id, data.name, data.email, data.picture)
                )
            }
        }
    }
}