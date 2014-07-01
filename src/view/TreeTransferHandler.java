package view;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
import model.tasks.basictasks.IExecuteTask;

import javax.swing.*;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom transfer handler for the IExecuteTask JTree in TaskCreationPanel.
 */
class TreeTransferHandler extends TransferHandler {
    DataFlavor nodesFlavor;
    DataFlavor[] flavors = new DataFlavor[1];
    MutableTreeNode[] nodesToRemove;

    /**
     * Define that this transfer handler is made for nodes, specifying the appropriate data flavor for that.
     */
    public TreeTransferHandler() {
        try {
            String mimeType = DataFlavor.javaJVMLocalObjectMimeType +
                    ";class=\"" +
                    MutableTreeNode[].class.getName() +
                    "\"";
            nodesFlavor = new DataFlavor(mimeType);
            flavors[0] = nodesFlavor;
        } catch(ClassNotFoundException e) {
            System.out.println("ClassNotFound: " + e.getMessage());
        }
    }

    /**
     * Decides whether or not we support the type of import in support.
     * @param support destination and type of movement requested
     * @return boolean for whether or not this can import input support
     */
    public boolean canImport(TransferSupport support) {
        if(!support.isDrop()) {
            return false;
        }
        support.setShowDropLocation(true);
        if(!support.isDataFlavorSupported(nodesFlavor)) {
            return false;
        }
        // Do not allow a drop on the drag source selections.
        JTree.DropLocation dl =
                (JTree.DropLocation)support.getDropLocation();
        JTree tree = (JTree)support.getComponent();
        int dropRow = tree.getRowForPath(dl.getPath());
        int[] selRows = tree.getSelectionRows();
        for(int i = 0; i < selRows.length; i++) {
            if(selRows[i] == dropRow) {
                return false;
            }
        }
        return true;
    }

    /**
     * Create a transferable of all selected nodes when drag is initiated, creating a deep copy of them.
     */
    protected Transferable createTransferable(JComponent c) {
        JTree tree = (JTree)c;
        TreePath[] paths = tree.getSelectionPaths();
        if(paths != null) {
            // Make up a node array of copies for transfer and
            // another for/of the nodes that will be removed in
            // exportDone after a successful drop.
            List<MutableTreeNode> copies = new ArrayList<MutableTreeNode>();
            List<MutableTreeNode> toRemove = new ArrayList<MutableTreeNode>();

            //copy every node
            for (TreePath path : paths) {
                MutableTreeNode node = (MutableTreeNode) path.getLastPathComponent();
                copies.add(node);
                toRemove.add(node);
            }

            MutableTreeNode[] nodes = copies.toArray(new MutableTreeNode[copies.size()]);
            nodesToRemove = toRemove.toArray(new MutableTreeNode[toRemove.size()]);
            return new NodesTransferable(nodes);
        }
        return null;
    }

    /**
     * Called when data has been successfully moved. If the data transfer was a copy action, delete
     * the original item.
     */
    @Override
    protected void exportDone(JComponent source, Transferable data, int action) {
        if((action & MOVE) == MOVE) {
            JTree tree = (JTree)source;
            DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
            // Remove nodes saved in nodesToRemove in createTransferable.
            for(int i = 0; i < nodesToRemove.length; i++) {
                model.removeNodeFromParent(nodesToRemove[i]);
            }
        }
    }

    /**
     * @return COPY_OR_MOVE, this jtree can do both
     */
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    /**
     * Called when a drag is initiated, packages up data to later drop it.
     * @param support checked at the beginning of transfer, return false if it cannot be imported
     * @return boolean for whether or not the transfer was successful
     */
    public boolean importData(TransferSupport support) {
        if(!canImport(support)) {
            return false;
        }

        // Extract transfer data.
        MutableTreeNode[] nodes = null;
        try {
            Transferable t = support.getTransferable();
            nodes = (MutableTreeNode[])t.getTransferData(nodesFlavor);
        } catch(UnsupportedFlavorException ufe) {
            System.out.println("UnsupportedFlavor: " + ufe.getMessage());
        } catch(java.io.IOException ioe) {
            System.out.println("I/O error: " + ioe.getMessage());
        }

        // Get drop location info.
        JTree.DropLocation dl =
                (JTree.DropLocation)support.getDropLocation();
        int childIndex = dl.getChildIndex();
        TreePath dest = dl.getPath();
        MutableTreeNode parent =
                (MutableTreeNode)dest.getLastPathComponent();
        JTree tree = (JTree)support.getComponent();
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        // Configure for drop mode.
        int index = childIndex;    // DropMode.INSERT
        if(childIndex == -1) {     // DropMode.ON
            index = parent.getChildCount();
        }

        //make a copy of the node and insert it into right place
        for (int i = 0; i < nodes.length; i++) {
            try {
                //make a copy of the node, and set its parent to be correct (or else the hierachy falls apart)
                IExecuteTask deepCopy = (IExecuteTask) JsonReader.jsonToJava(JsonWriter.objectToJson(nodes[i]));
                deepCopy.setParent(parent);
                deepCopy.resetParents();
                model.insertNodeInto(deepCopy, parent, index++);
            } catch (IOException e) {
                System.out.println("Could not make a copy of the node we are moving.");
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * @return string of class name
     */
    public String toString() {
        return getClass().getName();
    }

    /**
     * Helper class making a transferable out of MutableTreeNodes (which tasks implement).
     */
    public class NodesTransferable implements Transferable {
        MutableTreeNode[] nodes;

        public NodesTransferable(MutableTreeNode[] nodes) {
            this.nodes = nodes;
        }

        public Object getTransferData(DataFlavor flavor)
                throws UnsupportedFlavorException {
            if(!isDataFlavorSupported(flavor))
                throw new UnsupportedFlavorException(flavor);
            return nodes;
        }

        public DataFlavor[] getTransferDataFlavors() {
            return flavors;
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return nodesFlavor.equals(flavor);
        }
    }
}