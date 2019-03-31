package apps.abhibhardwaj.com.doctriod.patient.nearby;

import apps.abhibhardwaj.com.doctriod.patient.models.NearbyPlacesModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NearPlaceInterface {
  @GET("nearbysearch/json")
  Call<NearbyPlacesModel> getNearbyPlace (@Query("location") String location,
      @Query("radius") int radius,
      @Query("types") String types,
      @Query("key") String APIkey);
}
