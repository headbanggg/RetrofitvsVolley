package com.fo.retrofitvsvolley;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    private TextView textViewResult;
    private RetrofitAPI retrofitAPI;
    RequestQueue requestQueue;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewResult = findViewById(R.id.text_view_result);

        //initVolley();
        //getPostsWithVolley();
        //getPostsWithVolleySortedAndFilteredById();
        //getCommentssWithVolleyIdFiltered();
        //getCommentsWithVolleyPostIdFiltered();
        //createPostWithVolley();

        initRetrofit();
        getPostsWithRetrofit();
        //getPostsWithRetrofitSortedAndFilteredById();
        //getCommentsWithRetrofit();
        //createPostWithRetrofit();
        //updatePostWithRetrofit();
        //deletePostWithRetrofit();
    }

    private void initRetrofit(){

        //logu açmak için kullanılır
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        Request newRequest = originalRequest.newBuilder()
                                .header("Interceptor-Header", "xyz")
                                .build();

                        return chain.proceed(newRequest);
                    }
                })
                .addInterceptor(loggingInterceptor)
                .build();


        //retrofit null fieldları defaultta hiç göndermiyor. göndermesini istiyorsak bu gsonu GsonConverterFactory'e ekliyoruz.
        Gson gson1 = new GsonBuilder().serializeNulls().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        retrofitAPI = retrofit.create(RetrofitAPI.class);

    }

    private void initVolley(){
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        gson = gsonBuilder.create();


    }



    private void getPostsWithRetrofit() {

        Call<List<Post>> call = retrofitAPI.getPosts();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {


                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                List<Post> posts = response.body();

                for (Post post : posts) {
                    String content = "";
                    content += "ID: " + post.getId() + "\n";
                    content += "User ID: " + post.getUserId() + "\n";
                    content += "Title: " + post.getTitle() + "\n";
                    content += "Text: " + post.getText() + "\n\n";

                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    private void getPostsWithRetrofitSortedAndFilteredById() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userId", "1");
        parameters.put("_sort", "id");
        parameters.put("_order", "desc");

        Call<List<Post>> call = retrofitAPI.getPosts(parameters);

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                List<Post> posts = response.body();

                for (Post post : posts) {
                    String content = "";
                    content += "ID: " + post.getId() + "\n";
                    content += "User ID: " + post.getUserId() + "\n";
                    content += "Title: " + post.getTitle() + "\n";
                    content += "Text: " + post.getText() + "\n\n";

                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    private void getCommentsWithRetrofit() {
        Call<List<Comment>> call = retrofitAPI
                .getComments("https://jsonplaceholder.typicode.com/posts/3/comments");

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {

                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                List<Comment> comments = response.body();

                for (Comment comment : comments) {
                    String content = "";
                    content += "ID: " + comment.getId() + "\n";
                    content += "Post ID: " + comment.getPostId() + "\n";
                    content += "Name: " + comment.getName() + "\n";
                    content += "Email: " + comment.getEmail() + "\n";
                    content += "Text: " + comment.getText() + "\n\n";

                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    private void createPostWithRetrofit() {
        Post post = new Post(23, "New Title", "New Text");

        Map<String, String> fields = new HashMap<>();
        fields.put("userId", "25");
        fields.put("title", "New Title");

        Call<Post> call = retrofitAPI.createPost(fields);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                Post postResponse = response.body();

                String content = "";
                content += "Code: " + response.code() + "\n";
                content += "ID: " + postResponse.getId() + "\n";
                content += "User ID: " + postResponse.getUserId() + "\n";
                content += "Title: " + postResponse.getTitle() + "\n";
                content += "Text: " + postResponse.getText() + "\n\n";

                textViewResult.setText(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    private void updatePostWithRetrofit() {
        Post post = new Post(12, null, "New Text");

        Map<String, String> headers = new HashMap<>();
        headers.put("Map-Header1", "def");
        headers.put("Map-Header2", "ghi");

        Call<Post> call = retrofitAPI.patchPost(headers, 5, post);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                Post postResponse = response.body();

                String content = "";
                content += "Code: " + response.code() + "\n";
                content += "ID: " + postResponse.getId() + "\n";
                content += "User ID: " + postResponse.getUserId() + "\n";
                content += "Title: " + postResponse.getTitle() + "\n";
                content += "Text: " + postResponse.getText() + "\n\n";

                textViewResult.setText(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    private void deletePostWithRetrofit() {
        Call<Void> call = retrofitAPI.deletePost(5);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                textViewResult.setText("Code: " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }





    private void getPostsWithVolley(){

        final String url = "https://jsonplaceholder.typicode.com/posts";

        JsonArrayRequest getRequest = new JsonArrayRequest(com.android.volley.Request.Method.GET, url, null,
                new com.android.volley.Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {

                       Post[] posts = gson.fromJson(response.toString(), Post[].class);

                        for (Post post : posts) {
                            String content = "";
                            content += "ID: " + post.getId() + "\n";
                            content += "User ID: " + post.getUserId() + "\n";
                            content += "Title: " + post.getTitle() + "\n";
                            content += "Text: " + post.getText() + "\n\n";

                            textViewResult.append(content);
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        );

        requestQueue.add(getRequest);
    }

    //get requestinde parametreyi url içine girmek gerekiyor.
    private void getPostsWithVolleySortedAndFilteredById(){
        final String url = "https://jsonplaceholder.typicode.com/posts?userId=1&_sort=id&_order=desc";
        JsonArrayRequest getRequest = new JsonArrayRequest(com.android.volley.Request.Method.GET, url, null,
                new com.android.volley.Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {

                        Post[] posts = gson.fromJson(response.toString(), Post[].class);

                        for (Post post : posts) {
                            String content = "";
                            content += "ID: " + post.getId() + "\n";
                            content += "User ID: " + post.getUserId() + "\n";
                            content += "Title: " + post.getTitle() + "\n";
                            content += "Text: " + post.getText() + "\n\n";

                            textViewResult.append(content);
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        );

        requestQueue.add(getRequest);
    }

    private void getCommentssWithVolleyIdFiltered(){
        int id =1;
        final String url = "https://jsonplaceholder.typicode.com/posts/"+id+"/comments";
        JsonArrayRequest getRequest = new JsonArrayRequest(com.android.volley.Request.Method.GET, url, null,
                new com.android.volley.Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {

                        Comment[] comments = gson.fromJson(response.toString(), Comment[].class);

                        for (Comment comment : comments) {
                            String content = "";
                            content += "postId: " + comment.getPostId() + "\n";
                            content += "id: " + comment.getId() + "\n";
                            content += "name: " + comment.getName() + "\n";
                            content += "email: " + comment.getEmail() + "\n\n";

                            textViewResult.append(content);
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        );

        requestQueue.add(getRequest);

    }

    private void getCommentsWithVolleyPostIdFiltered(){
        int id =1;
        final String url = "https://jsonplaceholder.typicode.com/comments?postId="+id;
        JsonArrayRequest getRequest = new JsonArrayRequest(com.android.volley.Request.Method.GET, url, null,
                new com.android.volley.Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {

                        Comment[] comments = gson.fromJson(response.toString(), Comment[].class);

                        for (Comment comment : comments) {
                            String content = "";
                            content += "postId: " + comment.getPostId() + "\n";
                            content += "id: " + comment.getId() + "\n";
                            content += "name: " + comment.getName() + "\n";
                            content += "email: " + comment.getEmail() + "\n\n";

                            textViewResult.append(content);
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        );

        requestQueue.add(getRequest);

    }

    private void createPostWithVolley(){

        final String url = "https://jsonplaceholder.typicode.com/posts";

        Post post = new Post(23, "New Title", "New Text");

        Map<String, String> fields = new HashMap<>();
        fields.put("userId", "25");
        fields.put("title", "New Title");



        JsonObjectRequest postRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, url, new JSONObject(fields),
                new com.android.volley.Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        Post post = gson.fromJson(response.toString(), Post.class);


                            String content = "";
                            content += "ID: " + post.getId() + "\n";
                            content += "User ID: " + post.getUserId() + "\n";
                            content += "Title: " + post.getTitle() + "\n";
                            content += "Text: " + post.getText() + "\n\n";

                            textViewResult.append(content);

                    }
                },

                new com.android.volley.Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );

        requestQueue.add(postRequest);

    }
}
