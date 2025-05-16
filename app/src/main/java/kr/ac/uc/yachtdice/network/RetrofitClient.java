package kr.ac.uc.yachtdice.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://YOUR_SERVER_IP:8080"; // GCP 서버 주소로 교체
    private static Retrofit retrofit;

    public static GameApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(GameApiService.class);
    }
}

