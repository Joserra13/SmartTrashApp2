package dte.masteriot.mdp.smarttrashapp2;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ThingsboardService {

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @POST("auth/login")
    Call<JsonObject> getToken (@Body Usuario user);

    //Paper BIN
    @Headers({"Accept: application/json"})
    @GET("plugins/telemetry/DEVICE/5c0bf7a0-730d-11ec-9a04-591db17ccd5b/values/timeseries?keys=capacity")
    Call<JsonObject> getLatestPaperTel (@Header("X-Authorization") String token);

    //Glass
    @Headers({"Accept: application/json"})
    @GET ("plugins/telemetry/DEVICE/41ff1d10-730d-11ec-9a04-591db17ccd5b/values/timeseries?keys=capacity")
    Call<JsonObject> getLatestGlassTel (@Header("X-Authorization") String token);

    //Plastic
    @Headers({"Accept: application/json"})
    @GET ("plugins/telemetry/DEVICE/2e4cd8c0-730d-11ec-9a04-591db17ccd5b/values/timeseries?keys=capacity")
    Call<JsonObject> getLatestPlasticTel (@Header("X-Authorization") String token);

    //Organic
    @Headers({"Accept: application/json"})
    @GET ("plugins/telemetry/DEVICE/f9419f10-7309-11ec-9a04-591db17ccd5b/values/timeseries?keys=capacity")
    Call<JsonObject> getLatestOrganicTel (@Header("X-Authorization") String token);

    //STREET Plastic Container
    @Headers({"Accept: application/json"})
    @GET ("plugins/telemetry/DEVICE/6e2b9ca0-7971-11ec-9a04-591db17ccd5b/values/timeseries?keys=capacity,humidity,temperature,X,Y,Z")
    Call<JsonObject> getPlasticStreetContainersLatestTelemetry (@Header("X-Authorization") String token);

    //STREET Plastic Container ALARM
    @Headers({"Accept: application/json"})
    @GET ("alarm/DEVICE/6e2b9ca0-7971-11ec-9a04-591db17ccd5b?pageSize=5&page=1&sortOrder=descendant")
    Call<JsonObject> getPlasticStreetContainersAlarm (@Header("X-Authorization") String token);

    //STREET Paper Container
    @Headers({"Accept: application/json"})
    @GET ("plugins/telemetry/DEVICE/1810a5d0-790e-11ec-9a04-591db17ccd5b/values/timeseries?keys=capacity,humidity,temperature,X,Y,Z")
    Call<JsonObject> getPaperStreetContainersLatestTelemetry (@Header("X-Authorization") String token);

    //STREET Paper Container ALARM
    @Headers({"Accept: application/json"})
    @GET ("alarm/DEVICE/1810a5d0-790e-11ec-9a04-591db17ccd5b?pageSize=5&page=1&sortOrder=descendant")
    Call<JsonObject> getPaperStreetContainersAlarm (@Header("X-Authorization") String token);

    //STREET Organic Container
    @Headers({"Accept: application/json"})
    @GET ("plugins/telemetry/DEVICE/3c23e860-7891-11ec-9a04-591db17ccd5b/values/timeseries?keys=capacity,humidity,temperature,X,Y,Z")
    Call<JsonObject> getOrganicStreetContainersLatestTelemetry (@Header("X-Authorization") String token);

    //STREET Organic Container ALARM
    @Headers({"Accept: application/json"})
    @GET ("alarm/DEVICE/3c23e860-7891-11ec-9a04-591db17ccd5b?pageSize=5&page=1&sortOrder=descendant")
    Call<JsonObject> getOrganicStreetContainersAlarm (@Header("X-Authorization") String token);

    //STREET Glass Container
    @Headers({"Accept: application/json"})
    @GET ("plugins/telemetry/DEVICE/ddca70e0-7971-11ec-9a04-591db17ccd5b/values/timeseries?keys=capacity,humidity,temperature,X,Y,Z")
    Call<JsonObject> getGlassStreetContainersLatestTelemetry (@Header("X-Authorization") String token);

    //STREET Glass Container ALARM
    @Headers({"Accept: application/json"})
    @GET ("alarm/DEVICE/3c23e860-7891-11ec-9a04-591db17ccd5b?pageSize=5&page=1&sortOrder=descendant")
    Call<JsonObject> getGlassStreetContainersAlarm (@Header("X-Authorization") String token);

}
