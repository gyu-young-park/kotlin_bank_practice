package org.example.types.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name="user")
data class User(
    @Id
    @Column(name = "ulid", length = 12, nullable = false)
    val ulid: String,

    @Column(name = "platform", nullable = false, precision = 15, scale = 2)
    val platform: String,

    @Column(name = "username", length = 100, nullable = false, unique = true)
    val username: String,

    @Column(name = "access_token", nullable = false)
    val accessToken: String? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "user")
    val accounts: List<Account> = mutableListOf()

)
