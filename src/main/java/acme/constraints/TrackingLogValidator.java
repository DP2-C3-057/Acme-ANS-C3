
package acme.constraints;

import java.util.Comparator;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.entities.claims.Claim;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.trackingLogs.TrackingLogRepository;
import acme.entities.trackingLogs.TrackingLogStatus;

public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TrackingLogRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidTrackingLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (trackingLog == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			{
				boolean correctPercentaje = true;
				if (trackingLog.getIndicator() == TrackingLogStatus.ACCEPTED || trackingLog.getIndicator() == TrackingLogStatus.REJECTED)
					if (trackingLog.getResolutionPercentage() != 100)
						correctPercentaje = false;

				super.state(context, correctPercentaje, "*", "acme.validation.trackingLog.correctPercentaje.message");
			}
			{
				boolean correctResolution = true;
				if (trackingLog.getIndicator() == TrackingLogStatus.ACCEPTED || trackingLog.getIndicator() == TrackingLogStatus.REJECTED)
					if (trackingLog.getResolution() == null || trackingLog.getResolution().isBlank())
						correctResolution = false;

				super.state(context, correctResolution, "*", "acme.validation.trackingLog.correctResolution.message");
			}
			{
				boolean correctPercentajeClaim = true;
				List<Claim> claims = this.repository.findAllClaimsWithTrackingLogs();
				for (Claim claim : claims) {
					List<TrackingLog> trackingLogsClaim = this.repository.findTrackingLogByClaimId(claim);
					trackingLogsClaim.sort(Comparator.comparing(TrackingLog::getLastUpdateMoment));
					for (int i = 0; i < trackingLogsClaim.size() - 1; i++) {
						TrackingLog actual = trackingLogsClaim.get(i);
						TrackingLog siguiente = trackingLogsClaim.get(i + 1);
						if (siguiente.getLastUpdateMoment().after(actual.getLastUpdateMoment()) && siguiente.getResolutionPercentage() <= actual.getResolutionPercentage()) {
							correctPercentajeClaim = false;
							super.state(context, correctPercentajeClaim, "*", "acme.validation.trackingLog.correctPercentajeClaim.message");
							break;
						}
					}
				}
			}
			{
				boolean correctAgent = true;
				if (!trackingLog.getAssistanceAgent().equals(this.repository.findAgentByClaim(trackingLog.getClaim())))
					correctAgent = false;

				super.state(context, correctAgent, "*", "acme.validation.trackingLog.correctAgent.message");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}
}
