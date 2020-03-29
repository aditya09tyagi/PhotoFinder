package com.example.photofinder.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.photofinder.data.models.Error
import com.example.photofinder.data.models.Resource
import com.example.photofinder.data.models.ResponseImages
import com.example.photofinder.data.network.PhotoFinderRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val photoFinderRepository: PhotoFinderRepository
) : ViewModel(), PhotoFinderRepository.OnGetImageBySearchListener {

    private val _imageSearchLiveData = MutableLiveData<Resource<ResponseImages>>()
    val imageSearchLiveData: LiveData<Resource<ResponseImages>>
        get() = _imageSearchLiveData

    fun getImageBySearch(queryText:String,pageNumber:Int=1){
        _imageSearchLiveData.postValue(Resource.loading())
        photoFinderRepository.getImagesBySearch(queryText,pageNumber,this)
    }

    override fun onGetImageBySearchSuccess(responseImages: ResponseImages) {
        _imageSearchLiveData.postValue(Resource.success(responseImages))
    }

    override fun onGetImageBySearchFailure(error: Error) {
        _imageSearchLiveData.postValue(Resource.error(error))
    }

}