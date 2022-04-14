package com.xuaum.cstv.data.repository.match

import com.xuaum.cstv.data.model.NetworkState
import com.xuaum.cstv.data.model.response.getmatchesresponse.CSMatch
import com.xuaum.cstv.data.model.response.getteamsresponse.Team
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MatchRepositoryImp @Inject constructor(private val remoteDataSource: MatchRemoteDataSource) :
    MatchRepository {
    private val _getMatchesState = MutableStateFlow<NetworkState<Nothing>>(NetworkState.Idle)
    override val getMatchesState: StateFlow<NetworkState<Nothing>>
        get() = _getMatchesState

    override suspend fun getMatches(pageNumber: Int): List<CSMatch>? = withContext(Dispatchers.IO) {
        try {
            _getMatchesState.value = NetworkState.Loading
            val result = remoteDataSource.getMatches(pageNumber)
            _getMatchesState.value = NetworkState.Success(null)
            result
        } catch (e: Exception) {
            _getMatchesState.value = NetworkState.Failed(e)
            null
        }
    }

}