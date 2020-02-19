import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
//import java.io.RandomAccessFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.swing.JOptionPane;

/**
 * Classe che implementa i metodi(e le varie modalità) per dividere e unire file
 * 
 * @author Valerio Valletta
 *
 */

@SuppressWarnings("serial")
public class SplitFile extends File {

	public SplitFile(String pathname) {
		super(pathname);

	}
	
	/**
	 * Divide il file in parti uguali(numero di parti fornite dall'utente)
	 */

	public void split(int parts) {
		String FilePath = getAbsolutePath();
		long splitlen = length() / parts;
		long leng = 0;
		int count = 0, data;
		try {
			File filename = new File(FilePath);

			InputStream infile = new BufferedInputStream(new FileInputStream(filename));
			data = infile.read();
			while (data != -1) {
				filename = new File(FilePath + ".part" + (count + 1));

				OutputStream outfile = new BufferedOutputStream(new FileOutputStream(filename));
				while (data != -1 && leng <= splitlen) {
					outfile.write(data);
					leng++;
					data = infile.read();
				}
				leng = 0;
				outfile.close();

				count++;
			}
			infile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Divide il file in parti a seconda della dimensione fornita dall'utente
	 *
	 */
	
	public void split(long dimParts[]) {

		for (long part : dimParts) {
			System.out.println(part);
		}

		String FilePath = getAbsolutePath();
		long leng = 0;
		int count = 0, data;
		try {
			File filename = new File(FilePath);

			InputStream infile = new BufferedInputStream(new FileInputStream(filename));
			data = infile.read();
			while (data != -1) {
				filename = new File(FilePath + ".part" + (count + 1));

				OutputStream outfile = new BufferedOutputStream(new FileOutputStream(filename));
				while (data != -1 && leng <= dimParts[count] - 1) {

					outfile.write(data);
					leng++;
					data = infile.read();
				}
				leng = 0;
				outfile.close();

				count++;
			}
			infile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Comprime le parti 
	 */
	
	public void zipSplit(long dimParts[]) {
		String FilePath = getAbsolutePath();
		long leng = 0;
		int count = 0, data;
		try {
			File filename = new File(FilePath);

			InputStream infile = new BufferedInputStream(new FileInputStream(filename));

			data = infile.read();
			while (data != -1) {
				filename = new File(FilePath + ".zip" + ".part" + (count + 1));

				OutputStream outfile = new BufferedOutputStream(new FileOutputStream(filename));
				ZipOutputStream zos = new ZipOutputStream(outfile);
				zos.putNextEntry(new ZipEntry(getName()));
				while (data != -1 && leng <= dimParts[count] - 1) {
					zos.write(data);
					leng++;
					data = infile.read();
				}
				leng = 0;

				zos.close();

				count++;
			}
			infile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void unZipSplit() {
		List<File> filesM = listOfFilesToMerge();
		for (int i = 0; i < filesM.size(); i++) {
			String zipFilePath = filesM.get(i).getAbsolutePath();
			String destDir = zipFilePath.substring(0, zipFilePath.lastIndexOf(File.separator));
			String nPart = zipFilePath.substring(zipFilePath.lastIndexOf('.') + 1);
			String fileName = zipFilePath.substring(0, zipFilePath.lastIndexOf(".zip")) + "." + nPart;
			File dir = new File(destDir);
			// Crea la cartella se non esiste
			if (!dir.exists())
				dir.mkdirs();
			FileInputStream fis;
			byte[] buffer = new byte[1024];
			try {
				fis = new FileInputStream(zipFilePath);
				ZipInputStream zis = new ZipInputStream(fis);
				ZipEntry ze = zis.getNextEntry();
				while (ze != null) {

					File newFile = new File(fileName);

					System.out.println("Unzipping ==> " + newFile.getAbsolutePath());
					// create directories for sub directories in zip
					new File(newFile.getParent()).mkdirs();
					FileOutputStream fos = new FileOutputStream(newFile);
					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
					fos.close();
					// close this ZipEntry
					zis.closeEntry();
					ze = zis.getNextEntry();
				}

				// close last ZipEntry
				zis.closeEntry();
				zis.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Cripta il file e lo divide in parti con dimensione uguale
	 * @param dim 
	 */
	
	public void cryptSplit(long dim) {

		File inputFile = new File(getAbsolutePath());
		SplitFile encryFile = new SplitFile(getAbsolutePath() + ".crypt");

		System.out.println(getAbsolutePath());

		try {
			CryptUtils.encrypt(inputFile, encryFile);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		

		encryFile.split(GuiController.getDimArray(encryFile, dim));
		encryFile.delete();

	}
	
	/**
	 * Da piu parti criptate genera un file in chiaro
	 */

	public void cryptMerge(String fileSelectedPath, int j) {
		List<File> filesM = listOfFilesToMerge();
		for (File x : filesM) {
			System.out.println(x);
		}
		SplitFile cryptFile = new SplitFile(fileSelectedPath.substring(0, fileSelectedPath.lastIndexOf(File.separator))
				+ File.separator + Main.queue.get(j).getOutFileName() + ".crypt");
		SplitFile.mergeFiles(filesM, cryptFile, 1);
		File resFile = new File(cryptFile.getAbsolutePath().replace(".crypt", ""));
		try {
			CryptUtils.decrypt(cryptFile, resFile);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		cryptFile.delete();

	}

	/**
	 * Identifica le parti da fondere restituendo una lista che le contiene
	 */
	
	public List<File> listOfFilesToMerge() {
		String tmpName = getName();// {name}.{number}
		String destFileName = tmpName.substring(0, tmpName.lastIndexOf('.'));
		String abPath = getAbsolutePath();
		File dirr = new File(abPath.substring(0, abPath.lastIndexOf(File.separator)));

		File[] tmpList = dirr.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if (name.indexOf(".part") != -1) { // Se il file ha ".part" nel proprio nome ==> inseriscilo nella lista
					return true;
				}
				return false;
			}
		});
		int indexFile[] = new int[tmpList.length];
		int count = 0;
		for (int i = 0; i < tmpList.length; i++) {
			try {

				if (tmpList[i].getName().substring(0, tmpName.lastIndexOf('.')).equals(destFileName)
						&& !(tmpList[i].getName().equals(destFileName))) {
					indexFile[count] = i;
					count++;
				}

			} catch (Exception e) {
			}
		}

		File[] filesList = new File[count];

		for (int i = 0; i < filesList.length; i++) {
			filesList[i] = tmpList[indexFile[i]];
		}

		Arrays.sort(filesList);// ensuring order 001, 002, ..., 010, ...
		return Arrays.asList(filesList);
	}
	/**
	 * Fonde le parti in un unico  file
	 */
	public static void mergeFiles(List<File> files, File into, int crypt) {
		boolean flag = true;
		if (crypt == 0) {
			flag = false;
		}

		try (FileOutputStream fos = new FileOutputStream(into);
				BufferedOutputStream mergingStream = new BufferedOutputStream(fos)) {
			System.out.print("Merging files => ");
			for (File f : files) {

				if (f.getName().indexOf(".zip") == -1
						&& (f.getName().indexOf(".crypt") == -1 || flag)) { /* Evita di prendere i .zip o .crypt */
					System.out.print(f.getName() + " ");
					Files.copy(f.toPath(), mergingStream);
				}
			}
			mergingStream.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "File not found!");
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Invalid file name!");
			e.printStackTrace();
		}

	}

}