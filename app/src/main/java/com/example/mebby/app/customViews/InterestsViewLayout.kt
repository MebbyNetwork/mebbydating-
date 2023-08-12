package com.example.mebby.app.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.InterestModel
import com.example.mebby.R
import com.example.mebby.app.adapters.interestsAdapter.InterestsRecyclerViewAdapter
import com.example.mebby.app.adapters.interestsAdapter.SelectedInterestsRecyclerViewAdapter
import com.example.mebby.app.decorators.InterestsItemDecorator
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager

class InterestsViewLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
): ConstraintLayout(context, attrs, defStyleAttr) {

    var interests: RecyclerView
    private var selectedInterests: RecyclerView
    var search: ClearableEditText

    //InterestsRecyclerView
    private var interestsRecyclerViewAdapter: InterestsRecyclerViewAdapter? = null

    //SelectedInterestsRecyclerView
    private var selectedRecyclerViewAdapter: SelectedInterestsRecyclerViewAdapter? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.interests_view_layout, this, true)

        interests = findViewById(R.id.interests_recycler_view)
        selectedInterests = findViewById(R.id.selected_interests_recycler_view)
        search = findViewById(R.id.search_interests)
    }

    fun setInterestsAdapter(adapter: InterestsRecyclerViewAdapter): InterestsViewLayout {
        interestsRecyclerViewAdapter = adapter
        initInterests()
        return this
    }

    fun setSelectedInterestsAdapter(adapter: SelectedInterestsRecyclerViewAdapter): InterestsViewLayout {
        selectedRecyclerViewAdapter = adapter
        initSelectedInterests()
        return this
    }

    fun setSearchChange(callback: (String) -> Unit): InterestsViewLayout {
        search.editText.addTextChangedListener {
            callback(it.toString())
        }
        return this
    }

    fun setInterestsList(list: List<InterestModel>) {
        val searchText = search.editText.text.toString()
        if (searchText == "") interestsRecyclerViewAdapter?.setInterestsList(list)
    }

    fun setSelectedList(list: List<InterestModel>) {
        if (list.isEmpty()) selectedInterests.visibility = View.GONE
        else selectedInterests.visibility = View.VISIBLE
        selectedRecyclerViewAdapter?.setSelectedInterestsList(list)
    }

    private fun initInterests() {
        val recyclerView = interests

        val layoutManager = FlexboxLayoutManager(context)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = interestsRecyclerViewAdapter

        recyclerView.addItemDecoration(InterestsItemDecorator(12, 12))
    }

    private fun initSelectedInterests() {
        val recyclerView = selectedInterests
        val layoutManager = FlexboxLayoutManager(context)

        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.flexWrap = FlexWrap.WRAP

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = selectedRecyclerViewAdapter

        recyclerView.addItemDecoration(InterestsItemDecorator(12, 12))
    }

    fun filterInterestsList(string: String, list: List<InterestModel>) {
        val filteredList = ArrayList<InterestModel>()

        for (interest in list) {
            if (interest.interestValue.lowercase().startsWith(string.lowercase())) {
                filteredList.add(interest)
            }
        }

        filteredList.sortedBy { it.interestValue }
        interestsRecyclerViewAdapter?.setInterestsList(filteredList)
    }
}