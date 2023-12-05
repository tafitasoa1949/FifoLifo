/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 *
 * @author Tafitasoa-P15B-140
 */
public class Valeur {
    private Article article;
    private Unite unite;
    private BigDecimal valeur;
    private Entre entre;
    private Sortie sortie;
    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Unite getUnite() {
        return unite;
    }

    public void setUnite(Unite unite) {
        this.unite = unite;
    }

    public BigDecimal getValeur() {
        return valeur;
    }

    public void setValeur(BigDecimal valeur) {
        this.valeur = valeur;
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
    public void insererEntre(Connection con) throws Exception{
        String sql = "insert into valeur values (?,?,?,null,null);";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, this.getArticle().getId());
            stmt.setInt(2, this.getUnite().getId());
            stmt.setBigDecimal(3, this.getValeur());
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'insertion d'un produit : " + e.getMessage());
        }
    }
    public void insererSortie(Connection con) throws Exception{
        String sql = "insert into valeur values (?,?,?,null,null);";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, this.getArticle().getId());
            stmt.setInt(2, this.getUnite().getId());
            stmt.setBigDecimal(3, this.getValeur());
            stmt.setString(4, this.getSortie().getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'insertion d'un produit : " + e.getMessage());
        }
    }
}
