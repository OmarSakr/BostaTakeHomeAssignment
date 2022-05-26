package com.codevalley.bostatakehomeassignment.network

import com.codevalley.bostatakehomeassignment.model.albumsModel.AlbumsModel
import com.codevalley.bostatakehomeassignment.model.photosModel.PhotosModel
import com.codevalley.bostatakehomeassignment.model.usersModel.UsersModel
import retrofit2.Response
import javax.inject.Inject

class ApiHelper @Inject constructor(private val apiService: ApiService) {

    suspend fun users(): Response<UsersModel> = apiService.users()

    suspend fun albums(userId: String): Response<AlbumsModel> = apiService.albums(userId)

    suspend fun photos(albumId: String): Response<PhotosModel> = apiService.photos(albumId)
}