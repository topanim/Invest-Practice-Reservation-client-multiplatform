package app.what.reservation.ui.theme

object Strings {

    data class Values(
        val serviceCreatingFormTitle: String,
        val serviceCreatingFormValueTitle: String,
        val serviceCreatingFormValueDescription: String,
        val serviceCreatingFormValueDurationMinutes: String,
        val serviceCreatingFormValuePrice: String,
        val serviceCreatingFormConfirm: String,
        val greetings: String,
        val signWithGoogle: String,
        val logout: String,
        val refresh: String,
        val admins: String,
        val add: String,
        val availabilityRules: String,
    )

    val RU = Values(
        greetings = "Добро\nпожаловать",
        signWithGoogle = "Войти с Google",
        logout = "Выйти из аккаунта",
        serviceCreatingFormTitle = "Создание услуги",
        serviceCreatingFormValueTitle = "Заголовок",
        serviceCreatingFormValueDescription = "Описание",
        serviceCreatingFormValueDurationMinutes = "Длительность (в мин.)",
        serviceCreatingFormValuePrice = "Цена",
        serviceCreatingFormConfirm = "Создать",
        refresh = "Обновить",
        admins = "Администраторы",
        add = "Добавить",
        availabilityRules = "Правила доступности"
    )

    val Default = RU
}


