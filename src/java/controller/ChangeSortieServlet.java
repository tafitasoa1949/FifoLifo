/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Timestamp;
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

/**
 *
 * @author Tafitasoa-P15B-140
 */
public class ChangeSortieServlet extends HttpServlet {

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
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String idsortie = request.getParameter("idsortie");
            String idMagasin = request.getParameter("magasin");
            String idArticle = request.getParameter("article");
            String quantite = request.getParameter("quantite");
            String date = request.getParameter("date");
            Connection con = null;
            try {
                con = Connexion.getconnection();
                Magasin magasin = new Magasin().getById(idMagasin, con);
                Article article = new Article().getById(idArticle, con);
                BigDecimal quantite_vrai = new BigDecimal(quantite);
                Timestamp date_timestamp = Sortie.casteStringToTimestamp(date);
                Sortie sortie = new Sortie();
                sortie.setId(idsortie);
                sortie.setMagasin(magasin);
                sortie.setArticle(article);
                sortie.setQuantite(quantite_vrai);
                sortie.setDate(date_timestamp);
                sortie.change(con);
                con.close();
                String contextPath = request.getContextPath();
                response.sendRedirect(contextPath + "/SortieNonVServlet");
            } catch (Exception e) {
                //e.printStackTrace(out);
                request.setAttribute("message", e.getMessage());
                Sortie sortie = new Sortie().getById(idsortie, con);
                request.setAttribute("sortie", sortie);
                Magasin[] list_magasin = new Magasin().getAll(con);
                Article[] list_article = new Article().getAll(con);
                request.setAttribute("list_magasin", list_magasin);
                request.setAttribute("list_article", list_article);
                RequestDispatcher dispatcher = request.getRequestDispatcher("modifierSortie.jsp");
                dispatcher.forward(request, response);
            }finally{
                if (con != null) con.close();
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
        } catch (Exception ex) {
            Logger.getLogger(ChangeSortieServlet.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (Exception ex) {
            Logger.getLogger(ChangeSortieServlet.class.getName()).log(Level.SEVERE, null, ex);
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
