package org.example.domains.bank.repository

import org.example.types.entity.Account
import org.springframework.data.jpa.repository.JpaRepository

interface BankAccountRepository : JpaRepository<Account, String> {
    fun findByUlid(ulid: String) : Account?
}