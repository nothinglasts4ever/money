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

    def "Account should be created when request with all valid values sent"() {
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

    def "Inactive account without funds should be created when request with user name only sent"() {
        given:
            def account = Scenarios.createAccountAndGet(ACCOUNTS_ENDPOINT, name, null, null)
        when:
            def response = processGetRequest(ACCOUNTS_ENDPOINT + "/" + account.id)
        then:
            response.statusCode == SC_OK
            response.body.as(Account).userName == name
            response.body.as(Account).balance == BigDecimal.ZERO
            !response.body.as(Account).active
    }

    def "Account should not be created when request without user name sent"() {
        given:
            def request = Account.builder()
                    .balance(new BigDecimal(100))
                    .active(true)
                    .build()
        when:
            def response = processPostRequest(ACCOUNTS_ENDPOINT, request)
        then:
            response.statusCode == SC_BAD_REQUEST
    }

    def "Account should not be created when request with negative balance sent"() {
        given:
            def request = Account.builder()
                    .userName(name)
                    .balance(new BigDecimal(-100))
                    .active(true)
                    .build()
        when:
            def response = processPostRequest(ACCOUNTS_ENDPOINT, request)
        then:
            response.statusCode == SC_BAD_REQUEST
    }

    def "All existing accounts should be returned when GET /accounts request sent"() {
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

    def "Requested account should be returned when request with existing account id sent"() {
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

    def "404 error should be returned when request with non-existing account id sent"() {
        given:
            def id = Integer.MAX_VALUE
        when:
            def response = processGetRequest(ACCOUNTS_ENDPOINT + "/" + id)
        then:
            response.statusCode == SC_NOT_FOUND
    }

    def "Account should be activated when activation request for existing inactive account sent"() {
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

    def "Account should stay active when activation request for existing active account sent"() {
        given:
            def account = Scenarios.createActiveAccountWithFundsAndGet(ACCOUNTS_ENDPOINT, name)
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

    def "Account should be deactivated when deactivation request for existing active account sent"() {
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

    def "Account should stay inactive when deactivation request for existing inactive account sent"() {
        given:
            def account = Scenarios.createAccountAndGet(ACCOUNTS_ENDPOINT, name, new BigDecimal(1000), false)
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

    def "404 error should be returned when trying to activate and deactivate non-existing account"() {
        given:
            def id = Integer.MAX_VALUE
        when:
            def response = processPutRequest(ACCOUNTS_ENDPOINT + "/" + id + "/activate")
        then:
            response.statusCode == SC_NOT_FOUND
        when:
            response = processPutRequest(ACCOUNTS_ENDPOINT + "/" + id + "/deactivate")
        then:
            response.statusCode == SC_NOT_FOUND
    }

}