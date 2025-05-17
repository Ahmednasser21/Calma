package com.metafortech.calma.domain.interest

import kotlinx.coroutines.flow.Flow

interface InterestUseCase {
    operator fun invoke (): Flow<DomainInterestState>

}