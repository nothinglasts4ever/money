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

    def "Deposit funds - basic positive test"() {
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

    def "Withdraw funds - basic positive test"() {
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
    }

    def "Transfer funds - basic positive test"() {
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
    }

}