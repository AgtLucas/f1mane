package sowbreira.f1mane.visao;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import br.nnpe.Logger;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default;

import sowbreira.f1mane.MainFrame;
import sowbreira.f1mane.controles.ControleCampeonato;
import sowbreira.f1mane.controles.ControleEstatisticas;
import sowbreira.f1mane.controles.ControleJogoLocal;
import sowbreira.f1mane.entidades.Campeonato;
import sowbreira.f1mane.entidades.CorridaCampeonato;
import sowbreira.f1mane.recursos.idiomas.Lang;

public class PainelCampeonato extends JPanel {

	private Campeonato campeonato;
	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			"EEE, d MMM yyyy HH:mm:ss");
	private MainFrame mainFrame;

	protected ControleJogoLocal controleJogo;
	private ControleCampeonato controleCampeonato;

	private JTable corridasTable;

	private TableModel corridasTableModel;

	public PainelCampeonato(ControleCampeonato controleCampeonato,
			MainFrame mainFrame) {
		super();
		this.controleCampeonato = controleCampeonato;
		this.campeonato = controleCampeonato.getCampeonato();
		this.mainFrame = mainFrame;
		this.setLayout(new BorderLayout());

		JPanel p1 = gerarPanelDadosCampeonato();
		JPanel p2 = gerarPanelPilotos();
		JPanel p3 = gerarPanelCorridas();
		this.add(p1, BorderLayout.CENTER);
		this.add(p2, BorderLayout.SOUTH);
		this.add(p3, BorderLayout.EAST);

		int ret = JOptionPane.showConfirmDialog(mainFrame, this, Lang
				.msg("286"), JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (ret != JOptionPane.NO_OPTION) {
			try {
				controleJogo = new ControleJogoLocal("t"
						+ PainelCampeonato.this.campeonato.getTemporada());
				controleJogo.setMainFrame(PainelCampeonato.this.mainFrame);
				controleJogo.iniciarJogo(controleCampeonato);
			} catch (Exception e) {
				Logger.logarExept(e);
			}
		}
	}

	private JPanel gerarPanelCorridas() {
		corridasTable = new JTable();
		corridasTableModel = new AbstractTableModel() {

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				String circuito = (String) controleCampeonato.getCampeonato()
						.getCorridas().get(rowIndex);
				List dadosCorridas = (List) controleCampeonato.getCampeonato()
						.getDadosCorridas().get(circuito);

				CorridaCampeonato corridaCampeonato = null;
				for (Iterator iterator = dadosCorridas.iterator(); iterator
						.hasNext();) {
					CorridaCampeonato ccTemp = (CorridaCampeonato) iterator
							.next();
					if (ccTemp.getPosicao() == 1) {
						corridaCampeonato = ccTemp;
					}
				}

				switch (columnIndex) {
				case 0:
					return circuito;
				case 1:
					return dateFormat.format(new Date(corridaCampeonato
							.getTempoInicio()));
				case 2:
					return dateFormat.format(new Date(corridaCampeonato
							.getTempoInicio()));
				case 3:
					return corridaCampeonato.getPiloto();

				default:
					return "";
				}

			}

			@Override
			public int getRowCount() {
				return controleCampeonato.getCampeonato().getCorridas().size();
			}

			@Override
			public int getColumnCount() {
				return 4;
			}

			@Override
			public String getColumnName(int columnIndex) {

				switch (columnIndex) {
				case 0:
					return "Corrida";
				case 1:
					return "Inicio";
				case 2:
					return "Fim";
				case 3:
					return "Vencedor";
				default:
					return "";
				}

			}
		};
		corridasTable.setModel(corridasTableModel);
		JScrollPane jScrollPane = new JScrollPane(corridasTable);
		JPanel jPanel = new JPanel();
		jPanel.add(jScrollPane);
		return jPanel;
	}

	private JPanel gerarPanelPilotos() {
		DefaultListModel jogListModel = new DefaultListModel();
		for (Iterator iterator = campeonato.getPilotos().iterator(); iterator
				.hasNext();) {
			String jogador = (String) iterator.next();
			jogListModel.addElement(jogador);

		}
		JList jogadores = new JList(jogListModel);
		jogadores.setEnabled(false);
		JScrollPane jogPane = new JScrollPane(jogadores);
		jogPane.setBorder(new TitledBorder(Lang.msg("")));
		JPanel p2 = new JPanel();
		p2.add(jogPane, BorderLayout.CENTER);
		return p2;
	}

	private JPanel gerarPanelDadosCampeonato() {
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(3, 2));

		p1.add(new JLabel() {
			@Override
			public String getText() {
				return Lang.msg("284");
			}
		});
		p1.add(new JLabel(campeonato.getTemporada()));

		p1.add(new JLabel() {
			public String getText() {
				return Lang.msg("191");
			}
		});
		p1.add(new JLabel(campeonato.getNivel()));

		p1.add(new JLabel() {

			public String getText() {
				return Lang.msg("285");
			}
		});
		p1.add(new JLabel(campeonato.getQtdeVoltas().toString()));
		return p1;
	}
}
