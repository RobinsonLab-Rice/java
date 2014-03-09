package model.serialization;

import model.plate.objects.Plate;

/**
 * Adapter from serialization model to the plate model.
 * 
 * @author christianhenry
 */
public interface PlateAdapter {

	public Iterable<Plate> getPlateList();
	
	public void setPlateList(Iterable<Plate> plates);
	
}
