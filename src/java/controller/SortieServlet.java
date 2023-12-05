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
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Article;
import models.Connexion;
import models.Magasin;
import models.Sortie;
import models.Unite;
import models.Valeur;

/**
 *
 * @author Tafitasoa-P15B-140
 */
public class SortieServlet extends HttpServlet {

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
            Integer idUnite = Integer.parseInt(request.getParameter("unite"));
            String date = request.getParameter("date");
            Connection con = null;
            try {
                con = Connexion.getconnection();
                //
                con.setAutoCommit(false);
                Unite unite = new Unite().getById(idUnite, con);
                //
                BigDecimal quantite_true = new BigDecimal(quantite);
                Sortie sortie = new Sortie();
                Magasin magasin = new Magasin().getById(idMagasin, con);
                sortie.setMagasin(magasin);
                Article article = new Article().getById(idArticle, con);
                article.setUnite(unite);
                sortie.setArticle(article);
                sortie.setQuantite(quantite_true);
                sortie.setDate(date);
                sortie.inserer(con);
                //
                con.commit();
                con.close();
                String contextPath = request.getContextPath();
                response.sendRedirect(contextPath + "/TestServlet");
            } catch (Exception e) {
                con.rollback();
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
            Logger.getLogger(SortieServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(SortieServlet.class.getName()).log(Level.SEVERE, null, ex);
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
