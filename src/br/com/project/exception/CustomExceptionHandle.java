package br.com.project.exception;

import java.util.Iterator;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.hibernate.SessionFactory;
import org.primefaces.context.RequestContext;

import br.com.framework.hibernate.session.HibernateUtil;

public class CustomExceptionHandle extends ExceptionHandlerWrapper {

	private ExceptionHandler wrapper;
	final FacesContext facesContext = FacesContext.getCurrentInstance();
	final Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
	final NavigationHandler navigationHandler = facesContext.getApplication().getNavigationHandler();

	public CustomExceptionHandle(ExceptionHandler exceptionHandler) {
		this.wrapper = exceptionHandler;
	}

	// sobrescreve o método exceptionhandler que retorna a pilha de exceções
	@Override
	public ExceptionHandler getWrapped() {
		return wrapper;
	}

	// sobrescreve o método handle que é responsável por manipular as excessões do
	// JSF
	@Override
	public void handle() throws FacesException {
		final Iterator<ExceptionQueuedEvent> iterator = getUnhandledExceptionQueuedEvents().iterator();
		while (iterator.hasNext()) {
			ExceptionQueuedEvent event = iterator.next();
			ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

			// Recuperar a excessão do contexto
			Throwable exception = context.getException();

			// Aqui trabalhamos a exceção
			try {
				requestMap.put("exceptionMessage", exception.getMessage());

				if (exception != null && exception.getMessage() != null
						&& exception.getMessage().indexOf("ConstraintViolationException") != -1) {
					FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Registro não pode ser removido por estar associado.", ""));
				} else if (exception != null && exception.getMessage() != null
						&& exception.getMessage().indexOf("org.hibernate.StaleObjectStateException") != -1) {
					FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Registro foi atualizado ou removido por outro usuário. Consulte novamente.", ""));
				} else {
					// Avisar o usuário sobre o erro
					FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"O sistema se recuperou de um erro inesperado.", ""));
					FacesContext.getCurrentInstance().addMessage("msg",
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Você pode continuar a usar o sistema.", ""));
					FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Erro causado por:\n" + exception.getMessage(), ""));
					// Primefaces
					// Esse alert é exibido somente se a página não for redirecionada.
					RequestContext.getCurrentInstance()
							.execute("alert('O sistema se recuperou de um erro inesperado')");
					RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Erro", "O sistema se recuperou de um erro inesperado."));

					navigationHandler.handleNavigation(facesContext, null,
							"error/error.jsf?faces-redirect=true&expired=true");
				}
				// renderiza a página de erro e exibe as mensagens
				facesContext.renderResponse();
			} finally {
				SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
				if (sessionFactory.getCurrentSession().getTransaction().isActive()) {
					sessionFactory.getCurrentSession().getTransaction().rollback();
				}
				// imprime erro no log
				exception.printStackTrace();
				iterator.remove();
			}
		}
		getWrapped().handle();
	}

}
