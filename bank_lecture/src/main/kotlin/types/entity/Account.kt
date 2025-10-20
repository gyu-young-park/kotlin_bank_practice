package org.example.types.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal

@Table(name = "account")
@Entity
data class Account(
    @Id
    @Column(name = "ulid", length = 12, nullable = false)
    val ulid: String,

    // TODO
    @Column(name = "balance", nullable = false, precision = 15, scale = 2)
    var balance: BigDecimal = BigDecimal.ZERO
)
