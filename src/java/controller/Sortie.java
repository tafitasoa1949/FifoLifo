/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Article;
import models.Connexion;
import models.Magasin;
import models.Unite;

/**
 *
 * @author Tafitasoa-P15B-140
 */
public class Sortie extends HttpServlet {

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
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String idMagasin = request.getParameter("magasin");
            String idArticle = request.getParameter("article");
            String quantite = request.getParameter("quantite");
            String date = request.getParameter("date");
            try {
                Connection con = Connexion.getconnection();
                Magasin magasin = new Magasin().getById(idMagasin, con);
                Article article = new Article().getById(idArticle, con);
                Unite[] listunite = article.getByArticle(con);
                Magasin[] list_magasin = new Magasin().getAll(con);
                Article[] list_article = new Article().getAll(con);
                
                request.setAttribute("magasin", magasin);
                request.setAttribute("article", article);
                request.setAttribute("quantite", quantite);
                request.setAttribute("date", date);
                request.setAttribute("list_unite", listunite);
                request.setAttribute("list_magasin", list_magasin);
                request.setAttribute("list_article", list_article);
                con.close();
                RequestDispatcher dispatcher = request.getRequestDispatcher("ajouterSortie2.jsp");
                dispatcher.forward(request, response);
            } catch (Exception e) {
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
        processRequest(request, response);
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
        processRequest(request, response);
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
