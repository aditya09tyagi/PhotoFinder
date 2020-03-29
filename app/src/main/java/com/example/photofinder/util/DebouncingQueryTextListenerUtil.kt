package com.example.photofinder.util

import com.miguelcatalan.materialsearchview.MaterialSearchView
import kotlinx.coroutines.*

class DebouncingQueryTextListenerUtil(
    private val onDebouncingQueryTextChange: (String?) -> Unit
) : MaterialSearchView.OnQueryTextListener {
    private var debouncePeriod: Long = 1000

    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    private var searchJob: Job? = null

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let{
            onDebouncingQueryTextChange(it)
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        searchJob?.cancel()
        searchJob = coroutineScope.launch {
            newText?.let {
                delay(debouncePeriod)
                onDebouncingQueryTextChange(newText)
            }
        }
        return false
    }
}