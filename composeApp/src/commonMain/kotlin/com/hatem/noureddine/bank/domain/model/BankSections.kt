package com.hatem.noureddine.bank.domain.model

/**
 * Helper class to group banks into sections for UI display.
 *
 * @property caBanks List of Credit Agricole banks.
 * @property otherBanks List of other external banks.
 */
data class BankSections(
    val caBanks: List<CABank>,
    val otherBanks: List<OtherBank>,
)
