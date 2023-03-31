package com.devaffeine.auth.repository

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.r2dbc.core.DatabaseClient
import reactor.test.StepVerifier
import java.time.Duration

@DataR2dbcTest
class AuthUserRepositoryTests {
    @Autowired
    lateinit var client: DatabaseClient

    @Autowired
    lateinit var userRepository: UserRepository

    @Test
    fun contextLoads() {
        assertNotNull(client)
        assertNotNull(userRepository)
    }

    @Test
    fun testQueryNotExistent() {
        val username = "user-${System.currentTimeMillis()}"
        userRepository
            .findByUsername(username)
            .take(Duration.ofSeconds(1))
            .`as`(StepVerifier::create)
            .expectNextCount(0)
            .verifyComplete()
    }

    @Test
    fun testInsertAndQuery() {
        val username = "user-${System.currentTimeMillis()}"
        val password = "password-${System.currentTimeMillis()}"
        client
            .sql("INSERT INTO auth_user (name, username, password) VALUES (:name, :username, :password)")
            .bind("name", username)
            .bind("username", username)
            .bind("password", password)
            .fetch()
            .rowsUpdated()
            .`as`(StepVerifier::create)
            .expectNextCount(1)
            .verifyComplete()
        userRepository
            .findByUsername(username)
            .take(Duration.ofSeconds(1))
            .`as`(StepVerifier::create)
            .expectNextCount(1)
            .verifyComplete()
    }
}
