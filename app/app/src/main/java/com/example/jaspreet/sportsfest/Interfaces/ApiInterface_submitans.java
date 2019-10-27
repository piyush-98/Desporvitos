package com.example.jaspreet.sportsfest.Interfaces;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface_submitans {
    @GET("vote/{p1}/{p2}/{p3}/{p4}")
    Call<ResponseBody> getall(
            @Path("p1") String qid,
            @Path("p2") String ans,
            @Path("p3") String amt,
            @Path("p4") String acct
            );
}
