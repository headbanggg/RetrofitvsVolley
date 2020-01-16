package com.fo.retrofitvsvolley;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;


/**
 * Created by Mine Kandil on 2020-01-08.
 */
public interface RetrofitAPI {
    //tüm postları getirir
    @GET("posts")
    Call<List<Post>> getPosts();

    //istenen userIdlere ait postları sıralayıp getirir.
    @GET("posts")
    Call<List<Post>> getPosts(
            @Query("userId") Integer[] userId,
            @Query("_sort") String sort,
            @Query("_order") String order
    );
    //istenen postarı belli parametrelere göre filtreleyip getirir.
    @GET("posts")
    Call<List<Post>> getPosts(@QueryMap Map<String, String> parameters);

    //belli idlere ait commentleri getirir
    @GET("posts/{id}/comments")
    Call<List<Comment>> getComments(@Path("id") int postId);

    //istenen urldeki commentleri getirir.
    @GET
    Call<List<Comment>> getComments(@Url String url);

    //post oluşturur.
    @POST("posts")
    Call<Post> createPost(@Body Post post);

    //url encoded olarak post oluşturur.
    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPost(
            @Field("userId") int userId,
            @Field("title") String title,
            @Field("body") String text
    );
    //url encoded olarak post oluşturur.parametreleri key value çifti olarak alır.
    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPost(@FieldMap Map<String, String> fields);

   /* @PUT("posts/{id}")
    Call<Post> putPost(@Path("id") int id, @Body Post post);

    @PATCH("posts/{id}")
    Call<Post> patchPost(@Path("id") int id, @Body Post post);*/

    @Headers({"Static-Header1: 123", "Static-Header2: 456"})
    @PUT("posts/{id}")
    Call<Post> putPost(@Header("Dynamic-Header") String header,
                       @Path("id") int id,
                       @Body Post post);

    @PATCH("posts/{id}")
    Call<Post> patchPost(@HeaderMap Map<String, String> headers,
                         @Path("id") int id,
                         @Body Post post);


    @DELETE("posts/{id}")
    Call<Void> deletePost(@Path("id") int id);
}
