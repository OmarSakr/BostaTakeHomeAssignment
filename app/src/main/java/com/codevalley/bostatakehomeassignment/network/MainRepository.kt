package com.codevalley.bostatakehomeassignment.network

import com.codevalley.bostatakehomeassignment.model.albumsModel.AlbumsModel
import com.codevalley.bostatakehomeassignment.model.photosModel.PhotosModel
import com.codevalley.bostatakehomeassignment.model.usersModel.UsersModel
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper: ApiHelper) {
    suspend fun users(): Response<UsersModel> = apiHelper.users()

    suspend fun albums(userId: String): Response<AlbumsModel> = apiHelper.albums(userId)

    suspend fun photos(albumId: String): Response<PhotosModel> = apiHelper.photos(albumId)
}