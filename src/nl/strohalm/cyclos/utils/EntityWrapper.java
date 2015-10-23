package nl.strohalm.cyclos.utils;

import nl.strohalm.cyclos.entities.Entity;

/** holds a reference to an Entity */
public interface EntityWrapper {

	Entity create();
		
	Entity getEntity();
	
}
