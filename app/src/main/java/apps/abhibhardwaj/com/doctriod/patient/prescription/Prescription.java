package apps.abhibhardwaj.com.doctriod.patient.prescription;

public class Prescription {

  String imageName, imageURL, description, doctorName, doctorID, doctorSpecialization, doctorAddres, prescriptionID;

  public Prescription() {
  }


  public String getPrescriptionID() {
    return prescriptionID;
  }

  public void setPrescriptionID(String prescriptionID) {
    this.prescriptionID = prescriptionID;
  }

  public String getImageName() {
    return imageName;
  }

  public void setImageName(String imageName) {
    this.imageName = imageName;
  }

  public String getImageURL() {
    return imageURL;
  }

  public void setImageURL(String imageURL) {
    this.imageURL = imageURL;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDoctorName() {
    return doctorName;
  }

  public void setDoctorName(String doctorName) {
    this.doctorName = doctorName;
  }

  public String getDoctorID() {
    return doctorID;
  }

  public void setDoctorID(String doctorID) {
    this.doctorID = doctorID;
  }

  public String getDoctorSpecialization() {
    return doctorSpecialization;
  }

  public void setDoctorSpecialization(String doctorSpecialization) {
    this.doctorSpecialization = doctorSpecialization;
  }

  public String getDoctorAddres() {
    return doctorAddres;
  }

  public void setDoctorAddres(String doctorAddres) {
    this.doctorAddres = doctorAddres;
  }
}
