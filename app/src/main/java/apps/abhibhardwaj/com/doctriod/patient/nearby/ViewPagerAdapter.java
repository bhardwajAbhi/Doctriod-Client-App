package apps.abhibhardwaj.com.doctriod.patient.nearby;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import apps.abhibhardwaj.com.doctriod.patient.models.Result;

class ViewPagerAdapter extends FragmentPagerAdapter {

  private Result placeDetails;

  public ViewPagerAdapter(FragmentManager fm, Result placeDetails) {
    super(fm);
    this.placeDetails = placeDetails;
  }

  @Override
  public Fragment getItem(int i) {
    switch (i)
    {
      case 0:
      {
        Bundle bundle = new Bundle();
        PlaceInfoFragment fragment = new PlaceInfoFragment();
        bundle.putString("placeName", placeDetails.getName());
        bundle.putString("placeAddress", placeDetails.getFormattedAddress());
        fragment.setArguments(bundle);
        return fragment;
      }
      case 1:
      {
        return new PlaceReviewFragment();
      }
      case 2:
      {
        Bundle bundle = new Bundle();
        PlaceMapFragment fragment = new PlaceMapFragment();
        bundle.putString("Lat", String.valueOf(placeDetails.getGeometry().getLocation().getLat()));
        bundle.putString("Lng", String.valueOf(placeDetails.getGeometry().getLocation().getLng()));
        fragment.setArguments(bundle);
        return fragment;
      }
      default:
      {
        return null;
      }
    }
  }

  @Override
  public int getCount() {
    return 3;
  }

  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {

    switch (position)
    {
      case 0:
      {
        return "INFO";
      }
      case 1:
      {
        return "REVIEWS";
      }
      case 2:
      {
        return "MAPS";
      }
      default:
      {
        return null;
      }
    }


  }
}