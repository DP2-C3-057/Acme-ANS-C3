
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.managers.Manager;
import acme.realms.managers.ManagerRepository;

@Validator
public class ManagerValidator extends AbstractValidator<ValidManager, Manager> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidManager annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Manager manager, final ConstraintValidatorContext context) {
		// HINT: job can be null
		assert context != null;

		boolean result;

		if (manager == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			{
				boolean uniqueManager;
				Manager existingManager;

				existingManager = this.repository.findManagerByIdNumber(manager.getIdNumber());
				uniqueManager = existingManager == null || existingManager.equals(manager);

				super.state(context, uniqueManager, "idNumber", "acme.validation.manager.duplicated-idNumber.message");
			}
			{
				boolean correctIdNumber;

				correctIdNumber = manager.getIdNumber().charAt(0) == manager.getUserAccount().getIdentity().getName().charAt(0) && manager.getIdNumber().charAt(1) == manager.getUserAccount().getIdentity().getSurname().charAt(0);

				super.state(context, correctIdNumber, "*", "acme.validation.manager.idNumber.message");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
