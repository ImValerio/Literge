import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
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
import javax.swing.JComboBox;
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
public class InfoFrame extends MainFrame {
	private JPanel panel;
	private MyButton save;

	public InfoFrame(String title) {
		super(title);
		setResizable(false);
		panel = new JPanel();
		JPanel fieldsPanel = new JPanel();
		JPanel btnPanel = new JPanel();
		JPanel unitPanel = new JPanel();
		fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.PAGE_AXIS));
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		unitPanel.setLayout(new BoxLayout(unitPanel, BoxLayout.X_AXIS));
		fileName = new JTextField();
		fileParts = new JTextField();
		fileSize = new JTextField();
		save = new MyButton("Save");
		JLabel lFileName = new JLabel("Output file name:");
		lFileName.setAlignmentX(Component.LEFT_ALIGNMENT);
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
		btnPanel.setLayout(new GridLayout(0, 2));
		btnPanel.add(choose);
		btnPanel.add(save);
		add(panel, BorderLayout.CENTER);
		panel.add(Box.createRigidArea(new Dimension(55, 25)));
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


	}
	public JPanel getPanel() {
		return panel;
	}

	public MyButton getSave() {
		return save;
	}
	
	

}
