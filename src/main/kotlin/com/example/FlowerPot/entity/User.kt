package com.example.FlowerPot.entity

import jakarta.persistence.*

@Entity
@Table(name = "t_user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @Column(nullable = false, unique = true)
    val email: String,
    @Column(nullable = false)
    var password: String,
    @Column(nullable = false)
    val role: Role
)

