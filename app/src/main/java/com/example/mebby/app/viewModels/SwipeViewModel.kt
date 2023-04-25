package com.example.mebby.app.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mebby.domain.models.SwipeCardModel
import com.example.mebby.domain.models.SwipeModel
import com.squareup.picasso.Cache
import com.squareup.picasso.Picasso
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class SwipeViewModel @Inject constructor(): ViewModel() {
    private val mutableModelStream = MutableLiveData<SwipeModel>()
    val modelStream: LiveData<SwipeModel> get() = mutableModelStream

    val data: List<SwipeCardModel> = arrayListOf(
        SwipeCardModel(
            name = "Alexandra",
            age = 23,
            photo = "https://img.freepik.com/free-photo/cheerful-caucasian-girl-smiling-on-brown-wall-charming-female-model-with-black-hair_197531-14081.jpg?w=1380&t=st=1680466367~exp=1680466967~hmac=0fa4cc5cd67006611b5a84df7af05846a299e966d4b6ea079d7d3d64090ce1c5"
        ),
        SwipeCardModel(
            name = "Victoria",
            age = 22,
            photo = "https://img.freepik.com/free-photo/charming-woman-with-curly-hairstyle-and-make-up-in-studio_7502-8928.jpg?w=1380&t=st=1680466190~exp=1680466790~hmac=6098a71edd57be6ddc44c24f7c4b0420c95d3e243fb00080647ad9a78f8c3684"
        ),
        SwipeCardModel(
            name = "Masha",
            age = 24,
            photo = "https://img.freepik.com/free-photo/cute-young-girl-with-dark-wavy-hairstyle-and-bright-makeup-silk-dress-black-jacket-holding-sunglasses-in-hands-and-looking-away-against-beige-building-wall_197531-24462.jpg?w=1380&t=st=1680466285~exp=1680466885~hmac=40e337f466103d50e13a9338b0f41a151cdd12aa60c9ee0e27210f49f2358e65"
        ),
        SwipeCardModel(
            name = "Ekaterina",
            age = 25,
            photo = "https://img.freepik.com/free-photo/attractive-lady-in-red-sweater-walks-along-avenue-with-sakura-and-drinks-coffee-beautiful-woman-in-beret-smiling-and-enjoying-tea-outside_197531-17871.jpg?w=1380&t=st=1680466243~exp=1680466843~hmac=13883f56da26259339263255e449c462877c5b0e4f3d261dd8cb743e34c21985"
        ),
        SwipeCardModel(
            name = "Taya",
            age = 21,
            photo = "https://img.freepik.com/premium-photo/adorable-blonde-model-wearing-fashionable-red-dress-posing-on-the-bridge-in-the-evening_172420-8056.jpg?w=740"
        ),
        SwipeCardModel(
            name = "Anastasia",
            age = 29,
            photo = "https://img.freepik.com/free-photo/modern-woman-in-denim-jacket-and-white-pants-looking-away-outside-wavy-haired-woman-with-red-lips-with-handbag-posing-near-stairs_197531-19331.jpg?w=1380&t=st=1680466345~exp=1680466945~hmac=235cae2c7a381802f324494ef8d8d4671c708c47d5efff1bc7d580c28c29b14d"
        ),
        SwipeCardModel(
            name = "Maria",
            age = 31,
            photo = "https://img.freepik.com/free-photo/young-tender-fair-haired-teenage-girl-with-healthy-skin-wearing-blue-top-looking-with-serious-or-pensive-expression-caucasian-woman-model-with-hands-in-pockets-posing-indoors_176420-13442.jpg?w=1380&t=st=1680466359~exp=1680466959~hmac=be0e22d9ac070b7ff894b6ae8c5d7adf2f6fd9b0e164d0a1ce61253fe59a8263"
        ),
        SwipeCardModel(
            name = "Alexa",
            age = 19,
            photo = "https://img.freepik.com/free-photo/portrait-of-young-woman-with-natural-make-up_23-2149084942.jpg?w=740&t=st=1680466391~exp=1680466991~hmac=26b4bd4294be1930611fd3b1a1c56b34d26fa01c6985666026e3a27ff31280a0"
        ))

    private var currentIndex = 0

    private val topCard
        get() = data[currentIndex % data.size]

    private val bottomCard
        get() = data[(currentIndex + 1) % data.size]

    init {
        updateStream()
    }

    fun swipe() {
        currentIndex += 1

        updateStream()
    }

    private fun updateStream() {
        mutableModelStream.value = SwipeModel(top = topCard, bottom = bottomCard)
    }
}