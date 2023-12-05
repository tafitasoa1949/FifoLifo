/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.math.BigDecimal;

/**
 *
 * @author Tafitasoa-P15B-140
 */
public class EtatStock {
    private Magasin magasin;
    private Article article;
    private BigDecimal quantite_initial;
    private BigDecimal quanite_final;
    private BigDecimal pump;
    private BigDecimal montant;

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

    public BigDecimal getQuantite_initial() {
        return quantite_initial;
    }

    public void setQuantite_initial(BigDecimal quantite_initial) {
        this.quantite_initial = quantite_initial;
    }

    public BigDecimal getQuanite_final() {
        return quanite_final;
    }

    public void setQuanite_final(BigDecimal quanite_final) {
        this.quanite_final = quanite_final;
    }

    public BigDecimal getPump() {
        return pump;
    }

    public void setPump(BigDecimal pump) {
        this.pump = pump;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public EtatStock() {
    }
    
}
