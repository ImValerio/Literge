
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * Classe che gestisce l'interazione con la GUI del frame principale
 * 
 * @author Valerio Valletta
 *
 */

public class MainController {

	private MainFrame frame;

	public MainController(MainFrame frame) {
		this.frame = frame;

		JRadioButton split = frame.getSplit();
		JRadioButton merge = frame.getMerge();
		JRadioButton zip = frame.getZip();
		JRadioButton crypt = frame.getCrypt();
		JTextField fileName = frame.getFileName();
		JTextField fileSize = frame.getFileSize();
		JTextField fileParts = frame.getFileParts();

		split.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.clearFields();
				fileName.setEnabled(false);
				fileParts.setEnabled(true);
				fileSize.setEnabled(true);
				zip.setEnabled(true);
				crypt.setEnabled(true);

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
				if (merge.isSelected()) {
					frame.clearFields();
					fileName.setEnabled(true);
					fileParts.setEnabled(false);
					fileSize.setEnabled(false);
				} else {
					frame.clearFields();
					fileName.setEnabled(false);
					fileParts.setEnabled(false);
					fileSize.setEnabled(true);
				}

			}

		});

		crypt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (merge.isSelected()) {
					frame.clearFields();
					fileName.setEnabled(true);
					fileParts.setEnabled(false);
					fileSize.setEnabled(false);
				} else {
					frame.clearFields();
					fileName.setEnabled(false);
					fileParts.setEnabled(false);
					fileSize.setEnabled(true);
				}	

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
		
		frame.getPanel().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				frame.getModeG().clearSelection();
			}
		});

		frame.getChoose().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addQueue();
				frame.pack();
			}
		});
		frame.getStart().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				caseHandler();
			}
		});

		frame.getTable().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();
					InfoFrame iframe = new InfoFrame(Main.queue.get(row).getFile().getName());
					InfoController iframeCtrl = new InfoController(iframe, row, frame.getTable());
					iframe.pack();
					Point point = frame.getLocationOnScreen();
					iframe.setLocation(new Point(point.x - frame.getWidth() / 2, point.y));// Visualizza il frame sopra
																							// a
					// quello principale
					iframeCtrl.fillFields();
					iframe.setVisible(true);

				}
			}
		});

		frame.getTable().addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int col = frame.getTable().columnAtPoint(evt.getPoint());
				int row = frame.getTable().rowAtPoint(evt.getPoint());
				if (col == 4) {
					if(!Main.queue.get(row).isCompleted())
						Main.tmpQueueLength--;
					System.out.println("File in coda =>"+Main.tmpQueueLength);
					Main.queue.remove(row);
					TableModel dm = frame.getTable().getModel();
					((AbstractTableModel) dm).fireTableDataChanged();
					frame.pack();

				}
			}
		});
	}

	/**
	 * Permette di aggiungere alla coda il job compilato dall'utente tramite GUI
	 */
	public void addQueue() {
		System.setProperty("apple.awt.fileDialogForDirectories", "true");
		JFileChooser fc = new JFileChooser();
		fc.setMultiSelectionEnabled(true);

		int unit = 1;
		int returnValue = fc.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File[] files = fc.getSelectedFiles();

			for (int i = 0; i < files.length; i++) {
				String fileSelectedPath = files[i].getAbsolutePath();
				Literge f = new Literge(fileSelectedPath);
				Job tmp = new Job(f);
				if (frame.getMerge().isSelected()) {

					if (frame.getZip().isSelected()) {
						tmp.setMode(1);
					}
					if (frame.getCrypt().isSelected()) {
						tmp.setMode(2);
					}
					tmp.setOutFileName(frame.getFileName().getText());
					Main.queue.add(0, tmp);
					Main.tmpQueueLength++;
					TableModel dm = frame.getTable().getModel();
					((AbstractTableModel) dm).fireTableDataChanged();

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
							if (Long.parseLong(frame.getFileSize().getText()) * unit > f.length()) {
								JOptionPane.showMessageDialog(null,
										"Invalid file size! File size: " + Main.getHumanLength(f.length()));
								return;
							}
							tmp.setDim(Long.parseLong(frame.getFileSize().getText()) * unit);
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, "Invalid file size! ");
							return;
						}
					}

					Main.queue.add(0, tmp);
					Main.tmpQueueLength++;
					TableModel dm = frame.getTable().getModel();
					((AbstractTableModel) dm).fireTableDataChanged();

				}
			}

		}

	}

	/**
	 * Smista i job presenti nella coda eseguendo le operazioni richieste da esso
	 */

	public void caseHandler() {
		System.out.println("File in coda =>"+Main.tmpQueueLength);
		frame.getPbar().setValue(0);
		int valueSingleJob = (100 / (Main.tmpQueueLength));
		for (int j = 0; j < Main.queue.size(); j++) {

			Job tmp = Main.queue.get(j);
			if (!tmp.isCompleted()) {

				String fileSelectedPath = Main.queue.get(j).getFile().getAbsolutePath();
				Literge f = new Literge(fileSelectedPath);

				if (!tmp.getMode(0)) {
					if (tmp.getMode(2)) {
						f.cryptMerge(fileSelectedPath, j);
						frame.clearFields();
						tmp.setStatus("Completed");
						tmp.setCompleted(true);
						if(frame.getPbar().getValue() < 100) {
							frame.getPbar().setValue(100);
						}
						Main.tmpQueueLength = 0;
						TableModel dm = frame.getTable().getModel();
						((AbstractTableModel) dm).fireTableDataChanged();
						frame.getPbar().setValue(frame.getPbar().getValue() + valueSingleJob);
						return;
					}

					if (tmp.getMode(1)) {

						f.unZipSplit();
						f = new Literge(f.getAbsolutePath().replace(".zip", ""));
					}
					Literge c = new Literge(
							fileSelectedPath.substring(0, fileSelectedPath.lastIndexOf(File.separator)) + File.separator
									+ Main.queue.get(j).getOutFileName()); // Creo il file finale con il suo path
																			// associato
					List<File> filesM = f.listOfFilesToMerge();
					for (File fx : filesM) {
						System.out.println(fx.getName());
					}
					Literge.mergeFiles(filesM, c, 0);

				}
				if (tmp.getMode(0)) {

					if (tmp.getMode(1)) {
						long dim[] = getDimArray(f, tmp.getDim());
						f.zipSplit(dim);

					}
					if (tmp.getMode(2)) {
						long dim[] = getDimArray(f, tmp.getDim());
						f.cryptSplit(tmp.getDim());
					}
					if (tmp.getParts() > 0) {
						f.split(tmp.getParts());

					}
					if (tmp.getDim() > 0 && (!tmp.getMode(1) && !tmp.getMode(2))) {

						long dim[] = getDimArray(f, tmp.getDim());

						f.split(dim);

					}
				}
			}
			frame.clearFields();
			tmp.setStatus("Completed");
			tmp.setCompleted(true);
			TableModel dm = frame.getTable().getModel();
			((AbstractTableModel) dm).fireTableDataChanged();
			frame.getPbar().setValue(frame.getPbar().getValue() + valueSingleJob);
		}
		if(frame.getPbar().getValue() < 100) {
			frame.getPbar().setValue(100);
		}
		Main.tmpQueueLength = 0;
	}

	/**
	 * Restituisce un array contente le dimensioni di ogni parte che verra' creata
	 */

	public static long[] getDimArray(Literge f, long dimPart) {
		long dimFile = f.length();
		int parts = (int) (dimFile / dimPart);
		if ((dimFile % dimPart) >= 1)
			parts++;
		long dim[] = new long[parts];
		for (int i = 0; i < parts; i++) {
			if (i != (parts - 1)) {
				long s = dimPart;
				dim[i] = s;
				dimFile -= s;
			} else {
				dim[i] = dimFile;
			}
		}
		return dim;
	}

}
