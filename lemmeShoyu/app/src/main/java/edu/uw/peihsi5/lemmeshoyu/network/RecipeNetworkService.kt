package edu.uw.peihsi5.lemmeshoyu.network


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.parcelize.Parcelize
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*
import retrofit2.http.Body

import retrofit2.http.POST
import java.util.function.BinaryOperator


private const val BASE_URL = "https://api.spoonacular.com/recipes/"


//The API Interface
interface RecipeApiService{

    @GET("complexSearch")
    fun searchRecipe(@Query("query")query:String, @Query("apiKey")apiKey: String):Call<RecipeSearchResponse>

    @GET("{id}/ingredientWidget.json")
    fun getRecipeInfo(@Path("id")recipeID:Int, @Query("apiKey")apiKey: String):Call<RecipeSearchResponse>
}
//initialize Moshi
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

//initialize retrofit
private val client = OkHttpClient.Builder().build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(client)
    .build()

object RecipeApi {
    val retrofitService: RecipeApiService by lazy{
        retrofit.create(RecipeApiService::class.java)
    }
}

@Parcelize
data class Recipe(
    @Json(name = "id")
    val id: Int,

    @Json(name = "title")
    var title: String,

    @Json(name = "image")
    val imagePath: String
) : Parcelable

data class RecipeSearchResponse(
    val results: List<Recipe>

)

//@Parcelize
//data class Instruction(
//
//    @Json(name = "number")
//    val step: Int,
//
//    val amount:
//): Parcelable
