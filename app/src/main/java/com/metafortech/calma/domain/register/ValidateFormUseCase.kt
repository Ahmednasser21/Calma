package com.metafortech.calma.domain.register

interface ValidateFormUseCase {
    operator fun invoke(validationFormState: ValidationFormState): ValidationState
}
