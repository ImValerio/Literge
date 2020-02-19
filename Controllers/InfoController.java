
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * Classe che gestisce l'interazione con la gui associata al frame contenente
 * informazioni del job
 * 
 * @author Valerio Valletta
 *
 */

public class InfoController {
	private InfoFrame frame;
	private int row;

	public InfoController(InfoFrame frame, int row, JTable jTable) {
		this.frame = frame;
		this.row = row;

		frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);

		JRadioButton split = frame.getSplit();
		JRadioButton merge = frame.getMerge();
		JRadioButton zip = frame.getZip();
		JRadioButton crypt = frame.getCrypt();
		JTextField fileName = frame.getFileName();
		JTextField fileSize = frame.getFileSize();
		JTextField fileParts = frame.getFileParts();

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				frame.dispose();
				TableModel dm = jTable.getModel();
				((AbstractTableModel) dm).fireTableDataChanged();
			}
		});

		frame.getPanel().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				frame.getModeG().clearSelection();
			}
		});

		frame.getChoose().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeFile();
			}
		});

		split.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.clearFields();
				fileName.setEnabled(false);
				fileParts.setEnabled(true);
				fileSize.setEnabled(true);

			}

		});
		merge.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.clearFields();
				fileName.setEnabled(true);
				fileParts.setEnabled(false);
				fileSize.setEnabled(false);
				frame.getModeG().clearSelection();
			}

		});

		zip.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.clearFields();
				fileName.setEnabled(false);
				fileParts.setEnabled(false);
				fileSize.setEnabled(true);

			}

		});

		crypt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.clearFields();
				fileName.setEnabled(false);
				fileParts.setEnabled(false);
				fileSize.setEnabled(true);

			}

		});

		fileSize.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!merge.isSelected()) {
					frame.clearFields();
					fileSize.setEnabled(true);
					fileParts.setEnabled(false);
				}
			}
		});
		fileParts.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!merge.isSelected() && !zip.isSelected() && !crypt.isSelected()) {
					frame.clearFields();
					fileParts.setEnabled(true);
					fileSize.setEnabled(false);
				}
			}
		});

		frame.getSave().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				modifyJob();

			}

		});

		frame.getPanel().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				frame.getModeG().clearSelection();
			}
		});
	}

	/**
	 * Riempie i campi in base al Job selezionato
	 */
	public void fillFields() {
		Job tmp = Main.queue.get(row);
		if (tmp.getDim() > 0)
			frame.getFileSize().setText("" + tmp.getDim());
		if (tmp.getParts() > 0)
			frame.getFileParts().setText("" + tmp.getParts());
		if (tmp.getMode(0)) {
			frame.getSplit().setSelected(true);

		} else {
			frame.getMerge().setSelected(true);
			frame.getFileName().setText(tmp.getOutFileName());
			frame.getFileName().setEnabled(true);
			frame.getFileParts().setEnabled(false);
			frame.getFileSize().setEnabled(false);
		}
		if (tmp.getMode(1))
			frame.getZip().setSelected(true);
		if (tmp.getMode(2))
			frame.getCrypt().setSelected(true);

	}

	/**
	 * Modifica il path del Job selezionato
	 */

	public void changeFile() {
		JFileChooser fc = new JFileChooser();

		int returnValue = fc.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			String fileSelectedPath = fc.getSelectedFile().getAbsolutePath();
			File f = new File(fileSelectedPath);
			Main.queue.get(row).setFile(f);
		}
	}

	/**
	 * Crea un Job a seconda dei campi riempiti e sostiutisce il Job selezionato con
	 * quello appena generato
	 */
	public void modifyJob() {
		int unit = 1;
		Job tmp = new Job(Main.queue.get(row).getFile());

		if (frame.getMerge().isSelected()) {
			if (frame.getZip().isSelected()) {
				tmp.setMode(1);
			}
			if (frame.getCrypt().isSelected()) {
				tmp.setMode(2);
			}
			tmp.setOutFileName(frame.getFileName().getText());
			Main.queue.set(row, tmp);
			return;
		}
		if (frame.getSplit().isSelected()) {
			tmp.setMode(0);
			if (frame.getZip().isSelected()) {
				tmp.setMode(1);
			}
			if (frame.getCrypt().isSelected()) {
				tmp.setMode(2);
			}
			if (!frame.getFileParts().getText().equals("")) {
				try {
					tmp.setParts(Integer.parseInt(frame.getFileParts().getText()));
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Invalid number of parts!");
					return;
				}

			} else {
				switch (String.valueOf(frame.getUnitM().getSelectedItem())) {
				case "Byte":
					unit = 1;
					break;
				case "KB":
					unit = 1024;
					break;
				case "MB":
					unit = 1024 * 1024;
					break;
				case "GB":
					unit = 1024 * 1024 * 1024;
					break;
				}
				try {
					if (Long.parseLong(frame.getFileSize().getText()) * unit > tmp.getFile().length()) {
						JOptionPane.showMessageDialog(null,
								"Invalid file size! File size: " + Main.getHumanLength(tmp.getFile().length()));
						return;
					}
					tmp.setDim(Long.parseLong(frame.getFileSize().getText()) * unit);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Invalid file size! ");
					return;
				}
			}

			Main.queue.set(row, tmp);
		}
	}
}
