<div class="main-menu menu-fixed menu-light menu-accordion    menu-shadow " data-scroll-to-active="true" data-img="theme-assets/images/backgrounds/02.jpg">
      <div class="navbar-header">
        <ul class="nav navbar-nav flex-row">       
          <li class="nav-item mr-auto"><a class="navbar-brand" href="index.jsp"><img class="brand-logo" alt="Chameleon admin logo" src="theme-assets/images/logo/logo.png"/>
              <h3 class="brand-text">FifoLifo</h3></a></li>
          <li class="nav-item d-md-none"><a class="nav-link close-navbar"><i class="ft-x"></i></a></li>
        </ul>
      </div>
      <div class="main-menu-content">
        <ul class="navigation navigation-main" id="main-menu-navigation" data-menu="menu-navigation">
          <li class=" nav-item"><a href="<%= request.getContextPath() %>/ListEntreServlet"><i class="ft-home"></i><span class="menu-title" data-i18n="">Entre</span></a>
          </li>
          <li class=" nav-item"><a href="<%= request.getContextPath() %>/AjouterSortieServlet"><i class="ft-pie-chart"></i><span class="menu-title" data-i18n="">Sortie</span></a>
          </li>
          <li class=" nav-item"><a href="<%= request.getContextPath() %>/SortieNonVServlet"><i class="ft-droplet"></i><span class="menu-title" data-i18n="">Validation</span></a>
          </li>
          <li class=" nav-item"><a href="<%= request.getContextPath() %>/FiltrerServlet"><i class="ft-layers"></i><span class="menu-title" data-i18n="">Etat de stock</span></a>
          </li>
        </ul>
      </div>
    </div>