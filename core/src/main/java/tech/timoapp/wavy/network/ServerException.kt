package tech.timoapp.wavy.network

class ServerException(val code: Int) : Exception("Server error: $code")
