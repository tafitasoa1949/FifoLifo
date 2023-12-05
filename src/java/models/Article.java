/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Tafitasoa-P15B-140
 */
public class Article {
    private String id;
    private String nom;
    private Categorie categorie;
    private Unite unite;
    
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

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public Unite getUnite() {
        return unite;
    }

    public void setUnite(Unite unite) {
        this.unite = unite;
    }
    
    public Article() {
    }
    public Article getById(String id,Connection con)throws Exception{
        String sql = "select * from article where id=?";
        Article article = null;
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                article = new Article();
                article.setId(id);
                article.setNom(rs.getString("nom"));
                Integer idCategorie = rs.getInt("idcategorie");
                if(idCategorie != null){
                    Categorie categorie = new Categorie().getById(idCategorie, con);
                    article.setCategorie(categorie);
                }
            }
        }catch (Exception e) {
            throw new Exception("Erreur lors de la récupération du article : " + e.getMessage());
        }
        return article;
    }
    public Article[] getAll(Connection con)throws Exception{
        String sql = "SELECT * FROM  article";
        Article[] list_articles = null;
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }
            list_articles = new Article[rowCount];
            rs = stmt.executeQuery();
            int index = 0;
            while(rs.next()){
                Article article = new Article();
                article.setId(rs.getString("id"));
                article.setNom(rs.getString("nom"));
                Integer idCategorie = rs.getInt("idcategorie");
                if(idCategorie != null){
                    Categorie categorie = new Categorie().getById(idCategorie, con);
                    article.setCategorie(categorie);
                }
                list_articles[index] = article;
                index++;
            }
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
        return list_articles;
    }
    public Unite[] getByArticle(Connection con)throws Exception{
        String sql = "select * from unite where idarticle =?";
        Unite[] unite_list = null;
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, this.getId());
            ResultSet rs = stmt.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }
            unite_list = new Unite[rowCount];
            rs = stmt.executeQuery();
            int index = 0;
            while(rs.next()){
                Unite unite = new Unite();
                unite.setId(rs.getInt("id"));
                unite.setArticle(this);
                unite.setNom(rs.getString("nom"));
                unite.setEquivalence(rs.getBigDecimal("equivalence"));
                unite_list[index] = unite;
                index++;
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return unite_list;
    }
    public Unite getUnite(Connection con)throws Exception{
        String sql = "select * from unite where idarticle=? and equivalence =1";
        Unite unite = null;
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, this.getId());
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                unite = new Unite();
                unite.setId(rs.getInt("id"));
                unite.setArticle(this);
                unite.setNom(rs.getString("nom"));
                unite.setEquivalence(rs.getBigDecimal("equivalence"));
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return unite;
    }
    public static void main(String[] args) throws Exception {
        Connection con = Connexion.getconnection();
        String id="V001";
        //Article article = new Article().getById(id,con);
        //System.out.println("Article : "+article.getUnite().getNom());
        /*Article[] list = new Article().getAll(con);
        for(int i=0 ; i < list.length ; i++){
            System.out.println(list[i].getNom());
        }
        
        int id=2;
        Unite unite = new Unite().getById(id,con);
        Article article = new Article().getById("V001", con);
        unite.setArticle(article);
        /*System.out.println("Unite : "+unite.getNom());
        System.out.println("Article : "+unite.getArticle().getNom());
        System.out.println("Nom : "+unite.getNom());
        System.out.println("Equivalence : "+unite.getEquivalence());
        System.out.println("P U : "+unite.getPrix_unitaire());*/
        Article article = new Article().getById(id,con);
        /*Unite[] listUnite = article.getByArticle(con);
        for(int i=0 ; i < listUnite.length ; i++){
            System.out.print(" Unite : "+listUnite[i].getNom());
            System.out.print("   Article : "+listUnite[i].getArticle().getNom());
            System.out.print("  Nom : "+listUnite[i].getNom());
            System.out.print("  Equivalence : "+listUnite[i].getEquivalence());
        }*/
        article.setUnite(article.getUnite(con));
        System.out.println("Unite "+article.getUnite().getNom());
        con.close();
    }
}
