package app.what.reservation.data.local

import androidx.compose.ui.graphics.Color
import app.what.reservation.core.foundation.data.settings.Named
import app.what.reservation.core.foundation.data.settings.Preference
import app.what.reservation.core.foundation.data.settings.PreferenceStorage
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlin.uuid.ExperimentalUuidApi

@kotlinx.serialization.Serializable
enum class ThemeType(override val displayName: String) : Named {
    Light("Светлая"),
    Dark("Тёмная"),
    System("Системная")
}

@Serializable
enum class ThemeStyle(override val displayName: String) : Named {
    Default("По умолчанию"),
    Material("Material"),
    CustomColor("Свой цвет")
}


@OptIn(ExperimentalUuidApi::class)
class AppValues(prefs: Preference) : PreferenceStorage(prefs) {
    val isFirstLaunch = createValue(
        "is_first_launch", true, Boolean.serializer(),
        "Первый запуск", "Отслеживание первого запуска приложения"
    )

    val themeType = createValue(
        "theme_type", ThemeType.System, ThemeType.serializer(),
        "Тип темы", "Режим темы: светлая, темная или системная"
    )

    val themeStyle = createValue(
        "theme_style", ThemeStyle.Default, ThemeStyle.serializer(),
        "Стиль темы", "Визуальный стиль интерфейса"
    )

    val themeColor = createValue(
        "theme_color", Color(0xFF94FF28).value, ULong.serializer(),
        "Цвет темы", "Основной цвет оформления приложения"
    )

    val isAnalyticsEnabled = createValue(
        "is_analytics_enabled", true, Boolean.serializer(),
        "Анализ пользования", "Разрешите собирать анонимную статистику пользования"
    )

    val thePolicy = createValue(
        "the_policy", null, String.serializer(),
        "Политика конфиденциальности", "Ознакомлены с политикой и условиями пользования"
    )

    val devSettingsUnlocked = createValue(
        "dev_settings_unlocked", false, Boolean.serializer(),
        "Настройки разработчика", "Доступ к настройкам разработчика"
    )

    val devPanelEnabled = createValue(
        "dev_panel_enabled", false, Boolean.serializer(),
        "Дебаг панель", "Панель для отслеживания действий в приложении"
    )

    val debugMode = createValue(
        "debug_mode", false, Boolean.serializer(),
        "Режим отладки", "Включение дополнительной информации для разработки"
    )
}