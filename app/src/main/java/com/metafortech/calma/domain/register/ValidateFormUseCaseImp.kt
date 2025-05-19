package com.metafortech.calma.domain.register

import android.util.Patterns
import com.metafortech.calma.R
import jakarta.inject.Inject

class ValidateFormUseCaseImp @Inject constructor() : ValidateFormUseCase {

    override operator fun invoke(validationFormState: ValidationFormState): ValidationState {
        return when {
            validationFormState.name.isEmpty() -> {
                ValidationState.Error(R.string.name_required)
            }

            validationFormState.name.length < 2 -> {
                ValidationState.Error(R.string.name_too_short)
            }

            validationFormState.email.isEmpty() -> {
                ValidationState.Error(R.string.email_is_required)
            }

            !Patterns.EMAIL_ADDRESS.matcher(validationFormState.email).matches() &&
                    !Patterns.PHONE.matcher(validationFormState.email).matches() -> {
                ValidationState.Error(R.string.invalid_email_format)
            }

            validationFormState.phoneNumber.isEmpty() -> {
                ValidationState.Error(R.string.phone_number_required)
            }

            validationFormState.country.dialCode.isEmpty() -> {
                ValidationState.Error(R.string.country_required)
            }

            !Patterns.PHONE.matcher(validationFormState.country.dialCode + validationFormState.phoneNumber)
                .matches() -> {
                ValidationState.Error(R.string.phone_formate_error)
            }

            validationFormState.password.isEmpty() && validationFormState.email.isNotEmpty() -> {
                ValidationState.Error(R.string.password_required)
            }

            validationFormState.email.isNotEmpty() && validationFormState.password.length < 8 -> {
                ValidationState.Error(R.string.password_too_short)
            }

            validationFormState.email.isNotEmpty() && !isPasswordValid(validationFormState.password) -> {
                ValidationState.Error(R.string.password_requirements_not_met)
            }

            validationFormState.birthday.isEmpty() -> {
                ValidationState.Error(R.string.birthday_required)
            }

            validationFormState.gender.isEmpty() -> {
                ValidationState.Error(R.string.gender_required)
            }

            else -> ValidationState.Success
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        if (password.length < 8) return false

        val hasDigit = password.any { it.isDigit() }

        val hasSpecialChar = password.any { !it.isLetterOrDigit() }

        return hasDigit && hasSpecialChar
    }
}