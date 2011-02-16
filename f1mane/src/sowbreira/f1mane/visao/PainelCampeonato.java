package sowbreira.f1mane.visao;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import sowbreira.f1mane.MainFrame;
import sowbreira.f1mane.controles.ControleCampeonato;
import sowbreira.f1mane.controles.ControleJogoLocal;
import sowbreira.f1mane.entidades.Campeonato;
import sowbreira.f1mane.entidades.ConstrutoresPontosCampeonato;
import sowbreira.f1mane.entidades.CorridaCampeonato;
import sowbreira.f1mane.entidades.PilotosPontosCampeonato;
import sowbreira.f1mane.recursos.idiomas.Lang;
import br.nnpe.Logger;

public class PainelCampeonato extends JPanel {

	private Campeonato campeonato;
	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			"EEE, d MMM yyyy HH:mm:ss");
	private MainFrame mainFrame;

	private ControleCampeonato controleCampeonato;

	private JTable corridasTable;

	private TableModel corridasTableModel;
	private JTable pilotosTable;
	private AbstractTableModel pilotosTableModel;
	private JTable contrutoresTable;
	private AbstractTableModel contrutoresTableModel;

	public PainelCampeonato(ControleCampeonato controleCampeonato,
			MainFrame mainFrame) {
		super();
		this.controleCampeonato = controleCampeonato;
		this.campeonato = controleCampeonato.getCampeonato();
		controleCampeonato.geraListaPilotosPontos();
		controleCampeonato.geraListaContrutoresPontos();
		this.mainFrame = mainFrame;
		this.setLayout(new BorderLayout());

		JPanel dadosCampeonato = gerarPanelDadosCampeonato();
		JPanel listJogadoresSelecionados = gerarPanelPilotosSelecionados();
		JPanel criacao = new JPanel(new BorderLayout());
		criacao.add(dadosCampeonato, BorderLayout.WEST);
		criacao.add(listJogadoresSelecionados, BorderLayout.CENTER);

		JPanel corridas = gerarPanelCorridas();

		JPanel ptsPilotos = gerarPanelPilotos();
		JPanel ptsConstrutores = gerarPanelConstrutores();
		JPanel grid = new JPanel(new GridLayout(1, 2));
		grid.add(ptsPilotos);
		grid.add(ptsConstrutores);

		JPanel panelBorder = new JPanel(new BorderLayout());
		panelBorder.add(criacao, BorderLayout.SOUTH);
		panelBorder.add(corridas, BorderLayout.NORTH);
		panelBorder.add(grid, BorderLayout.CENTER);
		this.add(panelBorder, BorderLayout.CENTER);
		JPanel label = new JPanel();
		label.add(new JLabel("Jogar a proxima corrida?") {
			@Override
			public String getText() {
				return Lang.msg("292");
			}
		});
		this.add(label, BorderLayout.SOUTH);
		int ret = JOptionPane.showConfirmDialog(mainFrame, this, Lang
				.msg("286"), JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (ret == JOptionPane.YES_OPTION) {
			try {
				if (campeonato.getCircuitoVez() == null) {
					JOptionPane.showMessageDialog(mainFrame, Lang.msg("293"));
				} else {
					ControleJogoLocal controleJogo = (ControleJogoLocal) mainFrame
							.getControleJogo();
					if (controleJogo != null) {
						controleJogo.matarTodasThreads();
					}
					controleJogo = new ControleJogoLocal("t"
							+ PainelCampeonato.this.campeonato.getTemporada());
					controleJogo.setMainFrame(PainelCampeonato.this.mainFrame);
					mainFrame.setControleJogo(controleJogo);
					controleJogo.iniciarJogo(controleCampeonato);
				}
			} catch (Exception e) {
				Logger.logarExept(e);
			}
		}
	}

	private JPanel gerarPanelConstrutores() {

		contrutoresTable = new JTable();
		contrutoresTableModel = new AbstractTableModel() {

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				if (controleCampeonato.getContrutoresPontos() == null
						|| controleCampeonato.getContrutoresPontos().isEmpty()) {
					return "";
				}
				ConstrutoresPontosCampeonato construtoresPontosCampeonato = (ConstrutoresPontosCampeonato) controleCampeonato
						.getContrutoresPontos().get(rowIndex);
				switch (columnIndex) {
				case 0:
					return construtoresPontosCampeonato.getNomeEquipe();
				case 1:
					return new Integer(construtoresPontosCampeonato.getPontos());
				default:
					return "";
				}

			}

			@Override
			public int getRowCount() {
				if (controleCampeonato.getContrutoresPontos() == null
						|| controleCampeonato.getContrutoresPontos().isEmpty()) {
					return 0;
				}
				return controleCampeonato.getContrutoresPontos().size();
			}

			@Override
			public int getColumnCount() {
				return 2;
			}

			@Override
			public String getColumnName(int columnIndex) {

				switch (columnIndex) {
				case 0:
					/* "Equipe" */
					return Lang.msg("277");
				case 1:
					/* "Pontos" */
					return Lang.msg("161");
				default:
					return "";
				}

			}
		};
		contrutoresTable.setModel(contrutoresTableModel);
		JScrollPane jScrollPane = new JScrollPane(contrutoresTable) {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(300, 150);
			}
		};
		JPanel jPanel = new JPanel();
		jPanel.setBorder(new TitledBorder("Construtores ") {
			@Override
			public String getTitle() {
				return Lang.msg("222");
			}
		});
		jPanel.add(jScrollPane);
		return jPanel;
	}

	private JPanel gerarPanelPilotos() {

		pilotosTable = new JTable() {
		};
		pilotosTableModel = new AbstractTableModel() {

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				if (controleCampeonato.getPilotosPontos() == null
						|| controleCampeonato.getPilotosPontos().isEmpty()) {
					return "";
				}
				PilotosPontosCampeonato pilotosPontosCampeonato = (PilotosPontosCampeonato) controleCampeonato
						.getPilotosPontos().get(rowIndex);
				switch (columnIndex) {
				case 0:
					return pilotosPontosCampeonato.getNome();
				case 1:
					return new Integer(pilotosPontosCampeonato.getPontos());
				case 2:
					return new Integer(pilotosPontosCampeonato.getVitorias());
				default:
					return "";
				}

			}

			@Override
			public int getRowCount() {
				if (controleCampeonato.getPilotosPontos() == null
						|| controleCampeonato.getPilotosPontos().isEmpty()) {
					return 0;
				}

				return controleCampeonato.getPilotosPontos().size();
			}

			@Override
			public int getColumnCount() {
				return 3;
			}

			@Override
			public String getColumnName(int columnIndex) {

				switch (columnIndex) {
				case 0:
					/* Piloto */
					return Lang.msg("153");
				case 1:
					/* "Pontos" */
					return Lang.msg("161");
				case 2:
					/* "Vitorias" */
					return Lang.msg("289");
				default:
					return "";
				}

			}
		};
		pilotosTable.setModel(pilotosTableModel);
		JScrollPane jScrollPane = new JScrollPane(pilotosTable) {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(300, 150);
			}
		};
		JPanel jPanel = new JPanel();
		jPanel.setBorder(new TitledBorder("Pilotos ") {
			@Override
			public String getTitle() {
				return Lang.msg("294");
			}
		});
		jPanel.add(jScrollPane);
		return jPanel;
	}

	private JPanel gerarPanelCorridas() {
		corridasTable = new JTable() {
		};
		corridasTable.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (e.getClickCount() == 2) {
					String corrida = (String) corridasTableModel.getValueAt(
							corridasTable.getSelectedRow(), 0);
					int ret = JOptionPane.showConfirmDialog(corridasTable, Lang
							.msg("300", new String[] { corrida }), Lang
							.msg("299"), JOptionPane.YES_NO_OPTION);
					if (ret == JOptionPane.YES_OPTION) {
						gerarPainelDetalhesCorrida(corrida, corridasTable);
					}
				}
			}

		});

		corridasTableModel = new AbstractTableModel() {

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				String circuito = (String) controleCampeonato.getCampeonato()
						.getCorridas().get(rowIndex);
				List dadosCorridas = (List) controleCampeonato.getCampeonato()
						.getDadosCorridas().get(circuito);
				CorridaCampeonato corridaCampeonato = null;
				if (dadosCorridas != null) {
					for (Iterator iterator = dadosCorridas.iterator(); iterator
							.hasNext();) {
						CorridaCampeonato ccTemp = (CorridaCampeonato) iterator
								.next();
						if (ccTemp.getPosicao() == 1) {
							corridaCampeonato = ccTemp;
						}
					}
				}

				switch (columnIndex) {
				case 0:
					return circuito;
				case 1:
					if (corridaCampeonato == null) {
						return "";
					}
					return dateFormat.format(new Date(corridaCampeonato
							.getTempoInicio()));
				case 2:
					if (corridaCampeonato == null) {
						return "";
					}
					return dateFormat.format(new Date(corridaCampeonato
							.getTempoFim()));
				case 3:
					if (corridaCampeonato == null) {
						return "";
					}
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
					/* Corrida */
					return Lang.msg("290");
				case 1:
					/* Inicio */
					return Lang.msg("155");
				case 2:
					/* Fim */
					return Lang.msg("156");
				case 3:
					/* Vencedor */
					return Lang.msg("291");
				default:
					return "";
				}

			}
		};
		corridasTable.setModel(corridasTableModel);
		JScrollPane jScrollPane = new JScrollPane(corridasTable) {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(600, 150);
			}
		};
		JPanel jPanel = new JPanel();
		jPanel.setBorder(new TitledBorder("Campeonato") {
			@Override
			public String getTitle() {
				return Lang.msg("268");
			}
		});
		jPanel.add(jScrollPane);
		return jPanel;
	}

	protected void gerarPainelDetalhesCorrida(final String corrida, JTable table) {
		JTable detCorridaTable = new JTable();
		AbstractTableModel detCorridaTableModel = new AbstractTableModel() {

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				List dets = (List) campeonato.getDadosCorridas().get(corrida);
				if(dets ==null){
					return "";
				}
				CorridaCampeonato cc = (CorridaCampeonato) dets.get(rowIndex);

				switch (columnIndex) {
				case 0:
					return cc.getPosicao();
				case 1:
					return cc.getPiloto();
				case 2:
					return cc.getCarro();
				case 3:
					return Lang.msg(cc.getTpPneu());
				case 4:
					return cc.getNumVoltas();
				case 5:
					return cc.getVoltaMaisRapida();
				case 6:
					return cc.getQtdeParadasBox();
				case 7:
					return cc.getDesgastePneus();
				case 8:
					return cc.getCombustivelRestante();
				case 9:
					return cc.getDesgasteMotor();
				case 10:
					return cc.getPontos();
				default:
					return "";

				}

			}

			@Override
			public int getRowCount() {
				List dets = (List) campeonato.getDadosCorridas().get(corrida);
				if (dets == null)
					return 0;
				return dets.size();
			}

			@Override
			public int getColumnCount() {
				return 11;
			}

			public String getColumnName(int column) {
				switch (column) {
				// POsi��o
				case 0:
					return Lang.msg("160");
					// Piloto
				case 1:
					return Lang.msg("153");
					// Equipe
				case 2:
					return Lang.msg("277");
					// Tp Pneu
				case 3:
					return Lang.msg("264");
					// Voltas
				case 4:
					return Lang.msg("159");
					// MElhor
				case 5:
					return Lang.msg("278");
				case 6:
					return Lang.msg("146");
				case 7:
					return Lang.msg("216");
				case 8:
					return Lang.msg("279");
				case 9:
					return Lang.msg("217");
				case 10:
					return Lang.msg("161");
				default:
					return "";
				}
			}
		};
		detCorridaTable.setModel(detCorridaTableModel);
		JScrollPane jScrollPane = new JScrollPane(detCorridaTable) {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(650, 350);
			}
		};
		JPanel jPanel = new JPanel();
		jPanel.setBorder(new TitledBorder("Detalhes da Corrida") {
			@Override
			public String getTitle() {
				return Lang.msg("299");
			}
		});
		jPanel.add(jScrollPane);
		JOptionPane.showMessageDialog(corridasTable, jPanel, Lang.msg("300",
				new String[] { corrida }), JOptionPane.INFORMATION_MESSAGE);
	}

	private JPanel gerarPanelPilotosSelecionados() {
		DefaultListModel jogListModel = new DefaultListModel();
		for (Iterator iterator = campeonato.getPilotos().iterator(); iterator
				.hasNext();) {
			String jogador = (String) iterator.next();
			jogListModel.addElement(jogador);

		}
		JList jogadores = new JList(jogListModel);
		jogadores.setEnabled(false);
		jogadores.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		JScrollPane jogPane = new JScrollPane(jogadores) {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(450, 80);
			}
		};
		jogPane.setAlignmentX(LEFT_ALIGNMENT);
		jogPane.setBorder(new TitledBorder("Campeonato") {
			@Override
			public String getTitle() {
				return Lang.msg("295");
			}
		});
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