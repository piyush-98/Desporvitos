package com.example.jaspreet.sportsfest.Interfaces;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface_getCOSbal {
    @GET("balance/{p1}")
    Call<ResponseBody> getall(
            @Path("p1") String acctname
    );
}

