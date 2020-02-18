import java.io.IOException;
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

	public static long getDimKb(long a) {
		return a / 1024;
	}

	public static long getDimMb(long a) {
		return getDimKb(a) / 1024;
	}

	public static long getDimGb(long a) {
		return getDimMb(a) / 1024;
	}

	
	/**
	 * Data una lunghezza in byte restituisce una stringa contenente la forma piu
	 * umanamente leggebile(utilizzando le unita' di misura appropriate)
	 */

	public static String getHumanLength(long d) {
		String length = "";
		if (Main.getDimGb(d) > 0) {
			length += ", " + Main.getDimGb(d) + "Mb";
		}
		if (Main.getDimMb(d) > 0) {
			length += ", " + Main.getDimMb(d) + "Mb";
		}
		if (Main.getDimKb(d) > 0) {
			length += Main.getDimKb(d) + "Kb";
		} else {
			length += d + " Byte";
		}

		return length;
	}
}
