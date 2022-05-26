package com.codevalley.bostatakehomeassignment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codevalley.bostatakehomeassignment.model.photosModel.PhotosModel
import com.codevalley.bostatakehomeassignment.network.MainRepository
import com.codevalley.bostatakehomeassignment.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AlbumDetailsViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : ViewModel() {

    //photosFlow
    private val _mutableStateFlowPhotos: MutableStateFlow<Resource<Response<PhotosModel>>> =
        MutableStateFlow(Resource.loading(null))
    val stateFlowPhotos: StateFlow<Resource<Response<PhotosModel>>> =
        _mutableStateFlowPhotos

    fun getPhotos(
        albumId: String
    ) {
        viewModelScope.launch(Dispatchers.IO)
        {
            _mutableStateFlowPhotos.emit(Resource.loading(data = null))
            try {
                _mutableStateFlowPhotos.emit(Resource.success(data = mainRepository.photos(albumId)))
            } catch (exception: Exception) {
                _mutableStateFlowPhotos.emit(
                    Resource.error(data = null, message = exception.message ?: "Something went wrong.!"))
            }
        }
    }
}