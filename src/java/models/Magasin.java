/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 *
 * @author Tafitasoa-P15B-140
 */
public class Magasin {
    private String id;
    private String nom;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Magasin() {
    }
    public Magasin getById(String id,Connection con)throws Exception{
        String sql = "select * from magasin where id=?";
        Magasin magasin = null;
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                magasin = new Magasin();
                magasin.setId(id);
                magasin.setNom(rs.getString("nom"));
            }
        }catch (Exception e) {
            throw new Exception("Erreur lors de la récupération du magasin : " + e.getMessage());
        }
        return magasin;
    }
    public Magasin[] getAll(Connection con)throws Exception{
        String sql = "SELECT * FROM  magasin";
        Magasin[] magasins = null;
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }
            magasins = new Magasin[rowCount];
            rs = stmt.executeQuery();
            int index = 0;
            while(rs.next()){
                Magasin magasin = new Magasin();
                magasin.setId(rs.getString("id"));
                magasin.setNom(rs.getString("nom"));
                magasins[index] = magasin;
                index++;
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return magasins;
    }
    public Mouvement getLastMouvement(String idArticle,Connection con)throws Exception{
        Mouvement mouvement = null;
        String sqlDate = "SELECT MAX(date) AS derniere_date FROM mouvement WHERE idMagasin = ? AND idArticle = ?";
        try (PreparedStatement stmtDate = con.prepareStatement(sqlDate)) {
            stmtDate.setString(1, this.getId());
            stmtDate.setString(2, idArticle);
            ResultSet rsDate = stmtDate.executeQuery();
            if (rsDate.next()) {
                Timestamp derniereDate = rsDate.getTimestamp("derniere_date");
                String sqlData = "SELECT * FROM mouvement WHERE idMagasin = ? AND idArticle = ? AND date = ?";
                try (PreparedStatement stmtData = con.prepareStatement(sqlData)) {
                    stmtData.setString(1, this.getId());
                    stmtData.setString(2, idArticle);
                    stmtData.setTimestamp(3, derniereDate);

                    ResultSet rsData = stmtData.executeQuery();
                    if (rsData.next()) {
                        mouvement = new Mouvement();
                        mouvement.setId(rsData.getString("id"));
                        Magasin magasin = new Magasin().getById(this.getId(), con);
                        mouvement.setMagasin(magasin);
                        Article article = new Article().getById(idArticle, con);
                        mouvement.setArticle(article);
                        mouvement.setQuantite(rsData.getBigDecimal("quantite"));
                        mouvement.setPrixUnitaire(rsData.getBigDecimal("prix_unitaire"));
                        mouvement.setDate(rsData.getTimestamp("date"));
                    }
                }
            }
        } catch (SQLException e) {
            //throw new Exception(e.getMessage());
            e.printStackTrace();
        }
        return mouvement;
    }
    public void checkArticleExist(Article article_donne,Connection con,Timestamp date_donne)throws Exception{
        String sql = "select * from mouvement where idmagasin=? and idarticle=?";
        try (PreparedStatement stmt= con.prepareStatement(sql)) {
            stmt.setString(1, this.getId());
            stmt.setString(2, article_donne.getId());
            ResultSet rs = stmt.executeQuery();
            int count = 0;
            if (rs.next()) {
                count++;
            }
            if(count <= 0){
                System.out.println("tsisyyyyy");
                throw new Exception("Il n'y a pas de cette article dans cette magasin");
            }else{
                System.out.println("misy");
            }
        }
    }
    public EtatStock getEtatStock(Article article,Connection con,Timestamp datedebut,Timestamp datefin)throws Exception{
        EtatStock etatStock = new EtatStock();
        Entre entre = new Entre();
        etatStock.setMagasin(this);
        //
        article.setUnite(article.getUnite(con));
        etatStock.setArticle(article);
        BigDecimal quantite_initial = entre.getSoldeRestant(con, this, article, datedebut);
        etatStock.setQuantite_initial(quantite_initial);
        BigDecimal quantite_final = entre.getSoldeRestant(con, this, article, datefin);
        etatStock.setQuanite_final(quantite_final);
        BigDecimal pump = entre.getPUMP(con, this, article, datefin);
        etatStock.setPump(pump);
        BigDecimal montant = entre.getMontant(con, this, article, datefin);
        etatStock.setMontant(montant);
        return etatStock;
    }
    public static void main(String[] args) throws Exception {
        Connection con = Connexion.getconnection();
        String id="M001";
        String idArticle = "V001";
        //Magasin magasin = new Magasin().getById(id,con);
        //System.out.println("Magasin : "+magasin.getNom());
        /*Magasin[] list = new Magasin().getAll(con);
        for(int i=0 ; i< list.length ; i++){
            System.out.println(list[i].getNom());
        }*/
        String datedebut = "2021-01-01 00:00:00'";
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        java.util.Date parsedDateDebut = dateFormat.parse(datedebut);
        Timestamp timestampdebut = new Timestamp(parsedDateDebut.getTime());
        String datefin = "2022-01-01 00:00:00'";
        String pattern1 = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat1 = new SimpleDateFormat(pattern1);
        java.util.Date parsedDatefin = dateFormat1.parse(datefin);
        Timestamp timestampfin = new Timestamp(parsedDatefin.getTime());
        Magasin magasin = new Magasin().getById(id, con);
        Article article = new Article().getById(idArticle, con);
        //Mouvement mouvement = magasin.getLastMouvement(idArticle, con);
        
        /*try {
            EtatStock etatStock = magasin.getEtatStock(article, con, timestampdebut, timestampfin);
            System.out.println(etatStock.getQuantite_initial());
            System.out.println(etatStock.getQuanite_final());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        magasin.checkArticleExist(article, con, timestampfin);
        con.close();
    }
}
