package com.github.nl4.money.e2e

import com.github.nl4.money.api.Account
import com.github.nl4.money.api.BalanceUpdateRequest
import com.github.nl4.money.api.TransferRequest
import com.github.nl4.money.helper.Scenarios
import spock.lang.Specification

import static com.github.nl4.money.helper.RequestSpecificationTemplate.*
import static org.apache.http.HttpStatus.*

class FundsAPI extends Specification {

    String ACCOUNTS_ENDPOINT = "/accounts"
    String TRANSFER_ENDPOINT = "/transfer"
    String name
    String name2

    def setup() {
        name = "Mr. Meeseeks " + System.currentTimeMillis()
        name2 = "Abradolf Lincler " + System.currentTimeMillis()
    }

    def "Funds should be added to the balance when valid request sent"() {
        given:
            def account = Scenarios.createActiveAccountWithFundsAndGet(ACCOUNTS_ENDPOINT, name)
        when:
            def request = new BalanceUpdateRequest(new BigDecimal(100))
            def response = processPutRequest(ACCOUNTS_ENDPOINT + "/" + account.id + "/deposit", request)
        then:
            response.statusCode == SC_OK
        when:
            response = processGetRequest(ACCOUNTS_ENDPOINT + "/" + account.id)
        then:
            response.statusCode == SC_OK
            response.body.as(Account).balance == 1100
    }

    def "Funds should not be added when request without amount or with invalid amount sent"() {
        given:
            def account = Scenarios.createActiveAccountWithFundsAndGet(ACCOUNTS_ENDPOINT, name)
        when:
            def request = new BalanceUpdateRequest()
            def response = processPutRequest(ACCOUNTS_ENDPOINT + "/" + account.id + "/deposit", request)
        then:
            response.statusCode == SC_BAD_REQUEST
        when:
            request = new BalanceUpdateRequest(new BigDecimal(-100))
            response = processPutRequest(ACCOUNTS_ENDPOINT + "/" + account.id + "/deposit", request)
        then:
            response.statusCode == SC_BAD_REQUEST
        when:
            request = new BalanceUpdateRequest(BigDecimal.ZERO)
            response = processPutRequest(ACCOUNTS_ENDPOINT + "/" + account.id + "/deposit", request)
        then:
            response.statusCode == SC_BAD_REQUEST
        when:
            response = processGetRequest(ACCOUNTS_ENDPOINT + "/" + account.id)
        then:
            response.statusCode == SC_OK
            response.body.as(Account).balance == 1000
    }

    def "Funds should be subtracted from the balance when valid request sent"() {
        given:
            def account = Scenarios.createActiveAccountWithFundsAndGet(ACCOUNTS_ENDPOINT, name)
        when:
            def request = new BalanceUpdateRequest(new BigDecimal(100))
            def response = processPutRequest(ACCOUNTS_ENDPOINT + "/" + account.id + "/withdraw", request)
        then:
            response.statusCode == SC_OK
        when:
            response = processGetRequest(ACCOUNTS_ENDPOINT + "/" + account.id)
        then:
            response.statusCode == SC_OK
            response.body.as(Account).balance == 900
        when:
            request = new BalanceUpdateRequest(new BigDecimal(900))
            response = processPutRequest(ACCOUNTS_ENDPOINT + "/" + account.id + "/withdraw", request)
        then:
            response.statusCode == SC_OK
        when:
            response = processGetRequest(ACCOUNTS_ENDPOINT + "/" + account.id)
        then:
            response.statusCode == SC_OK
            response.body.as(Account).balance == BigDecimal.ZERO
    }

    def "Funds should not be subtracted when request without amount or with invalid amount sent"() {
        given:
            def account = Scenarios.createActiveAccountWithFundsAndGet(ACCOUNTS_ENDPOINT, name)
        when:
            def request = new BalanceUpdateRequest()
            def response = processPutRequest(ACCOUNTS_ENDPOINT + "/" + account.id + "/withdraw", request)
        then:
            response.statusCode == SC_BAD_REQUEST
        when:
            request = new BalanceUpdateRequest(new BigDecimal(-100))
            response = processPutRequest(ACCOUNTS_ENDPOINT + "/" + account.id + "/withdraw", request)
        then:
            response.statusCode == SC_BAD_REQUEST
        when:
            request = new BalanceUpdateRequest(BigDecimal.ZERO)
            response = processPutRequest(ACCOUNTS_ENDPOINT + "/" + account.id + "/withdraw", request)
        then:
            response.statusCode == SC_BAD_REQUEST
        when:
            response = processGetRequest(ACCOUNTS_ENDPOINT + "/" + account.id)
        then:
            response.statusCode == SC_OK
            response.body.as(Account).balance == 1000
    }

    def "Funds should not be subtracted when the account has not enough funds"() {
        given:
            def account = Scenarios.createActiveAccountWithFundsAndGet(ACCOUNTS_ENDPOINT, name)
        when:
            def request = new BalanceUpdateRequest(new BigDecimal(1001))
            def response = processPutRequest(ACCOUNTS_ENDPOINT + "/" + account.id + "/withdraw", request)
        then:
            response.statusCode == SC_OK
        when:
            response = processGetRequest(ACCOUNTS_ENDPOINT + "/" + account.id)
        then:
            response.statusCode == SC_OK
            response.body.as(Account).balance == 1000
    }

    def "404 error should be returned when trying to add or subtract funds from non-existing account"() {
        given:
            def id = Integer.MAX_VALUE
            def request = new BalanceUpdateRequest(new BigDecimal(100))
        when:
            def response = processPutRequest(ACCOUNTS_ENDPOINT + "/" + id + "/deposit", request)
        then:
            response.statusCode == SC_NOT_FOUND
        when:
            response = processPutRequest(ACCOUNTS_ENDPOINT + "/" + id + "/withdraw", request)
        then:
            response.statusCode == SC_NOT_FOUND
    }

    def "404 error should be returned when trying to add or subtract funds from inactive account"() {
        given:
            def account = Scenarios.createAccountAndGet(ACCOUNTS_ENDPOINT, name, new BigDecimal(1000), false)
        when:
            def request = new BalanceUpdateRequest(new BigDecimal(100))
            def response = processPutRequest(ACCOUNTS_ENDPOINT + "/" + account.id + "/deposit", request)
        then:
            response.statusCode == SC_NOT_FOUND
        when:
            request = new BalanceUpdateRequest(new BigDecimal(200))
            response = processPutRequest(ACCOUNTS_ENDPOINT + "/" + account.id + "/withdraw", request)
        then:
            response.statusCode == SC_NOT_FOUND
        when:
            response = processGetRequest(ACCOUNTS_ENDPOINT + "/" + account.id)
        then:
            response.statusCode == SC_OK
            response.body.as(Account).balance == 1000
    }

    def "Funds should be transferred from one account to another when valid request sent"() {
        given:
            def accountFrom = Scenarios.createActiveAccountWithFundsAndGet(ACCOUNTS_ENDPOINT, name)
            def accountTo = Scenarios.createActiveAccountWithFundsAndGet(ACCOUNTS_ENDPOINT, name2)
        when:
            def request = TransferRequest.builder()
                    .accountFromId(accountFrom.id)
                    .accountToId(accountTo.id)
                    .amount(new BigDecimal(100))
                    .build()
            def response = processPostRequest(TRANSFER_ENDPOINT, request)
        then:
            response.statusCode == SC_CREATED
        when:
            response = processGetRequest(ACCOUNTS_ENDPOINT + "/" + accountFrom.id)
        then:
            response.statusCode == SC_OK
            response.body.as(Account).balance == 900
        when:
            response = processGetRequest(ACCOUNTS_ENDPOINT + "/" + accountTo.id)
        then:
            response.statusCode == SC_OK
            response.body.as(Account).balance == 1100
        when:
            request = TransferRequest.builder()
                    .accountFromId(accountFrom.id)
                    .accountToId(accountTo.id)
                    .amount(new BigDecimal(900))
                    .build()
            response = processPostRequest(TRANSFER_ENDPOINT, request)
        then:
            response.statusCode == SC_CREATED
        when:
            response = processGetRequest(ACCOUNTS_ENDPOINT + "/" + accountFrom.id)
        then:
            response.statusCode == SC_OK
            response.body.as(Account).balance == BigDecimal.ZERO
        when:
            response = processGetRequest(ACCOUNTS_ENDPOINT + "/" + accountTo.id)
        then:
            response.statusCode == SC_OK
            response.body.as(Account).balance == 2000
    }

    def "Funds should not be transferred when originator does not have enough funds"() {
        given:
            def accountFrom = Scenarios.createActiveAccountWithFundsAndGet(ACCOUNTS_ENDPOINT, name)
            def accountTo = Scenarios.createActiveAccountWithFundsAndGet(ACCOUNTS_ENDPOINT, name2)
        when:
            def request = TransferRequest.builder()
                    .accountFromId(accountFrom.id)
                    .accountToId(accountTo.id)
                    .amount(new BigDecimal(1001))
                    .build()
            def response = processPostRequest(TRANSFER_ENDPOINT, request)
        then:
            response.statusCode == SC_CREATED
        when:
            response = processGetRequest(ACCOUNTS_ENDPOINT + "/" + accountFrom.id)
        then:
            response.statusCode == SC_OK
            response.body.as(Account).balance == 1000
        when:
            response = processGetRequest(ACCOUNTS_ENDPOINT + "/" + accountTo.id)
        then:
            response.statusCode == SC_OK
            response.body.as(Account).balance == 1000
    }

    def "Funds should not be transferred when request without required data sent"() {
        given:
            def request = TransferRequest.builder()
                    .amount(new BigDecimal(100))
                    .build()
        when:
            def response = processPostRequest(TRANSFER_ENDPOINT, request)
        then:
            response.statusCode == SC_BAD_REQUEST
    }

    def "Funds should not be transferred when request with invalid amount sent"() {
        given:
            def request = TransferRequest.builder()
                    .amount(new BigDecimal(-100))
                    .build()
        when:
            def response = processPostRequest(TRANSFER_ENDPOINT, request)
        then:
            response.statusCode == SC_BAD_REQUEST
        when:
            request = TransferRequest.builder()
                    .accountFromId(123)
                    .accountToId(456)
                    .amount(BigDecimal.ZERO)
                    .build()
            response = processPostRequest(TRANSFER_ENDPOINT, request)
        then:
            response.statusCode == SC_BAD_REQUEST
    }

    def "Funds should not be transferred when trying to send funds to the same account"() {
        given:
            def request = TransferRequest.builder()
                    .accountFromId(123)
                    .accountToId(123)
                    .amount(new BigDecimal(100))
                    .build()
        when:
            def response = processPostRequest(TRANSFER_ENDPOINT, request)
        then:
            response.statusCode == SC_BAD_REQUEST
    }

    def "404 error should be returned when trying to transfer funds from/to non-existing account"() {
        given:
            def accountFromId = Integer.MAX_VALUE
            def accountToId = Integer.MAX_VALUE - 1
            def request = TransferRequest.builder()
                    .accountFromId(accountFromId)
                    .accountToId(accountToId)
                    .amount(new BigDecimal(100))
                    .build()
        when:
            def response = processPostRequest(TRANSFER_ENDPOINT, request)
        then:
            response.statusCode == SC_NOT_FOUND
    }

    def "404 error should be returned when trying to transfer funds from/to inactive account"() {
        given:
            def accountFrom = Scenarios.createActiveAccountWithFundsAndGet(ACCOUNTS_ENDPOINT, name)
            def accountTo = Scenarios.createAccountAndGet(ACCOUNTS_ENDPOINT, name2, new BigDecimal(1000), false)
            def request = TransferRequest.builder()
                    .accountFromId(accountFrom.id)
                    .accountToId(accountTo.id)
                    .amount(new BigDecimal(100))
                    .build()
        when:
            def response = processPostRequest(TRANSFER_ENDPOINT, request)
        then:
            response.statusCode == SC_NOT_FOUND
    }

}