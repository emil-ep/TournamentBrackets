package com.ventura.emilp.tournamentbrackets.network;

import com.ventura.emilp.tournamentbrackets.model.WorldCupResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface WorldCupApi {
    @GET("get/games")
    Call<WorldCupResponse> getGames();
}
