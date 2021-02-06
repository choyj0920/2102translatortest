package com.example.a2102translatortest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    var koToen=true
    lateinit var api :API



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val retrofit = Retrofit.Builder()
            .baseUrl(RetrofitClient.BASE_URL_NAVER_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(API::class.java)

        btn_swap_lang.setOnClickListener {
            if(koToen) { // 영->한으로 변경
                koToen = false
                btn_swap_lang.setText("영->한")
                var temp =before_text.text
                before_text.text=after_text.text
                after_text.text=temp

            }
            else{
                koToen = true
                btn_swap_lang.setText("한->영")
                var temp =before_text.text
                before_text.text=after_text.text
                after_text.text=temp
            }
        }

        btn_translate.setOnClickListener {
            translate(before_text.text.toString())
        }

    }
    fun translate(targettext:String){
        var callPostTransferPapago = api.transferPapago(RetrofitClient.CLIENT_ID, RetrofitClient.CLIENT_SECRET,
            if(koToen) "ko" else "en", if(koToen) "en" else "ko", targettext )

        callPostTransferPapago.enqueue(object : Callback<TranslateResponse?> {
            override fun onResponse(
                call: Call<TranslateResponse?>,
                response: Response<TranslateResponse?>
            ) {
                val result = response.body()
                if (result != null) {
                    var Translated_text= result.message?.result?.translatedText
                    after_text.setText(Translated_text)
                    Log.d("TAG", "성공 : $result ,$targettext -> ${Translated_text}")
                }

            }

            override fun onFailure(call: Call<TranslateResponse?>, t: Throwable) {
                Log.d("TAG", "실패 : $t")
            }
        })

    }
}