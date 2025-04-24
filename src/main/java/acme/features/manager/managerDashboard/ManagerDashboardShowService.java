
package acme.features.manager.managerDashboard;

import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airports.Airport;
import acme.forms.ManagerDashboard;
import acme.realms.managers.Manager;

@GuiService
public class ManagerDashboardShowService extends AbstractGuiService<Manager, ManagerDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		int managerId;
		Manager manager;

		ManagerDashboard dashboard;
		int rankingPosition;
		int yearsToRetire;
		Double onTimeDelayedLegsRatio;
		Airport mostPopularAirport;
		Airport lessPopularAirport;
		int onTimeLegs;
		int delayedLegs;
		int cancelledLegs;
		int landedLegs;
		Double averageFlightCost;
		Double minimumFlightCost;
		Double maximumFlightCost;
		Double standardDeviationCost;

		managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		manager = this.repository.getManagerById(managerId);

		rankingPosition = this.repository.moreExperienceYearsThan(manager.getExperienceYears()) + 1; //el método empieza a contar por 0, de ahí que se necesite sumar 1

		yearsToRetire = 65 - (LocalDate.now().getYear() - this.repository.getManagerById(managerId).getBirthdate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear());

		mostPopularAirport = this.repository.airportPopularity(managerId).getFirst();
		lessPopularAirport = this.repository.airportPopularity(managerId).getLast();

		onTimeLegs = this.repository.onTimeLegs(managerId);
		delayedLegs = this.repository.delayedLegs(managerId);
		cancelledLegs = this.repository.cancelledLegs(managerId);
		landedLegs = this.repository.landedLegs(managerId);

		onTimeDelayedLegsRatio = 1.0 * onTimeLegs / delayedLegs;

		averageFlightCost = this.repository.averageCostFlight(managerId);
		minimumFlightCost = this.repository.minimumCostFlight(managerId);
		maximumFlightCost = this.repository.maximumCostFlight(managerId);
		standardDeviationCost = this.repository.standardDeviationCost(managerId);

		dashboard = new ManagerDashboard();
		dashboard.setRankingPosition(rankingPosition);
		dashboard.setYearsToRetire(yearsToRetire);
		dashboard.setOnTimeDelayedLegsRatio(onTimeDelayedLegsRatio);
		dashboard.setMostPopularAirport(mostPopularAirport.getName());
		dashboard.setLessPopularAirport(lessPopularAirport.getName());
		dashboard.setOnTimeLegs(onTimeLegs);
		dashboard.setDelayedLegs(delayedLegs);
		dashboard.setCancelledLegs(cancelledLegs);
		dashboard.setLandedLegs(landedLegs);
		dashboard.setAverageFlightCost(averageFlightCost);
		dashboard.setMinimumFlightCost(minimumFlightCost);
		dashboard.setMaximumFlightCost(maximumFlightCost);
		dashboard.setStandardDeviationCost(standardDeviationCost);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final ManagerDashboard dashboard) {
		Dataset dataset;

		dataset = super.unbindObject(dashboard, //
			"rankingPosition", "yearsToRetire", // 
			"onTimeDelayedLegsRatio", "mostPopularAirport", //
			"lessPopularAirport", "onTimeLegs", "delayedLegs", "cancelledLegs", "landedLegs",//
			"averageFlightCost", "minimumFlightCost", //
			"maximumFlightCost", "standardDeviationCost");

		super.getResponse().addData(dataset);
	}

}
