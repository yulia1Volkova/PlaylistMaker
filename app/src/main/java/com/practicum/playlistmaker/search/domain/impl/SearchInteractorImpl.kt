package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.api.TracksSearchRepository
import com.practicum.playlistmaker.util.Resource
import java.util.concurrent.Executors

class SearchInteractorImpl(private val repository: TracksSearchRepository) : SearchInteractor {

        private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: SearchInteractor.TracksConsumer) {
            executor.execute {
               // consumer.consume(repository.searchTracks(expression))

                when(val resource = repository.searchTracks(expression)) {
                    is Resource.Success -> { consumer.consume(resource.data, null) }
                    is Resource.Error -> { consumer.consume(null, resource.message) }
                }
            }
        }
    }
