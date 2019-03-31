package apps.abhibhardwaj.com.doctriod.patient.nearby;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

  static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/";
  static Retrofit retrofit = null;

  public static Retrofit getRetrofitInstance() {
    if (retrofit == null) {
      retrofit = new Retrofit.Builder()
          .baseUrl(BASE_URL)
          .addConverterFactory(GsonConverterFactory.create())
          .build();
    }
    return retrofit;
  }
}
