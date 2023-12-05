/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Article;
import models.Connexion;
import models.Entre;
import models.Magasin;
import models.Unite;
import models.Valeur;

/**
 *
 * @author Tafitasoa-P15B-140
 */
public class EntreServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String idMagasin = request.getParameter("magasin");
            String idArticle = request.getParameter("article");
            String quantite = request.getParameter("quantite");
            String prix_unitaire = request.getParameter("prix_unitaire");
            String date = request.getParameter("date");
            Integer idUnite = Integer.parseInt((String)request.getParameter("unite"));
            
            try {
                Connection con = Connexion.getconnection();
                //
                Unite unite = new Unite().getById(idUnite, con);
                //
                BigDecimal quantite_true = new BigDecimal(quantite);
                BigDecimal prix_unitaire_true = new BigDecimal(prix_unitaire);
                Entre entre = new Entre();
                Magasin magasin = new Magasin().getById(idMagasin, con);
                entre.setMagasin(magasin);
                Article article = new Article().getById(idArticle, con);
                article.setUnite(unite);
                entre.setArticle(article);
                entre.setQuantite(quantite_true);
                entre.setPrixUnitaire(prix_unitaire_true);
                entre.setDate(date);
                entre.inserer(con);
                //
                Valeur valeur = new Valeur();
                valeur.setArticle(article);
                valeur.setUnite(unite);
                valeur.setValeur(prix_unitaire_true);
                valeur.insererEntre(con);
                con.close();
                String contextPath = request.getContextPath();
                response.sendRedirect(contextPath + "/ListEntreServlet");
            } catch (Exception e) {
                /*request.setAttribute("errorMessage", e.getMessage());
                RequestDispatcher dispatcher = request.getRequestDispatcher("ajouterEntre.jsp");
                dispatcher.forward(request, response);*/
                e.printStackTrace(out);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(EntreServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(EntreServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
