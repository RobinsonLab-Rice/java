package main.adapters.plate;

import view.MainPanel;

/**
 * Adapter to link the plate model to the view. All functions that the plate model needs the view
 * to perform are described here.
 * @author Christian
 *
 */
public class Plate2ViewAdapter {

    private MainPanel view;

    /* Sets up model references necessary for this adapter. */
    public Plate2ViewAdapter(MainPanel view) {
        this.view = view;
    }

    /**
	 * Pushes the view to repaint its panels when the model needs it to.
	 */
	public void updateView(){
        view.update();
    }
}
