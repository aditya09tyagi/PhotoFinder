package com.example.photofinder.di.modules.network

import com.example.photofinder.data.network.PhotoFinderRepository
import com.example.photofinder.data.network.PhotoFinderService
import com.example.photofinder.di.scopes.PhotoFinderApplicationScope
import dagger.Module
import dagger.Provides

@Module(includes = [PhotoFinderServiceModule::class])
class PhotoFinderRepositoryModule {

    @Provides
    @PhotoFinderApplicationScope
    fun setUpRepository(photoFinderService: PhotoFinderService): PhotoFinderRepository {
        return PhotoFinderRepository(photoFinderService)
    }
}