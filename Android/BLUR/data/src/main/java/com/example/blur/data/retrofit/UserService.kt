package com.example.blur.data.retrofit

import com.example.blur.data.model.CommonResponse
import com.example.blur.data.model.SessionTestResponse
import com.example.blur.data.model.SignUpResponse
import com.example.blur.data.model.main.setting.ChangePasswordResponse
import com.example.blur.data.model.main.userinfo.ProfileResponse
import com.example.blur.data.model.main.userinfo.UserInfoResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface UserService {
    @GET("/login")
    suspend fun login(
        @Query("username") username: String,
        @Query("password") password: String,
    ): Response<CommonResponse<String>> // Retrofit의 Response 타입으로 변경

    @POST("/register/user/")
    suspend fun signup(
        @Query("originalname") originalname: String,
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("email") email: String,
    ): SignUpResponse<String>

    @POST("/session_test")
    suspend fun sessionTest(): Response<SessionTestResponse>

    @GET("/reset_password")
    fun findPassword(
        @Query("username") username: String,
        @Query("email") email: String,
    ): Call<ResponseBody>


    @POST("/change_password")
    suspend fun changePassword(
        @Query("current_password") current_password: String,
        @Query("new_password") new_password: String,
    ): Response<ChangePasswordResponse>

    @Multipart
    @POST("development_image")
    fun uploadImage(
        @Part image: MultipartBody.Part,
    ): Call<ResponseBody>

    @POST("/get_user_info")
    suspend fun getUserInfo(): Response<UserInfoResponse>


    @POST("/change_profile")
    suspend fun changeEmail(
        @Query("type") type: String,
        @Query("email") email: String?, // type이 email일 때 사용될 필드
    ): ProfileResponse

    @POST("/change_profile")
    suspend fun changeName(
        @Query("type") type: String,
        @Query("name") name: String?, // type이 email일 때 사용될 필드
    ): ProfileResponse
}

