package com.codevalley.bostatakehomeassignment.view.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.codevalley.bostatakehomeassignment.R
import com.codevalley.bostatakehomeassignment.databinding.ActivityAlbumDetailsScreenBinding
import com.codevalley.bostatakehomeassignment.model.photosModel.PhotosModelItem
import com.codevalley.bostatakehomeassignment.utils.BaseActivity
import com.codevalley.bostatakehomeassignment.utils.NetworkHelper
import com.codevalley.bostatakehomeassignment.utils.Status
import com.codevalley.bostatakehomeassignment.view.adapters.ImagesAdapter
import com.codevalley.bostatakehomeassignment.view.fragments.PhotoViewerDialogFragment
import com.codevalley.bostatakehomeassignment.viewmodel.AlbumDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class AlbumDetailsScreenActivity : BaseActivity<ActivityAlbumDetailsScreenBinding>() {

    private val albumDetailsViewModel: AlbumDetailsViewModel by viewModels()
    private lateinit var imagesAdapter: ImagesAdapter
    private var albumId = 0
    private val dataList: MutableList<PhotosModelItem> by lazy {
        ArrayList<PhotosModelItem>()
    }

    override fun getViewBinding() = ActivityAlbumDetailsScreenBinding.inflate(layoutInflater)

    override fun setUpViews() {
        initUi()
        initEventDriven()
    }

    private fun initUi() {
        initRecycler()
        binding.tvTitle.text = intent.getStringExtra("title")
        albumId = intent.getIntExtra("id", 0)
        if (NetworkHelper(this).isNetworkConnected()) {
            albumDetailsViewModel.getPhotos(albumId.toString())
            getPhotosResults()
        }else{
            Toast.makeText(this, getString(R.string.pleaseMakeSureToConnectToInternet), Toast.LENGTH_SHORT).show()
        }
    }

    private fun initEventDriven() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                search()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun search() {
        if (!TextUtils.isEmpty(binding.etSearch.text.toString())) {
            val filteredList = dataList.filter { it.title.contains(binding.etSearch.text.toString()) }
            imagesAdapter.addAll(filteredList)
        } else {
            imagesAdapter.addAll(dataList)
        }
    }

    private fun getPhotosResults() {
        lifecycleScope.launchWhenStarted {
            albumDetailsViewModel.stateFlowPhotos.collect { it ->
                when (it.status) {
                    Status.LOADING -> {
                        binding.pbLoading.visibility = View.VISIBLE
                    }
                    Status.SUCCESS -> {
                        if (it.data?.code() == 200) {
                            binding.pbLoading.visibility = View.GONE
                            val response = it.data.body()
                            if (response != null) {
                                imagesAdapter.addAll(response)
                                response.map { dataList.add(it) }
                            }
                        }
                    }
                    Status.ERROR -> {
                        binding.pbLoading.visibility = View.GONE
                    }
                }

            }
        }
    }

    private fun initRecycler() {
        imagesAdapter =
            ImagesAdapter(ImagesAdapter.OnClickListener { data, position -> opePhotoViewer(data,position) })
        val gridLayoutManager = GridLayoutManager(this, 3)
        binding.rvImages.layoutManager = gridLayoutManager
        binding.rvImages.adapter = imagesAdapter
    }

    //opens a fullscreen dialogFragment
    private fun opePhotoViewer(data: ArrayList<PhotosModelItem>,position: Int){
        val photoViewerDialogFragment = PhotoViewerDialogFragment()
        val bundle = Bundle()
        bundle.putString("image", data[position].url)
        photoViewerDialogFragment.arguments = bundle
        photoViewerDialogFragment.show(supportFragmentManager, "PhotoViewer")

    }
}