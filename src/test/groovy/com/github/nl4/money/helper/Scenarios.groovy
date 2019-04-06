package com.github.nl4.money.helper

import com.github.nl4.money.api.Account

import static com.github.nl4.money.helper.RequestSpecificationTemplate.processGetRequest
import static com.github.nl4.money.helper.RequestSpecificationTemplate.processPostRequest

class Scenarios {

    static Account createAccountAndGet(def endpoint, def name, def balance, def active) {
        def request = Account.builder()
                .userName(name)
                .balance(balance)
                .active(active)
                .build()
        processPostRequest(endpoint, request)
        def response = processGetRequest(endpoint)
        def accounts = response.body.as(Account[])
        def account = accounts.find { it -> it.userName == name }
        account
    }

    static Account createActiveAccountWithFundsAndGet(def endpoint, def name) {
        createAccountAndGet(endpoint, name, new BigDecimal(1000), true)
    }

}