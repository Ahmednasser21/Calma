package com.metafortech.calma.domain.sports

import kotlinx.coroutines.flow.Flow

interface GetSportsListUseCase {
    operator fun invoke (): Flow<DomainSportState>
}