package org.example.geoblinker.core.utils




object Validators {
    
    
    /**
     * Форматирование телефона: 9 XXX XXX XX XX
     * Из LEGACY_RESOURCES.md: CustomTextField.kt:102-112
     */
    fun formatPhoneNumber(phoneNumber: String): String {
        val digitsOnly = phoneNumber.filter { it.isDigit() }
        val formatted = StringBuilder()
        
        
        digitsOnly.take(11).forEachIndexed { index, c ->
            when (index) {
                1, 4, 7, 9 -> formatted.append(" $c")
                else -> formatted.append(c)
            }
        }
        return formatted.toString()
    }
    
    
    fun validatePhone(phone: String): Boolean {
        val digits = phone.filter { it.isDigit() }
        return digits.length == 11 && digits.startsWith("9")
    }
    
    
    fun validateCode(code: String): Boolean {
        return code.length == 6 && code.all { it.isDigit() }
    }
    
    
    fun validateIMEI(imei: String): Boolean {
        val digits = imei.filter { it.isDigit() }
        return digits.length == 15
    }
}
