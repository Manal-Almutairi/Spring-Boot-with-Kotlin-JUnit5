package com.bank.demo.controller

import com.bank.demo.model.Bank
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.*

@SpringBootTest
@AutoConfigureMockMvc
internal class BankControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {

    val baseUrl="/api/banks"
    @Nested
    @DisplayName("GET /api/banks")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class GetBanks{
        @Test
        fun `should return all banks`(){
            //when/then
            mockMvc.get(baseUrl)
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].accountNumber"){value("1234")}
                }

        }

    }

    @Nested
    @DisplayName("GET /api/banks/{accountNumber}")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class GetBank{

        @Test
        fun`should return the bank with the given account number`(){
            //given
            val accountNumber = 1234

            //when /then
            mockMvc.get("$baseUrl/$accountNumber")
                .andDo { print(accountNumber) }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON)}
                    jsonPath("$.trust"){value("15.0")}
                    jsonPath("$.transactionFee"){value("14")}
                }
        }

        @Test
        fun`should return NOT FOUND if the account number does not exist`() {
            //given
            val accountNumber="dose_not_exist"
            //when/then
            mockMvc.get("$baseUrl/$accountNumber")
                .andDo { print("Sorry, the account number does not exist") }
                .andExpect { status { isNotFound() } }
        }
    }

    @Nested
    @DisplayName("POST /api/banks")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class PostNewBank{

        @Test
        fun `should add the new bank`(){
            //given
            val newBank = Bank("222",31.42,2)
            //when
            val preformPost = mockMvc.post(baseUrl){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newBank)
            }
            //then
            preformPost
                .andDo { print("the new bank was added") }
                .andExpect {
                    status { isCreated() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(newBank))
                    }
                }
            mockMvc.get("$baseUrl/${newBank.accountNumber}")
                .andExpect { content { json(objectMapper.writeValueAsString(newBank)) } }

        }

        @Test
        fun`should return BAD REQUEST if bank with account number already exist`(){
            //given
            val invalidBank = Bank("1234",31.4,2)

            //when/then

            val preformPost = mockMvc.post(baseUrl){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidBank)
            }
            preformPost
                .andDo { print() }
                .andExpect { status { isBadRequest() } }
        }
    }


    @Nested
    @DisplayName("PATCH /api/banks")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class PatchExistingBank {
        @Test
        fun`should updated an existing account bank`(){
            //given
            val updatedBank = Bank("1234",31.4,4)

            //when/then

            val preformPatch = mockMvc.patch(baseUrl){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updatedBank)
            }
            preformPatch
                .andDo { print("The account bank was updated") }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(updatedBank))
                    }
                }
            mockMvc.get("$baseUrl/${updatedBank.accountNumber}")
                .andExpect { content { json(objectMapper.writeValueAsString(updatedBank)) } }
        }

        @Test
        fun`should return BAD REQUEST if no bank with given account number`(){
            //given
            val invalidBank = Bank("dose_not_exist",31.4,2)

            //when/then

            val preformPatch = mockMvc.patch(baseUrl){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidBank)
            }
            preformPatch
                .andDo { print("Sorry, there is no bank with the given account number") }
                .andExpect { status { isNotFound() } }
        }
    }

    @Nested
    @DisplayName("Delete /api/banks/{accountNumber}")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class DeleteExistingBank {

        @Test
        @DirtiesContext
        fun`should delete the bank with the given account number`(){
            //given
            val accountNumber = 1234

            //when/then
            mockMvc.delete("$baseUrl/$accountNumber")
                .andDo { print("The account bank was delete successfully ") }
                .andExpect {
                    status { isNoContent() }
                }
            mockMvc.get("$baseUrl/$accountNumber")
                .andExpect {
                    status { isNotFound() }
                }

        }

        @Test
        fun`should return NOT FOUND if the account number does not exist`() {
            //given
            val accountNumber="dose_not_exist"
            //when/then
            mockMvc.delete("$baseUrl/$accountNumber")
                .andDo { print("Sorry, the account number does not exist") }
                .andExpect { status { isNotFound() } }
        }

    }

}
