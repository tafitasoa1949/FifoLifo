/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Tafitasoa-P15B-140
 */
public class Categorie {
    private int id;
    private String nom;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Categorie() {
    }
    public Categorie getById(int id,Connection con)throws Exception{
        String sql = "select * from categorie where id=?";
        Categorie categorie = null;
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                categorie = new Categorie();
                categorie.setId(id);
                categorie.setNom(rs.getString("nom"));
            }
        }catch (Exception e) {
            throw new Exception("Erreur lors de la récupération du categorie : " + e.getMessage());
        }
        return categorie;
    }
    public static void main(String[] args) throws Exception {
        Connection con = Connexion.getconnection();
        int id=1;
        Categorie categorie = new Categorie().getById(id,con);
        System.out.println("Unite : "+categorie.getNom());
        con.close();
    }
}
