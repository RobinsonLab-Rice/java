package test.model.plate.objects;

import com.cedarsoftware.util.io.JsonReader;
import main.model.plate.objects.Plate;
import main.model.plate.objects.PlateSpecifications;
import main.model.serialization.SerializationModel;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class PlateTest {

    private Plate sixWell;

    @Before
    public void setUp() {
        PlateSpecifications sixWellSpecs = new PlateSpecifications(23.16, 24.76, 39.0, 2, 3, 127.76, 85.48, 35.43, 10.0, 17.0);
        sixWell = new Plate("6-well", new Point2D.Double(0,0), sixWellSpecs, "ALPHANUMERIC");
    }

    @Test
    public void testGetWellsInRange() throws Exception {
        String[] all = new String[] {"A1", "A2", "A3", "B1", "B2", "B3"};
        assertEquals(Arrays.asList(all), sixWell.getWellsInRange("A1", "B3", 1));
        String[] even = new String[] {"A2", "B1", "B3"};
        assertEquals(Arrays.asList(even), sixWell.getWellsInRange("A2", "B3", 2));
        String[] odd = new String[] {"A1", "A3", "B2"};
        assertEquals(Arrays.asList(odd), sixWell.getWellsInRange("A1", "B3", 2));
    }

    @Test
    public void testConvertIdToNum() throws Exception {
        assertEquals(4, sixWell.convertIdToNum("B1"));
        assertEquals(1, sixWell.convertIdToNum("A1"));
        assertEquals(5, sixWell.convertIdToNum("B2"));
        assertEquals(6, sixWell.convertIdToNum("B3"));
    }

    @Test
    public void testConvertNumToId() throws Exception {
        assertEquals("B1", sixWell.convertNumToId(4));
        assertEquals("A1", sixWell.convertNumToId(1));
        assertEquals("B2", sixWell.convertNumToId(5));
        assertEquals("B3", sixWell.convertNumToId(6));
    }
}