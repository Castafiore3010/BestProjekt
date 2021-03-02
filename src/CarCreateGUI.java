import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.time.LocalDate;


public class CarCreateGUI {


    private final JFrame frame;
    private final JPanel panel;
    private final JPanel buttonPanel;
    private  JTextField modelNameTextField;
    private  JTextField registrationNumberTextField;
    private  JTextField firstRegistrationTextField;
    private  JTextField odometerTextField;
    private  JTextField carGrpIdTextField;
    private  JTextField brandIdTextField;
    private  JTextField fuelTypeIdTextField;
    private  JButton applyBtn;
    private  JButton returnBtn;
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
        return applyBtn;
    }


    public JFrame getFrame() {
        return frame;
    }


    public void returnToMain() {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }




    public void apply() {

        JDBCWriter jdbcWriter = new JDBCWriter();
        String model_name;
        String registration_number;
        LocalDate first_registration;
        double odometer;
        int car_group_id;
        int brand_id;
        int fuelType_id;

        model_name = modelNameTextField.getText();
        registration_number = registrationNumberTextField.getText();

        String localDateText = firstRegistrationTextField.getText();
        first_registration = (!localDateText.equalsIgnoreCase("") ? LocalDate.parse(localDateText) : LocalDate.now());

        String odometerString = odometerTextField.getText();
        odometer = (!odometerString.equalsIgnoreCase("") ? Double.parseDouble(odometerString) : 0);

        String carGrpStr = carGrpIdTextField.getText();
        car_group_id = (!carGrpStr.equalsIgnoreCase("") ? Integer.parseInt(carGrpStr) : 1);

        String brandIdStr = brandIdTextField.getText();
        brand_id = (!brandIdStr.equalsIgnoreCase("") ? Integer.parseInt(brandIdStr) : 1);

        String fuelTypeStr = fuelTypeIdTextField.getText();
        fuelType_id = (!fuelTypeStr.equalsIgnoreCase("") ? Integer.parseInt(fuelTypeStr) : 1);

        Car car = new Car(model_name,registration_number,first_registration,odometer,car_group_id,
                brand_id,fuelType_id);


        if (jdbcWriter.setConnection()) {
            jdbcWriter.writeLines(car);
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));


        }
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        helpCar = car;



    }


    public CarCreateGUI() {

        // frame

        frame = new JFrame("Car to DataBase");
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
        applyBtn = new JButton("Apply ");

        returnBtn.addActionListener(a -> returnToMain());
        applyBtn.addActionListener(a -> apply());

        buttonPanel.add(applyBtn);
        buttonPanel.add(returnBtn);


        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        JPanel outputPanel = new JPanel();
        JLabel outputLabel = new JLabel("Output");
        JScrollPane scrollPane = new JScrollPane();
        JTextArea textArea = new JTextArea("", 5,20);
        textArea.setEditable(false);
        scrollPane.setViewportView(textArea);
        outputPanel.add(outputLabel);
        outputPanel.add(scrollPane);
        outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.Y_AXIS));





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
        panel.add(outputPanel);














        panel.setLayout(new FlowLayout(FlowLayout.CENTER));



        frame.setPreferredSize(new Dimension(250,450));
        frame.setLayout(new GridLayout());
        frame.setBounds(600,100,250,500);
        frame.getContentPane().add(panel);












    }

    public void run() {
        frame.setVisible(true);
    }

}
