# 파파고 api 테스트 & json 가져오기 연습

##  papago api test



<img src="_md/번역1.gif" alt="번역1" style="zoom: 50%;" />

<img src="_md/번역2.gif" alt="번역2" style="zoom: 50%;" />

<img src="_md/번역스왑.gif" alt="번역스왑" style="zoom:50%;" />

파파고 api : https://developers.naver.com/docs/nmt/reference/#papago-nmt-api-reference



#### 파파고 api의 요청 변수

```
POST /v1/papago/n2mt HTTP/1.1
HOST: openapi.naver.com
User-Agent: curl/7.49.1
Accept: */*
Content-Type: application/x-www-form-urlencoded; charset=UTF-8
X-Naver-Client-Id: {애플리케이션 등록 시 발급받은 client id 값}
X-Naver-Client-Secret: {애플리케이션 등록 시 발급받은 client secret 값}
srcLangType		:source language
tarLangType		:target language
translatedText	:	번역된 text
```

#### 파파고 api의 출력 변수

```json
srcLangType	string	source language
tarLangType	string	target language
translatedText	string	번역된 text
```

#### 하지만 response되는 json이 이런구조를 가지고있어

```json
{
    "message": {
        "@type": "response",
        "@service": "naverservice.labs.api",
        "@version": "1.0.0",
        "result": {
            "translatedText": "tea"
        }
    }
}
```

#### response 클래스를 이렇게 구성

```kotlin
class TranslateResponse {  //처음 들어오는 구성
    @SerializedName("message") //메시지
    val message : Translatemessage? =null   }
class Translatemessage{
    @SerializedName("type") //@type
    val type : String? =null
    @SerializedName("service") //@service
    val service: String? = null
    @SerializedName("version") //@version
    val version: String? = null
    @SerializedName("result")	//result
    val result:Messageresult? =null    }
class Messageresult{  //result
    @SerializedName("srcLangType")
    val srcLangType : String? =null
    @SerializedName("tarLangType")
    val tarLangType: String? = null
    @SerializedName("translatedText")
    val translatedText: String? = null    }

// API는 이렇게 구성
interface API {
    @FormUrlEncoded
    @POST("v1/papago/n2mt")
    fun transferPapago(
        @Header("X-Naver-Client-Id") clientId: String,
        @Header("X-Naver-Client-Secret") clientSecret: String,
        @Field("source") source: String,
        @Field("target") target: String,
        @Field("text") text: String
    ): Call<TranslateResponse?> }
```

#### 번역 수행 함수 구성

```kotlin
fun translate(targettext:String){
        if(targettext==""){ // 비어있을때는 변역이 되지 않으므로 
            after_text.setText("")
            return
        }
        var callPostTransferPapago = api.transferPapago(RetrofitClient.CLIENT_ID, RetrofitClient.CLIENT_SECRET, if(koToen) "ko" else "en", if(koToen) "en" else "ko", targettext ) // 각각 파파고 api 요청변수

        callPostTransferPapago.enqueue(object : Callback<TranslateResponse?> { 
            override fun onResponse( 
                call: Call<TranslateResponse?>, 
                response: Response<TranslateResponse?>
            ) {
                val result = response.body()
                if (result != null) {
                    var Translated_text= result.message?.result?.translatedText 
                    				// result 안에안에 있는 변역된 문장 저장
                    after_text.setText(Translated_text)
                    Log.d("TAG", "성공 :,$targettext -> ${Translated_text}")
                }
            }
            override fun onFailure(call: Call<TranslateResponse?>, t: Throwable) {
                Log.d("TAG", "실패 : $t")
            }
        })

    }
```



## json 가져오기

<img src="_md/json연습.gif" alt="json연습" style="zoom: 50%;" />

- https://jsonplaceholder.typicode.com 사용 

- https://jsonplaceholder.typicode.com/posts의 구조

- ```html
  [
    {
      "userId": 1,
      "id": 1,
      "title": "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
      "body": "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto"
    },
    {
      "userId": 1,
      "id": 2,
      "title": "qui est esse",
      "body": "est rerum tempore vitae\nsequi sint nihil reprehenderit dolor beatae ea dolores neque\nfugiat blanditiis voluptate porro vel nihil molestiae ut reiciendis\nqui aperiam non debitis possimus qui neque nisi nulla"
    },
    {
      "userId": 1,
      "id": 3,
      "title": "ea molestias quasi exercitationem repellat qui ipsa sit aut",
      "body": "et iusto sed quo iure\nvoluptatem occaecati omnis eligendi aut ad\nvoluptatem doloribus vel accusantium quis pariatur\nmolestiae porro eius odio et labore et velit aut"
    },
    {
      "userId": 1,
      "id": 4,
      "title": "eum et est occaecati",
      "body": "ullam et saepe reiciendis voluptatem adipisci\nsit amet autem assumenda provident rerum culpa\nquis hic commodi nesciunt rem tenetur doloremque ipsam iure\nquis sunt voluptatem rerum illo velit"
    },
    {
      "userId": 1,
      "id": 5,
      "title": "nesciunt quas odio",
      "body": "repudiandae veniam quaerat sunt sed\nalias aut fugiat sit autem sed est\nvoluptatem omnis possimus esse voluptatibus quis\nest aut tenetur dolor neque"
    },
    ...
  ```

### 가져오는 함수의 인터페이스를 구성할 때 GET , post의 인자안에 변수를 넣어 줄 수 있다.

```kotlin
// 응답 데이터 클래스
class jsonClass{
    @SerializedName("userId")
    val userId : String? =null
    @SerializedName("id")
    val id : String? =null
    @SerializedName("title")
    val title : String? =null
    @SerializedName("body")
    val body : String? =null
}

interface API {
	@GET("/posts/{index}")
    fun getsjson(@Path("index") index: String?): Call<jsonClass>
}
// 사용부 
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
                Log.d("TAG", "실패 : $t") }   })
```



