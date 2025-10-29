package org.example.domains.bank.controller

import org.example.domains.bank.service.BankService
import org.example.types.dto.Response
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
@RequestMapping("/api/v1/bank")
class BankController(
    private val bankService: BankService,
) {

    @PostMapping("/create/{ulid}")
    fun createAccount(
        @PathVariable("ulid", required = true) ulid: String,
    ) : Response<String> {
        return bankService.createAccount(ulid)
    }

    @GetMapping("/balance/{userUlid}/{accountUlid}")
    fun balance(
        @PathVariable("userUlid", required = true) userUlid: String,
        @PathVariable("accountUlid", required = true) accountUlid: String,
    ): Response<BigDecimal> {
        return bankService.balance(userUlid, accountUlid)
    }

    @GetMapping("/remove/{userUlid}/{accountUlid}")
    fun removeAccount(
        @PathVariable("userUlid", required = true) userUlid: String,
        @PathVariable("accountUlid", required = true) accountUlid: String,
    ): Response<String> {
        return bankService.removeAccount(userUlid, accountUlid)
    }
}