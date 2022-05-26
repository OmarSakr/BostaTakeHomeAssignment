package com.codevalley.bostatakehomeassignment.view.activities

import android.content.Intent
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codevalley.bostatakehomeassignment.R
import com.codevalley.bostatakehomeassignment.databinding.ActivityProfileBinding
import com.codevalley.bostatakehomeassignment.utils.BaseActivity
import com.codevalley.bostatakehomeassignment.utils.NetworkHelper
import com.codevalley.bostatakehomeassignment.utils.Status
import com.codevalley.bostatakehomeassignment.view.adapters.AlbumsAdapter
import com.codevalley.bostatakehomeassignment.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async

@AndroidEntryPoint
class ProfileActivity : BaseActivity<ActivityProfileBinding>() {

    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var albumsAdapter: AlbumsAdapter

    override fun getViewBinding() = ActivityProfileBinding.inflate(layoutInflater)

    override fun setUpViews() {
        initUi()
    }

    private fun initUi() {
        initRecycler()
        if (NetworkHelper(this).isNetworkConnected()){
            profileViewModel.getUsers()
            getUserResult()
        }else{
            Toast.makeText(this, getString(R.string.pleaseMakeSureToConnectToInternet), Toast.LENGTH_SHORT).show()
        }
    }

    private fun getUserResult() {
        lifecycleScope.launchWhenStarted {
            profileViewModel.stateFlowUsers.collect {
                when (it.status) {
                    Status.LOADING -> {
                        binding.pbLoading.visibility = VISIBLE
                    }
                    Status.SUCCESS -> {
                        if (it.data?.code() == 200) {
                            binding.pbLoading.visibility = GONE
                            val response = it.data.body()
                            if (response != null) {
                                val randomIndex = (0 until response.size).random()
                                val data = response[randomIndex]
                                binding.tvUserName.text = data.name
                                val address = data.address
                                ("${address.street},${address.suite},${address.city},${address.zipcode}").also { text ->
                                    binding.tvUserAddress.text = text
                                }
                                val userId = async { data.id }
                                profileViewModel.getAlbums(userId.await())
                                getAlbumResult()
                            }
                        }
                    }
                    Status.ERROR -> {
                        binding.pbLoading.visibility = GONE
                    }
                }

            }
        }
    }

    private fun getAlbumResult() {
        lifecycleScope.launchWhenStarted {
            profileViewModel.stateFlowAlbums.collect {
                when (it.status) {
                    Status.LOADING -> {
                        binding.pbLoading.visibility = VISIBLE
                    }
                    Status.SUCCESS -> {
                        if (it.data?.code() == 200) {
                            binding.pbLoading.visibility = GONE
                            val response = it.data.body()
                            if (response != null) {
                                albumsAdapter.addAll(response)
                            }
                        }
                    }
                    Status.ERROR -> {
                        binding.pbLoading.visibility = GONE
                    }
                }

            }
        }
    }

    private fun initRecycler() {
        albumsAdapter =
            AlbumsAdapter(
                AlbumsAdapter.OnClickListener { data, position ->
                    val intent = Intent(this, AlbumDetailsScreenActivity::class.java)
                    intent.putExtra("title", data[position].title)
                    intent.putExtra("id", data[position].id)
                    startActivity(intent)
                })
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvAlbums.layoutManager = linearLayoutManager
        binding.rvAlbums.adapter = albumsAdapter
    }
}