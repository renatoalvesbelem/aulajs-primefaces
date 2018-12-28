package br.com.project.bean.geral;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.web.context.request.FacesRequestAttributes;

public class ViewScope implements Scope, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public Object get(String name, ObjectFactory<?> objectFactory) {
		Object instancia = getViewMap().get(name);
		if (instancia == null) {
			instancia = objectFactory.getObject();
			getViewMap().put(name, instancia);
		}
		return instancia;
	}

	public static final String VIEW_SCOPE_CALLBACK = "viewScope.callBacks";

	@Override
	public String getConversationId() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		FacesRequestAttributes facesRequestAttributes = new FacesRequestAttributes(facesContext);
		return facesRequestAttributes.getSessionId().concat("-").concat(facesContext.getViewRoot().getViewId());
	}

	@Override
	public void registerDestructionCallback(String name, Runnable runnable) {
		@SuppressWarnings("unchecked")
		Map<String, Runnable> callBacks = (Map<String, Runnable>) getViewMap().get(VIEW_SCOPE_CALLBACK);
		if (callBacks != null) {
			callBacks.put(VIEW_SCOPE_CALLBACK, runnable);
		}
	}

	@Override
	public Object remove(String name) {
		Object instancia = getViewMap().remove(name);
		if (instancia != null) {
			@SuppressWarnings("unchecked")
			Map<String, Runnable> callBacks = (Map<String, Runnable>) getViewMap().get(VIEW_SCOPE_CALLBACK);
			if (callBacks != null) {
				callBacks.remove(name);
			}
		}
		return instancia;
	}

	@Override
	public Object resolveContextualObject(String name) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		FacesRequestAttributes facesRequestAttributes = new FacesRequestAttributes(facesContext);
		return facesRequestAttributes.resolveReference(name);
	}

	public Map<String, Object> getViewMap() {
		// retorna o componente raiz que está associado a esta documentação(request)
		return FacesContext.getCurrentInstance() != null ? FacesContext.getCurrentInstance().getViewRoot().getViewMap()
				: new HashMap<>();
	}
}
