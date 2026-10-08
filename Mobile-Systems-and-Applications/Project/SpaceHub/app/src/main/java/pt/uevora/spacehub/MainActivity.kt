package pt.uevora.spacehub

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import pt.uevora.spacehub.ui.SpaceHubApp

private const val TAG = "MainActivity"

/**
 * Main entry point activity that hosts the Compose UI and logs lifecycle events.
 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class MainActivity : ComponentActivity() {

    /**
     * Initializes Compose content, computes the window size class, and starts the app UI.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate Called")

        enableEdgeToEdge()
        setContent {
            // Determine window size class to drive adaptive navigation/layout.
            val windowSize = calculateWindowSizeClass(this)
            SpaceHubApp(
                windowSize = windowSize.widthSizeClass
            )
        }
    }


    /**
     * Logs when the activity becomes visible to the user.
     */
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart Called")
    }


    /**
     * Logs when the activity is in the foreground and interactive.
     */
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume Called")
    }


    /**
     * Logs when the activity is restarting after being stopped.
     */
    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart Called")
    }


    /**
     * Logs when the activity loses foreground focus.
     */
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause Called")
    }


    /**
     * Logs when the activity is no longer visible.
     */
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop Called")
    }


    /**
     * Logs when the activity is being destroyed.
     */
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy Called")
    }
}

