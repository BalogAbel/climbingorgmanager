package hu.bme.vik.szoftarch.climbingorgmanager.web.utils;


import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;

public class HttpParamProducer {

	@Produces
	@HttpParam
	String getHttpParameter(InjectionPoint ip) {
		String name = ip.getAnnotated().getAnnotation(HttpParam.class).value();
		if ("".equals(name)) name = ip.getMember().getName();
		return FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap()
				.get(name);
	}
}
