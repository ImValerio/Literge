import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * Modello della tabella da visualizzare
 * 
 * @author Valerio Valletta
 *
 */

@SuppressWarnings("serial")
public class MyTableModel extends AbstractTableModel {
	private String[] colNames = { "Name", "Size", "Info", "Status", "" };
	private List<Job> data;

	public MyTableModel() {
		data = Main.queue;
	}

	@Override
	public int getColumnCount() {
		return colNames.length;
	}

	/**
	 * In base alla dimensione della coda restituisce il numero di righe
	 */
	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public String getColumnName(int index) {
		return colNames[index];
	}

	/**
	 * Metodo che a seconda della colonna riempe la riga con le informazioni del job
	 * ad esso associate
	 */
	@Override
	public Object getValueAt(int row, int col) {
		Job tmp = data.get(row);
		switch (col) {
		case 0:
			return tmp.getFile().getName();
		case 1:
			if (Main.getDimMb(tmp.getFile().length()) > 0) {
				return Main.getDimMb(tmp.getFile().length()) + "Mb";
			}
			if (Main.getDimKb(tmp.getFile().length()) > 0) {
				return Main.getDimKb(tmp.getFile().length()) + "Kb";
			} else {
				return tmp.getFile().length() + " Byte";
			}

		case 2:
			String info = "";
			if (tmp.getMode(0)) {
				info += "split";
			} else {
				info += "merge";
			}
			if (tmp.getMode(1)) {
				info += "(zip)";
			}
			if (tmp.getMode(2)) {
				info += "(crypt)";
			}
			if (tmp.getParts() > 0) {
				info += ", " + tmp.getParts() + " parts";
			}
			if (tmp.getDim() > 0) {
				info += ", " + Main.getHumanLength(tmp.getDim());
			}
			return info;
		case 3:
			return tmp.getStatus();

		default:
			return "";
		}
	}

	/**
	 * Rende la quarta cella "editabile" quindi il bottone cliccabile
	 */
	@Override
	public boolean isCellEditable(int row, int col) {
		if (col == 4) {
			return true;
		} else {
			return false;
		}
	}

}
