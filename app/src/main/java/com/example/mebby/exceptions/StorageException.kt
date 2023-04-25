package com.example.mebby.exceptions

sealed class StorageException : Exception {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable) : super(cause)
}

class FailedUploadImageException : StorageException()
class DownloadUrlNullException : StorageException()