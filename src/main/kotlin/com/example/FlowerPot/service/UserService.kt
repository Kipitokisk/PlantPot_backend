package com.example.FlowerPot.service

import com.example.FlowerPot.entity.Role
import com.example.FlowerPot.entity.User
import com.example.FlowerPot.exception.*
import com.example.FlowerPot.repository.UserRepository
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
@Slf4j
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    private val log: Logger = LoggerFactory.getLogger(UserService::class.java)

    fun registerUser(email:String, password: String): User {
        log.info("Initializing registration of user: ${email}")

        if (userRepository.findByEmail(email).isPresent) {
            log.warn("User already exists with email: ${email}")
            throw UserAlreadyExistsException("User with email ${email} already exists!")
        }

        return try {
            val encodedPassword = passwordEncoder.encode(password)
            val savedUser = userRepository.save(User(email = email, password = encodedPassword, role = Role.USER))
            log.info("User successfully registered with email: ${savedUser.email}")
            savedUser
        } catch (e: DataAccessException) {
            log.error("Error occurred while saving user with email: ${email}. Exception: ${e.message}")
            throw UserRegistrationException("Could not save user with email: ${email}", e)
        }
    }


    fun loginUser(email: String, password: String): User {
        log.info("Attempting to login user with email: $email")

        val user = userRepository.findByEmail(email).orElseThrow {
            log.warn("User not found with email: $email")
            UserNotFoundException("User with email $email not found.")
        }

        if (!passwordEncoder.matches(password, user.password)) {
            log.warn("Invalid credentials for email: $email")
            throw InvalidCredentialsException("Invalid credentials for email $email.")
        }

        log.info("User login successful for email: $email")
        return user
    }
}