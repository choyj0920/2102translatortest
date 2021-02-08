package com.example.a2102translatortest


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import kotlinx.android.synthetic.main.activity_json.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JsonActivity : AppCompatActivity() {

    lateinit var api :API
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_json)
        val retrofit = RetrofitClient.client2
        api = retrofit.create(API::class.java)

        btn_getjson.setOnClickListener { //
            getJson()
        }
    }

    fun getJson(){
        var indextext=tv_jsonIndex.text.toString()
        Log.d("TAG", "index : $indextext 값 가져옵니다.")
        if(indextext==""){ // 비어있을때는 변역이 되지 않으므로
            return }
        api.getsjson(indextext).enqueue(object : Callback<jsonClass?> {
            override fun onResponse( call: Call<jsonClass?>, response: Response<jsonClass?>) {
                val result = response.body()
                if (result != null) {
                    var result_text= result // result 저장
                    tv_jsontext.setText("userid ${result_text.userId} \nid : ${result_text.id}\n title : ${result_text.title}\n body : ${result_text.body}\n")
                    Log.d("TAG", "성공 :,${result_text.userId}, ${result_text.id}")
                }
            }
            override fun onFailure(call: Call<jsonClass?>, t: Throwable) {
                Log.d("TAG", "실패 : $t") } })
    }
}