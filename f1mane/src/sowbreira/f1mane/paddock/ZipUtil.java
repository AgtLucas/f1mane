/**
 * 
 */
package sowbreira.f1mane.paddock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author paulo.sobreira
 * 
 */
public class ZipUtil {

	public static Object descompactarObjeto(InputStream in) {
		try {

			ZipInputStream zin = new ZipInputStream(in);
			ZipEntry entry = zin.getNextEntry();
			ByteArrayOutputStream arrayDinamico = new ByteArrayOutputStream();
			int byt = zin.read();

			while (-1 != byt) {
				arrayDinamico.write(byt);
				byt = zin.read();
			}

			arrayDinamico.flush();

			ByteArrayInputStream bin = new ByteArrayInputStream(arrayDinamico
					.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bin);

			zin.close();

			return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ByteArrayOutputStream compactarObjeto(Object aCompact, OutputStream stream) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(aCompact);
			oos.flush();

			ZipOutputStream zipOutputStream = new ZipOutputStream(stream);
			ZipEntry entry = new ZipEntry("root");
			zipOutputStream.putNextEntry(entry);
			zipOutputStream.write(bos.toByteArray());
			zipOutputStream.closeEntry();
			zipOutputStream.close();
			return bos;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
