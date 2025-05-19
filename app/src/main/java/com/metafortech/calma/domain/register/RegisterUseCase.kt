package com.metafortech.calma.domain.register

import kotlinx.coroutines.flow.Flow

interface RegisterUseCase {
    operator fun invoke(validationFormState: ValidationFormState,lang:String): Flow<DomainRegisterState>

}