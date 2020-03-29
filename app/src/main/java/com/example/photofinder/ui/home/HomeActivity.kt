package com.example.photofinder.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.photofinder.R
import com.example.photofinder.data.models.Status
import com.example.photofinder.ui.base.BaseActivity
import com.example.photofinder.ui.base.GridSpacingItemDecoration
import com.example.photofinder.ui.loader.CustomErrorListItemCreator
import com.example.photofinder.ui.loader.CustomLoadingListItemCreator
import com.example.photofinder.ui.widget.photo_view_layout.StfalconImageViewer
import com.example.photofinder.util.DebouncingQueryTextListenerUtil
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.shimmer_placeholder.*
import ru.alexbykov.nopaginate.callback.OnLoadMoreListener
import ru.alexbykov.nopaginate.paginate.NoPaginate
import javax.inject.Inject


class HomeActivity : BaseActivity(), ImageAdapter.OnItemClickListener, OnLoadMoreListener {

    @Inject
    lateinit var imageAdapter: ImageAdapter

    @Inject
    lateinit var picasso: Picasso

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var layoutManager: StaggeredGridLayoutManager
    private lateinit var paginate: NoPaginate
    private var isFirstLoad = true
    private lateinit var searchText: String
    private var currentPage = 0
    private var totalPages = 0

    companion object {
        private const val EXTRA_SPAN_COUNT = 2
        fun newIntent(context: Context) = Intent(context, HomeActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        initialiseObjects()
        initialiseLayout()
        setListener()
        observeData()
    }

    private fun initialiseObjects() {
        homeViewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    private fun initialiseLayout() {
        layoutManager = StaggeredGridLayoutManager(EXTRA_SPAN_COUNT, RecyclerView.VERTICAL)
        rvImages.layoutManager = layoutManager
        rvImages.addItemDecoration(GridSpacingItemDecoration(2, 20, true))
        rvImages.adapter = imageAdapter

        paginate = NoPaginate.with(rvImages)
            .setLoadingTriggerThreshold(1)
            .setOnLoadMoreListener(this)
            .setCustomErrorItem(CustomErrorListItemCreator(false))
            .setCustomLoadingItem(CustomLoadingListItemCreator(false))
            .build()

        searchBar.setAnimationDuration(500)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        searchBar.setMenuItem(menu?.findItem(R.id.action_search))
        return true
    }

    private fun setListener() {
        searchBar.setOnQueryTextListener(
            DebouncingQueryTextListenerUtil { newText ->
                newText?.let { text ->
                    if (text.isEmpty()) {
                        searchText = ""
                        isFirstLoad = true
                    } else {
                        if (text.length > 2) {
                            if (imageAdapter.getCurrentList().isNotEmpty()) {
                                imageAdapter.clearList()
                            }
                            searchText = text
                            homeViewModel.getImageBySearch(text)
                        }
                    }
                }
            }
        )

        imageAdapter.setOnItemClickListener(this)

        ivGoUp.setOnClickListener {
            scrollToTop()
            hideUpButton()
        }

        rvImages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (currentPage > 1) {
                    showUpButton()
                }
            }
        })
    }

    private fun observeData() {
        homeViewModel.imageSearchLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    ivNoImages.visibility = View.GONE
                    updateLoadingState(true)
                    updateErrorState(false)
                    if (isFirstLoad) {
                        shimmerViewContainer.startShimmer()
                        rvImages.visibility = View.GONE
                        shimmerViewContainer.visibility = View.VISIBLE
                    }
                }
                Status.SUCCESS -> {
                    ivNoImages.visibility = View.GONE
                    updateLoadingState(false)
                    updateErrorState(false)
                    shimmerViewContainer.stopShimmer()
                    shimmerViewContainer.visibility = View.GONE
                    rvImages.visibility = View.GONE
                    it.data?.let { resp ->
                        val list = resp.photos.imageList
                        totalPages = resp.photos.pages
                        currentPage = resp.photos.page
                        val previousSize = imageAdapter.getCurrentList().size
                        imageAdapter.addImages(list)

                        if (list.isEmpty()) {
                            hasLoadedAllItems()
                        }

                        if (isFirstLoad) {
                            isFirstLoad = false
                            imageAdapter.notifyDataSetChanged()
                        } else {
                            imageAdapter.notifyItemRangeInserted(
                                previousSize,
                                imageAdapter.getCurrentList().size - previousSize
                            )
                        }

                        if (imageAdapter.getCurrentList().isEmpty()) {
                            rvImages.visibility = View.GONE
                            ivNoImages.visibility = View.VISIBLE
                        } else {
                            ivNoImages.visibility = View.GONE
                            rvImages.visibility = View.VISIBLE
                        }
                    }
                }
                Status.ERROR -> {
                    updateErrorState(true)
                    updateLoadingState(false)
                    shimmerViewContainer.visibility = View.GONE
                    ivNoImages.visibility = View.VISIBLE
                    rvImages.visibility = View.GONE
                    it.message?.let { errorMsg ->
                        showErrorToast(errorMsg)
                    }
                }
            }
        })
    }

    private fun hideUpButton() {
        ivGoUp.visibility = View.GONE
    }

    private fun showUpButton() {
        ivGoUp.visibility = View.VISIBLE
    }

    private fun loadPosterImage(imageView: ImageView, imageUrl: String) {
        picasso.load(imageUrl).into(imageView)
    }

    private fun openPhotoView(url: String, imageView: ImageView) {
        val images = listOf(url)
        val photoView = getPhotoView(this, images, imageView)
        photoView.show(true)

    }

    private fun getPhotoView(
        context: Context,
        images: List<String>,
        imageView: ImageView
    ): StfalconImageViewer<String> {
        return StfalconImageViewer.Builder<String>(context, images, ::loadPosterImage)
            .withStartPosition(0)
            .withBackgroundColorResource(R.color.black)
            .withHiddenStatusBar(false)
            .allowZooming(true)
            .allowSwipeToDismiss(true)
            .withTransitionFrom(imageView)
            .build()
    }

    override fun onItemClick(imageUrl: String, imageView: ImageView, position: Int) {
        openPhotoView(imageUrl, imageView)
    }

    private fun updateLoadingState(isLoading: Boolean) {
        if (::paginate.isInitialized)
            paginate.showLoading(isLoading)
    }

    private fun updateErrorState(isError: Boolean) {
        if (::paginate.isInitialized)
            paginate.showError(isError)
    }

    private fun hasLoadedAllItems() {
        if (::paginate.isInitialized)
            paginate.setNoMoreItems(true)
    }

    private fun scrollToTop() {
        if (::layoutManager.isInitialized)
            layoutManager.scrollToPosition(0)
    }

    override fun onLoadMore() {
        if (::searchText.isInitialized) {
            if (searchText.isNotEmpty())
                homeViewModel.getImageBySearch(searchText, ++currentPage)
        }
    }

    override fun onBackPressed() {
        if (searchBar.isSearchOpen) {
            searchBar.closeSearch()
        } else {
            super.onBackPressed()
        }
    }
}
