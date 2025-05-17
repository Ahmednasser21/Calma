package com.metafortech.calma.domain.sports

import com.metafortech.calma.data.remote.sports.SportsResponse

sealed class DomainSportState(){
    data class OnSuccess(val data: SportsResponse): DomainSportState()
    data class OnFailed(val error: String?): DomainSportState()
}
