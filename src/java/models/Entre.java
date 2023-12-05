/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Tafitasoa-P15B-140
 */
public class Entre {
    private String id;
    private Magasin magasin;
    private Article article;
    private BigDecimal quantite;
    private BigDecimal prixUnitaire;
    private Timestamp date;
    private BigDecimal somme_quantite_sortie;
    private BigDecimal reste_quantite;

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

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) throws Exception{
        if (prixUnitaire.compareTo(BigDecimal.ZERO) > 0) {
            this.prixUnitaire = prixUnitaire;
        }else{
            throw new Exception("Prix negatif");
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

    public BigDecimal getSomme_quantite_sortie() {
        return somme_quantite_sortie;
    }

    public void setSomme_quantite_sortie(BigDecimal somme_quantite_sortie) {
        this.somme_quantite_sortie = somme_quantite_sortie;
    }

    public BigDecimal getReste_quantite() {
        return reste_quantite;
    }

    public void setReste_quantite(BigDecimal reste_quantite) {
        this.reste_quantite = reste_quantite;
    }
    
    public Entre() {
    }
    public Entre getById(String id,Connection con)throws Exception{
        String sql = "select * from mouvement where id=?";
        Entre entre = null;
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                entre = new Entre();
                entre.setId(id);
                String idMagasin = rs.getString("idmagasin");
                Magasin magasin = new Magasin().getById(idMagasin, con);
                entre.setMagasin(magasin);
                String idArticle = rs.getString("idarticle");
                Article article = new Article().getById(idArticle, con);
                entre.setArticle(article);
                entre.setQuantite(rs.getBigDecimal("quantite"));
                entre.setPrixUnitaire(rs.getBigDecimal("prix_unitaire"));
                entre.setDate(rs.getTimestamp("date"));
            }
        }catch (Exception e) {
            throw new Exception("Erreur lors de la récupération du sortie : " + e.getMessage());
        }
        return entre;
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
                    numericPart++;
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
    public String getIdentifiant(Connection con)throws Exception{
        String sql = "SELECT MAX(id) AS max_id FROM mouvement";
        String nextId = null;
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String maxId =  rs.getString("max_id");
                if(maxId != null){
                    String prefix = maxId.substring(0, 4);
                    int numericPart = Integer.parseInt(maxId.substring(4));
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
        String sql = "INSERT INTO mouvement (id, idMagasin, idArticle, quantite, prix_unitaire, date) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, this.getNextId(con));
            stmt.setString(2, this.getMagasin().getId());
            stmt.setString(3, this.getArticle().getId());
            BigDecimal quantite_equivalence = this.getQuantite().multiply(this.getArticle().getUnite().getEquivalence());
            stmt.setBigDecimal(4, quantite_equivalence);
            BigDecimal prix_equivalence = this.getPrixUnitaire().divide(this.getArticle().getUnite().getEquivalence());
            stmt.setBigDecimal(5, prix_equivalence);
            stmt.setTimestamp(6, this.getDate());
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'insertion d'un produit : " + e.getMessage());
        }
    }
    public Entre[] getAll(Connection con)throws Exception{
        String sql = "select * from v_entre";
        Entre[] list_entre = null;
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }
            list_entre = new Entre[rowCount];
            rs = stmt.executeQuery();
            int index = 0;
            while(rs.next()){
                Entre entre = new Entre();
                entre.setId(rs.getString("id"));
                String idMagasin = rs.getString("idmagasin");
                Magasin magasin = new Magasin().getById(idMagasin, con);
                entre.setMagasin(magasin);
                String idArticle = rs.getString("idarticle");
                Article article = new Article().getById(idArticle, con);
                entre.setArticle(article);
                entre.setQuantite(rs.getBigDecimal("quantite"));
                entre.setPrixUnitaire(rs.getBigDecimal("prix_unitaire"));
                entre.setDate(rs.getTimestamp("date"));
                entre.setSomme_quantite_sortie(rs.getBigDecimal("somme_quantite_sortie"));
                entre.setReste_quantite(rs.getBigDecimal("reste_quantite"));
                list_entre[index] = entre;
                index++;
            }
        } catch (Exception e) {
            throw e;
        }
        return list_entre;
    }
    public Entre[] getAll(Connection con,Magasin magasin_donne,Article article_donne,Timestamp date)throws Exception{
        String sql = null;
        if(article_donne.getCategorie().getId() == 1){
            sql = "select * from v_entre where idmagasin=? and idarticle=? and date <= ? order by date asc";
        }else if(article_donne.getCategorie().getId() == 2){
            sql = "select * from v_entre where idmagasin=? and idarticle=? and date <= ? order by date desc";
        }
        
        Entre[] list_entre = null;
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, magasin_donne.getId());
            stmt.setString(2, article_donne.getId());
            stmt.setTimestamp(3, date);
            ResultSet rs = stmt.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }
            list_entre = new Entre[rowCount];
            rs = stmt.executeQuery();
            int index = 0;
            while(rs.next()){
                Entre entre = new Entre();
                entre.setId(rs.getString("id"));
                String idMagasin = rs.getString("idmagasin");
                Magasin magasin = new Magasin().getById(idMagasin, con);
                entre.setMagasin(magasin);
                String idArticle = rs.getString("idarticle");
                Article article = new Article().getById(idArticle, con);
                entre.setArticle(article);
                entre.setQuantite(rs.getBigDecimal("quantite"));
                entre.setPrixUnitaire(rs.getBigDecimal("prix_unitaire"));
                entre.setDate(rs.getTimestamp("date"));
                entre.setSomme_quantite_sortie(rs.getBigDecimal("somme_quantite_sortie"));
                entre.setReste_quantite(rs.getBigDecimal("reste_quantite"));
                list_entre[index] = entre;
                index++;
            }
        } catch (Exception e) {
            throw e;
        }
        return list_entre;
    }
    public Mouvement[] getSortieEntre(Connection con,Magasin magasin_donne,Article article_donne,Timestamp date_donne)throws Exception{
        String sql = "select * from mouvement where idmagasin=? and idarticle=? and identre=? and date <= ?";
        Mouvement[] list_sortie = null;
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, magasin_donne.getId());
            stmt.setString(2, article_donne.getId());
            stmt.setString(3, this.getId());
            stmt.setTimestamp(4, date_donne);
            ResultSet rs = stmt.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }
            //System.out.println("con"+rowCount);
            list_sortie = new Mouvement[rowCount];
            rs = stmt.executeQuery();
            int index = 0;
            while(rs.next()){
                Mouvement mouvement = new Mouvement();
                mouvement.setId(rs.getString("id"));
                mouvement.setMagasin(magasin_donne);
                mouvement.setArticle(article_donne);
                mouvement.setQuantite(rs.getBigDecimal("quantite"));
                mouvement.setPrixUnitaire(rs.getBigDecimal("prix_unitaire"));
                mouvement.setDate(rs.getTimestamp("date"));
                String idEntre = rs.getString("identre");
                Entre entre = new Entre().getById(idEntre, con);
                mouvement.setEntre(entre);
                String idSortie = rs.getString("idsortie");
                Sortie sortie = new Sortie().getById(idSortie, con);
                mouvement.setSortie(sortie);
                list_sortie[index] = mouvement;
                index++;
            }
        } catch (Exception e) {
            throw e;
        }
        return list_sortie;
    }
    public BigDecimal getSoldeRestant(Connection con,Magasin magasin,Article article,Timestamp date)throws Exception{
        BigDecimal solde_restant = BigDecimal.ZERO;
        BigDecimal somme_entre = BigDecimal.ZERO;
        BigDecimal somme_sortie = BigDecimal.ZERO;
        Entre[] list_entre = this.getAll(con, magasin, article, date);
        
        for(int i=0 ; i < list_entre.length ; i++){
            somme_entre = somme_entre.add(list_entre[i].getQuantite());
            Mouvement[] list_sortie = list_entre[i].getSortieEntre(con, magasin, article, date);
            for(int m=0 ; m < list_sortie.length ; m++){
                somme_sortie = somme_sortie.add(list_sortie[m].getQuantite());
            }
        }
        
        //System.out.println("num"+somme_entre);
        //System.out.println("denom"+somme_sortie);
        solde_restant = somme_entre.subtract(somme_sortie);
        return solde_restant;
    }
    public BigDecimal getMontant(Connection con,Magasin magasin,Article article,Timestamp date)throws Exception{
        BigDecimal montant_entre = BigDecimal.ZERO;
        BigDecimal montant_sortie = BigDecimal.ZERO;
        BigDecimal montant = BigDecimal.ZERO;
        Entre[] list_entre = this.getAll(con, magasin, article, date);
        for(int i=0 ; i < list_entre.length ; i++){
            //leh entre
            BigDecimal montant_entre_one = list_entre[i].getQuantite().multiply(list_entre[i].getPrixUnitaire());
            montant_entre = montant_entre.add(montant_entre_one);
            Mouvement[] list_sortie = list_entre[i].getSortieEntre(con, magasin, article, date);
            for(int m=0 ; m < list_sortie.length ; m++){
                BigDecimal montant_sortie_one = list_sortie[m].getQuantite().multiply(list_entre[i].getPrixUnitaire());
                montant_sortie = montant_sortie.add(montant_sortie_one);
            }
        }
        //System.out.println("Mon Intital : "+montant_entre);
        //System.out.println("Mon Sortie : "+montant_sortie);
        montant = montant_entre.subtract(montant_sortie);
        return montant;
    }
    public BigDecimal getPUMP(Connection con,Magasin magasin,Article article,Timestamp date)throws Exception{
        BigDecimal pump = BigDecimal.ZERO;
        BigDecimal solde_restant = this.getSoldeRestant(con, magasin, article, date);
        BigDecimal montant = this.getMontant(con, magasin, article, date);
        if (solde_restant.compareTo(BigDecimal.ZERO) != 0) {
            // Perform division with specified scale and rounding mode
            pump = montant.divide(solde_restant, 10, RoundingMode.HALF_UP); // Adjust scale as needed
        } else {
            // Handle division by zero scenario, maybe throw an exception or set a default value
            // For example:
            throw new ArithmeticException("Division by zero not allowed");
            // Or set a default value:
            // pump = BigDecimal.ZERO; // Or any other default value as needed
        }
        return pump;
    }
    public static void main(String[] args) throws Exception {
        Entre entre = new Entre();
        Connection con = Connexion.getconnection();
        //System.out.println("NextId : "+entre.getNextId(con));
        String datedebut = "2023-12-28 00:00:00'";
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        java.util.Date parsedDateDebut = dateFormat.parse(datedebut);
        Timestamp timestampdebut = new Timestamp(parsedDateDebut.getTime());
        String datefin= "2023-12-31 00:00:00'";
        String pattern1 = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat1 = new SimpleDateFormat(pattern1);
        java.util.Date parsedDatefin = dateFormat1.parse(datefin);
        Timestamp timestampfin = new Timestamp(parsedDatefin.getTime());
        Magasin mm = new Magasin();
        mm.setId("M001");
        Categorie categorie = new Categorie().getById(1,con);
        Article article = new Article();
        article.setId("V001");
        article.setCategorie(categorie);
        Entre[] list = new Entre().getAll(con,mm,article,timestampdebut);
        for(int i=0 ; i< list.length ; i++){
            System.out.print(list[i].getId());
            System.out.print(" Prix U: "+list[i].getPrixUnitaire());
            System.out.print("    Somme niala : "+list[i].getSomme_quantite_sortie());
            System.out.println("  Reste "+list[i].getReste_quantite());
        }
        System.out.println("Quantite initial : "+new Entre().getSoldeRestant(con, mm, article, timestampdebut));
        System.out.println("Quantite final : "+new Entre().getSoldeRestant(con, mm, article, timestampfin));
        System.out.println("Montant : "+new Entre().getMontant(con, mm, article, timestampfin));
        System.out.println("PUMP : "+new Entre().getPUMP(con, mm, article, timestampfin));
        /*String idEntre = "MOUV001";
        Entre entrek = new Entre();
        entrek.setId(idEntre);
        Entre[] lisEnte = new Entre().getAll(con, mm, article, timestampdebut);
        for(int m=0 ; m < lisEnte.length ; m++){
            System.out.println("Entre : "+lisEnte[m].getId());
            Mouvement[] listsortie = lisEnte[m].getSortieEntre(con, mm, article, timestampdebut);
            for(int i=0 ; i < listsortie.length ; i++){
                System.out.print(listsortie[i].getId());
                System.out.println("  "+listsortie[i].getQuantite());
            }
        }*/
        
        con.close();
    }
}
