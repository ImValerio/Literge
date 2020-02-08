import java.io.File;

/**
 * Classe che identifica un Job, ovvero il "lavoro" da svolgere su un
 * determinato file selezionato
 * 
 * @author Valerio Valletta
 */

public class Job {
	private File file;
	private boolean mode[] = { false, false, false }; // mode[0] => marge/split | mode[1] => zip | mode[2] => crypt
	private int parts;
	private long dim;
	private String outFileName;
	private String status;
	private boolean completed;

	public Job(File f) {
		file = f;
		parts = 0;
		dim = 0;
		outFileName = "";
		status = "Waiting";
		completed = false;

	}

	/**
	 * Restituisce il file associato al Job
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Assegna il file associato al Job
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * Restituisce un array a cui sono associate le modalita'
	 */
	public boolean getMode(int i) {
		return mode[i];
	}

	/**
	 * Permette di impostare la modalita' desiderata
	 */
	public void setMode(int i) {
		this.mode[i] = true;
	}

	/**
	 * Restituisce lo status del Job
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Imposta lo status del Job
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Restituisce se il Job è completato meno
	 */
	public boolean isCompleted() {
		return completed;
	}

	/**
	 * Imposta se il Job è completato meno
	 */
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	/**
	 * Restituisce in quante parti deve essere diviso il file
	 */
	public int getParts() {
		return parts;
	}

	/**
	 * Imposta in quante parti deve essere diviso il file
	 */
	public void setParts(int parts) {
		this.parts = parts;
	}

	/**
	 * Restituisce la dimensione dei file(parti) da creare
	 */
	public long getDim() {
		return dim;
	}

	/**
	 * Imposta la dimensione dei file(parti) da creare
	 */
	public void setDim(long dim) {
		this.dim = dim;
	}

	/**
	 * Restituisce il nome del file di output(merge)
	 */
	public String getOutFileName() {
		return outFileName;
	}

	/**
	 * Imposta il nome del file di output(merge)
	 */
	public void setOutFileName(String outFileName) {
		this.outFileName = outFileName;
	}

}
