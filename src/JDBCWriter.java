import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class JDBCWriter {
    private static Connection connection = null;
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    public boolean setConnection(String username, String password) {
        final String url = "jdbc:mysql://127.0.0.1:3306/kailuacarrental?serverTimezone=UTC";
        boolean bres = false;

        try {
            connection = DriverManager.getConnection(url, username, password);
            bres = true;
        } catch (SQLException error) {
            System.out.println("\nNo connection made");
        }
        return bres;
    }

    public int updateCar(int carId, Car car) {
        String model_name = car.getModel_name();
        String registration_number = car.getRegistration_number();
        LocalDate first_registration = car.getFirst_registration();
        double odometer = car.getOdometer();
        int car_group_id = car.getCar_group_id();
        int brand_id = car.getBrand_id();
        int fuelType_id = car.getFuelType_id();


        PreparedStatement preparedStatement;
        String updateStr = "UPDATE cars SET model_name = ?, " +
                "registration_number = ?, " +
                "first_registration = ?, " +
                "odometer = ?, " +
                "car_group_id = ?, " +
                "brand_id = ?, " +
                "fueltype_id = ? " +
                "where car_id = ?";

        int result = -1;


        try {
            preparedStatement = connection.prepareStatement(updateStr);
            preparedStatement.setString(1, model_name);
            preparedStatement.setString(2, registration_number);
            preparedStatement.setString(3, first_registration.toString());
            preparedStatement.setDouble(4, odometer);
            preparedStatement.setInt(5, car_group_id);
            preparedStatement.setInt(6, brand_id);
            preparedStatement.setInt(7, fuelType_id);
            preparedStatement.setInt(8, carId);
            result = preparedStatement.executeUpdate();


        } catch (SQLException error) {
            error.printStackTrace();
        }
        return result;


    }

    public Car getCarFromDBbyIndex(int id) {
        ArrayList<Car> cars = new ArrayList<>();
        PreparedStatement preparedStatement;

        String searchStr = "SELECT * from cars where car_id = ?";


        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                String car_id = "" + resultSet.getObject("car_id");
                int tempCar_id = Integer.parseInt(car_id);
                String model_name = "" + resultSet.getObject("model_name");
                String registration_number = "" + resultSet.getObject("registration_number");
                LocalDate first_registration = LocalDate.parse("" + resultSet.getObject("first_registration"));
                String odometer = "" + resultSet.getObject("odometer");
                double odometer2 = Double.parseDouble(odometer);
                int car_group_id = (int) resultSet.getObject("car_group_id");
                int brand_id = (int) resultSet.getObject("brand_id");
                int fuelType_id = (int) resultSet.getObject("fueltype_id");

                Car car = new Car(model_name, registration_number, first_registration, odometer2, car_group_id,
                        brand_id, fuelType_id);

                car.setCar_id(tempCar_id);

                cars.add(car);


            }

        } catch (SQLException error) {
            error.printStackTrace();
        }


        return cars.get(0);

    }

    public int getLatestCarIndex() {
        PreparedStatement preparedStatement;
        String searchStr = "SELECT max(car_id) from cars";
        String result = "";

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                result = "" + resultSet.getObject(1);
            }

        } catch (SQLException error) {
            error.printStackTrace();
        }

        return Integer.parseInt(result);

    }


    public int deleteCar(int id) {

        String deleteString = "DELETE from cars where car_id = ?";
        PreparedStatement preparedStatement;
        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(deleteString);
            preparedStatement.setInt(1, id);
            result = preparedStatement.executeUpdate();

        } catch (SQLException error) {
            error.printStackTrace();
        }
        return result;
    }

    public int isCarAvailable(String start_time, String end_time, int car_id) {

        PreparedStatement preparedStatement;
        String searchStr = "SELECT count(*) from rental_contracts " +
                "join cars using (car_id) " +
                "where car_id = ? " +
                "and ((start_time between ? and ?) " +
                "or " +
                "(end_time between ? and ?)) ";
        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setInt(1, car_id);
            preparedStatement.setString(2, start_time);
            preparedStatement.setString(3, end_time);
            preparedStatement.setString(4, start_time);
            preparedStatement.setString(5, end_time);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                result = resultSet.getInt(1);
            }
        } catch (SQLException error) {
            error.printStackTrace();
        }

        return result;

    }

    public int insertCar(Car car) throws NullPointerException {

        String insstr = "INSERT INTO cars(model_name, registration_number, first_registration, " +
                "odometer, car_group_id, brand_id, fueltype_id) " +
                "VALUES ('" +
                car.getModel_name() + "','" +
                car.getRegistration_number() + "','" +
                car.getFirst_registration() + "','" +
                car.getOdometer() + "','" +
                car.getCar_group_id() + "','" +
                car.getBrand_id() + "','" +
                car.getFuelType_id() +
                "')";

        PreparedStatement preparedStatement;
        int result = 0;

        try {
            preparedStatement = connection.prepareStatement(insstr);
            int rowCount = preparedStatement.executeUpdate();
            result = rowCount;

        } catch (SQLException sqlerror) {
            System.out.println("sql fejl i writeline= " + sqlerror.getMessage());
        }

        System.out.println("\nSuccesfully saved car entry to DataBase\n");
        return result;

    }

    public int insertCustomer(Customer customer) throws NullPointerException {

        String insstr = "INSERT INTO customers(first_name, last_name, email, address_id) " +
                "VALUES ('" +
                customer.getFirst_name() + "','" +
                customer.getLast_name() + "','" +
                customer.getEmail() + "','" +
                customer.getAddress_id() +
                "')";

        PreparedStatement preparedStatement;
        int result = 0;

        try {
            preparedStatement = connection.prepareStatement(insstr);
            int rowCount = preparedStatement.executeUpdate();
            result = rowCount;

        } catch (SQLException sqlerror) {
            System.out.println("sql fejl i writeline= INSERTCUSTOMER METODE " + sqlerror.getMessage());
        }


        System.out.println("\nSuccesfully saved customer entry to DataBase\n");
        return result;

    }

    public int insertCity(String cityName) throws NullPointerException {

        String insstr = "INSERT INTO cities(city_name) " +
                "VALUES ('" + cityName + "')";

        PreparedStatement preparedStatement;
        int result = 0;

        try {
            preparedStatement = connection.prepareStatement(insstr);
            int rowCount = preparedStatement.executeUpdate();
            result = rowCount;

        } catch (SQLException sqlerror) {
            System.out.println("sql fejl i writeline=(insert city)" + sqlerror.getMessage());
        }


        System.out.println("\nSuccesfully saved city entry to DataBase\n");
        return result;

    }

    public int insertZipCode(int zipcode, int city_id) throws NullPointerException {
        int result = 0;


        String insstr = "INSERT INTO zipcodes(zipcode,city_id) " +
                "VALUES ('" + zipcode + "','" + city_id + "')";

        PreparedStatement preparedStatement;


        try {
            preparedStatement = connection.prepareStatement(insstr);
            int rowCount = preparedStatement.executeUpdate();
            result = rowCount;

        } catch (SQLException sqlerror) {
            System.out.println("sql fejl i writeline=zip code" + sqlerror.getMessage());
        }


        System.out.println("\nSuccesfully saved zipcode entry to DataBase\n");


        return result;
    }

    public int insertAddress(String address_name, int zipcode_id) throws NullPointerException {

        String insstr = "INSERT INTO addresses(address_name,zipcode_id) " +
                "VALUES ('" + address_name + "','" + zipcode_id + "')";

        PreparedStatement preparedStatement;
        int result = 0;

        try {
            preparedStatement = connection.prepareStatement(insstr);
            int rowCount = preparedStatement.executeUpdate();
            result = rowCount;

        } catch (SQLException sqlerror) {
            System.out.println("sql fejl i writeline=address " + sqlerror.getMessage());
        }


        System.out.println("\nSuccesfully saved address entry to DataBase\n");
        return result;

    }

    public int getAddressIDFromDbByAddressName(String address_name) {

        PreparedStatement preparedStatement;
        String searchStr = "SELECT address_id from addresses where address_name = ?";
        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setString(1, address_name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                result = resultSet.getInt(1);
            }

        } catch (SQLException error) {
            error.printStackTrace();
        }

        return result;

    }

    public int getCityIDFromDbByCityName(String city_name) {

        PreparedStatement preparedStatement;
        String searchStr = "SELECT city_id from cities where city_name = ?";
        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setString(1, city_name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                result = resultSet.getInt(1);
            }

        } catch (SQLException error) {
            error.printStackTrace();
        }

        return result;

    }

    public int getZipIDFromDbByCityId(int city_id) {

        PreparedStatement preparedStatement;
        String searchStr = "SELECT zipcode_id from zipcodes where city_id = ?";
        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setInt(1, city_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                result = resultSet.getInt(1);
            }

        } catch (SQLException error) {
            error.printStackTrace();
        }

        return result;

    }

    public int getZipcodeIdFromDbByAddressId(int address_id) {

        PreparedStatement preparedStatement;
        String searchStr = "SELECT zipcode_id from zipcodes " +
                "join addresses using(zipcode_id) " +
                "where address_id = ?";
        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setInt(1, address_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                result = resultSet.getInt(1);
            }

        } catch (SQLException error) {
            error.printStackTrace();
        }

        return result;

    }

    public int getZipcodeIdFromDbByCustomer(int customer_id) {

        PreparedStatement preparedStatement;
        String searchStr = "SELECT zipcode_id from zipcodes " +
                "join addresses using(zipcode_id) " +
                "join customers using(address_id) " +
                "where customer_id = ?";
        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setInt(1, customer_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                result = resultSet.getInt(1);
            }

        } catch (SQLException error) {
            error.printStackTrace();
        }

        return result;

    }

    public String getCityNameFromDbByCustomerId(int customer_id) {

        PreparedStatement preparedStatement;
        String searchStr = "SELECT city_name from cities " +
                "join zipcodes using (city_id) " +
                "join addresses using (zipcode_id) " +
                "join customers using (address_id) " +
                "where customer_id = ?";
        String result = "";

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setInt(1, customer_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                result = resultSet.getString("city_name");
            }

        } catch (SQLException error) {
            error.printStackTrace();
        }

        return result;

    }

    public String getAddressNameFromDbByCustomerId(int customer_id) {

        PreparedStatement preparedStatement;
        String searchStr = "SELECT address_name from addresses " +
                "join customers using(address_id) " +
                "where customer_id = ?";
        String result = "";

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setInt(1, customer_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                result = resultSet.getString(1);
            }

        } catch (SQLException error) {
            error.printStackTrace();
        }

        return result;

    }

    public Customer getCustomerFromDBbyId(int id) {
        ArrayList<Customer> customerList = new ArrayList<>();
        PreparedStatement preparedStatement;

        String searchStr = "SELECT * from customers where customer_id = ?";

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                String customer_id = "" + resultSet.getObject("customer_id");
                int tempCustomer_id = Integer.parseInt(customer_id);
                String first_name = "" + resultSet.getObject("first_name");
                String last_name = "" + resultSet.getObject("last_name");
                String email = "" + resultSet.getObject("email");
                int address_id = Integer.parseInt("" + resultSet.getObject("address_id"));
                String city_name = getCityNameFromDbByCustomerId(id);
                int city_id = getCityIDFromDbByCityName(city_name);
                int zipcode = getZipcodeFromDbByCustomerId(tempCustomer_id);
                int zipcode_id = getZipcodeIdFromDbByCustomer(tempCustomer_id); // tester
                String address_name = getAddressNameFromDbByCustomerId(id);


                Customer customer1 = new Customer(first_name, last_name, email, address_id, address_name, zipcode, zipcode_id, city_id, city_name);
                customer1.setCustomer_id(tempCustomer_id);
                customerList.add(customer1);

            }

        } catch (SQLException error) {
            error.printStackTrace();
        }

        return customerList.get(0);

    }

    public int deleteCustomer(int id) {
        String deleteString = "DELETE from customers where customer_id = ?";
        PreparedStatement preparedStatement;
        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(deleteString);
            preparedStatement.setInt(1, id);
            result = preparedStatement.executeUpdate();

        } catch (SQLException error) {
            error.printStackTrace();
        }
        return result;
    }

    public int deleteCityByCityId(int city_id){
        String deleteString = "DELETE from cities where city_id = ? ";
        PreparedStatement preparedStatement;
        int result = -1;
        try {
            preparedStatement = connection.prepareStatement(deleteString);
            preparedStatement.setInt(1, city_id);
            result = preparedStatement.executeUpdate();
            System.out.println(city_id+"Dette er den hentede city_id i metode");
        } catch (SQLException error) {
            error.printStackTrace();
        }
        return result;
    }

    public int deleteZipCodeIdByAddressId(int zipcode_id){
        String deleteString = "DELETE from zipcodes " +
                "where zipcode_id = ?";
        PreparedStatement preparedStatement;
        int result = -1;
        try {
            preparedStatement = connection.prepareStatement(deleteString);
            preparedStatement.setInt(1, zipcode_id);
            result = preparedStatement.executeUpdate();
            System.out.println(zipcode_id+"Dette er den hentede zip i metode");
        } catch (SQLException error) {
            error.printStackTrace();
        }
        return result;
    }

    public int deleteAddressByAddressId(int address_id){
        String deleteString = "DELETE from addresses where address_id = ? ";
        PreparedStatement preparedStatement;
        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(deleteString);
            preparedStatement.setInt(1, address_id);
            result = preparedStatement.executeUpdate();
            System.out.println(address_id+"Dette er den hentede address i metode");
        } catch (SQLException error) {
            error.printStackTrace();
        }
        return result;
    }

    public int getCustomerIdFromDB(String email) {

        PreparedStatement preparedStatement;
        String searchStr = "SELECT customer_id from customers where email = ?";
        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                result = resultSet.getInt(1);
            }

        } catch (SQLException error) {
            error.printStackTrace();
        }

        return result;

    }

    public int checkCustomerExists(int customer_id) {

        PreparedStatement preparedStatement;
        String searchStr = "SELECT count(*) from customers where customer_id = ?";
        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setInt(1, customer_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                result = resultSet.getInt(1);
            }

        } catch (SQLException error) {
            error.printStackTrace();
        }

        return result;
    }

    public int checkCarExists(int car_id) {

        PreparedStatement preparedStatement;
        String searchStr = "SELECT count(*) from cars where car_id = ?";
        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setInt(1, car_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                result = resultSet.getInt(1);
            }

        } catch (SQLException error) {
            error.printStackTrace();
        }

        return result;
    }

    public int getZipcodeFromDbByCustomerId(int customer_id) {

        PreparedStatement preparedStatement;
        String searchStr = "SELECT zipcode from zipcodes " +
                "join addresses using(zipcode_id) " +
                "join customers using(address_id) " +
                "where customer_id = ?";
        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setInt(1, customer_id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                result = resultSet.getInt(1);
            }

        } catch (SQLException error) {
            error.printStackTrace();
        }

        return result;

    }

    public int updateCity(String city_name, int city_id) {
        String updateCity = "UPDATE cities SET city_name = ? where city_id = ?";

        PreparedStatement preparedStatement;
        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(updateCity);
            preparedStatement.setString(1, city_name);
            preparedStatement.setInt(2, city_id);
            result = preparedStatement.executeUpdate();

        } catch (SQLException error) {
            error.printStackTrace();
        }
        return result;
    }

    public int updateZipcode(int zip_code, int zip_code_id) {

        String updateZipcode = "UPDATE zipcodes SET zipcode = ? WHERE zipcode_id = ?";

        PreparedStatement preparedStatement;
        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(updateZipcode);
            preparedStatement.setInt(1, zip_code);
            preparedStatement.setInt(2, zip_code_id);
            result = preparedStatement.executeUpdate();

        } catch (SQLException error) {
            error.printStackTrace();
        }
        return result;
    }

    public int updateAddressName(String newName, int address_id) {

        String updateAddressNameStr = "UPDATE addresses SET address_name = ? WHERE address_id = ?";

        PreparedStatement preparedStatement;
        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(updateAddressNameStr);
            preparedStatement.setString(1, newName);
            preparedStatement.setInt(2, address_id);
            result = preparedStatement.executeUpdate();

        } catch (SQLException error) {
            error.printStackTrace();
        }
        return result;
    }


    public int updateCustomer(int customer_id, Customer beforeChanges, Customer targetChanges) {

        Main reader = new Main();


        String first_name_old = beforeChanges.getFirst_name();
        String first_name_new = targetChanges.getFirst_name();

        String last_name_old = beforeChanges.getLast_name();
        String last_name_new = targetChanges.getLast_name();

        String email_old = beforeChanges.getEmail();
        String email_new = targetChanges.getEmail();

        int city_id_old = beforeChanges.getCity_id();


        String city_name_old = beforeChanges.getCity_name();
        String city_name_new = targetChanges.getCity_name();

        int zipcode_old = beforeChanges.getZipcode();
        int zipcode_new = targetChanges.getZipcode();


        int address_id_old = beforeChanges.getAddress_id();


        String address_name_old = beforeChanges.getAddress_name();
        String address_name_new = targetChanges.getAddress_name();


        PreparedStatement preparedStatement;
        String updateStr = "UPDATE customers SET first_name = ?, last_name = ?, email = ?," +
                " address_id = ? WHERE customer_id = ?";

        int result = -1;

        // Hvis der er ingen ændringer, så ryger vi bare ud af metoden.
        if (first_name_old.equalsIgnoreCase(first_name_new) && last_name_old.equalsIgnoreCase(last_name_new)
                && email_old.equalsIgnoreCase(email_new) && address_name_old.equalsIgnoreCase(address_name_new)
                && zipcode_old == zipcode_new && city_name_old.equalsIgnoreCase(city_name_new)) {
            System.out.println("No changes made");
            return result;
        }

        boolean stop=false;

        // hvis enten zipcode, city_name eller street_name er blevet opdateret
        if (zipcode_old != zipcode_new || !city_name_old.equalsIgnoreCase(city_name_new) ||
                !address_name_old.equalsIgnoreCase(address_name_new)) {

            // tilfælde hvor KUN zipcode er ændret.
            if (zipcode_old != zipcode_new && city_name_old.equalsIgnoreCase(city_name_new) &&
                    address_name_old.equalsIgnoreCase(address_name_new)) {
                System.out.println("It seems that the Zipcode value has been updated - to avoid confusion in our database" +
                        " - Please enter the new corresponding City name: ");
                city_name_new = reader.readChoiceString();
                int zipCodeID = getZipcodeIdFromDbByAddressId(address_id_old);
                updateCity(city_name_new, city_id_old);
                updateZipcode(zipcode_new, zipCodeID);
                System.out.println("It seems that the Street name value hasn't been updated, however City name and Zipcode has been");
                System.out.println("Do you wish to enter a new street name? (1 for yes, 0 for no)");
                int choice = new Main().readChoiceInt();

                if (choice == 1) {
                    System.out.println("Please enter new street name: ");
                    reader.readChoiceString();
                    String newStreetName = reader.readChoiceString();
                    updateAddressName(newStreetName, address_id_old);

                } else {
                    updateAddressName(address_name_old, address_id_old);
                }

            stop=true; // sat til at stoppe koden i at gå i gang længere nede. (Multiple changes)

            }

            // tilfælde hvor KUN city name er ændret.
            if (!city_name_old.equalsIgnoreCase(city_name_new) && zipcode_old == zipcode_new
                    && address_name_old.equalsIgnoreCase(address_name_new)) {
                System.out.println("It seems that the City name value has been updated - to avoid confusion in our database" +
                        " - Please enter the new corresponding Zipcode: ");
                zipcode_new = reader.readChoiceInt();
                int zipCodeID = getZipcodeIdFromDbByAddressId(address_id_old);
                updateCity(city_name_new, city_id_old);
                updateZipcode(zipcode_new, zipCodeID);
                System.out.println("It seems that the Street name value hasn't been updated, however City name and Zipcode has been");
                System.out.println("Do you wish to enter a new street name? (1 for yes, 0 for no)");
                int choice = new Main().readChoiceInt();

                if (choice == 1) {
                    System.out.println("Please enter new street name: ");
                    reader.readChoiceString();
                    String newStreetName = reader.readChoiceString();
                    updateAddressName(newStreetName, address_id_old);

                } else {
                    updateAddressName(address_name_old, address_id_old);
                }
                stop=true; // sat til at stoppe koden i at gå i gang længere nede.
            }


            /// tilfælde hvor KUN address name er ændret.
            if (zipcode_old == zipcode_new && city_name_old.equalsIgnoreCase(city_name_new) &&
                    !address_name_old.equalsIgnoreCase(address_name_new)) {
                System.out.println("It seems that the Street name value has been updated, however City name and Zipcode hasn't been");
                System.out.println("Do you wish to enter a new city name? (1 for yes, 0 for no)");
                int choice1 = reader.readChoiceInt();
                if (choice1 == 1) {
                    System.out.println("Please enter new city name: ");
                    reader.readChoiceString();
                    String newCityName = reader.readChoiceString();
                    updateCity(newCityName, address_id_old);

                    System.out.println("Do you wish to enter a new zipcode too? (1 for yes, 0 for no)");
                    int choice2 = reader.readChoiceInt();

                    if (choice2 == 1) {
                        System.out.println("Please enter new zipcode: ");
                        int newZipCode = reader.readChoiceInt();
                        int zipCodeID = getZipcodeIdFromDbByAddressId(address_id_old);
                        updateZipcode(newZipCode, zipCodeID);

                    } else {
                        int zipCodeID = getZipcodeIdFromDbByAddressId(address_id_old);
                        updateZipcode(zipcode_old, zipCodeID);
                    }

                } else {
                    System.out.println("Do you wish to enter a new zipcode then? (1 for yes, 0 for no)");
                    int choice3 = reader.readChoiceInt();
                    if (choice3 == 1) {
                        System.out.println("Please enter new zipcode: ");
                        int newZipCode = reader.readChoiceInt();
                        int zipCodeID = getZipcodeIdFromDbByAddressId(address_id_old);
                        updateZipcode(newZipCode, zipCodeID);
                    } else {
                        int zipCodeID = getZipcodeIdFromDbByAddressId(address_id_old);
                        updateZipcode(zipcode_old, zipCodeID);
                    }
                }
                updateAddressName(address_name_new, address_id_old);
                stop=true; // sat til at stoppe koden i at gå i gang længere nede.
            }

            // tilfælde hvor både zipcode og city_name er ændret, men ikke address_name.
            if (zipcode_old != zipcode_new && !city_name_old.equalsIgnoreCase(city_name_new) &&
                    address_name_old.equalsIgnoreCase(address_name_new) && !stop) {
                System.out.println("It seems that the zipcode and city_name values have been updated," +
                        "however the street name value has not");
                System.out.println("Do you wish to enter a new street name? (1 for yes, 0 for no)");
                int choice = new Main().readChoiceInt();
                if (choice == 1) {
                    System.out.println("Please enter new street name: ");
                    reader.readChoiceString();
                    String newStreetName = reader.readChoiceString();

                    int zipcodeId = getZipcodeIdFromDbByAddressId(address_id_old);
                    updateCity(city_name_new, city_id_old);
                    updateZipcode(zipcode_new, zipcodeId);
                    updateAddressName(newStreetName, address_id_old);

                } else {
                    int zipcodeId = getZipcodeIdFromDbByAddressId(address_id_old);
                    updateCity(city_name_new, city_id_old);
                    updateZipcode(zipcode_new, zipcodeId);
                    updateAddressName(address_name_old, address_id_old);
                }
            }


            // tilfælde hvor zipcode og address_name er ændret, men ikke city_name.

            if (zipcode_old != zipcode_new && city_name_old.equalsIgnoreCase(city_name_new) &&
                    !address_name_old.equalsIgnoreCase(address_name_new) && !stop) {
                System.out.println("It seems that the zipcode and street_name values have been updated," +
                        "however the city_name value has not");
                System.out.println("Do you wish to enter a new city name? (1 for yes, 0 for no)");
                int choice = new Main().readChoiceInt();
                if (choice == 1) {
                    System.out.println("Please enter new city name: ");
                    reader.readChoiceString();
                    String newCityName = reader.readChoiceString();

                    int zipcodeId = getZipcodeIdFromDbByAddressId(address_id_old);
                    updateCity(newCityName, city_id_old);
                    updateZipcode(zipcode_new, zipcodeId);
                    updateAddressName(address_name_new, address_id_old);

                } else {
                    int zipcodeId = getZipcodeIdFromDbByAddressId(address_id_old);
                    updateCity(city_name_old, city_id_old);
                    updateZipcode(zipcode_new, zipcodeId);
                    updateAddressName(address_name_new, address_id_old);
                }
            }

            // tilfælde hvor city_name og address_name er ændret, men ikke zipcode

            if (zipcode_old == zipcode_new && !city_name_old.equalsIgnoreCase(city_name_new) &&
                    !address_name_old.equalsIgnoreCase(address_name_new) && !stop) {
                System.out.println("It seems that the city_name and street_name values have been updated," +
                        "however the zipcode value has not");
                System.out.println("Do you wish to enter a new zipcode? (1 for yes, 0 for no)");
                int choice = new Main().readChoiceInt();
                if (choice == 1) {
                    System.out.println("Please enter new zipcode: ");
                    reader.readChoiceString();
                    int newZipCode = reader.readChoiceInt();

                    int zipcodeId = getZipcodeIdFromDbByAddressId(address_id_old);
                    updateCity(city_name_new, city_id_old);
                    updateZipcode(newZipCode, zipcodeId);
                    updateAddressName(address_name_new, address_id_old);

                } else {
                    int zipcodeId = getZipcodeIdFromDbByAddressId(address_id_old);
                    updateCity(city_name_new, city_id_old);
                    updateZipcode(zipcode_old, zipcodeId);
                    updateAddressName(address_name_new, address_id_old);
                }
            }

            // tilfælde hvor alle er ændret

            if (zipcode_old != zipcode_new && !city_name_old.equalsIgnoreCase(city_name_new) &&
                    !address_name_old.equalsIgnoreCase(address_name_new)) {


                int zipIDFinal = getZipcodeIdFromDbByAddressId(address_id_old);
                updateCity(city_name_new, city_id_old);
                updateZipcode(zipcode_new, zipIDFinal);
                updateAddressName(address_name_new, address_id_old);
            }
        }

        // opdatér resterende værdier
        try {
            preparedStatement = connection.prepareStatement(updateStr);
            preparedStatement.setString(1, first_name_new);
            preparedStatement.setString(2, last_name_new);
            preparedStatement.setString(3, email_new);
            preparedStatement.setInt(4, address_id_old);
            preparedStatement.setInt(5, customer_id);
            preparedStatement.executeUpdate();
        } catch (SQLException error9000) {
            error9000.printStackTrace();
        }

        System.out.println("Successfully updated customer");

        return result;
    }

    public int updateRentalContract(int rental_contract_id,RentalContract rentalContract,RentalContract oldRentalContract){
        Main reader = new Main();

        LocalDateTime start_time = rentalContract.getRental_start();
        LocalDateTime end_time = rentalContract.getRental_end();
        double max_km = rentalContract.getMax_km();
        int customer_id = rentalContract.getCustomer_id();
        int car_id = rentalContract.getCar_id();

        String start_time_str2 = String.valueOf(start_time);
        String end_time_str2 = String.valueOf(end_time);

        int old_customer_id = oldRentalContract.getCustomer_id();
        int old_car_id = oldRentalContract.getCar_id();



        int result = -1;

        PreparedStatement preparedStatement;
        String updateStr = "UPDATE rental_contracts SET start_time = ?, end_time = ?, max_km = ?," +
                " customer_id = ?, car_id = ?" +
                " WHERE rental_contract_id = ?";


        while (rentalContract.getRental_end().isBefore(rentalContract.getRental_start())) { // Hvis end_time er tidligere end start og omvendt
            System.out.println("the new end_time is before start_time. Please enter new end_time: ");
            System.out.println(rentalContract.getRental_start() + "  første  " + rentalContract.getRental_end());
            end_time_str2 = reader.readChoiceString();
            end_time = LocalDateTime.parse(end_time_str2,formatter);
            rentalContract.setRental_end(end_time);
            System.out.println(rentalContract.getRental_start() + "  anden  " + rentalContract.getRental_end());
        }

        if (old_customer_id != customer_id || old_car_id != car_id) {

            //TILFÆLDE 1 :Kun kunde_id ændret, IKKE bilen.
            if (old_customer_id != customer_id && old_car_id == car_id) {
                int customerControl = checkCustomerExists(customer_id);
                int counter = 0; // kontrol at kunden findes.
                while (customerControl == 0) {
                    System.out.println("It looks like you are trying to assign a user that does not exist. Please enter a valid ID");

                    int new_customer_id = reader.readChoiceInt();
                    rentalContract.setCustomer_id(new_customer_id);

                    counter++;
                    if (counter > 4) { // Lav ny kunde ved fejl indtast.
                        System.out.println("It looks like you can't find the customer you are looking for.\n" +
                                "Do you want to create a new customer?\n1 for yes, 2 for no");
                        int choice = reader.readChoiceInt();
                        reader.scanner.nextLine();
                        if (choice == 1) {
                            Customer customer = reader.createCustomer();
                            insertCustomer(customer);
                            rentalContract.setCustomer_id(getCustomerIdFromDB(customer.getEmail()));
                            customerControl=checkCustomerExists(rentalContract.getCustomer_id());
                        } else {
                            System.out.println("Error. Contract not updated.");
                            return -1;
                        }
                    }
                }

            } //TILFÆLDE 1 færdig

            int counter = 0;
            boolean carCreated = false;

            //TILFÆLDE 2, Vi ændrer bilen, men IKKE kunden.
            if (old_customer_id == customer_id && old_car_id != car_id) {
                int carControl = checkCarExists(car_id);
                while (carControl != 1) {
                    System.out.println("It looks like you can't find the car you are looking for.\n" +
                            "Please enter a valid id: ");
                    int new_car_id = reader.readChoiceInt();
                    rentalContract.setCar_id(new_car_id);
                    counter++;
                    if (counter > 4) {
                        System.out.println("It looks like you can't find the car you are looking for.\n" +
                                "Do you want to create a new car?\n1 for yes, 2 for no");
                        int choice2 = reader.readChoiceInt();
                        if (choice2 == 1) {
                            Car car = reader.createCar();
                            insertCar(car);
                            rentalContract.setCar_id(getLatestCarIndex());
                            carControl=checkCarExists(rentalContract.getCar_id());
                            carCreated = true;
                        } else {
                            System.out.println("Error. Contract not updated.");
                            return -1;
                        }

                    }
                }


                // Kontrol af om bilen er reserveret eller fri.
                counter = 0;
                if (!carCreated) {
                    int control_car_available = isCarAvailable(start_time_str2, end_time_str2, car_id);
                    while (control_car_available > 0) {
                        System.out.println("It looks like your car is already reserved. Please enter a different car id: ");
                        int new_car_id = reader.readChoiceInt();
                        rentalContract.setCar_id(new_car_id);
                        counter++;

                        if (counter > 4) {
                            System.out.println("The cars you've requested are all reserved. Do you wish to create a new car?\n" +
                                    "1 for yes, 2 for no");
                            int choice3 = reader.readChoiceInt();
                            if (choice3 == 1) {
                                Car car = reader.createCar();
                                insertCar(car);
                            } else {
                                System.out.println("Error. Contract not updated.");
                                return -1;
                            }
                        }

                    }
                }
            } // TILFÆLDE 2.  færdig

            if (old_customer_id != customer_id && old_car_id != car_id){

                int customerControl = checkCustomerExists(customer_id);
                int counter1 = 0; // kontrol at kunden findes.
                while (customerControl == 0) {
                    System.out.println("It looks like you are trying to assign a user that does not exist. Please enter a valid ID");

                    int new_customer_id = reader.readChoiceInt();
                    rentalContract.setCustomer_id(new_customer_id);

                    counter1++;
                    if (counter1 > 4) { // Lav ny kunde ved fejl indtast.
                        System.out.println("It looks like you can't find the customer you are looking for.\n" +
                                "Do you want to create a new customer?\n1 for yes, 2 for no");
                        int choice = reader.readChoiceInt();
                        reader.scanner.nextLine();
                        if (choice == 1) {
                            Customer customer = reader.createCustomer();
                            insertCustomer(customer);
                            rentalContract.setCustomer_id(getCustomerIdFromDB(customer.getEmail()));
                            customerControl=checkCustomerExists(rentalContract.getCustomer_id());
                            customer_id = rentalContract.getCustomer_id();
                        } else {
                            System.out.println("Error. Contract not updated.");
                            return -1;
                        }
                    }
                }  //WHILELOOP



                int counter2 =0;
                int carControl = checkCarExists(car_id);
                while (carControl != 1) {
                    System.out.println("It looks like you can't find the car you are looking for.\n" +
                            "Please enter a valid id: ");
                    int new_car_id = reader.readChoiceInt();
                    rentalContract.setCar_id(new_car_id);
                    counter2++;
                    if (counter2 > 4) {
                        System.out.println("It looks like you can't find the car you are looking for.\n" +
                                "Do you want to create a new car?\n1 for yes, 2 for no");
                        int choice2 = reader.readChoiceInt();
                        reader.scanner.nextLine();
                        if (choice2 == 1) {
                            Car car = reader.createCar();
                            insertCar(car);
                            rentalContract.setCar_id(getLatestCarIndex());
                            carControl=checkCarExists(rentalContract.getCar_id());
                            car_id = getLatestCarIndex();
                        } else {
                            System.out.println("Error. Contract not updated.");
                            return -1;
                        }

                    }
                }
            }

        }

        try {
                preparedStatement = connection.prepareStatement(updateStr);
                preparedStatement.setString(1, start_time_str2);
                preparedStatement.setString(2, end_time_str2);
                preparedStatement.setDouble(3, max_km);
                preparedStatement.setInt(4, customer_id);
                preparedStatement.setInt(5, car_id);
                preparedStatement.setInt(6, rental_contract_id);
                result = preparedStatement.executeUpdate();

        } catch (SQLException error) {
                error.printStackTrace();
        }
        System.out.println("You successfully updated a rental contract!\n");
        return result;
        }




    public RentalContract getRentalContractByRCID(int rental_contract_id){
        ArrayList<RentalContract> rentalContractList = new ArrayList<>();
        PreparedStatement preparedStatement;

        String searchStr = "SELECT * from rental_contracts where rental_contract_id = ?";


        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setInt(1, rental_contract_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                int rc_id = resultSet.getInt("rental_contract_id");
                LocalDateTime start_time = (LocalDateTime) resultSet.getObject("start_time");
                LocalDateTime end_time = (LocalDateTime) resultSet.getObject("end_time");
                double max_km = resultSet.getDouble("max_km");
                int customer_id = resultSet.getInt("customer_id");
                int car_id = resultSet.getInt("car_id");

                RentalContract rentalContract = new RentalContract(start_time,end_time,max_km,customer_id,car_id);
                rentalContract.setRental_contract_id(rc_id);

                rentalContractList.add(rentalContract);
            }

        } catch (SQLException error) {
            error.printStackTrace();
        }

        return rentalContractList.get(0);
    }

    public int insertRentalContract(RentalContract rentalContract) throws NullPointerException {

        String insstr = "INSERT INTO rental_contracts(start_time, end_time, max_km, customer_id,car_id) " +
                "VALUES ('" + rentalContract.getRental_start() + "','" + rentalContract.getRental_end() + "','" +
                rentalContract.getMax_km() + "','" + rentalContract.getCustomer_id() +
                "','" +rentalContract.getCar_id() + "')";

        PreparedStatement preparedStatement;
        int result = 0;

        try {
            preparedStatement = connection.prepareStatement(insstr);
            int rowCount = preparedStatement.executeUpdate();
            result = rowCount;

        } catch (SQLException sqlerror) {
            System.out.println("sql fejl i writeline= insertRental METODE " + sqlerror.getMessage());
        }


        System.out.println("\nSuccesfully saved rental entry to DataBase\n");
        return result;

    }

    public int deleteRentalContract(int id) {
        String deleteString = "DELETE from rental_contracts where rental_contract_id = ?";
        PreparedStatement preparedStatement;
        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(deleteString);
            preparedStatement.setInt(1, id);
            result = preparedStatement.executeUpdate();

        } catch (SQLException error) {
            error.printStackTrace();
        }
        return result;
    }

}
/*





























.''人
'（__）
. ┃口┃
. ┃口┃
. ┃口┃　　　　　★
. ┃口┃ 　　　　''''人
. ┃口┃ 　　'''''（_____）　　　　　　
. ┃口┃ 　'''（__________） 　　　　　
. ┃口┃____╭━━━━━╮_____'|_口|_
. ┃╭╮╭╮╭╮╭╮╭╮╭╮╭╮ |三||╭╮╭╮ |
. ┃┃┃┃┃┃┃┃┃┃┃┃┃┃┃ |三||┃┃┃┃ |
. ┃┃┃┃┃┃┃┃┃┃┃┃┃┃┃ |三||┃┃┃┃ |
. ┃┃┃┃┃┃┃┃┃┃┃┃┃┃┃ |三||┃┃┃┃ |
♣══════════ஜ۩۞۩¤இஇஇ¤۩۞۩ஜ══════════♣
░▓░▓░▓░║+Putin approves this code║░░▓░▓░▓
♣══════════ஜ۩۞۩¤இஇஇ¤۩۞۩ஜ═════════♣

// The end */  //Click me