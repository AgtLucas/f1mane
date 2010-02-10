package sowbreira.f1mane.controles;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import sowbreira.f1mane.MainFrame;
import sowbreira.f1mane.entidades.Campeonato;
import sowbreira.f1mane.entidades.Piloto;
import sowbreira.f1mane.recursos.CarregadorRecursos;
import sowbreira.f1mane.recursos.idiomas.Lang;
import sowbreira.f1mane.visao.PainelCampeonato;
import br.nnpe.Logger;

public class ControleCampeonato {

	private MainFrame mainFrame;

	private Campeonato campeonato;

	public ControleCampeonato(MainFrame mainFrame) {
		carregarCircuitos();
		this.mainFrame = mainFrame;
		CarregadorRecursos carregadorRecursos = new CarregadorRecursos();
		circuitosPilotos = carregadorRecursos.carregarTemporadasPilotos();
	}

	protected Map circuitos = new HashMap();

	protected Map circuitosPilotos = new HashMap();

	protected void carregarCircuitos() {
		final Properties properties = new Properties();

		try {
			properties.load(CarregadorRecursos
					.recursoComoStream("properties/pistas.properties"));

			Enumeration propName = properties.propertyNames();
			while (propName.hasMoreElements()) {
				final String name = (String) propName.nextElement();
				circuitos.put(name, properties.getProperty(name));

			}
		} catch (IOException e) {
			Logger.logarExept(e);
		}
	}

	public void criarCampeonato() throws Exception {
		final DefaultListModel defaultListModelCircuitos = new DefaultListModel();
		final DefaultListModel defaultListModelCircuitosSelecionados = new DefaultListModel();
		for (Iterator iterator = circuitos.keySet().iterator(); iterator
				.hasNext();) {
			String key = (String) iterator.next();
			defaultListModelCircuitos.addElement(key);
		}

		final JList listCircuitos = new JList(defaultListModelCircuitos);

		final JList listSelecionados = new JList(
				defaultListModelCircuitosSelecionados);
		JPanel panel1st = new JPanel(new BorderLayout());
		JPanel buttonsPanel = new JPanel(new GridLayout(6, 1));
		JButton esq = new JButton("<");
		esq.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (listSelecionados.getSelectedIndex() == -1)
					return;
				defaultListModelCircuitos
						.addElement(defaultListModelCircuitosSelecionados
								.remove(listSelecionados.getSelectedIndex()));
			}

		});
		JButton dir = new JButton(">");
		dir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (listCircuitos.getSelectedIndex() == -1)
					return;
				defaultListModelCircuitosSelecionados
						.addElement(defaultListModelCircuitos
								.remove(listCircuitos.getSelectedIndex()));
			}

		});

		JButton esqAll = new JButton("<<");
		esqAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}

		});
		esqAll.setEnabled(false);
		JButton dirAll = new JButton(">>");
		dirAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}

		});
		dirAll.setEnabled(false);
		buttonsPanel.add(dir);
		buttonsPanel.add(esq);
		buttonsPanel.add(dirAll);
		buttonsPanel.add(esqAll);

		JButton cima = new JButton("Cima");
		cima.setEnabled(false);
		JButton baixo = new JButton("Baixo");
		baixo.setEnabled(false);
		buttonsPanel.add(cima);
		buttonsPanel.add(baixo);

		JPanel sul = new JPanel();
		sul.add(new JLabel() {
			@Override
			public String getText() {
				return Lang.msg("272");
			}
		});
		JComboBox temporadas = new JComboBox(mainFrame.getVectorTemps());
		sul.add(temporadas);

		panel1st.add(buttonsPanel, BorderLayout.CENTER);
		panel1st.add(new JScrollPane(listCircuitos) {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(150, 300);
			}
		}, BorderLayout.WEST);
		panel1st.add(new JScrollPane(listSelecionados) {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(150, 300);
			}
		}, BorderLayout.EAST);

		panel1st.add(sul, BorderLayout.SOUTH);

		final DefaultListModel defaultListModelPilotosSelecionados = new DefaultListModel();
		JList listPilotosSelecionados = new JList(
				defaultListModelPilotosSelecionados);
		final List tempList = new LinkedList();
		temporadas.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				tempList.clear();
				String temporarada = (String) ControleCampeonato.this.mainFrame
						.getTemporadas().get(arg0.getItem());
				tempList.addAll((Collection) circuitosPilotos.get(temporarada));
				Collections.sort(tempList, new Comparator() {

					@Override
					public int compare(Object o1, Object o2) {
						Piloto p1 = (Piloto) o1;
						Piloto p2 = (Piloto) o2;
						return p1.getCarro().getNome().compareTo(
								p2.getCarro().getNome());
					}

				});
				defaultListModelPilotosSelecionados.clear();
				for (Iterator iterator = tempList.iterator(); iterator
						.hasNext();) {
					Piloto piloto = (Piloto) iterator.next();
					defaultListModelPilotosSelecionados.addElement(piloto);
				}

			}
		});
		temporadas.setSelectedIndex(1);
		temporadas.setSelectedIndex(0);
		final JPanel panel2nd = new JPanel(new BorderLayout());

		JPanel grid = new JPanel();

		grid.setLayout(new GridLayout(2, 2));
		grid.add(new JLabel() {

			public String getText() {
				return Lang.msg("110");
			}
		});
		JSpinner spinnerQtdeVoltas = new JSpinner();
		spinnerQtdeVoltas.setValue(new Integer(22));
		grid.add(spinnerQtdeVoltas);
		JComboBox comboBoxNivelCorrida = new JComboBox();
		comboBoxNivelCorrida.addItem(Lang.msg(ControleJogoLocal.NORMAL));
		comboBoxNivelCorrida.addItem(Lang.msg(ControleJogoLocal.FACIL));
		comboBoxNivelCorrida.addItem(Lang.msg(ControleJogoLocal.DIFICIL));
		grid.add(new JLabel() {
			public String getText() {
				return Lang.msg("212");
			}
		});
		grid.add(comboBoxNivelCorrida);

		JScrollPane scrolllistPilotosSelecionados = new JScrollPane(
				listPilotosSelecionados) {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(210, 225);
			}
		};
		scrolllistPilotosSelecionados.setBorder(new TitledBorder(Lang
				.msg("274")));
		panel2nd.add(scrolllistPilotosSelecionados, BorderLayout.CENTER);
		panel2nd.add(grid, BorderLayout.SOUTH);

		JPanel panel3rd = new JPanel();
		// panel3rd.setLayout(new GridLayout(1, 2));
		panel3rd.add(panel1st);
		panel3rd.add(panel2nd);

		JOptionPane.showMessageDialog(mainFrame, panel3rd, Lang.msg("276"),
				JOptionPane.INFORMATION_MESSAGE);

		campeonato = new Campeonato();
		List corridas = new ArrayList();
		for (int i = 0; i < defaultListModelCircuitosSelecionados.getSize(); i++) {
			corridas.add(defaultListModelCircuitosSelecionados.get(i));
		}

		List pilotos = new ArrayList();
		Object[] pilotosSel = listPilotosSelecionados.getSelectedValues();
		for (int i = 0; i < pilotosSel.length; i++) {
			pilotos.add(pilotosSel[i].toString());
		}
		campeonato.setCorridas(corridas);

		campeonato.setPilotos(pilotos);

		campeonato.setTemporada((String) temporadas.getSelectedItem());

		campeonato.setNivel(Lang.key((String) comboBoxNivelCorrida
				.getSelectedItem()));

		campeonato.setQtdeVoltas((Integer) spinnerQtdeVoltas.getValue());

		new PainelCampeonato(campeonato, mainFrame);

	}

	public void continuarCampeonato() {
		try {
			JTextArea xmlArea = new JTextArea(30, 50);
			JScrollPane xmlPane = new JScrollPane(xmlArea);
			xmlPane.setBorder(new TitledBorder(Lang.msg("282")));
			JOptionPane.showMessageDialog(mainFrame, xmlPane, Lang.msg("281"),
					JOptionPane.INFORMATION_MESSAGE);

			ByteArrayInputStream bin = new ByteArrayInputStream(xmlArea
					.getText().getBytes());
			XMLDecoder xmlDecoder = new XMLDecoder(bin);
			campeonato = (Campeonato) xmlDecoder.readObject();
		} catch (Exception e) {
			StackTraceElement[] trace = e.getStackTrace();
			StringBuffer retorno = new StringBuffer();
			int size = ((trace.length > 10) ? 10 : trace.length);
			for (int i = 0; i < size; i++)
				retorno.append(trace[i] + "\n");
			JOptionPane.showMessageDialog(mainFrame, retorno.toString(), Lang
					.msg("283"), JOptionPane.ERROR_MESSAGE);
			Logger.logarExept(e);
		}
		new PainelCampeonato(campeonato, mainFrame);
	}

	public void dadosPersistencia() {
		if (campeonato != null) {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			XMLEncoder encoder = new XMLEncoder(byteArrayOutputStream);
			encoder.writeObject(campeonato);
			encoder.flush();
			JTextArea xmlArea = new JTextArea(30, 50);
			xmlArea.setText(new String(byteArrayOutputStream.toByteArray())
					+ "</java>");
			xmlArea.setEditable(false);
			xmlArea.setSelectionStart(0);
			xmlArea.setSelectionEnd(xmlArea.getCaretPosition());
			JScrollPane xmlPane = new JScrollPane(xmlArea);
			xmlPane.setBorder(new TitledBorder(Lang.msg("280")));
			JOptionPane.showMessageDialog(mainFrame, xmlPane, Lang.msg("281"),
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
