package com.example.mebby.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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

        vm.modelStream.observe(viewLifecycleOwner) {
            CoroutineScope(Dispatchers.Main).launch {

                with(it.bottom) {
                    Picasso
                        .get()
                        .load(this.photo)
                        .placeholder(R.color.dark_grey_normal)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(binding.bottomCardImage)

                    binding.bottomCardNameAndAge?.text = resources.getString(
                        R.string.name_and_age,
                        this.name,
                        this.age
                    )
                }


                with(it.top) {
                    Picasso
                        .get()
                        .load(this.photo)
                        .placeholder(R.color.white)
                        .memoryPolicy(MemoryPolicy.NO_STORE)
                        .into(binding.topCardImage)

                    binding.topCardNameAndAge?.text = resources.getString(
                        R.string.name_and_age,
                        this.name,
                        this.age
                    )

                    binding.topCard?.setOnClickListener {
                        findNavController().navigateSafe(R.id.action_swipeFragment_to_profileFragmentSwipe)
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
