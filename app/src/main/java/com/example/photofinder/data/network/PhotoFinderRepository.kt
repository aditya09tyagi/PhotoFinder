package com.example.photofinder.data.network

import com.example.photofinder.data.models.Error
import com.example.photofinder.data.models.ResponseImages
import com.example.photofinder.util.ApiCallback

class PhotoFinderRepository (private val photoFinderService: PhotoFinderService){

    fun getImagesBySearch(
        queryText:String,
        pageNumber:Int,
        onGetImageBySearchListener: OnGetImageBySearchListener
    ){
        photoFinderService.getImagesBySearch(queryText,pageNumber).enqueue(object :ApiCallback<ResponseImages>(){
            override fun success(response: ResponseImages) {
                onGetImageBySearchListener.onGetImageBySearchSuccess(response)
            }

            override fun failure(error: Error) {
                onGetImageBySearchListener.onGetImageBySearchFailure(error)
            }

        })
    }

    //--------LISTENERS--------

    interface OnGetImageBySearchListener{

        fun onGetImageBySearchSuccess(responseImages: ResponseImages)

        fun onGetImageBySearchFailure(error:Error)

    }

}