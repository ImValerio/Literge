import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Main {
	public static List<Job> queue = new ArrayList<Job>();
	public static int queueIndex = 0;
	
	public static void main(String[] args) throws IOException {
		MainFrame frame = new MainFrame("File splitter");
		GuiController controller = new GuiController(frame);
		frame.pack();
		frame.setLocationRelativeTo(null); // centra la finestra
		frame.setVisible(true);
	}

	public static double getDimKb(long a) {
		return a / 1024;
	}

	public static double getDimMb(long a) {
		
		return getDimKb(a) / 1024;
	}

	public static double getDimGb(long a) {
		return getDimMb(a) / 1024;
	}

	
	/**
	 * Data una lunghezza in byte restituisce una stringa contenente la forma piu
	 * umanamente leggebile(utilizzando le unita' di misura appropriate)
	 */

	public static String getHumanLength(long d) {
		DecimalFormat df = new DecimalFormat("#.#");
		if (Main.getDimGb(d) > 0.99) {
			return df.format(Main.getDimGb(d)) + "Gb";
		}
		if (Main.getDimMb(d) > 0.99) {
			return df.format(Main.getDimMb(d)) + "Mb";
		}
		if (Main.getDimKb(d) > 0.99) {
			return Main.getDimKb(d) + "Kb";
		} else {
			return d + " Byte";
		}

		
	}
}
