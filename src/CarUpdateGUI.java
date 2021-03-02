import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.time.LocalDate;


public class CarUpdateGUI {


    private JFrame frame;
    private JPanel panel;
    private JPanel buttonPanel;
    private JTextField modelNameTextField;
    private JTextField registrationNumberTextField;
    private JTextField firstRegistrationTextField;
    private JTextField odometerTextField;
    private JTextField carGrpIdTextField;
    private JTextField brandIdTextField;
    private JTextField fuelTypeIdTextField;
    private JButton updateBtn;
    private JButton returnBtn;
    private Car helpCar;


    public JTextField getModelNameTextField() {
        return modelNameTextField;
    }

    public void setModelNameTextField(JTextField modelNameTextField) {
        this.modelNameTextField = modelNameTextField;
    }

    public JTextField getRegistrationNumberTextField() {
        return registrationNumberTextField;
    }

    public void setRegistrationNumberTextField(JTextField registrationNumberTextField) {
        this.registrationNumberTextField = registrationNumberTextField;
    }

    public JTextField getFirstRegistrationTextField() {
        return firstRegistrationTextField;
    }

    public void setFirstRegistrationTextField(JTextField firstRegistrationTextField) {
        this.firstRegistrationTextField = firstRegistrationTextField;
    }

    public JTextField getOdometerTextField() {
        return odometerTextField;
    }

    public void setOdometerTextField(JTextField odometerTextField) {
        this.odometerTextField = odometerTextField;
    }

    public JTextField getCarGrpIdTextField() {
        return carGrpIdTextField;
    }

    public void setCarGrpIdTextField(JTextField carGrpIdTextField) {
        this.carGrpIdTextField = carGrpIdTextField;
    }

    public JTextField getBrandIdTextField() {
        return brandIdTextField;
    }

    public void setBrandIdTextField(JTextField brandIdTextField) {
        this.brandIdTextField = brandIdTextField;
    }

    public JTextField getFuelTypeIdTextField() {
        return fuelTypeIdTextField;
    }

    public void setFuelTypeIdTextField(JTextField fuelTypeIdTextField) {
        this.fuelTypeIdTextField = fuelTypeIdTextField;
    }


    public Car getHelpCar() {
        return helpCar;
    }

    public JButton getReturnBtn() {
        return returnBtn;
    }

    public JButton getApplyBtn() {
        return updateBtn;
    }


    public JFrame getFrame() {
        return frame;
    }


    public void returnToMain() {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }


    public void fillTextFields(Car car) {

        modelNameTextField = new JTextField();
        registrationNumberTextField = new JTextField();
        firstRegistrationTextField = new JTextField();
        odometerTextField = new JTextField();
        carGrpIdTextField = new JTextField();
        brandIdTextField = new JTextField();
        fuelTypeIdTextField = new JTextField();


        modelNameTextField.setText(car.getModel_name());
        registrationNumberTextField.setText(car.getRegistration_number());
        firstRegistrationTextField.setText(String.valueOf(car.getFirst_registration()));
        odometerTextField.setText(String.valueOf(car.getOdometer()));
        carGrpIdTextField.setText(String.valueOf(car.getCar_group_id()));
        brandIdTextField.setText(String.valueOf(car.getBrand_id()));
        fuelTypeIdTextField.setText(String.valueOf(car.getFuelType_id()));


    }


    public void update() {

        JDBCWriter jdbcWriter = new JDBCWriter();
        jdbcWriter.setConnection();


        String model_name = modelNameTextField.getText();
        String registration_number = registrationNumberTextField.getText();
        LocalDate first_registration = LocalDate.parse(firstRegistrationTextField.getText());
        double odometer = Double.parseDouble(odometerTextField.getText());
        int car_group_id = Integer.parseInt(carGrpIdTextField.getText());
        int brand_id = Integer.parseInt(brandIdTextField.getText());
        int fuelType_id = Integer.parseInt(fuelTypeIdTextField.getText());

        Car car = new Car(model_name, registration_number, first_registration, odometer, car_group_id,
                brand_id, fuelType_id);

        //fillTextFields(car);

        int s1 = jdbcWriter.getCarIdFromDB(car.getModel_name());


        jdbcWriter.updateCar(s1, car);


        frame.setVisible(false);


    }


    public CarUpdateGUI() {

        // frame

        frame = new JFrame("Update Car to DataBase");
        frame.setResizable(false);


        // panels

        panel = new JPanel();
        buttonPanel = new JPanel();


        // Text fields

        modelNameTextField = new JTextField("", 20);
        registrationNumberTextField = new JTextField("", 20);
        firstRegistrationTextField = new JTextField("", 20);
        odometerTextField = new JTextField("", 20);
        carGrpIdTextField = new JTextField("", 20);
        brandIdTextField = new JTextField("", 20);
        fuelTypeIdTextField = new JTextField("", 20);


        // Labels

        JLabel modelNameLabel = new JLabel("Model name");
        JLabel registrationNumberLabel = new JLabel("Reg. number");
        JLabel firstRegistrationLabel = new JLabel("First reg. date");
        JLabel odometerLabel = new JLabel("Odometer reading");
        JLabel carGrpLabel = new JLabel("CarGrp ID");
        JLabel brandIdLabel = new JLabel("Brand ID");
        JLabel fuelTypeIdLabel = new JLabel("Fuel type ID");

        // ButtonPanel edit + buttons
        returnBtn = new JButton("Return");
        updateBtn = new JButton("Update");


        returnBtn.addActionListener(a -> returnToMain());
        updateBtn.addActionListener(a -> update());

        buttonPanel.add(updateBtn);
        buttonPanel.add(returnBtn);


        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));


        panel.add(modelNameLabel);
        panel.add(modelNameTextField);
        panel.add(registrationNumberLabel);
        panel.add(registrationNumberTextField);
        panel.add(firstRegistrationLabel);
        panel.add(firstRegistrationTextField);
        panel.add(odometerLabel);
        panel.add(odometerTextField);
        panel.add(carGrpLabel);
        panel.add(carGrpIdTextField);
        panel.add(brandIdLabel);
        panel.add(brandIdTextField);
        panel.add(fuelTypeIdLabel);
        panel.add(fuelTypeIdTextField);
        panel.add(buttonPanel);


        panel.setLayout(new FlowLayout(FlowLayout.CENTER));


        frame.setPreferredSize(new Dimension(250, 450));
        frame.setLayout(new GridLayout());
        frame.setBounds(600, 100, 250, 500);
        frame.getContentPane().add(panel);


    }

    public void run() {
        frame.setVisible(true);
    }
}


