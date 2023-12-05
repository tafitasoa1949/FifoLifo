/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Tafitasoa-P15B-140
 */
public class Connexion {
    public static Connection getconnection() throws Exception{
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fifo_lifo", "postgres", "admin");	
            return con;
        }catch (SQLException e) {
            e.getMessage();
        }
	return null;
    }
}
