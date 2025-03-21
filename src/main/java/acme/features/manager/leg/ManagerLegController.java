
package acme.features.manager.leg;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.legs.Leg;
import acme.realms.managers.Manager;

@GuiController
public class ManagerLegController extends AbstractGuiController<Manager, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerLegListService listService;
	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
	}

}
