/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 *
 * @author Tafitasoa-P15B-140
 */
public class Mouvement {
    private String id;
    private Magasin magasin;
    private Article article;
    private BigDecimal quantite;
    private BigDecimal prixUnitaire;
    private Timestamp date;
    private Entre entre;
    private Sortie sortie;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Magasin getMagasin() {
        return magasin;
    }

    public void setMagasin(Magasin magasin) {
        this.magasin = magasin;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Entre getEntre() {
        return entre;
    }

    public void setEntre(Entre entre) {
        this.entre = entre;
    }

    public Sortie getSortie() {
        return sortie;
    }

    public void setSortie(Sortie sortie) {
        this.sortie = sortie;
    }

    public Mouvement() {
    }
    public String getNextId(Connection con)throws Exception{
        String sql = "SELECT MAX(id) AS max_id FROM mouvement";
        String nextId = null;
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String maxId =  rs.getString("max_id");
                if(maxId != null){
                    String prefix = maxId.substring(0, 4);
                    int numericPart = Integer.parseInt(maxId.substring(4));
                    numericPart++; //
                    nextId = prefix + String.format("%03d", numericPart);
                }else{
                    nextId = "MOUV001";
                }
            }
        } catch (Exception e) {
            throw new Exception("Erreur id non recuperer : "+e.getMessage());
        }
        return nextId;
    }
    public void inserer(Connection con) throws Exception{
        String sql = "INSERT INTO mouvement (id, idMagasin, idArticle, quantite, prix_unitaire, date,identre,idsortie) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, this.getNextId(con));
            stmt.setString(2, this.getMagasin().getId());
            stmt.setString(3, this.getArticle().getId());
            stmt.setBigDecimal(4, this.getQuantite());
            stmt.setBigDecimal(5, this.getPrixUnitaire());
            stmt.setTimestamp(6, this.getDate());
            stmt.setString(7, this.getEntre().getId());
            stmt.setString(8, this.getSortie().getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'insertion d'un produit efa mouvement : " + e.getMessage());
        }
    }
}
