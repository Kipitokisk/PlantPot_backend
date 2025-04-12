package com.example.FlowerPot.exception

import java.lang.RuntimeException

class UserAlreadyExistsException(message: String): RuntimeException(message)