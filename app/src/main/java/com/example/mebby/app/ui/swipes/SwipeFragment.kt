package com.example.mebby.app.ui.swipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mebby.R
import com.example.mebby.app.viewModels.SwipeViewModel
import com.example.mebby.databinding.FragmentSwipeBinding
import com.example.mebby.extensions.navigateSafe
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SwipeFragment : Fragment() {
    // ViewBinding
    private var _binding: FragmentSwipeBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val vm by viewModels<SwipeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSwipeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.swipeMotionLayout?.setTransitionListener(object : TransitionAdapter() {
            override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                when (currentId) {
                    R.id.offScreenPass,
                    R.id.offScreenLike -> {
                        motionLayout.progress = 0f
                        motionLayout.setTransition(R.id.rest, R.id.like)
                        vm.swipe()
                    }
                }
            }
        })

        binding.filtersButton?.setOnClickListener {
            findNavController().navigateSafe(R.id.action_swipeFragment_to_swipeFilterFragment)
        }

        vm.modelStream.observe(viewLifecycleOwner) {
            CoroutineScope(Dispatchers.Main).launch {

                with(it.bottomCard) {
                    Glide
                        .with(requireContext())
                        .load(this.picture.imageUrl)
                        .apply(RequestOptions.overrideOf(960, 960))
                        .placeholder(R.color.dark_grey_normal)
                        .centerCrop()
                        .into(binding.bottomCardImage as ImageView)

                    binding.bottomCardNameAndAge?.text = resources.getString(R.string.name_and_age, this.username, this.age)
                    binding.bottomCardAbout?.text = this.about
                    binding.bottomCardCity?.text = resources.getString(R.string.city_and_country, this.city.city, this.city.country)
                }


                with(it.topCard) {
                    Glide
                        .with(requireContext())
                        .load(this.picture.imageUrl)
                        .apply(RequestOptions.overrideOf(960, 960))
                        .placeholder(R.color.dark_grey_normal)
                        .centerCrop()
                        .into(binding.topCardImage as ImageView)

                    binding.topCardNameAndAge?.text = resources.getString(R.string.name_and_age, this.username, this.age)
                    binding.topCardAbout?.text = this.about
                    binding.topCardCity?.text = resources.getString(R.string.city_and_country, this.city.city, this.city.country)

                    binding.topCard?.setOnClickListener {
                        findNavController().navigateSafe(R.id.action_swipeFragment_to_profileFragmentSwipe, bundleOf("UserId" to this.userId))
                    }
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
