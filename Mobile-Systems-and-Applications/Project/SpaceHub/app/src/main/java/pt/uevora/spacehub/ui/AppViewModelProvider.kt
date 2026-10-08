package pt.uevora.spacehub.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import pt.uevora.spacehub.SpaceHubApplication
import pt.uevora.spacehub.ui.screens.apod.ApodViewModel
import pt.uevora.spacehub.ui.screens.epic.EpicViewModel
import pt.uevora.spacehub.ui.screens.home.HomeViewModel
import pt.uevora.spacehub.ui.screens.imagedetail.ImageDetailViewModel
import pt.uevora.spacehub.ui.screens.imagesearch.ImageSearchViewModel

/**
 * Provides a Factory to create instances of the various ViewModels.
 * The Factory is used to retrieve the NasaRepository from the Application class and pass it to the ViewModels.
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(spaceHubApplication().container.nasaRepository)
        }
        initializer {
            ApodViewModel(spaceHubApplication().container.nasaRepository)
        }
        initializer {
            ImageSearchViewModel(spaceHubApplication().container.nasaRepository)
        }
        initializer {
            ImageDetailViewModel()
        }
        initializer {
            EpicViewModel(spaceHubApplication().container.nasaRepository)
        }
    }
}

/**
 * Extension function to retrieve the SpaceHubApplication instance from the CreationExtras.
 * This allows us to access the NasaRepository from the Application class when creating ViewModels.
 */
private fun CreationExtras.spaceHubApplication(): SpaceHubApplication {
    return (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SpaceHubApplication)
}
