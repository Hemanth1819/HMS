package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctor {
    private Connection connection;


    public Doctor(Connection connection) {
        this.connection = connection;
    }


    public void viewDoctors(){

        String query = "select * from doctors";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            System.out.println("Doctors List");
            System.out.println("+------------+--------------+------------------------+");
            System.out.println("| Doctor id  |   Name       |   Specialization       |");
            System.out.println("+------------+--------------+------------------------+");
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String specialization = rs.getString(3);
                System.out.printf("| %-10s | %-12s | %-22s |\n", id, name, specialization);
                System.out.println("+------------+--------------+------------------------+");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getDoctorByID(int id){

        String query = "select * from doctors where id = ?";
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
