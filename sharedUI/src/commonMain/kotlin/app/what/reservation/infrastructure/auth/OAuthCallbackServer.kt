package app.what.reservation.infrastructure.auth

import io.ktor.server.cio.CIO
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration.Companion.minutes
import kotlin.uuid.Uuid


class OAuthCallbackServer {
    companion object {
        private const val PORT: Int = 46573
    }

    private var sessionDeferred = CompletableDeferred<Uuid>()
    private var server: EmbeddedServer<*, *>? = null

    val callbackUrl: String = "http://127.0.0.1:$PORT/callback"

    fun start() {
        if (server != null) return
        sessionDeferred = CompletableDeferred()
        server = buildServer().apply {
            start()
        }
    }

    fun stop() {
        server?.stop()
        server = null
    }

    suspend fun awaitSessionId(): Uuid {
        return withTimeout(1.minutes) {
            sessionDeferred.await()
        }
    }

    fun buildServer() = embeddedServer(CIO, port = PORT) {
        routing {
            get("/callback") {
                val sessionId = runCatching {
                    call.request.queryParameters["sessionId"]?.let(Uuid::parse)
                }.getOrNull()

                if (sessionId != null && !sessionDeferred.isCompleted) {
                    sessionDeferred.complete(sessionId)
                }

                call.respondText(
                    "Authorization completed. You can close this window."
                )
            }
        }
    }
}