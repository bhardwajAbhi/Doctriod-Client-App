package apps.abhibhardwaj.com.doctriod.patient.reminder;

public class PillReminder {

  private Integer id;
  private String name;
  private String days;
  private String dosage;
  private String time;
  private String message;

  public PillReminder() {

  }

  public PillReminder(Integer id, String name, String days, String dosage, String time,
      String message) {
    this.id = id;
    this.name = name;
    this.days = days;
    this.dosage = dosage;
    this.time = time;
    this.message = message;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDays() {
    return days;
  }

  public void setDays(String days) {
    this.days = days;
  }

  public String getDosage() {
    return dosage;
  }

  public void setDosage(String dosage) {
    this.dosage = dosage;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void setDosage(int value) {
  }
}