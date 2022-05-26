package com.codevalley.bostatakehomeassignment.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import coil.load
import com.codevalley.bostatakehomeassignment.R
import com.codevalley.bostatakehomeassignment.databinding.FragmentPhotoViewerDialogBinding
import com.codevalley.bostatakehomeassignment.utils.ImageUtils
import com.codevalley.bostatakehomeassignment.utils.TouchImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PhotoViewerDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentPhotoViewerDialogBinding
    private var imageUrl: String? = null
    private val imageUtils by lazy {
        ImageUtils(requireActivity())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.fullScreenDialog)
        imageUrl = arguments?.getString("image")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentPhotoViewerDialogBinding.inflate(layoutInflater)
        this.binding = binding
        initUi()
        initEventDriven()
        return binding.root
    }

    private fun initEventDriven() {
        binding.ivShare.setOnClickListener {
            share()
        }
    }

    private fun initUi() {
        binding.ivImage.loadImage()
    }

    private fun TouchImageView.loadImage() {
        load(imageUrl)
    }

    private fun share() {
        lifecycleScope.launch(Dispatchers.IO) {
            //coil converts url into bitmap
            val bitmap = imageUtils.convertUrlIntoBitmap(imageUrl)
            if (bitmap != null) {
                val i = Intent(Intent.ACTION_SEND)
                i.type = "image/*"
                i.putExtra(Intent.EXTRA_STREAM, imageUtils.getLocalBitmapUri(bitmap))
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(Intent.createChooser(i, "Share Image"))
            }
        }
    }


}