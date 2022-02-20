package com.bank.demo.datasource.mock

import com.bank.demo.datasource.BankDataSource
import com.bank.demo.model.Bank
import org.springframework.stereotype.Repository

@Repository
class MockBankDataSource: BankDataSource {

    val banks= mutableListOf(
        Bank("1234",15.0,14),
        Bank("1224",150.0,17),
        Bank("1334",10.0,100)
    )

    override fun retrieveBanks(): Collection<Bank> = banks
    override fun retrieveBank(accountNumber: String): Bank {
        return banks.firstOrNull(){ it.accountNumber==accountNumber}
            ?: throw NoSuchElementException("Could not find a bank with account number $accountNumber")
    }

    override fun createBank(bank: Bank): Bank {
        if(banks.any{it.accountNumber == bank.accountNumber}){
            throw IllegalArgumentException("Bank with account number ${bank.accountNumber} is already exist")
        }
        banks.add(bank)
        return bank
    }

    override fun updateBank(bank: Bank): Bank {
        val currentBank = banks.firstOrNull{ it.accountNumber == bank.accountNumber}
            ?: throw NoSuchElementException("Could not find a bank with account number ${bank.accountNumber}")
        banks.remove(currentBank)
        banks.add(bank)
        return bank

    }

    override fun deleteBank(accountNumber: String) {
        val bank = banks.firstOrNull{ it.accountNumber == accountNumber}
            ?: throw NoSuchElementException("Could not find a bank with account number $accountNumber")
        banks.remove(bank)
    }
}