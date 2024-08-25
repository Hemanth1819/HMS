package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {

    private static final String url="jdbc:mysql://localhost:3306/hospital";
    private static final String user="root";
    private static final String password="Ooty@123";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner sc=new Scanner(System.in);
        try{
            Connection con= DriverManager.getConnection(url,user,password);
            Patient patient= new Patient(con,sc);
            Doctor doctor= new Doctor(con);

            while (true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. view Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter your choice:");
                int choice=sc.nextInt();
                switch(choice){
                    case 1:
                        //Add
                        patient.addPatient();
                        break;
                    case 2:
                        //view patient
                        patient.viewPatients();
                        System.out.println("Patient viewed");
                        break;
                    case 3:
                        //view doctors
                        doctor.viewDoctors();
                        break;
                    case 4:
                        //Book Appointment
                        bookAppointment(patient,doctor,con,sc);;
                        break;
                    case 5:
                        System.out.println("Thank you for using Hospital Management System...");
                        return;
                    default:
                        System.out.println("Invalid choice");
                        break;
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patient patient,Doctor doctor,Connection connection,Scanner scanner){
        System.out.println("Enter patient ID:");
        int patientID=scanner.nextInt();
        System.out.println("Enter doctor ID:");
        int doctorID=scanner.nextInt();
        System.out.println("Enter appointment Date(YYYY-MM-DD):");
        String appointmentDate=scanner.next();

        if(patient.getPatientByID(patientID) && doctor.getDoctorByID(doctorID)){
            if(checkDoctorAvailability(doctorID,appointmentDate,connection)){
               String sql = "INSERT INTO appontments(patient_id,doctor_id,appointment_date) VALUES(?,?,?)";
               try{
                   PreparedStatement preparedStatement=connection.prepareStatement(sql);
                   preparedStatement.setInt(1,patientID);
                   preparedStatement.setInt(2,doctorID);
                   preparedStatement.setString(3,appointmentDate);
                   int result=preparedStatement.executeUpdate();

                   if(result>0){
                       System.out.println("Appointment Booked...");
                   }else {
                       System.out.println("Failed to Book Appointment!!!");
                   }
               }catch (SQLException e){
                   e.printStackTrace();
               }

            }else {
                System.out.println("Doctor is not available!!!");
            }
        }else{
            System.out.println("Either Doctor or Patient does not exist!!!");
        }
    }

    public static boolean checkDoctorAvailability(int doctorID,String appointmentDate,Connection connection){
        String query = "SELECT COUNT(*) FROM appontments WHERE doctor_id=? AND appointment_date=?";
        try {
            PreparedStatement preparedStatement =connection.prepareStatement(query);
            preparedStatement.setInt(1,doctorID);
            preparedStatement.setString(2,appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                if(count == 0){
                    return true;
                }else {
                    return false;
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
