package apps.abhibhardwaj.com.doctriod.patient.nearby;

import apps.abhibhardwaj.com.doctriod.patient.models.NearbyPlacesModel;
import apps.abhibhardwaj.com.doctriod.patient.models.PlaceDetailsModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlaceDetailsInterface {
  @GET("details/json")
  Call<PlaceDetailsModel> getPlaceDetails (
      @Query("placeid") String placeID,
      @Query("key") String APIkey);

}
