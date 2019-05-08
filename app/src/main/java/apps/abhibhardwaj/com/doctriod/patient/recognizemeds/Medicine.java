package apps.abhibhardwaj.com.doctriod.patient.recognizemeds;

public class Medicine {

  private String id, contains, diet, manufacturer, name, precautions, schedule, sideffect, usedfor, uses, work;
  private Long price, quantity;

  public Medicine ()
  {

  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getContains() {
    return contains;
  }

  public void setContains(String contains) {
    this.contains = contains;
  }

  public String getDiet() {
    return diet;
  }

  public void setDiet(String diet) {
    this.diet = diet;
  }

  public String getManufacturer() {
    return manufacturer;
  }

  public void setManufacturer(String manufacturer) {
    this.manufacturer = manufacturer;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPrecautions() {
    return precautions;
  }

  public void setPrecautions(String precautions) {
    this.precautions = precautions;
  }

  public String getSchedule() {
    return schedule;
  }

  public void setSchedule(String schedule) {
    this.schedule = schedule;
  }

  public String getSideffect() {
    return sideffect;
  }

  public void setSideffect(String sideffect) {
    this.sideffect = sideffect;
  }

  public String getUsedfor() {
    return usedfor;
  }

  public void setUsedfor(String usedfor) {
    this.usedfor = usedfor;
  }

  public String getUses() {
    return uses;
  }

  public void setUses(String uses) {
    this.uses = uses;
  }

  public String getWork() {
    return work;
  }

  public void setWork(String work) {
    this.work = work;
  }

  public Long getPrice() {
    return price;
  }

  public void setPrice(Long price) {
    this.price = price;
  }

  public Long getQuantity() {
    return quantity;
  }

  public void setQuantity(Long quantity) {
    this.quantity = quantity;
  }
}
