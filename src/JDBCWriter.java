import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class JDBCWriter {
    private static Connection connection = null;


    public boolean setConnection() {
        final String url = "jdbc:mysql://127.0.0.1:3306/kailuacarrental?serverTimezone=UTC";
        boolean bres = false;

        try {
            connection = DriverManager.getConnection(url, "Marc", "wilma");
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
        String updateStr = "UPDATE cars SET model_name = ?, registration_number = ?, first_registration = ?," +
                " odometer = ?, car_group_id = ?," +
                "brand_id = ?, fueltype_id = ? where car_id = ?";

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

    public ArrayList<Car> getCarsFromDatabase() {
        ArrayList<Car> cars = new ArrayList<>();

        PreparedStatement preparedStatement;
        String searchStr = "SELECT * from cars";

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            ResultSet resultSet = preparedStatement.executeQuery();

            int i = 1;
            while (resultSet.next()) {

                String car_id = "" + resultSet.getObject("car_id");
                Integer help = Integer.parseInt(car_id);
                String model_name = "" + resultSet.getObject("model_name");
                String registration_number = "" + resultSet.getObject("registration_number");
                LocalDate first_registration = LocalDate.parse("" + resultSet.getObject("first_registration"));
                String odometer = "" + resultSet.getObject("odometer");
                Double odometer2 = Double.parseDouble(odometer);
                int car_group_id = (int) resultSet.getObject("car_group_id");
                int brand_id = (int) resultSet.getObject("brand_id");
                int fuelType_id = (int) resultSet.getObject("fueltype_id");

                Car car = new Car(model_name, registration_number, first_registration, odometer2, car_group_id,
                        brand_id, fuelType_id);

                car.setCar_id(help);

                cars.add(car);
            }

        } catch (SQLException error) {
            error.printStackTrace();
        }

        return cars;


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
                Integer tempCar_id = Integer.parseInt(car_id);
                String model_name = "" + resultSet.getObject("model_name");
                String registration_number = "" + resultSet.getObject("registration_number");
                LocalDate first_registration = LocalDate.parse("" + resultSet.getObject("first_registration"));
                String odometer = "" + resultSet.getObject("odometer");
                Double odometer2 = Double.parseDouble(odometer);
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

    public Car getLatestCar(int index) {
        ArrayList<Car> cars = new ArrayList<>();
        PreparedStatement preparedStatement;
        String searchStr = "SELECT * from cars where car_id = ?";

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setInt(1, index);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                String car_id = "" + resultSet.getObject("car_id");
                Integer help = Integer.parseInt(car_id);
                String model_name = "" + resultSet.getObject("model_name");
                String registration_number = "" + resultSet.getObject("registration_number");
                LocalDate first_registration = LocalDate.parse("" + resultSet.getObject("first_registration"));
                String odometer = "" + resultSet.getObject("odometer");
                Double odometer2 = Double.parseDouble(odometer);
                int car_group_id = (int) resultSet.getObject("car_group_id");
                int brand_id = (int) resultSet.getObject("brand_id");
                int fuelType_id = (int) resultSet.getObject("fueltype_id");

                Car car = new Car(model_name, registration_number, first_registration, odometer2, car_group_id,
                        brand_id, fuelType_id);

                car.setCar_id(help);

                cars.add(car);
            }

        } catch (SQLException error) {
            error.printStackTrace();
        }

        Car tempCar;

        int length = cars.size();
        tempCar = cars.get(length - 1);

        return tempCar;


    }

    public int getCarIdFromDB(String modelName) {

        PreparedStatement preparedStatement;
        String searchStr = "SELECT car_id from cars where model_name = ?";
        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setString(1, modelName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                result = resultSet.getInt(1);
            }

        } catch (SQLException error) {
            error.printStackTrace();
        }

        return result;


    }


    public String getBrandNameFromDB(int id) {

        PreparedStatement preparedStatement;
        String searchStr = "SELECT brand_name from brands where brand_id = ?";
        String result = "";

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                result = "" + resultSet.getObject(1);
            }

        } catch (SQLException error) {
            error.printStackTrace();
        }

        return result;

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

    public int searchDB(String url, String line) {

        String searchStr = "SELECT count(*) from urlreads where url like ? and line like ?";
        PreparedStatement preparedStatement;
        int result = -1;

        try {

            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setString(1, "%" + url + "%");
            preparedStatement.setString(2, "%" + line + "%");
            ResultSet resSet = preparedStatement.executeQuery();
            if (resSet.next()) {
                String str = "" + resSet.getObject(1);
                result = Integer.parseInt(str);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return result;
    }


    public int writeLines(Car car) throws NullPointerException {

        String insstr = "INSERT INTO cars(model_name, registration_number, first_registration, " +
                "odometer, car_group_id, brand_id, fueltype_id) " +
                "VALUES ('" + car.getModel_name() + "','" + car.getRegistration_number() + "','" +
                car.getFirst_registration() + "','" + car.getOdometer() + "','" + car.getCar_group_id() +
                "','" + car.getBrand_id() + "','" + car.getFuelType_id() + "')";

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


    // ---------------------------------------------------
    // ---------------------------------------------------
    // ---------------------------------------------------
    // ---------------------------------------------------
    // ---------------------------------------------------
    // ---------------------------------------------------
    // ---------------------------------------------------
    // ---------------------------------------------------

    public int insertCustomer(Customer customer) throws NullPointerException {

        String insstr = "INSERT INTO customers(first_name, last_name, email, address_id) " +
                "VALUES ('" + customer.getFirst_name() + "','" + customer.getLast_name() + "','" +
                customer.getEmail() + "','" + customer.getAddress_id() + "')";

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

    public boolean zipCodeExists(int zipcode) {
        boolean exists = false;


        PreparedStatement preparedStatement;
        String searchStr = "SELECT zipcode_id from zipcodes where zipcode = ?";
        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setInt(1, zipcode);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                result = resultSet.getInt(1);
            }

            exists = true;
        } catch (SQLException error) {
            error.printStackTrace();
        }

        return exists;
    }

    public boolean cityExists(String city_name) {
        boolean exists = false;


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

            exists = true;
        } catch (SQLException error) {
            error.printStackTrace();
        }

        return exists;
    }

    public boolean addressExists(int address_id) {
        boolean exists = false;


        PreparedStatement preparedStatement;
        String searchStr = "SELECT address_id from addresses where address_id = ?";
        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setInt(1, address_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                result = resultSet.getInt(1);
            }

            exists = true;
        } catch (SQLException error) {
            error.printStackTrace();
        }

        return exists;
    }

    public int insertZipCode(int zipcode, String city_name) throws NullPointerException {
        int result = 0;


        String insstr = "INSERT INTO zipcodes(zipcode,city_id) " +
                "VALUES ('" + zipcode + "','" + getCityIDFromDbByCityName(city_name) + "')";

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
                Integer tempCustomer_id = Integer.parseInt(customer_id);
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

    public int getCustomerIdFromDB(String first_name, String last_name) {

        PreparedStatement preparedStatement;
        String searchStr = "SELECT customer_id from customers where first_name = ? and last_name = ?";
        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setString(1, first_name);
            preparedStatement.setString(2, last_name);
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
        int city_id_new = targetChanges.getCity_id();

        String city_name_old = beforeChanges.getCity_name();
        String city_name_new = targetChanges.getCity_name();

        int zipcode_old = beforeChanges.getZipcode();
        int zipcode_new = targetChanges.getZipcode();

        int zipcode_id_old = beforeChanges.getZipcode_id();
        int zipcode_id_new = targetChanges.getZipcode_id();

        int address_id_old = beforeChanges.getAddress_id();
        int address_id_new = targetChanges.getAddress_id();

        String address_name_old = beforeChanges.getAddress_name();
        String address_name_new = targetChanges.getAddress_name();


        PreparedStatement preparedStatement;
        String updateStr = "UPDATE customers SET first_name = ?, last_name = ?, email = ?," +
                " address_id = ? WHERE customer_id = ?";

        int result = -1;


        String updateCity = "UPDATE cities SET city_name = ? where city_id = ?";
        String updateZipcode = "UPDATE zipcodes SET zipcode = ? WHERE zipcode_id = ?";
        String updateAddress = "UPDATE addresses SET address_name = ? WHERE customer_id = ?";


        if (first_name_old.equalsIgnoreCase(first_name_new) && last_name_old.equalsIgnoreCase(last_name_new)
                && email_old.equalsIgnoreCase(email_new) && address_name_old.equalsIgnoreCase(address_name_new)
                && zipcode_old == zipcode_new && city_name_old.equalsIgnoreCase(city_name_new)) {
            System.out.println("No changes made");
            return result;
        }


        // hvis enten zipcode, city_name eller street_name er blevet opdateret
        if (zipcode_old != zipcode_new || !city_name_old.equalsIgnoreCase(city_name_new) ||
                !address_name_old.equalsIgnoreCase(address_name_new)) {

            // tilfælde hvor KUN zipcode er ændret. TESTET!

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

            }
            // tilfælde hvor KUN city name er ændret. TESTET!

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

            }


            /// tilfælde hvor KUN address name er ændret. TESTET!
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
            }

            // tilfælde hvor både zipcode og city_name er ændret, men ikke address_name. TESTET!
            if (zipcode_old != zipcode_new && !city_name_old.equalsIgnoreCase(city_name_new) &&
                    address_name_old.equalsIgnoreCase(address_name_new)) {
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


            // tilfælde hvor zipcode og address_name er ændret, men ikke city_name. TESTET!

            if (zipcode_old != zipcode_new && city_name_old.equalsIgnoreCase(city_name_new) &&
                    !address_name_old.equalsIgnoreCase(address_name_new)) {
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

            // tilfælde hvor city_name og address_name er ændret, men ikke zipcode TESTET

            if (zipcode_old == zipcode_new && !city_name_old.equalsIgnoreCase(city_name_new) &&
                    !address_name_old.equalsIgnoreCase(address_name_new)) {
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
}


    // ---------------------------------------------------
    // ---------------------------------------------------
    // ---------------------------------------------------
    // ---------------------------------------------------
    // ---------------------------------------------------
    // ---------------------------------------------------
    // ---------------------------------------------------


