package com.rahees.unicalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.rahees.unicalc.ui.navigation.NavGraph
import com.rahees.unicalc.ui.theme.UniCalcTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by dataStore.data.map { prefs ->
                prefs[stringPreferencesKey("theme_mode")] ?: "system"
            }.collectAsState(initial = "system")

            val isDarkTheme = when (themeMode) {
                "light" -> false
                "dark" -> true
                else -> null
            }

            UniCalcTheme(
                darkTheme = isDarkTheme
                    ?: androidx.compose.foundation.isSystemInDarkTheme()
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph()
                }
            }
        }
    }
}
