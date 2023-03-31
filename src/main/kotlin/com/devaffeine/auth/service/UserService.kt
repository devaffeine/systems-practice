package com.devaffeine.auth.service

import com.devaffeine.auth.exceptions.InvalidCredentialsException
import com.devaffeine.auth.exceptions.UsernameAlreadyExistsException
import com.devaffeine.auth.model.AuthUser
import com.devaffeine.auth.repository.UserRepository
import org.springframework.dao.DuplicateKeyException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserService(val userRepository: UserRepository, val passwordEncoder: PasswordEncoder) {
    fun signIn(username: String, password: String): Mono<AuthUser> {
        return userRepository.findByUsername(username)
            .switchIfEmpty(Mono.error(InvalidCredentialsException("Invalid credentials")))
            .doOnNext {
                val passwordMatch = passwordEncoder.matches(password, it.password)
                if (!passwordMatch) {
                    throw InvalidCredentialsException("Invalid credentials")
                }
            }
    }

    fun findUserByUsername(username: String): Mono<AuthUser> {
        return userRepository.findByUsername(username)
    }

    fun saveUser(authUser: AuthUser): Mono<AuthUser> {
        val encodedPassword = passwordEncoder.encode(authUser.password)
        val user = AuthUser(authUser.id, authUser.name, authUser.username, encodedPassword)
        return userRepository.save(user)
            .onErrorMap(::mapException)
    }

    fun mapException(e: Throwable): Throwable {
        return when (e) {
            is DuplicateKeyException -> UsernameAlreadyExistsException("Username already exists.", e)
            else -> e
        }
    }
}
