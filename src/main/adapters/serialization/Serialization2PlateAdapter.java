package main.adapters.serialization;

import model.plate.PlateModel;
import model.plate.objects.Plate;

/**
 * Adapter from serialization model to the plate model.
 * 
 * @author christianhenry
 */
public class Serialization2PlateAdapter {

    private PlateModel plateModel;

    /* Sets up model references necessary for this adapter. */
    public Serialization2PlateAdapter(PlateModel plateModel) { this.plateModel = plateModel; }

    public Iterable<Plate> getPlateList(){
        return plateModel.getPlateList();
    }
	
	public void setPlateList(Iterable<Plate> plates){
        plateModel.setPlateList(plates);
    }
	
}
