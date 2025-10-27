package org.example.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
class MySQLConfig(
    @Value("\${database.mysql.url}") val url: String,
    @Value("\${database.mysql.username}") val user: String,
    @Value("\${database.mysql.password}") val pass: String,
    @Value("\${database.mysql.driver-class-name}") val driver: String,
) {
    @Bean
    fun dataSource(): DataSource {
        val config = HikariConfig().apply {
            jdbcUrl = url
            username = user
            password = pass
            driverClassName = driver
            maximumPoolSize = 10
        }
        return HikariDataSource(config)
    }

    @Bean
    fun transactionManager(dataSource: DataSource) : PlatformTransactionManager = DataSourceTransactionManager(dataSource)
}