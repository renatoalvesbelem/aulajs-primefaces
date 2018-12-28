package br.com.project.bean.geral;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIViewRoot;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostConstructViewMapEvent;
import javax.faces.event.PreDestroyViewMapEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.ViewMapListener;

public class ViewScopeCallbackRegister implements Serializable, ViewMapListener {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean isListenerForSource(Object source) {

		return source instanceof UIViewRoot;
	}

	@Override
	public void processEvent(SystemEvent event) throws AbortProcessingException {
		if (event instanceof PostConstructViewMapEvent) {
			PostConstructViewMapEvent viewMapEvent = (PostConstructViewMapEvent) event;
			UIViewRoot uiViewRoot = (UIViewRoot) viewMapEvent.getComponent();
			uiViewRoot.getViewMap().put(ViewScope.VIEW_SCOPE_CALLBACK, new HashMap<String, Runnable>());
		} else if (event instanceof PreDestroyViewMapEvent) {
			PreDestroyViewMapEvent viewMapEvent = (PreDestroyViewMapEvent) event;
			UIViewRoot viewRoot = (UIViewRoot) viewMapEvent.getComponent();
			Map<String, Runnable> callback = (Map<String, Runnable>) viewRoot.getViewMap()
					.get(ViewScope.VIEW_SCOPE_CALLBACK);
			if (callback != null) {
				for (Runnable c : callback.values()) {
					c.run();
				}
				callback.clear();
			}
		}
	}

}
