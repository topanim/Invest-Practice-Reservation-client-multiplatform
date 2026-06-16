package app.what.reservation.core.foundation.services


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import app.what.reservation.utils.AppDispatchers
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.concurrent.atomics.AtomicLong
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.incrementAndFetch
import kotlin.time.Clock

enum class LogLevel(val emoji: String, val color: Color) {
    DEBUG("🐛", Color(0xFF4CAF50)),    // Зеленый
    INFO("🧢", Color(0xFF2196F3)),     // Синий
    WARNING("🍣", Color(0xFFFF9800)),  // Оранжевый
    ERROR("🌶️", Color(0xFFF44336)),    // Красный
    CRITICAL("🪻", Color(0xFF9C27B0))  // Фиолетовый
}

data class LogEntry(
    val id: Long,
    val timestamp: Long = Clock.System.now().toEpochMilliseconds(),
    val level: LogLevel,
    val tag: String,
    val message: String,
    val throwable: Throwable? = null
)

class AppLogger {

    companion object {
        private const val MAX_MEMORY_LOGS = 500

        val Auditor: AppLogger = AppLogger()
    }

    @OptIn(ExperimentalAtomicApi::class)
    private val atomicId = AtomicLong(0)

    var isLoggingPaused by mutableStateOf(false)
        private set

    private val _logFlow = MutableStateFlow<List<LogEntry>>(emptyList())
    val logs = _logFlow.asStateFlow()

    @Composable
    fun collectLogs() = logs.collectAsState()

    private val scope = CoroutineScope(AppDispatchers.IO + SupervisorJob())

    fun setIsLoggingPaused(value: Boolean) {
        isLoggingPaused = value
    }

    @OptIn(ExperimentalAtomicApi::class)
    fun log(level: LogLevel, tag: String, message: String, throwable: Throwable? = null) {
        val entry = LogEntry(
            id = atomicId.incrementAndFetch(),
            level = level,
            tag = tag,
            message = message,
            throwable = throwable
        )

        val logcatPriority = when (level) {
            LogLevel.DEBUG -> Severity.Debug
            LogLevel.INFO -> Severity.Info
            LogLevel.WARNING -> Severity.Warn
            else -> Severity.Error
        }

        Logger.log(logcatPriority, tag, throwable, message)

        if (!isLoggingPaused) {
            _logFlow.update { current ->
                (current + entry).takeLast(MAX_MEMORY_LOGS)
            }
        }
    }

    fun clearLogs() {
        _logFlow.value = emptyList()
    }

    fun debug(tag: String, msg: String) = log(LogLevel.DEBUG, tag, msg)
    fun info(tag: String, msg: String) = log(LogLevel.INFO, tag, msg)
    fun warn(tag: String, msg: String) = log(LogLevel.WARNING, tag, msg)
    fun err(tag: String, msg: String, t: Throwable? = null) = log(LogLevel.ERROR, tag, msg, t)
    fun critic(tag: String, msg: String, t: Throwable? = null) = log(LogLevel.CRITICAL, tag, msg, t)
}