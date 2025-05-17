package com.metafortech.calma.domain.sports

import kotlinx.coroutines.flow.Flow

interface SportUseCase {
    operator fun invoke (): Flow<DomainSportState>
}