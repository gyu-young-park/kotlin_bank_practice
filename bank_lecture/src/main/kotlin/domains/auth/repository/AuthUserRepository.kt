package org.example.domains.auth.repository

import org.example.types.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface AuthUserRepository : JpaRepository<User, String> {
    fun existsByUsername(username: String) : Boolean

    @Modifying
    @Query("UPDATE User SET accessToken = :accessToken WHERE username = :username")
    fun updateAccessTokenByUserName(
        @Param("username") username: String,
        @Param("accessToken") token: String
    )
}