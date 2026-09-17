package tech.timoapp.wavy.domain.model

import tech.timoapp.wavy.network.ServerException

class ProductNotRemovableException : Exception("Product is not removable")

typealias ServerException = tech.timoapp.wavy.network.ServerException
