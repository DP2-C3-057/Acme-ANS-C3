
package acme.features.administrator.aircraft;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.aircraft.AircraftStatus;
import acme.entities.airlines.Airline;
import acme.entities.legs.Leg;

@GuiService
public class AdministratorAircraftDeleteService extends AbstractGuiService<Administrator, Aircraft> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAircraftRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Aircraft aircraft;

		masterId = super.getRequest().getData("id", int.class);
		aircraft = this.repository.findAircraftById(masterId);
		status = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class) && aircraft != null;
		super.getResponse().setAuthorised(status);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Aircraft aircraft;
		int id;

		id = super.getRequest().getData("id", int.class);
		aircraft = this.repository.findAircraftById(id);

		super.getBuffer().addData(aircraft);
	}

	@Override
	public void bind(final Aircraft aircraft) {
		super.bindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "details", "airline");
	}

	@Override
	public void validate(final Aircraft aircraft) {
		boolean status = true;
		int id;
		id = super.getRequest().getData("id", int.class);
		Collection<Leg> legs = this.repository.findLegsByAircraftId(id);
		if (!legs.isEmpty())
			for (Leg leg : legs)
				if (!leg.isDraftMode()) {
					status = false;
					break;
				}
		super.state(status, "*", "administrator.aircraft.delete.published-legs");
	}

	@Override
	public void perform(final Aircraft aircraft) {
		Collection<Leg> legs;

		legs = this.repository.findLegsByAircraftId(aircraft.getId());
		this.repository.deleteAll(legs);
		this.repository.delete(aircraft);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		Dataset dataset;
		Collection<Airline> airlines;
		SelectChoices airlineChoices;
		SelectChoices statusChoices;

		airlines = this.repository.findAllAirline();
		airlineChoices = SelectChoices.from(airlines, "name", aircraft.getAirline());

		statusChoices = SelectChoices.from(AircraftStatus.class, aircraft.getStatus());

		dataset = super.unbindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "details");
		dataset.put("airline", airlineChoices.getSelected().getKey());
		dataset.put("airlines", airlineChoices);
		dataset.put("status", statusChoices.getSelected().getKey());
		dataset.put("statuses", statusChoices);

		super.getResponse().addData(dataset);
	}
}
