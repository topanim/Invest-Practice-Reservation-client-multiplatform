package app.what.reservation.features.serviceDetail.domain

import androidx.compose.ui.util.fastFilteredMap
import androidx.lifecycle.viewModelScope
import app.what.reservation.core.foundation.core.UIController
import app.what.reservation.core.foundation.data.RemoteState
import app.what.reservation.core.foundation.services.AppLogger.Companion.Auditor
import app.what.reservation.core.foundation.utils.launchSafe
import app.what.reservation.data.local.AuthStorage
import app.what.reservation.data.remote.AvailabilityService
import app.what.reservation.data.remote.BookingsService
import app.what.reservation.data.remote.ServicesService
import app.what.reservation.data.remote.UsersService
import app.what.reservation.data.remote.dto.CreateAvailabilityBlockRequest
import app.what.reservation.data.remote.dto.CreateAvailabilityRuleRequest
import app.what.reservation.data.remote.dto.CreateBookingRequest
import app.what.reservation.data.remote.dto.CreateOneTimeAvailabilityRuleRequest
import app.what.reservation.domain.models.BookingDomain
import app.what.reservation.domain.models.ServiceDomain
import app.what.reservation.domain.models.UserDomain
import app.what.reservation.features.serviceDetail.domain.models.ServiceDetailAction
import app.what.reservation.features.serviceDetail.domain.models.ServiceDetailEvent
import app.what.reservation.features.serviceDetail.domain.models.ServiceDetailState
import app.what.reservation.features.serviceDetail.domain.models.Slot
import kotlinx.datetime.LocalDate
import kotlin.uuid.Uuid

class ServiceDetailController(
    serviceId: String,
    private val services: ServicesService,
    private val availability: AvailabilityService,
    private val users: UsersService,
    private val bookings: BookingsService,
    private val authStorage: AuthStorage
) : UIController<ServiceDetailState, ServiceDetailAction, ServiceDetailEvent>(
    ServiceDetailState()
) {
    private val serviceId = Uuid.parse(serviceId)

    init {
        load()
    }

    override fun obtainEvent(viewEvent: ServiceDetailEvent) = when (viewEvent) {
        ServiceDetailEvent.OnRefresh -> load()
        ServiceDetailEvent.OnAddAdminFormOpen -> clearSearchResults()
        is ServiceDetailEvent.OnUserSearchConfirm -> searchUsers(viewEvent)
        is ServiceDetailEvent.OnRemoveAdminClicked -> removeAdmin(viewEvent.id)
        is ServiceDetailEvent.OnAddAdminFormConfirmed -> addAdmins(viewEvent.adminIds)
        is ServiceDetailEvent.OnAddAvailabilityRuleFormConfirmed -> addAvailability(viewEvent)
        is ServiceDetailEvent.OnAddAvailabilityBlockFormConfirmed -> addAvailabilityBlock(viewEvent)
        is ServiceDetailEvent.OnAddOneTimeAvailabilityRuleFormConfirmed -> addOneTimeAvailability(
            viewEvent
        )

        is ServiceDetailEvent.OnRemoveAvailabilityRuleClicked -> deleteAvailability(viewEvent.id)
        is ServiceDetailEvent.OnRemoveAvailabilityBlockClicked -> deleteBlockAvailability(viewEvent.id)
        is ServiceDetailEvent.OnRemoveAvailabilityOneTimeRuleClicked -> deleteOneTimeAvailability(
            viewEvent.id
        )

        is ServiceDetailEvent.OnBookingDateChoiceComplete -> loadTimeSlots(viewEvent.date)
        is ServiceDetailEvent.OnBookingFormConfirmed -> makeBooking(viewEvent.slot)
        is ServiceDetailEvent.OnCancelBookingAsUser -> cancelBooking(viewEvent.id)
        is ServiceDetailEvent.OnCancelBookingAsAdmin -> cancelBooking(
            viewEvent.id,
            viewEvent.reason
        )
    }

    private fun load() {
        loadServiceInfo()
        loadServiceAdmins()
        loadAvailability()
        loadOneTimeAvailability()
        loadAvailabilityBlock()
        loadMyReservations()
    }

    private fun loadAllAvailability() {
        loadAvailability()
        loadOneTimeAvailability()
        loadAvailabilityBlock()
    }

    private fun cancelBooking(id: Uuid, reason: String? = null) {
        viewModelScope.launchSafe(
            onFailure = { Auditor.err("d", it.message ?: "", it) }
        ) { bookings.cancel(id, reason); loadAllReservations(); loadMyReservations() }
    }

    private fun loadTimeSlots(date: LocalDate) {
        updateState { copy(slots = emptyList(), slotsState = RemoteState.Loading) }
        viewModelScope.launchSafe(
            onFailure = {
                updateState { copy(slotsState = RemoteState.Error(it)) }
                Auditor.err("d", it.message ?: "", it)
            }
        ) {
            val slots = bookings.slots(serviceId, date).map { Slot(it.from, it.to) }
            updateState { copy(slots = slots, slotsState = RemoteState.Success) }
        }
    }


    private fun loadMyReservations() {
        viewModelScope.launchSafe(
            onFailure = {
                Auditor.err("d", it.message ?: "", it)
            }
        ) {
            val reservations = bookings
                .my(serviceId)
                .map {
                    BookingDomain(
                        it.id,
                        it.userId,
                        it.serviceId,
                        it.startsAt,
                        it.endsAt,
                        it.status,
                        it.cancelReason
                    )
                }

            reservations
                .fastFilteredMap({ it.userId !in viewState.userCacheInfo.map { it.id } }) { it.userId }
                .toSet()
                .forEach {
                    loadUserInfo(it)
                }

            updateState { copy(myReservations = reservations) }
        }
    }

    private fun loadAllReservations() {
        viewModelScope.launchSafe(
            onFailure = {
                Auditor.err("d", it.message ?: "", it)
            }
        ) {
            val reservations = bookings
                .allByService(serviceId)
                .map {
                    BookingDomain(
                        it.id,
                        it.userId,
                        it.serviceId,
                        it.startsAt,
                        it.endsAt,
                        it.status,
                        it.cancelReason
                    )
                }

            reservations
                .fastFilteredMap({ it.userId !in viewState.userCacheInfo.map { it.id } }) { it.userId }
                .toSet()
                .forEach {
                    loadUserInfo(it)
                }

            updateState { copy(allReservations = reservations) }
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

    private fun makeBooking(slot: Slot) {
        viewModelScope.launchSafe(
            onFailure = {
                Auditor.err("d", it.message ?: "", it)
            }
        ) {
            bookings.create(CreateBookingRequest(serviceId, slot.from))
            loadTimeSlots(slot.from.date)
            loadMyReservations()
            loadAllReservations()
        }
    }

    private fun removeAdmin(id: Uuid) {
        viewModelScope.launchSafe(
            onFailure = { Auditor.err("d", it.message ?: "", it) }
        ) { services.removeAdmin(serviceId, id); loadServiceAdmins() }
    }

    private fun addAdmins(ids: List<Uuid>) {
        viewModelScope.launchSafe(
            onFailure = { Auditor.err("d", it.message ?: "", it) }
        ) { services.addAdmin(serviceId, ids); loadServiceAdmins() }
    }

    private fun clearSearchResults() {
        updateState { copy(userSearchResult = emptyList()) }
    }

    private fun loadAvailability() {
        updateState { copy(availabilityRulesState = RemoteState.Loading) }
        viewModelScope.launchSafe(
            onFailure = {
                updateState { copy(availabilityRulesState = RemoteState.Error(it)) }
                Auditor.err("d", it.message ?: "", it)
            }
        ) {
            val rules = availability.get(serviceId)
            updateState {
                copy(
                    availabilityRules = rules,
                    availabilityRulesState = RemoteState.Success
                )
            }
        }
    }

    private fun loadAvailabilityBlock() {
        updateState { copy(availabilityRulesState = RemoteState.Loading) }
        viewModelScope.launchSafe(
            onFailure = {
                updateState { copy(availabilityRulesState = RemoteState.Error(it)) }
                Auditor.err("d", it.message ?: "", it)
            }
        ) {
            val rules = availability.getBlocks(serviceId)
            updateState {
                copy(
                    availabilityBlockRules = rules,
                    availabilityRulesState = RemoteState.Success
                )
            }
        }
    }

    private fun loadOneTimeAvailability() {
        updateState { copy(availabilityRulesState = RemoteState.Loading) }
        viewModelScope.launchSafe(
            onFailure = {
                updateState { copy(availabilityRulesState = RemoteState.Error(it)) }
                Auditor.err("d", it.message ?: "", it)
            }
        ) {
            val rules = availability.getOneTimes(serviceId)
            updateState {
                copy(
                    availabilityOneTimeRules = rules,
                    availabilityRulesState = RemoteState.Success
                )
            }
        }
    }

    private fun deleteBlockAvailability(id: Uuid) {
        viewModelScope.launchSafe(
            onFailure = { Auditor.err("d", it.message ?: "", it) }
        ) { availability.deleteOneTime(id); loadAllAvailability() }
    }

    private fun addAvailabilityBlock(viewEvent: ServiceDetailEvent.OnAddAvailabilityBlockFormConfirmed) {
        viewModelScope.launchSafe(
            onFailure = {
                Auditor.err("d", it.message ?: "", it)
            }
        ) {
            availability.createBlock(
                serviceId, CreateAvailabilityBlockRequest(
                    viewEvent.from,
                    viewEvent.to,
                    viewEvent.reason ?: ""
                )
            )

            loadAllAvailability()
        }
    }

    private fun deleteOneTimeAvailability(id: Uuid) {
        viewModelScope.launchSafe(
            onFailure = { Auditor.err("d", it.message ?: "", it) }
        ) { availability.deleteOneTime(id); loadAllAvailability() }
    }

    private fun addOneTimeAvailability(viewEvent: ServiceDetailEvent.OnAddOneTimeAvailabilityRuleFormConfirmed) {
        viewModelScope.launchSafe(
            onFailure = {
                Auditor.err("d", it.message ?: "", it)
            }
        ) {
            availability.createOneTime(
                serviceId, CreateOneTimeAvailabilityRuleRequest(
                    viewEvent.dateFrom,
                    viewEvent.dateTo,
                    viewEvent.startTime,
                    viewEvent.endTime
                )
            )

            loadAllAvailability()
        }
    }

    private fun deleteAvailability(id: Uuid) {
        viewModelScope.launchSafe(
            onFailure = { Auditor.err("d", it.message ?: "", it) }
        ) { availability.delete(id); loadAllAvailability() }
    }

    private fun addAvailability(viewEvent: ServiceDetailEvent.OnAddAvailabilityRuleFormConfirmed) {
        viewModelScope.launchSafe(
            onFailure = {
                Auditor.err("d", it.message ?: "", it)
            }
        ) {
            availability.create(
                serviceId, CreateAvailabilityRuleRequest(
                    viewEvent.daysOfWeek,
                    viewEvent.startTime,
                    viewEvent.endTime
                )
            )

            loadAllAvailability()
        }
    }

    private fun searchUsers(viewEvent: ServiceDetailEvent.OnUserSearchConfirm) {
        viewModelScope.launchSafe(
            onFailure = {
                updateState { copy(serviceState = RemoteState.Error(it)) }
                Auditor.err("d", it.message ?: "", it)
            }
        ) {
            val users = users.search(viewEvent.query).map {
                UserDomain(
                    it.id,
                    it.name,
                    it.email,
                    it.picture
                )
            }

            updateState { copy(userSearchResult = users) }
        }
    }

    private fun loadServiceAdmins() {
        viewModelScope.launchSafe(
            onFailure = {
                updateState { copy(adminsState = RemoteState.Error(it)) }
                Auditor.err("d", it.message ?: "", it)
            }
        ) {
            val admins = services.getAdmins(serviceId).map {
                UserDomain(
                    it.id,
                    it.name,
                    it.email,
                    it.picture
                )
            }

            Auditor.debug("d", authStorage.userId.get().toString())
            Auditor.debug("d", admins.map { it.id }.toString())
            val isAdmin = authStorage.userId.get() in admins.map { it.id }

            updateState {
                copy(
                    adminsState = RemoteState.Success,
                    admins = admins,
                    isAdmin = isAdmin
                )
            }

            if (isAdmin) loadAllReservations()
        }
    }

    private fun loadServiceInfo() {
        viewModelScope.launchSafe(
            onFailure = {
                updateState { copy(serviceState = RemoteState.Error(it)) }
                Auditor.err("d", it.message ?: "", it)
            }
        ) {
            val service = services.get(serviceId)
            updateState {
                copy(
                    serviceState = RemoteState.Success,
                    service = ServiceDomain(
                        serviceId,
                        service.title,
                        service.description,
                        service.durationMinutes,
                        service.price,
                        service.bufferMinutes,
                        service.isActive
                    )
                )
            }
        }
    }
}