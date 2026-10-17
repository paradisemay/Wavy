package tech.timoapp.wavy.domain.model

class ProductNotRemovableException : Exception("Product is not removable")

class ServerException(val code: Int) : Exception("Server error: $code")
