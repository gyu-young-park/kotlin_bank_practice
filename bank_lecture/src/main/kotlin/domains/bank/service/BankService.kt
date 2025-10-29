package org.example.domains.bank.service

import com.github.f4b6a3.ulid.UlidCreator
import org.example.common.exception.CustomException
import org.example.common.exception.ErrorCode
import org.example.common.logging.Logging
import org.example.common.transaction.Transactional
import org.example.domains.bank.repository.BankAccountRepository
import org.example.domains.bank.repository.BankUserRepository
import org.example.types.dto.Response
import org.example.types.dto.ResponseProvider
import org.example.types.entity.Account
import org.slf4j.Logger
import org.springframework.stereotype.Service
import java.lang.Exception
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.random.Random

@Service
class BankService(
    private val transaction: Transactional,
    private val bankUserRepository: BankUserRepository,
    private val bankAccountRepository: BankAccountRepository,
    private val log: Logger = Logging.getLogger(BankService::class.java)
) {
    fun createAccount(userUIlid: String) : Response<String> = Logging.logFor(log) { log ->
        log["userUlid"] = userUIlid
        transaction.run {
            val user = bankUserRepository.findByUlid(userUIlid)
            val ulid = UlidCreator.getUlid().toString()
            val accountNumber = generateRandomAccountNumber()

            val account = Account(
                ulid = ulid,
                user = user,
                accountNumber = accountNumber,
            )

            try {
                bankAccountRepository.save(account)
            } catch (e: Exception) {
                throw CustomException(ErrorCode.FAILED_TO_SAVE_DATA, e.message)
            }

        }

        return@logFor ResponseProvider.success("SUCCESS")
    }

    fun balance(userUlid: String, accountUlid: String) : Response<BigDecimal> = Logging.logFor(log) { log ->
        log["userUlid"] = userUlid
        log["accountUlid"] = accountUlid

        return@logFor transaction.run {
            val account = bankAccountRepository.findByUlid(accountUlid) ?: throw CustomException(ErrorCode.FAILED_TO_FIND_ACCOUNT, accountUlid)
            if (account.user.ulid != userUlid) throw CustomException(ErrorCode.MISS_MATCH_ACCOUNT_ULID_AND_USER_ULD)
            return@run ResponseProvider.success(account.balance)
        }
    }

    fun removeAccount(userUlid: String, accountUlid: String) : Response<String> = Logging.logFor(log) { log ->
        log["userUlid"] = userUlid
        log["accountUlid"] = accountUlid

        return@logFor transaction.run {
            val account = bankAccountRepository.findByUlid(accountUlid) ?: throw CustomException(ErrorCode.FAILED_TO_FIND_ACCOUNT, accountUlid)

            if (account.user.ulid != userUlid) throw CustomException(ErrorCode.MISS_MATCH_ACCOUNT_ULID_AND_USER_ULD)
            if (account.balance.compareTo(BigDecimal.ZERO) != 0) throw CustomException(ErrorCode.ACCOUNT_BALANCE_IS_NOT_ZERO, accountUlid)

            val updatedAccount = account.copy(
                isDeleted = true,
                deletedAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            )

            bankAccountRepository.save(updatedAccount)
            return@run ResponseProvider.success("SUCCESS")
        }
    }

    private fun generateRandomAccountNumber(): String {
        val bankCode = "003"
        val section = "12"

        val number = Random.toString()
        return "$bankCode-$section-$number"
    }
}