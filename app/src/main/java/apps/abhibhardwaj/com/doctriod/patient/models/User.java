package apps.abhibhardwaj.com.doctriod.patient.models;

public class User {

  private String profileImageURL;
  private String profileImageName;
  private String fullName;
  private String email;
  private String phone;
  private String doB;
  private String gender;
  private String bloodGrp;
  private String height;
  private String weight;
  private String address;

  public User()
  {
    // empty constructor
  }

  public String getProfileImageURL() {
    return profileImageURL;
  }

  public void setProfileImageURL(String profileImageURL) {
    this.profileImageURL = profileImageURL;
  }

  public String getProfileImageName() {
    return profileImageName;
  }

  public void setProfileImageName(String profileImageName) {
    this.profileImageName = profileImageName;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getDoB() {
    return doB;
  }

  public void setDoB(String doB) {
    this.doB = doB;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getBloodGrp() {
    return bloodGrp;
  }

  public void setBloodGrp(String bloodGrp) {
    this.bloodGrp = bloodGrp;
  }

  public String getHeight() {
    return height;
  }

  public void setHeight(String height) {
    this.height = height;
  }

  public String getWeight() {
    return weight;
  }

  public void setWeight(String weight) {
    this.weight = weight;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
}
