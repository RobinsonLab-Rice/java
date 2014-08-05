package main.view;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.plaf.synth.SynthTreeUI;
import javax.swing.tree.AbstractLayoutCache;
import javax.swing.tree.TreePath;
import java.awt.*;

/**
 * Custom tree UI that makes the editing field width slightly larger than display area, to avoid truncating new values
 * that are longer than old ones.
 */
public class CustomTreeUI extends SynthTreeUI {

    private Component scrollPane;

    public CustomTreeUI(Component parent) {
        this.scrollPane = parent;
    }

    @Override
    protected AbstractLayoutCache.NodeDimensions createNodeDimensions() {
        return new NodeDimensionsHandler() {
            @Override
            public Rectangle getNodeDimensions(Object value, int row, int depth, boolean expanded, Rectangle size) {
                Rectangle dimensions = super.getNodeDimensions(value, row, depth, expanded, size);
                dimensions.width = dimensions.width + 20;
                return dimensions;
            }
        };
    }

    @Override
    protected void paintHorizontalLine(Graphics g, JComponent c, int y, int left, int right) {
        // do nothing.
    }

    @Override
    protected void paintVerticalPartOfLeg(Graphics g, Rectangle clipBounds, Insets insets, TreePath path) {
        // do nothing.
    }
}
