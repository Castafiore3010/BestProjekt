import java.time.LocalDate;
import java.util.Date;

public class Car {
    private int car_id;
    private String model_name;
    private String registration_number;
    private LocalDate first_registration;
    private double odometer;
    private int car_group_id;
    private int brand_id;
    private int fuelType_id;

    public int getCar_id () {
        return car_id;
    }

    public void setCar_id (int car_id) {this.car_id = car_id;}

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    public String getRegistration_number() {
        return registration_number;
    }

    public void setRegistration_number(String registration_number) {
        this.registration_number = registration_number;
    }

    public LocalDate getFirst_registration() {
        return first_registration;
    }

    public void setFirst_registration(LocalDate first_registration) {
        this.first_registration = first_registration;
    }

    public double getOdometer() {
        return odometer;
    }

    public void setOdometer(double odometer) {
        this.odometer = odometer;
    }

    public int getCar_group_id() {
        return car_group_id;
    }

    public void setCar_group_id(int car_group_id) {
        this.car_group_id = car_group_id;
    }

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }

    public int getFuelType_id() {
        return fuelType_id;
    }

    public void setFuelType_id(int fuelType_id) {
        this.fuelType_id = fuelType_id;
    }



    public Car() {}


    public Car(String model_name, String registration_number, LocalDate first_registration,
               double odometer, int car_group_id, int brand_id, int fuelType_id) {


        this.model_name = model_name;
        this.registration_number = registration_number;
        this.first_registration = first_registration;
        this.odometer = odometer;
        this.car_group_id = car_group_id;
        this.brand_id = brand_id;
        this.fuelType_id = fuelType_id;
    }

    @Override
    public String toString() {
        return "Selected car:" +
                "\nModel name: " + model_name +
                "\nRegistration number: " + registration_number +
                "\nFirst registration date: " + first_registration +
                "\nOdometer: " + odometer +"km"+
                "\nCargroup ID: " + car_group_id +
                "\nBrand ID: " + brand_id +
                "\nFuelType ID: " + fuelType_id ;


    }

}
