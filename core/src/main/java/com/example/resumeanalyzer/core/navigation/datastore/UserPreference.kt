
package com.example.resumeanalyzer.core.navigation.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension property for DataStore
val Context.userDataStore by preferencesDataStore(name = "user_prefs")

// Data class for all cached user info
data class UserCache(
    val email: String? = null,
    val role: String? = null,
    val name: String? = null,
    val theme: String = "system",
    val notificationsEnabled: Boolean = true
)

object UserPreference {
    // Keys
    private val USER_EMAIL = stringPreferencesKey("user_email")
    private val USER_ROLE = stringPreferencesKey("user_role")
    private val USER_NAME = stringPreferencesKey("user_name")

    // Theme and notifications are device-level, not user-specific
    private val DEVICE_THEME = stringPreferencesKey("device_theme")
    private val DEVICE_NOTIFICATIONS = booleanPreferencesKey("device_notifications")

    // Get user details (Flow)
    fun getUser(context: Context): Flow<UserCache> =
        context.userDataStore.data.map { prefs ->
            UserCache(
                email = prefs[USER_EMAIL],
                role = prefs[USER_ROLE],
                name = prefs[USER_NAME],
                theme = prefs[DEVICE_THEME] ?: "system",
                notificationsEnabled = prefs[DEVICE_NOTIFICATIONS] ?: true
            )
        }

    // Save user details (login or profile update)
    suspend fun saveUser(context: Context, email: String, role: String, name: String? = null) {
        context.userDataStore.edit { prefs ->
            prefs[USER_EMAIL] = email
            prefs[USER_ROLE] = role
            if (name != null) prefs[USER_NAME] = name
        }
    }

    // Save theme selection (device-level, persists after logout)
    suspend fun saveTheme(context: Context, theme: String) {
        context.userDataStore.edit { prefs ->
            prefs[DEVICE_THEME] = theme
        }
    }

    // Save notification toggle (device-level, persists after logout)
    suspend fun saveNotification(context: Context, enabled: Boolean) {
        context.userDataStore.edit { prefs ->
            prefs[DEVICE_NOTIFICATIONS] = enabled
        }
    }

    // Clear only user-specific data (theme and notifications remain)
    suspend fun clearUser(context: Context) {
        context.userDataStore.edit { prefs ->
            prefs.remove(USER_EMAIL)
            prefs.remove(USER_ROLE)
            prefs.remove(USER_NAME)
            // Keep DEVICE_THEME and DEVICE_NOTIFICATIONS
        }
    }
}