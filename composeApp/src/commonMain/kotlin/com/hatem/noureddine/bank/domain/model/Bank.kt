package com.hatem.noureddine.bank.domain.model

/**
 * Represents a generic Bank entity.
 * This is a sealed class to allow for different bank types (e.g., CA Bank vs Other Banks).
 */
sealed class Bank {
    /** The name of the bank. */
    abstract val name: String

    /** The list of accounts associated with this bank. */
    abstract val accounts: List<Account>
}

/**
 * Represents a Credit Agricole Bank.
 * @property name The name of the bank.
 * @property accounts The list of accounts held at this bank.
 */
data class CABank(
    override val name: String,
    override val accounts: List<Account>,
) : Bank()

/**
 * Represents an external or other bank.
 * @property name The name of the bank.
 * @property accounts The list of accounts held at this bank.
 */
data class OtherBank(
    override val name: String,
    override val accounts: List<Account>,
) : Bank()
