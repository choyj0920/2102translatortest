package com.example.a2102translatortest

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.*

// ResultTransferPapago.kt

data class ResultTransferPapago (
    var message: Message
)

data class Message(
    var result: Result
)

data class Result (
    var srcLangType: String = "",
    var tarLangType: String = "",
    var translatedText: String = ""
)

class TranslateResponse {
    @SerializedName("message")
    val message : Translatemessage? =null

}
class Translatemessage{

    @SerializedName("type")
    val type : String? =null
    @SerializedName("service")
    val service: String? = null
    @SerializedName("version")
    val version: String? = null
    @SerializedName("result")
    val result:Messageresult? =null

}

class Messageresult{
    @SerializedName("srcLangType")
    val srcLangType : String? =null
    @SerializedName("tarLangType")
    val tarLangType: String? = null
    @SerializedName("translatedText")
    val translatedText: String? = null
}

// NaverAPI 인터페이스
interface API {
    @FormUrlEncoded
    @POST("v1/papago/n2mt")
    fun transferPapago(
        @Header("X-Naver-Client-Id") clientId: String,
        @Header("X-Naver-Client-Secret") clientSecret: String,
        @Field("source") source: String,
        @Field("target") target: String,
        @Field("text") text: String
    ): Call<TranslateResponse?>
}