package br.com.project.listener;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@ApplicationScoped
public class ContextLoaderListenerCaixaKiUtils extends ContextLoaderListener implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * @return Retorna todo o contexto da aplicação
	 */
	private static WebApplicationContext getWebAC() {
		return WebApplicationContextUtils
				.getWebApplicationContext(getCurrentWebApplicationContext().getServletContext());
	}

	public static Object getBean(String idNomeBean) {
		return getWebAC().getBean(idNomeBean);
	}

	public static Object getBean(Class<?> classe) {
		return getWebAC().getBean(classe);
	}
}
