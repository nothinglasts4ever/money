package com.github.nl4.money.e2e

import com.github.nl4.money.api.Account
import com.github.nl4.money.helper.Scenarios
import spock.lang.Specification

import static com.github.nl4.money.helper.RequestSpecificationTemplate.*
import static org.apache.http.HttpStatus.*

class AccountAPI extends Specification {

    String ACCOUNTS_ENDPOINT = "/accounts"
    String name

    def setup() {
        name = "Bird Person " + System.currentTimeMillis()
    }

    def "Create account - basic positive test"() {
        given:
            def request = Account.builder()
                    .userName(name)
                    .balance(new BigDecimal(100))
                    .active(true)
                    .build()
        when:
            def response = processPostRequest(ACCOUNTS_ENDPOINT, request)
        then:
            response.statusCode == SC_CREATED
    }

    def "Get all accounts - basic positive test"() {
        given:
            def request = Account.builder()
                    .userName(name)
                    .balance(new BigDecimal(100))
                    .active(true)
                    .build()
        when:
            def response = processPostRequest(ACCOUNTS_ENDPOINT, request)
        then:
            response.statusCode == SC_CREATED
        when:
            response = processGetRequest(ACCOUNTS_ENDPOINT)
        then:
            response.statusCode == SC_OK
        when:
            def accounts = response.body.as(Account[])
            def account = accounts.find { it -> it.userName == name }
        then:
            account.id
    }

    def "Get account - basic positive test"() {
        given:
            def account = Scenarios.createActiveAccountWithFundsAndGet(ACCOUNTS_ENDPOINT, name)
        when:
            def response = processGetRequest(ACCOUNTS_ENDPOINT + "/" + account.id)
        then:
            response.statusCode == SC_OK
            response.body.as(Account).id == account.id
            response.body.as(Account).userName == account.userName
            response.body.as(Account).balance == account.balance
            response.body.as(Account).active == account.active
    }

    def "Activate account - basic positive test"() {
        given:
            def account = Scenarios.createAccountAndGet(ACCOUNTS_ENDPOINT, name, new BigDecimal(1000), false)
        when:
            def response = processPutRequest(ACCOUNTS_ENDPOINT + "/" + account.id + "/activate")
        then:
            response.statusCode == SC_OK
        when:
            response = processGetRequest(ACCOUNTS_ENDPOINT + "/" + account.id)
        then:
            response.statusCode == SC_OK
            response.body.as(Account).active
    }

    def "Deactivate account - basic positive test"() {
        given:
            def account = Scenarios.createActiveAccountWithFundsAndGet(ACCOUNTS_ENDPOINT, name)
        when:
            def response = processPutRequest(ACCOUNTS_ENDPOINT + "/" + account.id + "/deactivate")
        then:
            response.statusCode == SC_OK
        when:
            response = processGetRequest(ACCOUNTS_ENDPOINT + "/" + account.id)
        then:
            response.statusCode == SC_OK
            !response.body.as(Account).active
    }

}