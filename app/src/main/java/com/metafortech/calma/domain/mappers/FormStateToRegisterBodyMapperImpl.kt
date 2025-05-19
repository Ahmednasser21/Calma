package com.metafortech.calma.domain.mappers

import com.metafortech.calma.data.remote.register.RegisterBody
import com.metafortech.calma.domain.register.ValidationFormState
import jakarta.inject.Inject

class FormStateToRegisterBodyMapperImpl @Inject constructor():FormStateToRegisterBodyMapper{
    override operator fun invoke(
        validationFormState: ValidationFormState,
        lang: String,
    ): RegisterBody {
        return RegisterBody(
            name = validationFormState.name,
            email = validationFormState.email,
            dob = validationFormState.birthday,
            password = validationFormState.password,
            phone = validationFormState.country.dialCode + validationFormState.phoneNumber,
            gendar = validationFormState.gender,
            lang = lang,
            sports = emptyList(),
            intersets = emptyList()
        )
    }
}

