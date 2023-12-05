<%@page import="models.EtatStock"%>
<%@page import="java.sql.Timestamp"%>
<%
    Timestamp timestampdatedebut = (Timestamp) request.getAttribute("timestampdatedebut");
    Timestamp timestampdatefin = (Timestamp) request.getAttribute("timestampdatefin");
    EtatStock etatStock = (EtatStock) request.getAttribute("etatStock");
%>
<!DOCTYPE html>
<html class="loading" lang="en" data-textdirection="ltr">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimal-ui">
    <meta name="description" content="Chameleon Admin is a modern Bootstrap 4 webapp &amp; admin dashboard html template with a large number of components, elegant design, clean and organized code.">
    <meta name="keywords" content="admin template, Chameleon admin template, dashboard template, gradient admin template, responsive admin template, webapp, eCommerce dashboard, analytic dashboard">
    <meta name="author" content="ThemeSelect">
    <title>Gestion de stock</title>
    <link rel="apple-touch-icon" href="theme-assets/images/ico/apple-icon-120.png">
    <link rel="shortcut icon" type="image/x-icon" href="theme-assets/images/ico/favicon.ico">
    <link href="https://fonts.googleapis.com/css?family=Muli:300,300i,400,400i,600,600i,700,700i%7CComfortaa:300,400,700" rel="stylesheet">
    <link href="https://maxcdn.icons8.com/fonts/line-awesome/1.1/css/line-awesome.min.css" rel="stylesheet">
    <!-- BEGIN VENDOR CSS-->
    <link rel="stylesheet" type="text/css" href="theme-assets/css/vendors.css">
    <!-- END VENDOR CSS-->
    <!-- BEGIN CHAMELEON  CSS-->
    <link rel="stylesheet" type="text/css" href="theme-assets/css/app-lite.css">
    <!-- END CHAMELEON  CSS-->
    <!-- BEGIN Page Level CSS-->
    <link rel="stylesheet" type="text/css" href="theme-assets/css/core/menu/menu-types/vertical-menu.css">
    <link rel="stylesheet" type="text/css" href="theme-assets/css/core/colors/palette-gradient.css">
    <!-- END Page Level CSS-->
    <!-- BEGIN Custom CSS-->
    <!-- END Custom CSS-->
  </head>
  <body class="vertical-layout vertical-menu 2-columns   menu-expanded fixed-navbar" data-open="click" data-menu="vertical-menu" data-color="bg-gradient-x-purple-blue" data-col="2-columns">

    <!-- fixed-top-->
    <jsp:include page="content/navbar.jsp" />
    <!-- ////////////////////////////////////////////////////////////////////////////-->
    <jsp:include page="content/sidebar.jsp" />

    <div class="app-content content">
      <div class="content-wrapper">
        <div class="content-wrapper-before"></div>
        <div class="content-header row">
          <div class="content-header-left col-md-4 col-12 mb-2">
            <h3 class="content-header-title"></h3>
          </div>
          <div class="content-header-right col-md-8 col-12">
            <div class="breadcrumbs-top float-md-right">
              <div class="breadcrumb-wrapper mr-1">
                <ol class="breadcrumb">
                  <li class="breadcrumb-item"><a href="index.html"></a>
                  </li>
                  <li class="breadcrumb-item active">
                  </li>
                </ol>
              </div>
            </div>
          </div>
        </div>
        <div class="content-body">
        <!-- Table head options start -->
            <div class="row">
                <div class="col-12">
                    <div class="card">
			<div class="card-header">
                            <h4 class="card-title">Etat de stock entre <%= timestampdatedebut.toString() %> à <%= timestampdatefin.toString()%></h4>
			</div>
			<div class="card-content collapse show">
                            <div class="table-responsive">
                                <table class="table">
                                    <thead class="thead-dark">
					<tr>
                                            <th scope="col">Magasin</th>
                                            <th scope="col">Article</th>
                                            <th scope="col">Unité</th>
                                            <th scope="col">Quantité initial</th>
                                            <th scope="col">Quantité final</th>
                                            <th scope="col">Pump</th>
                                            <th scope="col">Montant</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td><%= etatStock.getMagasin().getNom() %></td>
                                            <td><%= etatStock.getArticle().getNom() %></td>
                                            <td><%= etatStock.getArticle().getUnite().getNom() %></td>
                                            <td><%= etatStock.getQuantite_initial() %></td>
                                            <td><%= etatStock.getQuanite_final() %></td>
                                            <td><%= etatStock.getPump()%></td>
                                            <td><%= etatStock.getMontant()%></td>
                                        </tr>
                                    </tbody>
				</table>
                            </div>
			</div>
                    </div>
                </div>
            </div>
            <!-- Table head options end -->
        </div>
      </div>
    </div>
    <!-- ////////////////////////////////////////////////////////////////////////////-->


    <jsp:include page="content/footer.jsp" />

    <!-- BEGIN VENDOR JS-->
    <script src="theme-assets/vendors/js/vendors.min.js" type="text/javascript"></script>
    <!-- BEGIN VENDOR JS-->
    <!-- BEGIN PAGE VENDOR JS-->
    <!-- END PAGE VENDOR JS-->
    <!-- BEGIN CHAMELEON  JS-->
    <script src="theme-assets/js/core/app-menu-lite.js" type="text/javascript"></script>
    <script src="theme-assets/js/core/app-lite.js" type="text/javascript"></script>
    <!-- END CHAMELEON  JS-->
    <!-- BEGIN PAGE LEVEL JS-->
    <!-- END PAGE LEVEL JS-->
  </body>
</html>