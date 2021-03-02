import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class UserIDPrompt {



    private  JFrame frame;
    private  JPanel panel;
    private  JPanel buttonPanel;
    private  JTextField modelNameTextField;
    private  JTextField registrationNumberTextField;
    private  JTextField firstRegistrationTextField;
    private Car carHelp;

    public Car getCarHelp() {
        return carHelp;
    }



    public int getID() {
        return ID;
    }

    private int ID;



    private  JTextField odometerTextField;
    private  JTextField carGrpIdTextField;
    private  JTextField brandIdTextField;
    private  JTextField fuelTypeIdTextField;



    private  JButton applyBtn;

    public JButton getReturnBtn() {
        return returnBtn;
    }

    private  JButton returnBtn;
    private Car helpCar;



    public JFrame getFrame() {
        return frame;
    }


    public JButton getApplyBtn() {
        return applyBtn;
    }



    public Car applyPress() {

        JDBCWriter jdbcWriter = new JDBCWriter();

        Car car = jdbcWriter.getCarFromDBbyIndex(Integer.parseInt(modelNameTextField.getText()));
        ID = Integer.parseInt(modelNameTextField.getText());


        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));

        carHelp = car;

        return car;


    }

    public UserIDPrompt() {

        // frame

        frame = new JFrame("Enter Car ID");
        frame.setResizable(false);


        // panels

        panel = new JPanel();
        buttonPanel = new JPanel();


        // Text fields

        modelNameTextField = new JTextField("", 20);
        modelNameTextField.setEditable(true);



        // Labels

        JLabel modelNameLabel = new JLabel("Enter ID");


        // ButtonPanel edit + buttons
        returnBtn = new JButton("Return");
        applyBtn = new JButton("Apply ");

        applyBtn.addActionListener(a -> applyPress());



        buttonPanel.add(applyBtn);
        buttonPanel.add(returnBtn);


        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));


        panel.add(modelNameLabel);
        panel.add(modelNameTextField);
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
