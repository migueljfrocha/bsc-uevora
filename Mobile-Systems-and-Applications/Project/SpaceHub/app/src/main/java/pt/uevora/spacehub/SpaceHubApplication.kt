package pt.uevora.spacehub

import android.app.Application
import android.util.Log
import pt.uevora.spacehub.data.AppContainer
import pt.uevora.spacehub.data.DefaultAppContainer

private const val TAG = "SpaceHubApplication"

/**
 * Application class that initializes app-wide dependencies.
 */
class SpaceHubApplication : Application() {

    /**
     * App-wide dependency container.
     */
    lateinit var container: AppContainer

    /**
     * Creates the dependency container when the application starts.
     */
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Application onCreate Called")
        container = DefaultAppContainer()
    }
}