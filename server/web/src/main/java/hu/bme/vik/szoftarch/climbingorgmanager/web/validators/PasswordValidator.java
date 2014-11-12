package hu.bme.vik.szoftarch.climbingorgmanager.web.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("passwordValidator")
public class PasswordValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {

		String password = value.toString();

		UIInput uiInputPassword2 = (UIInput) component.getAttributes()
				.get("password2");
		String password2 = uiInputPassword2.getSubmittedValue()
				.toString();

		if (!password.equals(password2)) {
			uiInputPassword2.setValid(false);
			throw new ValidatorException(new FacesMessage(
					"password and its confirmation does not match"));
		}

	}
}
