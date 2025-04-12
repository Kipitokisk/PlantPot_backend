package com.example.FlowerPot.controller

import com.example.FlowerPot.dto.AuthRequest
import com.example.FlowerPot.exception.*
import com.example.FlowerPot.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(private val userService: UserService) {

    @PostMapping("/register")
    fun registerUser(@RequestBody authRequest: AuthRequest): ResponseEntity<Any> {
        return try {
            val savedUser = userService.registerUser(authRequest.email, authRequest.password)
            ResponseEntity.status(HttpStatus.CREATED).body(savedUser)
        } catch (e: UserAlreadyExistsException) {
            ResponseEntity.status(HttpStatus.CONFLICT).body(e.message)
        } catch (e: UserRegistrationException) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Could not register user: ${e.message}")
        } catch (e: RuntimeException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Invalid data: ${e.message}")
        }
    }

    @PostMapping("/login")
    fun loginUser(@RequestBody authRequest: AuthRequest): ResponseEntity<Any> {
        return try {
            val user = userService.loginUser(authRequest.email, authRequest.password)
            ResponseEntity.ok(user)
        } catch (e: UserNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        } catch (e: InvalidCredentialsException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.message)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: ${e.message}")
        }
    }
}