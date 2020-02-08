import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class MyButton extends JButton {

	public MyButton(String s) {
		super(s);
		Font font = new Font("Arial", Font.BOLD, 30);
		setFont(font);
		setForeground(Color.BLACK);
		setBackground(Color.WHITE);
		Border line = new LineBorder(Color.BLACK);
		Border margin = new EmptyBorder(5, 15, 5, 15);
		Border compound = new CompoundBorder(line, margin);
		setBorder(compound);
		setFocusable(false);
	}

}
