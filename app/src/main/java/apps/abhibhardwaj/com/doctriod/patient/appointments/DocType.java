package apps.abhibhardwaj.com.doctriod.patient.appointments;

import android.os.Parcel;
import android.os.Parcelable;

public class DocType implements Parcelable {

  private String docType, id;

  public DocType ()
  {

  }

  public String getDocType() {
    return docType;
  }

  public void setDocType(String docType) {
    this.docType = docType;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public static Creator<DocType> getCREATOR() {
    return CREATOR;
  }

  protected DocType(Parcel in) {
    docType = in.readString();
    id = in.readString();
  }

  public static final Creator<DocType> CREATOR = new Creator<DocType>() {
    @Override
    public DocType createFromParcel(Parcel in) {
      return new DocType(in);
    }

    @Override
    public DocType[] newArray(int size) {
      return new DocType[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(docType);
    dest.writeString(id);
  }
}
