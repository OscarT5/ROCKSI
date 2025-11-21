package ui;

import jakarta.faces.application.NavigationHandler;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.PhaseEvent;
import jakarta.faces.event.PhaseId;
import jakarta.faces.event.PhaseListener;
import java.util.Map;

public class AuthorizationListener implements PhaseListener {

    @Override
    public void afterPhase(PhaseEvent event) {
        FacesContext facesContext = event.getFacesContext();
        String currentPage = facesContext.getViewRoot().getViewId();

        boolean isLoginPage = (currentPage.lastIndexOf("login.xhtml") > -1);

        Map<String, Object> sessionMap = facesContext.getExternalContext().getSessionMap();
        Object usuario = sessionMap.get("USUARIO_ACTUAL");

        if (!isLoginPage && usuario == null) {
            NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
            nh.handleNavigation(facesContext, null, "/login.xhtml?faces-redirect=true");
            facesContext.renderResponse();
        }
    }

    @Override
    public void beforePhase(PhaseEvent event) {
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}