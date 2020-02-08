import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

/**
 * Classe che gestisce la GUI del programma
 * 
 * @author Valerio Valletta
 *
 */
@SuppressWarnings("serial")
public class MyFrame extends JFrame {

	private JRadioButton split;
	private JRadioButton merge;
	private JRadioButton crypt;
	private JRadioButton zip;
	private JTextField fileName;
	private JTextField fileParts;
	JComboBox<String> unitM;
	private JTextField fileSize;
	private JTable table;
	private JProgressBar pbar;

	public MyFrame(String title) {
		super(title);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		JPanel fieldsPanel = new JPanel();
		JPanel btnPanel = new JPanel();
		JPanel unitPanel = new JPanel();
		fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.PAGE_AXIS));
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		unitPanel.setLayout(new BoxLayout(unitPanel, BoxLayout.X_AXIS));
		split = new JRadioButton("Split");
		merge = new JRadioButton("Merge");
		crypt = new JRadioButton("Crypt");
		zip = new JRadioButton("Zip");
		fileName = new JTextField();
		fileParts = new JTextField();
		fileSize = new JTextField();
		MyButton choose = new MyButton("Choose file...");
		MyButton start = new MyButton("Start");

		TableModel dataModel = new MyTableModel();
		table = new JTable(dataModel);
		JTableHeader header = table.getTableHeader();
		table.getColumn("").setCellRenderer(new ButtonRenderer());
		table.getColumn("").setCellEditor(new ButtonEditor(new JCheckBox(), table, this));
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(300, 101));

		pbar = new JProgressBar();
		pbar.setValue(0);
		pbar.setStringPainted(true);
		JLabel lFileName = new JLabel("Output file name:");
		lFileName.setAlignmentX(Component.LEFT_ALIGNMENT);
		String[] unitMList = new String[] { "Byte", "KB", "MB", "GB" };

		unitM = new JComboBox<>(unitMList);
		unitM.setPreferredSize(new Dimension(76, 10));
		JLabel lFileParts = new JLabel("File parts:");
		JLabel lFileSize = new JLabel("File size:");

		ButtonGroup typeG = new ButtonGroup();
		ButtonGroup modeG = new ButtonGroup();

		typeG.add(split);
		typeG.add(merge);
		modeG.add(zip);
		modeG.add(crypt);

		split.setSelected(true);
		fileName.setEnabled(false);
		add(btnPanel, BorderLayout.NORTH);
		// btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
		btnPanel.setLayout(new GridLayout(0, 2));
		btnPanel.add(choose);
		btnPanel.add(start);

		add(panel, BorderLayout.CENTER);
		panel.add(Box.createRigidArea(new Dimension(78, 25)));
		panel.add(split, BorderLayout.WEST);
		panel.add(Box.createRigidArea(new Dimension(20, 0)));
		panel.add(zip, BorderLayout.WEST);
		panel.add(Box.createRigidArea(new Dimension(20, 0)));
		panel.add(crypt, BorderLayout.EAST);
		panel.add(Box.createRigidArea(new Dimension(20, 0)));
		panel.add(merge, BorderLayout.EAST);

		add(fieldsPanel, BorderLayout.SOUTH);
		fieldsPanel.add(lFileName);
		fieldsPanel.add(fileName);
		fieldsPanel.add(lFileParts);
		fieldsPanel.add(fileParts);
		fieldsPanel.add(lFileSize);
		fieldsPanel.add(unitPanel);
		unitPanel.add(fileSize);
		unitPanel.add(unitM);
		fieldsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		fieldsPanel.add(pbar);
		fieldsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		fieldsPanel.add(header);
		fieldsPanel.add(scrollPane);
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				modeG.clearSelection();
			}
		});

		split.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				clearFields();
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
				clearFields();
				fileName.setEnabled(true);
				fileParts.setEnabled(false);
				fileSize.setEnabled(false);
				modeG.clearSelection();
			}

		});

		zip.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (merge.isSelected()) {
					clearFields();
					fileName.setEnabled(true);
					fileParts.setEnabled(false);
					fileSize.setEnabled(false);
				} else {
					clearFields();
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
					clearFields();
					fileName.setEnabled(true);
					fileParts.setEnabled(false);
					fileSize.setEnabled(false);
				} else {
					clearFields();
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
					clearFields();
					fileSize.setEnabled(true);
					fileParts.setEnabled(false);
				}
			}
		});

		fileParts.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!merge.isSelected() && !zip.isSelected() && !crypt.isSelected()) {
					clearFields();
					fileParts.setEnabled(true);
					fileSize.setEnabled(false);
				}
			}
		});

		choose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addQueue();
				pack();
				
				// caseHandler();
			}
		});
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				caseHandler();
			}
		});

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();
					InfoFrame frame = new InfoFrame(Main.queue.get(row).getFile().getName(), row, getTable());
					frame.pack();
					Point point = getLocationOnScreen();
					frame.setLocation(new Point(point.x - getWidth() / 2, point.y));// Visualizza il frame sopra a
					// quello principale

					frame.setVisible(true);

				}
			}
		});

	}

	public JRadioButton getSplit() {
		return split;
	}

	public JRadioButton getMerge() {
		return merge;
	}

	public JRadioButton getCrypt() {
		return crypt;
	}

	public JRadioButton getZip() {
		return zip;
	}

	public JTextField getFileName() {
		return fileName;
	}

	public JTextField getFileParts() {
		return fileParts;
	}

	public JTextField getFileSize() {
		return fileSize;
	}

	public JComboBox<String> getUnitM() {
		return unitM;
	}

	public JTable getTable() {
		return table;
	}

	public void clearFields() {
		fileName.setText("");
		fileSize.setText("");
		fileParts.setText("");
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
				SplitFile f = new SplitFile(fileSelectedPath);
				Job tmp = new Job(f);
				if (getMerge().isSelected()) {
					
					if (getZip().isSelected()) {
						tmp.setMode(1);
					}
					if (getCrypt().isSelected()) {
						tmp.setMode(2);
					}
					tmp.setOutFileName(fileName.getText());
					Main.queue.add(0, tmp);
					TableModel dm = getTable().getModel();
					((AbstractTableModel) dm).fireTableDataChanged();

				}
				if (getSplit().isSelected()) {
					tmp.setMode(0);
					if (getZip().isSelected()) {
						tmp.setMode(1);
					}
					if (getCrypt().isSelected()) {
						tmp.setMode(2);
					}
					if (!fileParts.getText().equals("")) {
						try {
							tmp.setParts(Integer.parseInt(fileParts.getText()));
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, "Invalid number of parts!");
							return;
						}

					} else {

						switch (String.valueOf(getUnitM().getSelectedItem())) {
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
							if (Long.parseLong(getFileSize().getText()) * unit > f.length()) {
								JOptionPane.showMessageDialog(null,
										"Invalid file size! File size: " + Main.getHumanLength(f.length()));
								return;
							}
							tmp.setDim(Long.parseLong(getFileSize().getText()) * unit);
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, "Invalid file size! ");
							return;
						}
					}
					
					Main.queue.add(0, tmp);
					TableModel dm = getTable().getModel();
					((AbstractTableModel) dm).fireTableDataChanged();

				}
			}

		}

	}

	/**
	 * Smista i job presenti nella coda eseguendo le operazioni richieste da esso
	 */

	public void caseHandler() {
		pbar.setValue(0);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int valueSingleJob = (100/(Main.queue.size() - Main.queueIndex));
		System.out.println(valueSingleJob);
		for (int j = 0; j < Main.queue.size(); j++) {
			Main.queueIndex++;
			Job tmp = Main.queue.get(j);
			String fileSelectedPath = Main.queue.get(j).getFile().getAbsolutePath();
			SplitFile f = new SplitFile(fileSelectedPath);

			if (!tmp.getMode(0)) {
				if (tmp.getMode(2)) {
					f.cryptMerge(fileSelectedPath, j);
					clearFields();
					tmp.setStatus("Completed");
					TableModel dm = getTable().getModel();
					((AbstractTableModel) dm).fireTableDataChanged();
					pbar.setValue(pbar.getValue()+valueSingleJob);
					return;
				}

				if (tmp.getMode(1)) {

					f.unZipSplit();
					f = new SplitFile(f.getAbsolutePath().replace(".zip", ""));
				}
				SplitFile c = new SplitFile(fileSelectedPath.substring(0, fileSelectedPath.lastIndexOf(File.separator))
						+ File.separator + Main.queue.get(j).getOutFileName()); // Creo il file finale con il suo path
																				// associato
				List<File> filesM = f.listOfFilesToMerge();
				for (File fx : filesM) {
					System.out.println(fx.getName());
				}
				SplitFile.mergeFiles(filesM, c, 0);

			}
			if (tmp.getMode(0)) {

				if (tmp.getMode(1)) {
					long dim[] = getDimArray(f, tmp);
					f.zipSplit(dim);

				}
				if (tmp.getMode(2)) {

					f.cryptSplit(tmp.getDim());
				}
				if (tmp.getParts() > 0) {
					f.split(tmp.getParts());

				}
				if (tmp.getDim() > 0 && (!tmp.getMode(1) && !tmp.getMode(2))) {

					long dim[] = getDimArray(f, tmp);

					f.split(dim);

				}
			}
			clearFields();
			tmp.setStatus("Completed");
			TableModel dm = getTable().getModel();
			((AbstractTableModel) dm).fireTableDataChanged();
			pbar.setValue(pbar.getValue()+valueSingleJob);
		}

	}

	/**
	 * Restituisce un array contente le dimensioni di ogni parte che verra' creata
	 */

	long[] getDimArray(SplitFile f, Job tmp) {
		long dimFile = f.length();
		int parts = (int) (dimFile / tmp.getDim());
		if ((dimFile % tmp.getDim()) >= 1)
			parts++;
		long dim[] = new long[parts];
		for (int i = 0; i < parts; i++) {
			if (i != (parts - 1)) {
				long s = tmp.getDim();
				dim[i] = s;
				dimFile -= s;
			} else {
				dim[i] = dimFile;
			}
		}
		return dim;
	}

}
