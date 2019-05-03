package apps.abhibhardwaj.com.doctriod.patient.appointments;

import android.os.Parcel;
import android.os.Parcelable;

public class Doctor implements Parcelable {

  private String address, id, location, name, password, specialization, username, phone, profileURL;
  private Long rating;

  public Doctor() {

  }

  public Doctor(String address, String id, String location, String name, String password,
      String specialization, String username, String phone, String profileURL, Long rating) {
    this.address = address;
    this.id = id;
    this.location = location;
    this.name = name;
    this.password = password;
    this.specialization = specialization;
    this.username = username;
    this.phone = phone;
    this.profileURL = profileURL;
    this.rating = rating;
  }

  protected Doctor(Parcel in) {
    address = in.readString();
    id = in.readString();
    location = in.readString();
    name = in.readString();
    password = in.readString();
    specialization = in.readString();
    username = in.readString();
    phone = in.readString();
    profileURL = in.readString();
    if (in.readByte() == 0) {
      rating = null;
    } else {
      rating = in.readLong();
    }
  }

  public static final Creator<Doctor> CREATOR = new Creator<Doctor>() {
    @Override
    public Doctor createFromParcel(Parcel in) {
      return new Doctor(in);
    }

    @Override
    public Doctor[] newArray(int size) {
      return new Doctor[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(address);
    dest.writeString(id);
    dest.writeString(location);
    dest.writeString(name);
    dest.writeString(password);
    dest.writeString(specialization);
    dest.writeString(username);
    dest.writeString(phone);
    dest.writeString(profileURL);
    if (rating == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeLong(rating);
    }
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getSpecialization() {
    return specialization;
  }

  public void setSpecialization(String specialization) {
    this.specialization = specialization;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getProfileURL() {
    return profileURL;
  }

  public void setProfileURL(String profileURL) {
    this.profileURL = profileURL;
  }

  public Long getRating() {
    return rating;
  }

  public void setRating(Long rating) {
    this.rating = rating;
  }

  public static Creator<Doctor> getCREATOR() {
    return CREATOR;
  }
}