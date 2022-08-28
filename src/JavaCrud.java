import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JavaCrud {
    private static Connection dbConnection;
    private static PreparedStatement preparedStatement;
    private JPanel Main;
    private JTextField txtProductPrice;
    private JTextField txtProductQty;
    private JButton saveButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JTextField txtProductName;
    private JButton searchButton;
    private JTextField txtProductId;


    public static void main(String[] args) {
        JFrame frame = new JFrame("JavaCrud");
        frame.setContentPane(new JavaCrud().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        //db connection
        DatabaseConnection databaseConnection = new DatabaseConnection();
        dbConnection = databaseConnection.connectToDatabase();
    }

    public JavaCrud() {

        // adding record in database
        saveButton.addActionListener(e -> {

            String productName;
            int productPrice;
            int productQty;

            productName = txtProductName.getText();
            productPrice = Integer.parseInt(txtProductPrice.getText());
            productQty = Integer.parseInt(txtProductQty.getText());


            try {
                preparedStatement = dbConnection.prepareStatement(
                        "INSERT INTO Products(name, price, quantity) VALUES (?,?,?)"
                );

                preparedStatement.setString(1, productName);
                preparedStatement.setInt(2, productPrice);
                preparedStatement.setInt(3, productQty);
                preparedStatement.executeUpdate();

                JOptionPane.showMessageDialog(null, "Record added successfully");

                txtProductName.setText("");
                txtProductPrice.setText("");
                txtProductQty.setText("");
                txtProductName.requestFocus();

            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        });

        // searching record from database
        searchButton.addActionListener(e -> {
            try {
                int productId = Integer.parseInt(txtProductId.getText());

                preparedStatement = dbConnection.prepareStatement(
                        "SELECT name,price,quantity FROM Products WHERE id = ?"
                );
                preparedStatement.setInt(1, productId);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {

                    String productName = resultSet.getString(1);
                    int productPrice = resultSet.getInt(2);
                    int productQty = resultSet.getInt(3);

                    txtProductName.setText(productName);
                    txtProductPrice.setText(String.valueOf(productPrice));
                    txtProductQty.setText(String.valueOf(productQty));

                } else {
                    JOptionPane.showMessageDialog(null, "No record found.");
                }

            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        });

        // updating the record in database
        updateButton.addActionListener(e -> {

            String productId;
            String productName;
            int productPrice;
            int productQty;


            productId = txtProductId.getText();
            if (productId.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter product id and " +
                        "click search to get data first from db.");
                return;
            }
            productName = txtProductName.getText();
            productPrice = Integer.parseInt(txtProductPrice.getText());
            productQty = Integer.parseInt(txtProductQty.getText());

            try {

                preparedStatement = dbConnection.prepareStatement(
                        "UPDATE Products set name = ?, price = ?, quantity = ? WHERE id = ?"
                );
                preparedStatement.setString(1, productName);
                preparedStatement.setInt(2, productPrice);
                preparedStatement.setInt(3, productQty);
                preparedStatement.setString(4, productId);
                preparedStatement.executeUpdate();

                JOptionPane.showMessageDialog(null, "Record updated successfully");

            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        });

        // deleting the record from database
        deleteButton.addActionListener(e -> {
            String productId = txtProductId.getText();

            if (productId.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please product id to delete it from" +
                        "database.");
                return;
            }

            try {
                preparedStatement = dbConnection.prepareStatement(
                        "DELETE FROM Products WHERE id = ?"
                );
                preparedStatement.setString(1, productId);
                preparedStatement.executeUpdate();

                JOptionPane.showMessageDialog(null, "Record deleted successfully.");

                txtProductName.setText("");
                txtProductPrice.setText("");
                txtProductQty.setText("");
                txtProductId.setText("");

            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        });
    }
}
