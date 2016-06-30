/* *********************************************************************** *
 * project: org.matsim.*												   *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2008 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */
package org.matsim.core.population;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Population;
import org.matsim.api.core.v01.population.PopulationFactory;
import org.matsim.core.scenario.Lockable;
import org.matsim.utils.objectattributes.ObjectAttributes;

/**
 * @author nagel
 *
 */
class PopulationImpl implements Population, Lockable {
	private static final Logger log = Logger.getLogger(PopulationImpl.class);

	private String name;
	private Map<Id<Person>, Person> persons = new LinkedHashMap<Id<Person>, Person>();
	private final PopulationFactory populationFactory;
	private final ObjectAttributes personAttributes = new ObjectAttributes();
	protected long counter = 0;
	protected long nextMsg = 1;

	PopulationImpl(PopulationFactory populationFactory2) {
		this.populationFactory = populationFactory2 ;
	}

	@Override
	public void addPerson(final Person p) {
		// validation
		if (this.getPersons().containsKey(p.getId())) {
			throw new IllegalArgumentException("Person with id = " + p.getId() + " already exists.");
		}
		if ( p instanceof Lockable ) {
			((Lockable) p).setLocked();
		}

		// show counter
		this.counter++;
		if (this.counter % this.nextMsg == 0) {
			this.nextMsg *= 2;
			printPlansCount();
		}

	}

	@Override
	public Person removePerson(Id<Person> personId) {
		return this.persons.remove(personId) ;
	}

	@Override
	public final Map<Id<Person>, ? extends Person> getPersons() {
		return persons ;
	}

	@Override
	public ObjectAttributes getPersonAttributes() {
		return this.personAttributes;
	}

	@Override
	public PopulationFactory getFactory() {
		return this.populationFactory;
	}

	@Override
	public String getName() {
		return this.name ;
	}

	@Override
	public void setName(String name) {
		this.name = name ;
	}

	@Override
	public final void setLocked() {
		for ( Person person : this.persons.values() ) {
			if ( person instanceof Lockable ) {
				((Lockable)person).setLocked() ;
			}
		}
	}

	public void printPlansCount() {
		log.info(" person # " + this.counter);
	}

}
