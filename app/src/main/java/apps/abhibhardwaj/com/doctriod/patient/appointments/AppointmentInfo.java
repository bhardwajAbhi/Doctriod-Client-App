package apps.abhibhardwaj.com.doctriod.patient.appointments;

public class AppointmentInfo {

  private String customerName, customerPhone, doctorID, doctorName, time, docSpec, docAddess, docPhone, appointmentID, userID;
  private Long slot;

  public AppointmentInfo() {
  }

  public String getUserID() {
    return userID;
  }

  public void setUserID(String userID) {
    this.userID = userID;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public String getCustomerPhone() {
    return customerPhone;
  }

  public void setCustomerPhone(String customerPhone) {
    this.customerPhone = customerPhone;
  }

  public String getDoctorID() {
    return doctorID;
  }

  public void setDoctorID(String doctorID) {
    this.doctorID = doctorID;
  }

  public String getDoctorName() {
    return doctorName;
  }

  public void setDoctorName(String doctorName) {
    this.doctorName = doctorName;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getDocSpec() {
    return docSpec;
  }

  public void setDocSpec(String docSpec) {
    this.docSpec = docSpec;
  }

  public String getDocAddess() {
    return docAddess;
  }

  public void setDocAddess(String docAddess) {
    this.docAddess = docAddess;
  }

  public String getDocPhone() {
    return docPhone;
  }

  public void setDocPhone(String docPhone) {
    this.docPhone = docPhone;
  }

  public Long getSlot() {
    return slot;
  }

  public void setSlot(Long slot) {
    this.slot = slot;
  }

  public String getAppointmentID() {
    return appointmentID;
  }

  public void setAppointmentID(String appointmentID) {
    this.appointmentID = appointmentID;
  }
}