/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Tafitasoa-P15B-140
 */
public class Unite {
    private int id;
    private Article article;
    private String nom;
    private BigDecimal equivalence;

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

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public BigDecimal getEquivalence() {
        return equivalence;
    }

    public void setEquivalence(BigDecimal equivalence) {
        this.equivalence = equivalence;
    }
    
    public Unite() {
    }
    public Unite getById(int id,Connection con)throws Exception{
        String sql = "select * from unite where id=?";
        Unite unite = null;
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                unite = new Unite();
                unite.setId(id);
                String idArticle = rs.getString("idarticle");
                Article article = new Article().getById(idArticle, con);
                unite.setArticle(article);
                unite.setNom(rs.getString("nom"));
                unite.setEquivalence(rs.getBigDecimal("equivalence"));
                
            }
        }catch (Exception e) {
            throw new Exception("Erreur lors de la récupération d'unite : " + e.getMessage());
        }
        return unite;
    }
    public static void main(String[] args) throws Exception {
        Connection con = Connexion.getconnection();
        
        con.close();
    }
}
