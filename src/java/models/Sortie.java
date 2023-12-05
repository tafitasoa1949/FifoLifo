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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Tafitasoa-P15B-140
 */
public class Sortie {
    private String id;
    private Magasin magasin;
    private Article article;
    private BigDecimal quantite;
    private Timestamp date;

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

    public void setQuantite(BigDecimal quantite) throws Exception{
        if (quantite.compareTo(BigDecimal.ZERO) >= 0) {
            this.quantite = quantite;
        }else{
            throw new Exception("Quantite invalid");
        }
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
    public void setDate(String date)throws Exception{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        try {
            Date parsedDate = dateFormat.parse(date);
            this.date = new Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            throw new Exception("Erreur lors de la conversion de la date en timestamp");
        }
    }

    public Sortie() {
    }
    public String getNextId(Connection con)throws Exception{
        String sql = "SELECT MAX(id) AS max_id FROM sortie_provisoire";
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
                    nextId = "SORT001";
                }
            }
        } catch (SQLException e) {
            throw new Exception("Erreur id non recuperer : "+e.getMessage());
        }
        return nextId;
    }
    public void inserer(Connection con) throws Exception{
        String sql = "INSERT INTO sortie_provisoire VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, this.getNextId(con));
            stmt.setString(2, this.getMagasin().getId());
            stmt.setString(3, this.getArticle().getId());
            BigDecimal quantite_equivalence = this.getQuantite().multiply(this.getArticle().getUnite().getEquivalence());
            stmt.setBigDecimal(4, quantite_equivalence);
            stmt.setTimestamp(5, this.getDate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erreur lors de l'insertion de sortie d'un produit : " + e.getMessage());
        }
    }
    public void change(Connection con)throws Exception{
        String sql = "UPDATE sortie_provisoire SET idmagasin=?, idarticle=?, quantite=?, date=? WHERE id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, this.getMagasin().getId());
            stmt.setString(2, this.getArticle().getId());
            stmt.setBigDecimal(3, this.getQuantite());
            stmt.setTimestamp(4, this.getDate());
            stmt.setString(5,this.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erreur lors d'update de sortie d'un produit : " + e.getMessage());
        }
    }
    public Sortie getById(String id,Connection con)throws Exception{
        String sql = "select * from sortie_provisoire where id=?";
        Sortie sortie = null;
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                sortie = new Sortie();
                sortie.setId(id);
                String idMagasin = rs.getString("idmagasin");
                Magasin magasin = new Magasin().getById(idMagasin, con);
                sortie.setMagasin(magasin);
                String idArticle = rs.getString("idarticle");
                Article article = new Article().getById(idArticle, con);
                sortie.setArticle(article);
                sortie.setQuantite(rs.getBigDecimal("quantite"));
                sortie.setDate(rs.getTimestamp("date"));
            }
        }catch (Exception e) {
            throw new Exception("Erreur lors de la récupération du sortie : " + e.getMessage());
        }
        return sortie;
    }
    public Sortie[] getSortieNonValide(Connection con)throws Exception{
        String sql = "select * from sortienonvalide";
        Sortie[] list_sortie = null;
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }
            list_sortie = new Sortie[rowCount];
            rs = stmt.executeQuery();
            int index = 0;
            while(rs.next()){
                Sortie sortie = new Sortie();
                sortie.setId(rs.getString("id"));
                String idMagasin = rs.getString("idmagasin");
                Magasin magasin = new Magasin().getById(idMagasin, con);
                sortie.setMagasin(magasin);
                String idArticle = rs.getString("idarticle");
                Article article = new Article().getById(idArticle, con);
                sortie.setArticle(article);
                sortie.setQuantite(rs.getBigDecimal("quantite"));
                sortie.setDate(rs.getTimestamp("date"));
                list_sortie[index] = sortie;
                index++;
            }
        } catch (SQLException e) {
            throw e;
        }
        return list_sortie;
    }
    public void checkDate(Connection con,Timestamp date_validation)throws Exception{
        Mouvement lastMouve = this.getMagasin().getLastMouvement(this.getArticle().getId(), con);
        System.out.println("lll"+lastMouve.getDate());
        if(lastMouve != null){
            Timestamp lastMouveDate = lastMouve.getDate();
            if (date_validation.before(lastMouveDate)) {
                //System.out.println("huhu : "+date_validation);
                throw new Exception("La date de validation est antérieure à la date du dernier mouvement.");
            }else{
                //System.out.println("date farany "+lastMouve);
                System.out.println("Date validation : mety leh "+date_validation);
            }
        }
    }
    public void checkSolde(Connection con,Timestamp date_validation)throws Exception{
        Boolean result = false;
        Entre[] list_entre = new Entre().getAll(con, this.getMagasin(), this.getArticle(),date_validation);
        BigDecimal somme_reste_quantite = BigDecimal.ZERO;
        for(int i=0 ; i<list_entre.length ; i++){
            somme_reste_quantite = somme_reste_quantite.add(list_entre[i].getReste_quantite());
        }
        BigDecimal difference = somme_reste_quantite.subtract(this.getQuantite());
        if (difference.compareTo(BigDecimal.ZERO) >= 0) {
            result = true;
        }else{
            throw new Exception("Stock insuffisant");
        }
    }
    public void decomposition(Connection con,Timestamp date_validation)throws Exception{
        Entre[] list_entre = new Entre().getAll(con, this.getMagasin(), this.getArticle(),date_validation);
        for (Entre entre : list_entre) {
            BigDecimal resteEntre = entre.getReste_quantite();

            if (this.getQuantite().compareTo(resteEntre) > 0) {
                // La quantité de sortie est supérieure à la quantité restante de l'entrée actuelle
                //System.out.println("Quantite restante "+resteEntre);
                Mouvement mouvement = new Mouvement();
                mouvement.setMagasin(this.getMagasin());
                mouvement.setArticle(this.getArticle());
                mouvement.setQuantite(resteEntre);
                mouvement.setPrixUnitaire(BigDecimal.ZERO);
                mouvement.setDate(date_validation);
                mouvement.setEntre(entre);
                mouvement.setSortie(this);
                mouvement.inserer(con);
                this.setQuantite(this.getQuantite().subtract(resteEntre));
                //System.out.println("Quantite restante aview "+this.getQuantite());
            } else {
                //quantité de sortie est inférieure ou égale à la quantité restante
                Mouvement mouvement1 = new Mouvement();
                mouvement1.setMagasin(this.getMagasin());
                mouvement1.setArticle(this.getArticle());
                mouvement1.setQuantite(this.getQuantite());
                mouvement1.setPrixUnitaire(BigDecimal.ZERO);
                mouvement1.setDate(date_validation);
                mouvement1.setEntre(entre);
                mouvement1.setSortie(this);
                mouvement1.inserer(con);
                //System.out.println("Mety ein");
                break;
            }
        }
    }
    public static Timestamp casteStringToTimestamp(String date)throws Exception{
        Timestamp result = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        try {
            Date parsedDate = dateFormat.parse(date);
            result = new Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            // Gérer l'exception de conversion de date
            throw new Exception("Erreur lors de la conversion de la date en timestamp", e);
        }
        return result;
    }
    public void insertValidation(Connection con,Timestamp date_validation)throws Exception{
        String sql = "INSERT INTO validation1 (idsortie, date) VALUES (?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, this.getId());
            stmt.setTimestamp(2, date_validation);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'insertion de validation : " + e.getMessage());
        }
    }
    public static void main(String[] args) throws Exception {
        Entre entre = new Entre();
        Connection con = Connexion.getconnection();
        //System.out.println("NextId : "+entre.getNextId(con));
        String datedebut = "2023-11-24 22:04:00'";
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        java.util.Date parsedDateDebut = dateFormat.parse(datedebut);
        Timestamp timestampdebut = new Timestamp(parsedDateDebut.getTime());
        Magasin mm = new Magasin();
        mm.setId("M001");
        Categorie categorie = new Categorie().getById(1,con);
        Article article = new Article();
        article.setId("V001");
        article.setCategorie(categorie);
        Entre[] list = new Entre().getAll(con,mm,article,timestampdebut);
        for(int i=0 ; i< list.length ; i++){
            System.out.print(list[i].getId());
            System.out.print("    Somme niala : "+list[i].getSomme_quantite_sortie());
            System.out.println("  Reste "+list[i].getReste_quantite());
        }
        String idsortie = "SORT001";
        Sortie sortie = new Sortie().getById(idsortie, con);
        sortie.setQuantite(new BigDecimal("51"));
        //sortie.checkSolde(con, timestampdebut);
        try {
            sortie.checkDate(con, timestampdebut);
            sortie.insertValidation(con, timestampdebut);
        } catch (Exception e) {
            e.printStackTrace();
        }
        con.close();
    }
}
