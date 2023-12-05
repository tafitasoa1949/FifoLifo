/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
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
import models.EtatStock;
import models.Magasin;
import models.Sortie;

/**
 *
 * @author Tafitasoa-P15B-140
 */
public class VoirServlet extends HttpServlet {

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
            Connection con = null;
            try {
                con = Connexion.getconnection();
                String idMagasin = request.getParameter("magasin");
                String idArticle = request.getParameter("article");
                String datedebut = request.getParameter("datedebut");
                String datefin = request.getParameter("datefin");
                Timestamp timestampdatedebut = Sortie.casteStringToTimestamp(datedebut);
                Timestamp timestampdatefin = Sortie.casteStringToTimestamp(datefin);
                Article article = new Article().getById(idArticle, con);
                Magasin magasin = new Magasin().getById(idMagasin, con);
                magasin.checkArticleExist(article, con, timestampdatefin);
                EtatStock etatStock = magasin.getEtatStock(article, con, timestampdatedebut, timestampdatefin);
                request.setAttribute("timestampdatedebut", timestampdatedebut);
                request.setAttribute("timestampdatefin", timestampdatefin);
                request.setAttribute("etatStock", etatStock);
                con.close();
                RequestDispatcher dispatcher = request.getRequestDispatcher("filtrer.jsp");
                dispatcher.forward(request, response);
                
            } catch (Exception e) {
                Magasin[] list_magasin = new Magasin().getAll(con);
                Article[] list_article = new Article().getAll(con);
                request.setAttribute("list_magasin", list_magasin);
                request.setAttribute("list_article", list_article);
                String message = e.getMessage();
                request.setAttribute("message", message);
                RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
                dispatcher.forward(request, response);
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
            Logger.getLogger(VoirServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(VoirServlet.class.getName()).log(Level.SEVERE, null, ex);
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
