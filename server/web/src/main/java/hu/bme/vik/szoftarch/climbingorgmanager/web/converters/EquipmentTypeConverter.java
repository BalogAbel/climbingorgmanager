package hu.bme.vik.szoftarch.climbingorgmanager.web.converters;

import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.NoSuchEquipmentTypeException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.EquipmentManager;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.EquipmentType;

import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;

@ManagedBean
public class EquipmentTypeConverter implements Converter {
	@Inject
	private EquipmentManager equipmentManager;


	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
		try {
			return equipmentManager.getEquipmentTypeById(Long.valueOf(s));
		} catch (NoSuchEquipmentTypeException | NumberFormatException e) {
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
		if (!(o instanceof EquipmentType)) return null;
		return ((EquipmentType) o).getId().toString();
	}
}
