package com.codevalley.bostatakehomeassignment.network

import com.codevalley.bostatakehomeassignment.model.albumsModel.AlbumsModel
import com.codevalley.bostatakehomeassignment.model.photosModel.PhotosModel
import com.codevalley.bostatakehomeassignment.model.usersModel.UsersModel
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET(AppUrls.users)
    suspend fun users(): Response<UsersModel>

    @GET(AppUrls.albums)
    suspend fun albums(
        @Query("userId") userId: String
    ): Response<AlbumsModel>

    @GET(AppUrls.photos)
    suspend fun photos(
        @Query("albumId") albumId: String
    ): Response<PhotosModel>
}