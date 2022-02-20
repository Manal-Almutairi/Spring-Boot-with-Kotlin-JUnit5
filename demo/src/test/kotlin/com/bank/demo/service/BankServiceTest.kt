package com.bank.demo.service

import com.bank.demo.datasource.BankDataSource
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class BankServiceTest{

    private val dataSource: BankDataSource = mockk(relaxed = true)
    private val bankService = BankService(dataSource)
    @Test
    fun `should provide a collection of bank`() {

        //when
        val banks = bankService.getBanks()

        //then
        verify(exactly = 1){ dataSource.retrieveBanks() }
    }

}