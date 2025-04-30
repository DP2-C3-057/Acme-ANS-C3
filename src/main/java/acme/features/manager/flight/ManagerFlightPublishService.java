
package acme.features.manager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.realms.managers.Manager;

@GuiService
public class ManagerFlightPublishService extends AbstractGuiService<Manager, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerFlightRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int flightId;
		Flight flight;
		Manager manager;

		flightId = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(flightId);
		manager = flight == null ? null : flight.getManager();
		status = flight != null && flight.isDraftMode() && super.getRequest().getPrincipal().hasRealm(manager);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Flight flight;
		int id;

		id = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(id);

		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {
		;
	}

	@Override
	public void validate(final Flight flight) {
		{
			boolean status = true;
			int id;
			id = super.getRequest().getData("id", int.class);
			Collection<Leg> legs = this.repository.findLegsByFlightId(id);
			if (legs.isEmpty())
				status = false;
			else
				for (Leg leg : legs)
					if (leg.isDraftMode()) {
						status = false;
						break;
					}
			super.state(status, "*", "manager.flight.publish.non-published-legs");
		}
		{
			boolean correctSelfTransfer;
			int id;
			id = super.getRequest().getData("id", int.class);
			Collection<Leg> legs = this.repository.findLegsByFlightId(id);
			if (flight.isSelfTransfer())
				correctSelfTransfer = legs.size() >= 2;
			else
				correctSelfTransfer = legs.size() == 1;
			super.state(correctSelfTransfer, "selfTransfer", "manager.flight.publish.correctSelfTransfer");
		}
	}

	@Override
	public void perform(final Flight flight) {
		flight.setDraftMode(false);
		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		dataset = super.unbindObject(flight, "tag", "selfTransfer", "cost", "description", "draftMode");
		dataset.put("scheduledDeparture", flight.getScheduledDeparture());
		dataset.put("scheduledArrival", flight.getScheduledArrival());
		dataset.put("originCity", flight.getOriginCity());
		dataset.put("destinationCity", flight.getDestinationCity());
		dataset.put("numberOfLayovers", flight.getNumberOfLayovers());
		super.getResponse().addData(dataset);
	}
}
