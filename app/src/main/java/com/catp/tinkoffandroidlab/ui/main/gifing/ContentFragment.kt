package com.catp.tinkoffandroidlab.ui.main.gifing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.catp.tinkoffandroidlab.R
import com.catp.tinkoffandroidlab.databinding.ImageFragmentBinding
import com.catp.tinkoffandroidlab.ui.main.data.Item
import com.catp.tinkoffandroidlab.ui.main.visibleIf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

class ContentFragment : Fragment() {

    companion object {
        fun newInstance(item: Item) = ContentFragment().apply {
            arguments = bundleOf(ITEM to item)
        }

        private const val ITEM = "item"
    }

    private val viewBinding: ImageFragmentBinding by viewBinding()
    private lateinit var viewModel: ContentViewModel

    private val item by lazy {
        arguments?.get(ITEM) as Item
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.image_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(ContentViewModel::class.java)

        viewModel.imageLoadChannel
            .receiveAsFlow()
            .onEach { loadImage() }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.imageLoadFailStatus
            .onEach { viewBinding.btnRetry.visibleIf { it } }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewBinding.tvTitle.text = item.description
        viewBinding.btnRetry.setOnClickListener { viewModel.onRetryImageLoad() }
        loadImage()
    }

    private fun loadImage() {
        Glide.with(requireContext())
            .asGif()
            .load(item.gifURL)
            .error(R.drawable.ic_baseline_error_24)
            .placeholder(R.drawable.ic_baseline_cloud_download_24)
            .listener(object : RequestListener<GifDrawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    viewModel.onLoadFailed()
                    return false
                }

                override fun onResourceReady(
                    resource: GifDrawable?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

            })
            .centerCrop()
            .into(viewBinding.ivContent)
    }
}
