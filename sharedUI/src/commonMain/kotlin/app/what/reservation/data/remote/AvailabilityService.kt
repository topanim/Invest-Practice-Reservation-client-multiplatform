package app.what.reservation.data.remote

import app.what.reservation.data.local.AuthSessionManager
import app.what.reservation.data.remote.dto.CreateAvailabilityBlockRequest
import app.what.reservation.data.remote.dto.CreateAvailabilityRuleRequest
import app.what.reservation.data.remote.dto.CreateOneTimeAvailabilityRuleRequest
import app.what.reservation.domain.models.AvailabilityBlock
import app.what.reservation.domain.models.AvailabilityRule
import app.what.reservation.domain.models.OneTimeAvailabilityRule
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.uuid.Uuid

class AvailabilityService(
    private val client: HttpClient,
    private val apiSessionManager: AuthSessionManager
) : ApiService(apiSessionManager) {
    suspend fun get(serviceId: Uuid) = authCall {
        client.get("$baseUrl/services/availability") {
            bearerAuth(apiSessionManager.getAccessToken())
            parameter("serviceId", serviceId)
        }
    }.body<List<AvailabilityRule>>()

    suspend fun create(serviceId: Uuid, data: CreateAvailabilityRuleRequest) = authCall {
        client.post("$baseUrl/services/availability") {
            bearerAuth(apiSessionManager.getAccessToken())
            parameter("serviceId", serviceId)
            contentType(ContentType.Application.Json)
            setBody(data)
        }
    }.body<AvailabilityRule>()

    suspend fun delete(id: Uuid) {
        authCall {
            client.delete("$baseUrl/services/availability") {
                bearerAuth(apiSessionManager.getAccessToken())
                parameter("id", id)
            }
        }
    }

    suspend fun getOneTimes(serviceId: Uuid) = authCall {
        client.get("$baseUrl/services/availability/onetime") {
            bearerAuth(apiSessionManager.getAccessToken())
            parameter("serviceId", serviceId)
        }
    }.body<List<OneTimeAvailabilityRule>>()

    suspend fun createOneTime(serviceId: Uuid, data: CreateOneTimeAvailabilityRuleRequest) =
        authCall {
            client.post("$baseUrl/services/availability/onetime") {
                bearerAuth(apiSessionManager.getAccessToken())
                parameter("serviceId", serviceId)
                contentType(ContentType.Application.Json)
                setBody(data)
            }
        }.body<OneTimeAvailabilityRule>()

    suspend fun deleteOneTime(id: Uuid) {
        authCall {
            client.delete("$baseUrl/services/availability/onetime") {
                bearerAuth(apiSessionManager.getAccessToken())
                parameter("id", id)
            }
        }
    }

    suspend fun getBlocks(serviceId: Uuid) = authCall {
        client.get("$baseUrl/services/availability/blocks") {
            bearerAuth(apiSessionManager.getAccessToken())
            parameter("serviceId", serviceId)
        }
    }.body<List<AvailabilityBlock>>()

    suspend fun createBlock(serviceId: Uuid, data: CreateAvailabilityBlockRequest) = authCall {
        client.post("$baseUrl/services/availability/blocks") {
            bearerAuth(apiSessionManager.getAccessToken())
            parameter("serviceId", serviceId)
            contentType(ContentType.Application.Json)
            setBody(data)
        }
    }.body<AvailabilityBlock>()

    suspend fun deleteBlock(id: Uuid) {
        authCall {
            client.delete("$baseUrl/services/availability/blocks") {
                bearerAuth(apiSessionManager.getAccessToken())
                parameter("id", id)
            }
        }
    }
}