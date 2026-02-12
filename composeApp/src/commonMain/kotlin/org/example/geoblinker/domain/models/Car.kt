package org.example.geoblinker.domain.models


import kotlinx.serialization.Serializable


@Serializable
data class Cars(
    val code: String,
    val data: ListCars
)


@Serializable
data class ListCars(
    val cars: Map<String, Car>
)


@Serializable
data class Car(
    val id: String = "",
    val registrationPlate: String,


    val details: Details
)


@Serializable
data class Details(
    val imei: String,
    val name: String,
    val isConnected: Boolean = true,
    val bindingTime: Long,
    val typeName: String?,
    val markerId: Int=0,
)


@Serializable
data class ResponseCreateCar(
    val code: String,
    val message: String? = null,
    val data: DataResponseCreateCar
)


@Serializable
data class DataResponseCreateCar(
    val createdCar: IdDataResponseCreateCar
)


@Serializable
data class IdDataResponseCreateCar(
    val cId: String
