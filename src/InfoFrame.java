import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * Classe che gestisce la GUI associata alla modifica di un Job
 * 
 * @author Valerio Valletta
 *
 */

@SuppressWarnings("serial")
public class InfoFrame extends JFrame {
	private JRadioButton split;
	private JRadioButton merge;
	private JRadioButton crypt;
	private JRadioButton zip;
	private JTextField fileName;
	private JTextField fileParts;
	private JTextField fileSize;
	int row;

	public InfoFrame(String title, int row, JTable jTable) {
		super(title);
		setResizable(false);
		this.row = row;
		JPanel panel = new JPanel();
		JPanel fieldsPanel = new JPanel();
		JPanel btnPanel = new JPanel();
		fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.PAGE_AXIS));
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		split = new JRadioButton("Split");
		merge = new JRadioButton("Merge");
		crypt = new JRadioButton("Crypt");
		zip = new JRadioButton("Zip");
		fileName = new JTextField();
		fileParts = new JTextField();
		fileSize = new JTextField();
		MyButton choose = new MyButton("Choose file...");
		MyButton save = new MyButton("Save");
		JLabel lFileName = new JLabel("Output file name:");
		lFileName.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel lFileParts = new JLabel("File parts:");
		JLabel lFileSize = new JLabel("File size(byte):");

		ButtonGroup typeG = new ButtonGroup();
		ButtonGroup modeG = new ButtonGroup();

		typeG.add(split);
		typeG.add(merge);
		modeG.add(zip);
		modeG.add(crypt);

		split.setSelected(true);
		fileName.setEnabled(false);
		add(btnPanel, BorderLayout.NORTH);
		btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
		btnPanel.add(choose);
		btnPanel.add(save);
		add(panel, BorderLayout.CENTER);
		panel.add(Box.createRigidArea(new Dimension(25, 25)));
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
		fieldsPanel.add(fileSize);
		fieldsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		fillFields();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				dispose();
				TableModel dm = jTable.getModel();
				((AbstractTableModel) dm).fireTableDataChanged();
			}
		});

		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				modeG.clearSelection();
			}
		});

		choose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeFile();
			}
		});

		split.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				clearFields();
				fileName.setEnabled(false);
				fileParts.setEnabled(true);
				fileSize.setEnabled(true);

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
				clearFields();
				fileName.setEnabled(false);
				fileParts.setEnabled(false);
				fileSize.setEnabled(true);

			}

		});

		crypt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				clearFields();
				fileName.setEnabled(false);
				fileParts.setEnabled(false);
				fileSize.setEnabled(true);

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

		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				modifyJob();
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

	public void clearFields() {
		fileName.setText("");
		fileSize.setText("");
		fileParts.setText("");
	}

	/**
	 * Riempie i campi in base al Job selezionato
	 */
	public void fillFields() {
		Job tmp = Main.queue.get(row);
		if (tmp.getDim() > 0)
			getFileSize().setText("" + tmp.getDim());
		if (tmp.getParts() > 0)
			getFileParts().setText("" + tmp.getParts());
		if (tmp.getMode(0)) {
			getSplit().setSelected(true);

		} else {
			getMerge().setSelected(true);
			getFileName().setText(tmp.getOutFileName());
			fileName.setEnabled(true);
			fileParts.setEnabled(false);
			fileSize.setEnabled(false);
		}
		if (tmp.getMode(1))
			getZip().setSelected(true);
		if (tmp.getMode(2))
			getCrypt().setSelected(true);

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

		Job tmp = new Job(Main.queue.get(row).getFile());
		if (getMerge().isSelected()) {
			if (getZip().isSelected()) {
				tmp.setMode(1);
			}
			if (getCrypt().isSelected()) {
				tmp.setMode(2);
			}
			tmp.setOutFileName(fileName.getText());
			Main.queue.set(row, tmp);
			return;
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
				try {
					tmp.setDim(Long.parseLong(getFileSize().getText()));
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Invalid file size!");
					return;
				}
			}
			Main.queue.set(row, tmp);

		}

	}

}
