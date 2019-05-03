package apps.abhibhardwaj.com.doctriod.patient.appointments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AppointmentPagerAdapter extends FragmentPagerAdapter {

  public AppointmentPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public Fragment getItem(int i) {

    switch (i)
    {
      case 0:
      {
        return BookAppointment1Fragment.getInstance();
      }
      case 1:
      {
        return BookAppointment2Fragment.getInstance();
      }

      case 2:
      {
        return BookAppointment3Fragment.getInstance();
      }

      case 3:
      {
        return BookAppointment4Fragment.getInstance();
      }

      default:
      {
        return null;
      }
    }
  }

  @Override
  public int getCount() {
    return 4;
  }
}
