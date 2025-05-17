package com.metafortech.calma.domain.interest

import com.metafortech.calma.data.remote.interest.InterestsResponse

sealed class DomainInterestState {
    data class OnSuccess(val interestResponse: InterestsResponse) : DomainInterestState()
    data class OnFailed( val error: String? = null) : DomainInterestState()

}