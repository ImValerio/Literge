import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
	public static List<Job> queue = new ArrayList<Job>();
	public static int queueIndex = 0;
	
	public static void main(String[] args) throws IOException {
		MyFrame frame = new MyFrame("File splitter");
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null); // centra la finestra

	}

	public static long getDimKb(long a) {
		return a / 1024;
	}

	public static long getDimMb(long a) {
		return getDimKb(a) / 1024;
	}

	/**
	 * Data una lunghezza in byte restituisce una stringa contenente la forma piu
	 * umanamente leggebile(utilizzando le unit� di misura appropriate)
	 */

	public static String getHumanLength(long d) {
		String length = "";
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
