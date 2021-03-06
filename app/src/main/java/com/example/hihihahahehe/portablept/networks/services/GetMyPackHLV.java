package com.example.hihihahahehe.portablept.networks.services;

import com.example.hihihahahehe.portablept.models.JSONModel.GetPackJSONModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by hieuit on 9/2/17.
 */

public interface GetMyPackHLV {
    @GET("get-my-pack-hlv/{id}")
    Call<List<GetPackJSONModel>> getPacks(@Path("id") String id);
}
