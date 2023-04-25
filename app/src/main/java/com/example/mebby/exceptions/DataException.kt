package com.example.mebby.exceptions

sealed class DataException : Exception {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable) : super(cause)
}

class UserHasAlreadyBeenCreatedException : DataException()
class CurrentUserNullException : DataException()
