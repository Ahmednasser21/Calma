package com.metafortech.calma.domain.mappers

import com.metafortech.calma.data.remote.register.RegisterBody
import com.metafortech.calma.domain.register.ValidationFormState

interface FormStateToRegisterBodyMapper {
    operator fun invoke(
        validationFormState: ValidationFormState,
        lang: String
    ): RegisterBody

}
