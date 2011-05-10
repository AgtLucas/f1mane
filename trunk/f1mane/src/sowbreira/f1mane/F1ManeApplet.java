package sowbreira.f1mane;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import sowbreira.f1mane.paddock.applet.AppletPaddock;
import sowbreira.f1mane.recursos.idiomas.Lang;
import br.nnpe.Util;

/**
 * @author Paulo Sobreira Criado Em 12:05:02
 */
public class F1ManeApplet extends JApplet {

	private AppletPaddock appletPaddock;

	public void init() {
		MainFrame frame;
		try {
			appletPaddock = new AppletPaddock();
			appletPaddock.init();
			String lang = getParameter("lang");
			if (!Util.isNullOrEmpty(lang)) {
				Lang.mudarIdioma(lang);
			}
			frame = new MainFrame(this, true);
			Component parent = this;
			while (parent.getParent() != null)
				parent = parent.getParent();
			if (parent instanceof Frame) {
				if (!((Frame) parent).isResizable()) {
					((Frame) parent).setResizable(true);
					((Frame) parent).setLayout(new GridLayout());
				}
			}
			frame.iniciar();
		} catch (IOException e) {
			StackTraceElement[] trace = e.getStackTrace();
			StringBuffer retorno = new StringBuffer();
			int size = ((trace.length > 10) ? 10 : trace.length);
			for (int i = 0; i < size; i++)
				retorno.append(trace[i] + "\n");
			JOptionPane.showMessageDialog(this, retorno.toString(), Lang
					.msg("059"), JOptionPane.ERROR_MESSAGE);
		}

	}

	public static void main(String[] args) {
		JFrame frame1 = new JFrame("Frame 1");
		frame1.setSize(400, 500);
		JFrame frame2 = new JFrame("Frame 2");
		frame2.setSize(500, 400);
		frame1.setVisible(true);
		frame2.setVisible(true);
		frame1.toFront();
	}

	public String getVersao() {
		return appletPaddock.getVersao();
	}
}
