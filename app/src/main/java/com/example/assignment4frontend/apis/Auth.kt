package com.example.assingment4.apis

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.io.Serializable

data class RegisterRequest(
    val name: String,
    val username: String,
    val contact: Int,
    val email: String,
    val password: String
)

data class RegisterResponse(
    val name: String,
    val username: String,
    val email: String,
    val password: String,
    val contact: Int,
    val _id: String,
    val __v: Int
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val message: String,
    val token: String
)

data class Recipe(
    val _id: String,
    val recipeName: String,
    val ingredients: List<String>,
    val cookingTime: Int,
    val difficulty: String,
    val cuisine: String,
    val description: String,
    val photoLink: String,
    val averageRating: Double
) : Serializable

interface AuthApi {
    @POST("user/create")
    fun registerUser(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("user/login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>

    @GET("getTopRecipe")
    fun getTop20Recipes(@Header("Authorization") token: String): Call<List<Recipe>>

    @PUT("recipe/update/{id}")
    fun updateRecipe(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body recipe: Recipe
    ): Call<Recipe>

    @DELETE("recipe/delete/{id}")
    fun deleteRecipe(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<Void>

    @POST("recipe/add")
    fun createRecipe(
        @Header("Authorization") token: String,
        @Body recipe: Recipe
    ): Call<Recipe>
}
