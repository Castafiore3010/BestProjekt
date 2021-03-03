import java.time.LocalDate;
import java.time.LocalDateTime;

public class RentalContract {


    private int rental_contract_id;
    private LocalDateTime rental_start;
    private LocalDateTime rental_end;
    private double max_km;
    private int customer_id;
    private int car_id;


    public int getRental_contract_id() {
        return rental_contract_id;
    }

    public void setRental_contract_id(int rental_contract_id) {
        this.rental_contract_id = rental_contract_id;
    }

    public LocalDateTime getRental_start() {
        return rental_start;
    }

    public void setRental_start(LocalDateTime rental_start) {
        this.rental_start = rental_start;
    }

    public LocalDateTime getRental_end() {
        return rental_end;
    }

    public void setRental_end(LocalDateTime rental_end) {
        this.rental_end = rental_end;
    }

    public double getMax_km() {
        return max_km;
    }

    public void setMax_km(double max_km) {
        this.max_km = max_km;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    RentalContract(){

    }
    RentalContract(LocalDateTime rental_start, LocalDateTime rental_end, double max_km, int customer_id, int car_id){
        this.rental_start=rental_start;
        this.rental_end=rental_end;
        this.max_km=max_km;
        this.customer_id=customer_id;
        this.car_id=car_id;

    }





}
