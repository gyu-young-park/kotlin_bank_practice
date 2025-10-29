package org.example.types.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Table(name = "account")
@Entity
data class Account(
    @Id
    @Column(name = "ulid", length = 26, nullable = false)
    val ulid: String,

    // TODO
    @Column(name = "balance", nullable = false, precision = 15, scale = 2)
    var balance: BigDecimal = BigDecimal.ZERO,

    @Column(name = "account_number", length = 100, nullable = false, unique = true)
    val accountNumber: String,

    @Column(name = "is_deleted", nullable = false)
    val isDeleted: Boolean = false,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY) // 사용할 때만 가져오기, 이렇게 만드는 것이 효율이 좋다.
    @JoinColumn(name = "user_ulid", nullable = false)
    val user: User,

    @Column(name = "deleted_at", nullable = true)
    val deletedAt: LocalDateTime? = null,
)
