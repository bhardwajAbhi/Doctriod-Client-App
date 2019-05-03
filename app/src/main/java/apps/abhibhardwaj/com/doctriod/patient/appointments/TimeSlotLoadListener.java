package apps.abhibhardwaj.com.doctriod.patient.appointments;

import java.util.List;

public interface TimeSlotLoadListener {

  void onTimeSlotLoadSucess(List<TimeSlot> timeSlotList);
  void onTimeSlotLoadFailed(String message);
  void onTimeSlotLoadEmpty();

}
