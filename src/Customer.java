public class Customer {

    private int customer_id;
    private String first_name;
    private String last_name;
    private String email;
    private int address_id;
    private String address_name;
    private int zipcode;
    private int zipcode_id;
    private int city_id;

    private String city_name;


    public int getZipcode_id() {
        return zipcode_id;
    }

    public void setZipcode_id(int zipcode_id) {
        this.zipcode_id = zipcode_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    Customer() {

    }


    public Customer(String first_name, String last_name, String email, int address_id, String address_name, int zipcode,int zipcode_id, int city_id, String city_name) {

        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.address_id = address_id;
        this.address_name = address_name;
        this.zipcode = zipcode;
        this.city_id = city_id;
        this.city_name = city_name;
    }


    @Override
    public String toString() {
        return "Selected Customer:" +
                "\nCustomer ID: " + customer_id +
                "\nFirst name: " + first_name +
                "\nLast name: " + last_name +
                "\nEmail: " + email +
                "\nStreetaddress: " + address_name +
                "\nZipcode: " + zipcode +
                "\nCity name: " + city_name;
    }




}
