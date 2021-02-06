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
        val retrofit = RetrofitClient.client
        api = retrofit.create(API::class.java)

        btn_swap_lang.setOnClickListener { // 번역언어 변경
            btn_swap_lang.setText(if(koToen)"영->한" else "한->영")
            koToen = !koToen
            var temp =before_text.text
            before_text.text=after_text.text
            after_text.text=temp
        }
        btn_translate.setOnClickListener { // 번역 버튼 리스너
            translate(before_text.text.toString())
        }
    }
    fun translate(targettext:String){
        if(targettext==""){ // 비어있을때는 변역이 되지 않으므로
            after_text.setText("")
            return
        }
        var callPostTransferPapago = api.transferPapago(RetrofitClient.CLIENT_ID, RetrofitClient.CLIENT_SECRET,
            if(koToen) "ko" else "en", if(koToen) "en" else "ko", targettext ) // 각각 파파고 api 요청변수

        callPostTransferPapago.enqueue(object : Callback<TranslateResponse?> {
            override fun onResponse( call: Call<TranslateResponse?>, response: Response<TranslateResponse?>) {
                val result = response.body()
                if (result != null) {
                    var Translated_text= result.message?.result?.translatedText // result 안에안에 있는 변역된 문장 저장
                    after_text.setText(Translated_text)
                    Log.d("TAG", "성공 :,$targettext -> ${Translated_text}")
                }
            }
            override fun onFailure(call: Call<TranslateResponse?>, t: Throwable) {
                Log.d("TAG", "실패 : $t")
            }
        })

    }
}