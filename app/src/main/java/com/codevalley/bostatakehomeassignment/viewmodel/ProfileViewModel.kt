package com.codevalley.bostatakehomeassignment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codevalley.bostatakehomeassignment.model.albumsModel.AlbumsModel
import com.codevalley.bostatakehomeassignment.model.usersModel.UsersModel
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
class ProfileViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : ViewModel() {

    //usersFlow
    private val _mutableStateFlowUsers: MutableStateFlow<Resource<Response<UsersModel>>> =
        MutableStateFlow(Resource.loading(null))
    val stateFlowUsers: StateFlow<Resource<Response<UsersModel>>> = _mutableStateFlowUsers

    //albumsFlow
    private val _mutableStateFlowAlbums: MutableStateFlow<Resource<Response<AlbumsModel>>> =
        MutableStateFlow(Resource.loading(null))
    val stateFlowAlbums: StateFlow<Resource<Response<AlbumsModel>>> =
        _mutableStateFlowAlbums

    fun getUsers(
    ) {
        viewModelScope.launch(Dispatchers.IO)
        {
            _mutableStateFlowUsers.emit(Resource.loading(data = null))
            try {
                _mutableStateFlowUsers.emit(Resource.success(data = mainRepository.users()))
            } catch (exception: Exception) {
                _mutableStateFlowUsers.emit(
                    Resource.error(
                        data = null,
                        message = exception.message ?: "Something went wrong.!"
                    )
                )
            }
        }
    }

    fun getAlbums(userId: Int) {
        viewModelScope.launch(Dispatchers.IO)
        {
            _mutableStateFlowAlbums.emit(Resource.loading(data = null))
            try {
                _mutableStateFlowAlbums.emit(Resource.success(data = mainRepository.albums(userId.toString())))
            } catch (exception: Exception) {
                _mutableStateFlowAlbums.emit(
                    Resource.error(data = null, message = exception.message ?: "Something went wrong.!")
                )
            }
        }
    }
}