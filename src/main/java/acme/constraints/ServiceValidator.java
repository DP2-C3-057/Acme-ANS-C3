
package acme.constraints;

import java.time.LocalDate;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.service.Service;
import acme.entities.service.ServiceRepository;

@Validator
public class ServiceValidator extends AbstractValidator<ValidService, Service> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ServiceRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidService annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Service service, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (service == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			{
				boolean uniquePromoCode;
				Service existingService;

				existingService = this.repository.findServiceByPromoCode(service.getPromoCode());
				uniquePromoCode = existingService == null || existingService.equals(service);

				super.state(context, uniquePromoCode, "promoCode", "acme.validation.service.duplicated-promoCode.message");
			}
			{
				boolean last2digits;
				Service existingService;
				String promoCode;
				String year;

				existingService = this.repository.findServiceByPromoCode(service.getPromoCode());
				promoCode = existingService.getPromoCode();
				year = String.valueOf(LocalDate.now().getYear());
				last2digits = existingService == null || promoCode.endsWith(year.substring(year.length() - 2));

				super.state(context, last2digits, "promoCode", "acme.validation.service.lastDigits-promoCode.message");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
