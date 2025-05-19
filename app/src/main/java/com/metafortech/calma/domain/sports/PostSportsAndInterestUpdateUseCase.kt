package com.metafortech.calma.domain.sports

import com.metafortech.calma.data.remote.interest.InterestsUpdateRequest
import kotlinx.coroutines.flow.Flow

interface PostSportsAndInterestUpdateUseCase {
    suspend operator fun invoke(
        request: InterestsUpdateRequest,
        token: String
    ): Flow<DomainSportState>

}