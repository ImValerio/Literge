import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class ButtonEditor extends DefaultCellEditor {
	protected JButton button;
	private JTable table;
	private JFrame frame;

	public ButtonEditor(JCheckBox checkBox, JTable table, JFrame frame) {
		super(checkBox);
		button = new JButton();
		button.setOpaque(true);
		button.setText("Delete");
		this.table = table;
		this.frame = frame;
	}

	public Component getTableCellEditorComponent( Object value, boolean isSelected, int row, int column) {
		Main.queue.remove(row);
		TableModel dm = table.getModel();
		((AbstractTableModel) dm).fireTableDataChanged();
		frame.pack();
		return button;
	}

}
