
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.assistanceAgents.AssistanceAgent;
import acme.realms.assistanceAgents.AssistanceAgentRepository;

@Validator
public class AssistanceAgentValidator extends AbstractValidator<ValidAssistanceAgent, AssistanceAgent> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidAssistanceAgent annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AssistanceAgent assistanceAgent, final ConstraintValidatorContext context) {
		// HINT: job can be null
		assert context != null;

		boolean result;

		if (assistanceAgent == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			{
				boolean uniqueAssistanceAgent;
				AssistanceAgent existingAssistanceAgent;

				existingAssistanceAgent = this.repository.findAssitanceAgentByEmployeeCode(assistanceAgent.getEmployeeCode());
				uniqueAssistanceAgent = existingAssistanceAgent == null || existingAssistanceAgent.equals(assistanceAgent);

				super.state(context, uniqueAssistanceAgent, "employeeCode", "acme.validation.assistanceAgent.duplicated-employeeCode.message");
			}
			{
				boolean correctIdNumber;

				correctIdNumber = assistanceAgent.getEmployeeCode().charAt(0) == assistanceAgent.getUserAccount().getIdentity().getName().charAt(0)
					&& assistanceAgent.getEmployeeCode().charAt(1) == assistanceAgent.getUserAccount().getIdentity().getSurname().charAt(0);

				super.state(context, correctIdNumber, "*", "acme.validation.assistanceAgent.employeeCode.message");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
