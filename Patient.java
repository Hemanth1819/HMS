package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection,Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatient(){
        System.out.print("Enter Patient Name:");
        String name = scanner.next();
        System.out.print("Enter Patient Age:");
        int age = scanner.nextInt();
        System.out.print("Enter Patient Gender:");
        String gender = scanner.next();


        try {
            String query = "insert into patients(name, age, gender) values(?,?,?)";
            PreparedStatement ps = connection.prepareStatement(query);

            ps.setString(1,name);
            ps.setInt(2,age);
            ps.setString(3,gender);

            int affectedRows = ps.executeUpdate();
            if(affectedRows > 0) {
                System.out.println("Patient Added Successfully...");
            }
            else {
                System.out.println("Failed to add Patient!!!");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void viewPatients(){

        String query = "select * from patients";
        try {
                PreparedStatement ps = connection.prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                System.out.println("Patient List");
                System.out.println("+------------+--------------+-----------+------------+");
                System.out.println("| Patient id | Name         | Age       | Gender     |");
                System.out.println("+------------+--------------+-----------+------------+");
                while (rs.next()) {
                    int id = rs.getInt(1);
                    String name = rs.getString(2);
                    int age = rs.getInt(3);
                    String gender = rs.getString(4);
                    System.out.printf("| %-10s | %-12s | %-9s | %-10s |\n", id, name, age, gender);
                    System.out.println("+------------+--------------+-----------+------------+");
                }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getPatientByID(int id){

        String query = "select * from patients where id = ?";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return true;
            }else{
                return false;
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
