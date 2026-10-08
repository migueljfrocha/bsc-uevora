package pt.uevora.spacehub.ui.screens.imagedetail

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import pt.uevora.spacehub.data.model.NasaImage

/**
 * Stores the image selected for the detail screen.
 */
class ImageDetailViewModel : ViewModel() {
    private val _selectedImage = MutableStateFlow<NasaImage?>(null)
    val selectedImage: StateFlow<NasaImage?> = _selectedImage.asStateFlow()

    /**
     * Updates the selected image shown in the detail screen.
     */
    fun selectImage(image: NasaImage) {
        _selectedImage.value = image
    }
}