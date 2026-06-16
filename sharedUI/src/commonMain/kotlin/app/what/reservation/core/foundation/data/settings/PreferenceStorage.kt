package app.what.reservation.core.foundation.data.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.vector.ImageVector
import app.what.reservation.core.foundation.ui.useState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

interface Named {
    val displayName: String
}

interface Preference {
    fun registerOnPreferenceChangeListener(block: (key: String?) -> Unit)
    fun getString(key: String, default: String?): String?
    fun putString(key: String, value: String?): Unit
}

abstract class PreferenceStorage(protected val prefs: Preference) {
    private val preferencesFlow = MutableSharedFlow<String>(extraBufferCapacity = 1)

    init {
        prefs.registerOnPreferenceChangeListener { key ->
            key?.let { preferencesFlow.tryEmit(it) }
        }
    }

    fun <T : Any> createValue(
        key: String,
        defaultValue: T?,
        serializer: KSerializer<T>,
        title: String = "",
        description: String? = null,
        icon: ImageVector? = null
    ): Value<T> =
        Value(prefs, preferencesFlow, key, defaultValue, serializer, title, description, icon)

    class Value<T : Any>(
        private val prefs: Preference,
        private val preferencesFlow: MutableSharedFlow<String>,
        val key: String,
        private val defaultValue: T?,
        private val serializer: KSerializer<T>,
        val title: String,
        val description: String? = null,
        val icon: ImageVector? = null,
    ) {
        fun get(): T? = prefs
            .getString(key, null)
            ?.let { Json.decodeFromString(serializer, it) }
            ?: defaultValue

        fun set(value: T?) {
            prefs.putString(
                key,
                if (value == null) null else Json.encodeToString(serializer, value)
            )
            preferencesFlow.tryEmit(key)
        }

        fun observe(): Flow<T?> = preferencesFlow
            .filter { it == key }
            .map { get() }
            .onStart { emit(get()) }
            .distinctUntilChanged()

        @Composable
        fun collect() = observe().collectAsState(get())

        @Composable
        fun <R> collect(block: (T?) -> R): State<R?> {
            val state = useState<R?>(null)

            LaunchedEffect(Unit) {
                observe().collect { state.value = block(it) }
            }
            return state
        }
    }
}