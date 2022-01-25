package dte.masteriot.mdp.smarttrashapp2;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static final String BASE_URI = "https://srv-iot.diatel.upm.es/api/"; // "http://192.168.1.92:8080/api/"; //
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASE_URI)
            .client(new OkHttpClient.Builder().addInterceptor((new HttpLoggingInterceptor()).setLevel(HttpLoggingInterceptor.Level.BODY)).build())                .addConverterFactory(GsonConverterFactory.create());
    public static <S> S createService(Class <S> serviceClass) {
        Retrofit adapter = builder.build();
        return adapter.create(serviceClass);
    }
}
