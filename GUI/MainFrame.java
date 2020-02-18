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
 * Classe che gestisce la GUI del frame principale
 * 
 * @author Valerio Valletta
 *
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private MyButton start;
	protected MyButton choose;
	protected JRadioButton split;
	protected JRadioButton merge;
	protected JRadioButton crypt;
	protected JRadioButton zip;
	protected JTextField fileName;
	protected JTextField fileParts;
	protected JComboBox<String> unitM;
	protected JTextField fileSize;
	protected JTable table;
	private JProgressBar pbar;
	protected ButtonGroup modeG;

	public MainFrame(String title) {
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
		choose = new MyButton("Choose file...");
		start = new MyButton("Start");

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
		modeG = new ButtonGroup();

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


	}

	
	
	public MyButton getStart() {
		return start;
	}


	public MyButton getChoose() {
		return choose;
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

	public JProgressBar getPbar() {
		return pbar;
	}

	public ButtonGroup getModeG() {
		return modeG;
	}

	public void clearFields() {
		fileName.setText("");
		fileSize.setText("");
		fileParts.setText("");
	}

	
}
