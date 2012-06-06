package sowbreira.f1mane.visao;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import sowbreira.f1mane.controles.ControleJogoLocal;
import sowbreira.f1mane.controles.InterfaceJogo;
import sowbreira.f1mane.entidades.Campeonato;
import sowbreira.f1mane.entidades.Carro;
import sowbreira.f1mane.entidades.Circuito;
import sowbreira.f1mane.entidades.Clima;
import sowbreira.f1mane.entidades.No;
import sowbreira.f1mane.entidades.Piloto;
import sowbreira.f1mane.entidades.Volta;
import sowbreira.f1mane.paddock.entidades.TOs.TravadaRoda;
import sowbreira.f1mane.recursos.CarregadorRecursos;
import sowbreira.f1mane.recursos.idiomas.Lang;
import br.nnpe.Constantes;
import br.nnpe.Html;
import br.nnpe.Logger;
import br.nnpe.Util;

public class GerenciadorVisual {
	public static final int VDP1 = 1;
	public static final int VDP2 = 2;
	private JPanel painelInfText;
	private JEditorPane infoTextual;
	private ArrayList bufferTextual;
	private JScrollPane scrollPaneTextual;
	private JLabel imgClima;
	private JLabel infoAdicionaLinha1;
	private JComboBox comboBoxClimaInicial;
	private JComboBox comboBoxNivelCorrida;
	private JComboBox comboBoxCircuito;
	private JComboBox boxPilotoSelecionado;
	private JComboBox comboBoxTemporadas;
	private JList listPilotosSelecionados;
	private JComboBox boxPneuInicial;
	private JComboBox comboBoxAsaInicial;
	private JSlider spinnerCombustivel;
	private JSpinner spinnerQtdeVoltas;
	private JSlider sliderTempoCiclo;
	private JSpinner spinnerSkillPadraoPilotos;
	private JSpinner spinnerPotenciaPadraoCarros;
	private JSpinner spinnerQtdeMinutosQualificacao;
	private JSlider spinnerDificuldadeUltrapassagem;
	private PainelCircuito painelCircuito;
	private JScrollPane scrollPane;
	private InterfaceJogo controleJogo;
	private JPanel centerPanel = new JPanel();
	private JLabel infoCorrida;
	private JLabel infoPiloto;
	private JPanel infoText = new JPanel();
	private JTextField nomeJogador;
	private PainelTabelaResultadoFinal resultadoFinal;
	private ThreadMudancaClima clima;
	private int tempoSleep = 30;
	private Color corPadraoBarra;
	private long lastPress;
	private ProgamacaoBox progamacaoBox;
	private long ultimaChamadaBox;
	private List listaPilotosCombo;
	private List listaCarrosCombo;
	protected JCheckBox semTrocaPneu;
	protected JCheckBox semReabastacimento;
	protected JCheckBox kers;
	protected JCheckBox drs;
	private ImageIcon iconLua = new ImageIcon(CarregadorRecursos
			.carregarImagem("clima/lua.gif"));
	private ImageIcon iconSol = new ImageIcon(CarregadorRecursos
			.carregarImagem("clima/sol.gif"));
	private ImageIcon iconNublado = new ImageIcon(CarregadorRecursos
			.carregarImagem("clima/nublado.gif"));
	private ImageIcon iconChuva = new ImageIcon(CarregadorRecursos
			.carregarImagem("clima/chuva.gif"));
	private long ultimaTravavadaRodas;
	private Thread thDesenhaQualificacao;
	private JFrame radioPadock;

	public JComboBox getComboBoxTemporadas() {
		return comboBoxTemporadas;
	}

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public GerenciadorVisual(InterfaceJogo controleJogo) throws IOException {
		this.controleJogo = controleJogo;
		progamacaoBox = new ProgamacaoBox();
		radioPadock = new JFrame();
	}

	public JFrame getRadioPadock() {
		return radioPadock;
	}

	private void disableKeys(InputMap inputMap) {
		String[] keys = { "UP", "DOWN", "LEFT", "RIGHT" };
		for (String key : keys) {
			inputMap.put(KeyStroke.getKeyStroke(key), "none");
		}
	}

	public ArrayList getBufferTextual() {
		return bufferTextual;
	}

	public void iniciarInterfaceGraficaJogo() throws IOException {
		Logger.logar("iniciarInterfaceGraficaJogo()");
		painelCircuito = new PainelCircuito(controleJogo, this);
		scrollPane = new JScrollPane(painelCircuito,
				JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		disableKeys(scrollPane
				.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT));

		carregarInfoClima();
		addiconarListenerComandos();
		gerarPainelInfoText();
		gerarLayout();
		JFrame frame = controleJogo.getMainFrame();

		MouseWheelListener mw = new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				synchronized (PainelCircuito.zoomMutex) {
					double val = painelCircuito.mouseZoom;
					val += e.getWheelRotation() / 60.0;
					if (controleJogo != null
							&& painelCircuito.getBackGround() != null
							&& controleJogo.getCircuito() != null
							&& controleJogo.getCircuito().isUsaBkg()) {
						Rectangle limitesViewPort = (Rectangle) painelCircuito
								.limitesViewPort();
						if ((painelCircuito.getBackGround().getWidth() * val) < limitesViewPort
								.getWidth()) {
							return;
						}
						if ((painelCircuito.getBackGround().getHeight() * val) < limitesViewPort
								.getHeight()) {
							return;
						}

					}
					if (val > 1.0) {
						painelCircuito.mouseZoom = 1.0;
					} else if (val < 0.2) {
						painelCircuito.mouseZoom = 0.2;
					} else {
						painelCircuito.mouseZoom = Util.double2Decimal(val);
					}
				}
			}
		};
		KeyListener keyListener = geraKeyListener();
		frame.addKeyListener(keyListener);
		painelCircuito.addKeyListener(keyListener);
		scrollPane.addKeyListener(keyListener);
		frame.addMouseWheelListener(mw);
		painelCircuito.addMouseWheelListener(mw);
		scrollPane.addMouseWheelListener(mw);
		frame.requestFocus();
	}

	private void gerarLayout() {
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(scrollPane, BorderLayout.CENTER);
		controleJogo.getMainFrame().getContentPane().removeAll();
		controleJogo.getMainFrame().getContentPane().setLayout(
				new BorderLayout());
		controleJogo.getMainFrame().getContentPane().add(centerPanel,
				BorderLayout.CENTER);
		centerPanel.revalidate();

		radioPadock.getContentPane().setLayout(new BorderLayout());
		radioPadock.getContentPane().add(painelInfText, BorderLayout.CENTER);
	}

	public void finalize() throws Throwable {
		tempoSleep = 0;
		if (clima != null) {
			clima.interrupt();
		}
		if (thDesenhaQualificacao != null) {
			thDesenhaQualificacao.interrupt();
		}
		if (painelCircuito != null) {
			painelCircuito.setBackGround(null);
		}
		ControleSom.paraTudo();
		super.finalize();
	}

	public void carregarInfoClima() {
		imgClima = new JLabel();
	}

	private void addiconarListenerComandos() {
		JFrame frame = controleJogo.getMainFrame();
		WindowListener[] listeners = frame.getWindowListeners();
		for (int i = 0; i < listeners.length; i++) {
			frame.removeWindowListener(listeners[i]);
		}
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				if (controleJogo == null) {
					return;
				}
				int ret = JOptionPane.showConfirmDialog(controleJogo
						.getMainFrame(), Lang.msg("095"), Lang.msg("094"),
						JOptionPane.YES_NO_OPTION);
				if (ret == JOptionPane.NO_OPTION) {
					return;
				}
				controleJogo.abandonar();
				super.windowClosing(e);
				if (controleJogo.getMainFrame().isModoApplet()) {
					controleJogo.getMainFrame().setVisible(false);
					controleJogo.matarTodasThreads();
				} else {
					System.exit(0);
				}
			}
		});
	}

	protected void mudarAutoPos() {
		if (controleJogo == null) {
			return;
		}
		controleJogo.mudarAutoPos();

	}

	protected void mudarPos2() {
		if (controleJogo == null) {
			return;
		}
		controleJogo.mudarPos(2);
	}

	protected void mudarPos0() {
		if (controleJogo == null) {
			return;
		}
		controleJogo.mudarPos(0);
	}

	protected void mudarPos1() {
		if (controleJogo == null) {
			return;
		}
		controleJogo.mudarPos(1);
	}

	protected void progamacaoBox() {
		JOptionPane.showMessageDialog(null, progamacaoBox.getPainel());
	}

	protected void mudarModoPilotagem(String modo) {
		if (controleJogo == null) {
			return;
		}
		controleJogo.mudarModoPilotagem(modo);

	}

	private KeyListener geraKeyListener() {
		return new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				long now = System.currentTimeMillis();
				if ((now - lastPress) < 50) {
					return;
				}
				lastPress = now;
				int keyCoode = e.getKeyCode();

				if (keyCoode == KeyEvent.VK_F1) {
					controleJogo.mudarGiroMotor(Carro.GIRO_MIN);
				}
				if (keyCoode == KeyEvent.VK_F2) {
					controleJogo.mudarGiroMotor(Carro.GIRO_NOR);
				}
				if (keyCoode == KeyEvent.VK_F3) {
					controleJogo.mudarGiroMotor(Carro.GIRO_MAX);
				}
				if (keyCoode == KeyEvent.VK_F4) {
					mudarModoAgressivo();
				}
				if (keyCoode == KeyEvent.VK_F11) {
					progamacaoBox();
				}
				if (keyCoode == KeyEvent.VK_F12) {
					if (painelCircuito != null) {
						painelCircuito.mudarModoBox();
					} else {
						mudarModoBox();
					}
				}
				if (keyCoode == KeyEvent.VK_F5) {
					mudarModoPilotagem(Piloto.LENTO);
				}
				if (keyCoode == KeyEvent.VK_F6) {
					mudarModoPilotagem(Piloto.NORMAL);
				}
				if (keyCoode == KeyEvent.VK_F7) {
					mudarModoPilotagem(Piloto.AGRESSIVO);
				}
				if (keyCoode == KeyEvent.VK_ESCAPE) {
					painelCircuito.setDesenhaInfo(!painelCircuito
							.isDesenhaInfo());
				}
				if (keyCoode == KeyEvent.VK_F8) {
					mudarAutoPos();
					controleJogo.selecionaPilotoJogador();
				}
				if (keyCoode == KeyEvent.VK_LEFT) {
					if (controleJogo.getPilotoJogador() == null) {
						return;
					}
					if (controleJogo.getPilotoJogador().getTracado() == 2) {
						mudarPos0();
					} else {
						mudarPos1();
					}
				}
				if (keyCoode == KeyEvent.VK_RIGHT) {
					if (controleJogo.getPilotoJogador() == null) {
						return;
					}
					if (controleJogo.getPilotoJogador().getTracado() == 1) {
						mudarPos0();
					} else {
						mudarPos2();
					}
				}
				if (keyCoode == KeyEvent.VK_F9) {
					mudaPilotoSelecionado();
				}
				if (keyCoode == KeyEvent.VK_F10) {
					ligaDesligaSom();
				}
				if (keyCoode == KeyEvent.VK_K) {
					kers();
				}
				if (keyCoode == KeyEvent.VK_D) {
					drs();
				}
				if (Logger.ativo) {
					if (keyCoode == KeyEvent.VK_EQUALS) {
						if (controleJogo.getPilotoJogador() != null
								&& controleJogo.getPilotoJogador()
										.isJogadorHumano())
							controleJogo.aumentaFatorAcidade();
					}
					if (keyCoode == KeyEvent.VK_MINUS) {
						if (controleJogo.getPilotoJogador() != null
								&& controleJogo.getPilotoJogador()
										.isJogadorHumano())
							controleJogo.diminueFatorAcidade();
					}
				}
			}
		};
	}

	protected void drs() {
		if (controleJogo == null) {
			return;
		}
		boolean drs = controleJogo.mudarModoDRS();
	}

	protected void kers() {
		if (controleJogo == null) {
			return;
		}
		boolean kers = controleJogo.mudarModoKers();
	}

	protected void ligaDesligaSom() {
		ControleSom.ligaDesligaSom();
	}

	protected void mudaPilotoSelecionado() {
		if (controleJogo == null) {
			return;
		}
		controleJogo.mudaPilotoSelecionado();

	}

	protected void mudarModoBox() {
		if (controleJogo == null) {
			return;
		}
		controleJogo.setBoxJogadorHumano(painelCircuito.getTpPneu(),
				painelCircuito.getPorcentCombust(), painelCircuito.getTpAsa());
		modoBox();
	}

	private void modoBox() {
		if ((System.currentTimeMillis() - ultimaChamadaBox) < 1000) {
			return;
		}
		ultimaChamadaBox = System.currentTimeMillis();
	}

	protected void mudarModoAgressivo() {
		if (controleJogo == null) {
			return;
		}
		boolean modo = controleJogo.mudarModoAgressivo();
	}

	private void atualizarImgClima(Clima clima) {
		if (Clima.SOL.equals(clima.getClima())) {
			imgClima.setIcon(iconSol);
			if (controleJogo.getCircuito() != null
					&& controleJogo.getCircuito().isNoite()) {
				imgClima.setIcon(iconLua);
			}
		}
		if (Clima.CHUVA.equals(clima.getClima()))
			imgClima.setIcon(iconChuva);
		if (Clima.NUBLADO.equals(clima.getClima()))
			imgClima.setIcon(iconNublado);

	}

	public void atualizaPainel() {
		if (controleJogo == null) {
			return;
		}

		Piloto pilotoSelecionado = controleJogo.getPilotoSelecionado();
		atualizaInfoAdicional(pilotoSelecionado);
		atualizarImgClima(new Clima(controleJogo.getClima()));
		if (pilotoSelecionado == null) {
			List l = controleJogo.getCircuito().getPistaFull();
			No n = (No) l.get(0);
			painelCircuito.centralizarPonto(n.getPoint());
		} else {
			painelCircuito.centralizarPonto(pilotoSelecionado.getNoAtual()
					.getPoint());
		}
	}

	private void atualizaInfoAdicional(Piloto pilotoSelecionado) {
		Volta voltaCorrida = controleJogo.obterMelhorVolta();
		if (voltaCorrida != null) {
			List piltos = controleJogo.getPilotos();
			Piloto piloto = null;
			for (Iterator iter = piltos.iterator(); iter.hasNext();) {
				Piloto element = (Piloto) iter.next();
				if (element.getId() == voltaCorrida.getPiloto()) {
					piloto = element;
					break;
				}

			}
			String text = Lang.msg("142", new Object[] { voltaCorrida
					.obterTempoVoltaFormatado() })
					+ (piloto != null ? piloto.getNome() + " - "
							+ piloto.getCarro().getNome() : "")
					+ " "
					+ controleJogo.getNumVoltaAtual()
					+ "/"
					+ controleJogo.totalVoltasCorrida() + " ";

			text += Lang.msg("245") + " : " + controleJogo.getNivelCorrida();
			if (controleJogo.isSemReabastacimento()) {
				text += Lang.msg("304");
			}
			if (controleJogo.isSemTrocaPneu()) {
				text += Lang.msg("305");
			}
			if (controleJogo.isSafetyCarNaPista()) {
				text += Lang.msg("145");
			}
			if (controleJogo.isKers()) {
				text += " Kers ";
			}
			if (controleJogo.isDrs()) {
				text += " DRS ";
			}
			infoCorrida.setText(text);
		}

		if (pilotoSelecionado != null) {

			String plider = "";

			String infoBox = pilotoSelecionado.getCarro().getTipoPneu() + " "
					+ pilotoSelecionado.getCarro().getAsa() + Lang.msg("146")
					+ pilotoSelecionado.getQtdeParadasBox() + " "
					+ (pilotoSelecionado.isBox() ? Lang.msg("147") : "");

			if (pilotoSelecionado.getPosicao() == 1) {
				plider = Lang.msg("148");
			} else {
				controleJogo.calculaSegundosParaLider(pilotoSelecionado);
				plider = pilotoSelecionado.getSegundosParaLider();
			}

			String text = Lang.msg("149") + plider;

			if ((pilotoSelecionado.getNumeroVolta() > 0)) {
				if (pilotoSelecionado.getUltimaVolta() != null) {
					text += (Lang.msg("150") + pilotoSelecionado
							.getUltimaVolta().obterTempoVoltaFormatado());
				}

				Volta voltaPiloto = controleJogo
						.obterMelhorVolta(pilotoSelecionado);

				if (voltaPiloto != null) {
					text += (Lang.msg("151") + voltaPiloto
							.obterTempoVoltaFormatado());
				}
			}
			infoPiloto.setText(text + " " + infoBox);
		}
	}

	public JSlider getSpinnerDificuldadeUltrapassagem() {
		return spinnerDificuldadeUltrapassagem;
	}

	public JComboBox getComboBoxCircuito() {
		return comboBoxCircuito;
	}

	public JSlider getSpinnerTempoCiclo() {
		return sliderTempoCiclo;
	}

	public JComboBox getBoxPilotoSelecionado() {
		return boxPilotoSelecionado;
	}

	public JComboBox getBoxPneuInicial() {
		return boxPneuInicial;
	}

	public JComboBox getComboBoxClimaInicial() {
		return comboBoxClimaInicial;
	}

	public JComboBox getComboBoxNivelCorrida() {
		return comboBoxNivelCorrida;
	}

	public InterfaceJogo getControleJogo() {
		return controleJogo;
	}

	public JLabel getImgClima() {
		return imgClima;
	}

	public JLabel getInfoAdicionaLinha1() {
		return infoAdicionaLinha1;
	}

	public JEditorPane getInfoTextual() {
		return infoTextual;
	}

	public JPanel getPainelInfText() {
		return painelInfText;
	}

	public JSpinner getSpinnerSkillPadraoPilotos() {
		return spinnerSkillPadraoPilotos;
	}

	public JSpinner getSpinnerPotenciaPadraoCarros() {
		return spinnerPotenciaPadraoCarros;
	}

	public JSpinner getSpinnerQtdeMinutosQualificacao() {
		return spinnerQtdeMinutosQualificacao;
	}

	public JSlider getSpinnerCombustivelInicial() {
		return spinnerCombustivel;
	}

	public JSpinner getSpinnerQtdeVoltas() {
		return spinnerQtdeVoltas;
	}

	public PainelTabelaResultadoFinal exibirResultadoFinal() {
		PainelTabelaResultadoFinal resultadoFinal = new PainelTabelaResultadoFinal(
				controleJogo.getPilotos(), controleJogo.getMainFrame()
						.isModoApplet());
		this.resultadoFinal = resultadoFinal;
		return resultadoFinal;

	}

	public PainelTabelaResultadoFinal getResultadoFinal() {
		return resultadoFinal;
	}

	public void apagarLuz() {
		painelCircuito.apagarLuz();
		painelCircuito.mouseZoom = 0.8;
		atualizaPainel();
	}

	public JTextField getNomeJogador() {
		return nomeJogador;
	}

	public void informaMudancaClima() {
		clima = new ThreadMudancaClima(painelCircuito);
		clima.start();

	}

	private void gerarPainelInfoText() {
		painelInfText = new JPanel(new BorderLayout());
		infoTextual = new JEditorPane("text/html", "");
		infoTextual.setEditable(false);
		bufferTextual = new ArrayList();
		scrollPaneTextual = new JScrollPane(infoTextual) {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(scrollPaneTextual.getWidth(), 165);
			}
		};
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(scrollPaneTextual, BorderLayout.CENTER);
		painelInfText.add(panel, BorderLayout.CENTER);
		infoText.setLayout(new GridLayout(1, 1));
		infoCorrida = new JLabel(Lang.msg("213"));
		infoPiloto = new JLabel(Lang.msg("214"));
		infoText.add(infoCorrida);
		painelInfText.add(infoText, BorderLayout.NORTH);
	}

	private void gerarPainelJogoSingle(JPanel painelInicio) {
		painelInicio.setLayout(new GridLayout(10, 2, 5, 5));
		JLabel label = new JLabel() {

			public String getText() {
				return Lang.msg("110", new String[] {
						String.valueOf(Constantes.MIN_VOLTAS),
						String.valueOf(Constantes.MAX_VOLTAS) });
			}
		};
		painelInicio.add(label);
		spinnerQtdeVoltas = new JSpinner();
		spinnerQtdeVoltas.setValue(new Integer(12));
		if (!ControleJogoLocal.VALENDO) {
			spinnerQtdeVoltas.setValue(new Integer(30));
		}

		painelInicio.add(spinnerQtdeVoltas);

		painelInicio.add(new JLabel() {

			public String getText() {
				return Lang.msg("111");
			}
		});
		nomeJogador = new JTextField();
		painelInicio.add(nomeJogador);

		boxPilotoSelecionado = new JComboBox();
		boxPilotoSelecionado.addItem(Lang.msg("119"));

		for (Iterator iter = controleJogo.getPilotos().iterator(); iter
				.hasNext();) {
			Piloto piloto = (Piloto) iter.next();
			boxPilotoSelecionado.addItem(piloto);
		}

		painelInicio.add(new JLabel() {
			public String getText() {
				return Lang.msg("120");
			}
		});
		painelInicio.add(boxPilotoSelecionado);
		comboBoxNivelCorrida = new JComboBox();
		comboBoxNivelCorrida.addItem(Lang.msg(ControleJogoLocal.NORMAL));
		comboBoxNivelCorrida.addItem(Lang.msg(ControleJogoLocal.FACIL));
		comboBoxNivelCorrida.addItem(Lang.msg(ControleJogoLocal.DIFICIL));
		painelInicio.add(new JLabel() {
			public String getText() {
				return Lang.msg("212");
			}
		});
		painelInicio.add(comboBoxNivelCorrida);

		comboBoxClimaInicial = new JComboBox();
		comboBoxClimaInicial.addItem(new Clima(Clima.SOL));
		comboBoxClimaInicial.addItem(new Clima(Clima.NUBLADO));
		comboBoxClimaInicial.addItem(new Clima(Clima.CHUVA));
		comboBoxClimaInicial.addItem(new Clima(Clima.ALEATORIO));

		for (int i = 0; i < comboBoxClimaInicial.getItemCount(); i++) {
			Clima clima = (Clima) comboBoxClimaInicial.getItemAt(i);

			if (clima.getClima().equals(controleJogo.getClima())) {
				comboBoxClimaInicial.setSelectedIndex(i);
			}
		}

		painelInicio.add(new JLabel() {
			public String getText() {

				return Lang.msg("123");
			}
		});
		painelInicio.add(comboBoxClimaInicial);

		JLabel tipoPneu = new JLabel(Lang.msg("009"));
		boxPneuInicial = new JComboBox();
		boxPneuInicial.addItem(Lang.msg(Carro.TIPO_PNEU_MOLE));
		boxPneuInicial.addItem(Lang.msg(Carro.TIPO_PNEU_DURO));
		boxPneuInicial.addItem(Lang.msg(Carro.TIPO_PNEU_CHUVA));

		JLabel tipoAsa = new JLabel() {
			@Override
			public String getText() {
				return Lang.msg("010");
			}
		};
		comboBoxAsaInicial = new JComboBox();
		comboBoxAsaInicial.addItem(Lang.msg(Carro.ASA_NORMAL));
		comboBoxAsaInicial.addItem(Lang.msg(Carro.MAIS_ASA));
		comboBoxAsaInicial.addItem(Lang.msg(Carro.MENOS_ASA));

		JLabel qtdeComustivel = new JLabel() {
			public String getText() {
				return Lang.msg("011");
			}
		};
		spinnerCombustivel = new JSlider(0, 100);
		spinnerCombustivel.setValue(new Integer(50));
		spinnerCombustivel.setPaintLabels(true);
		painelInicio.add(tipoPneu);
		painelInicio.add(boxPneuInicial);
		painelInicio.add(tipoAsa);
		painelInicio.add(comboBoxAsaInicial);
		painelInicio.add(qtdeComustivel);
		painelInicio.add(spinnerCombustivel);
		painelInicio.add(new JLabel() {
			public String getText() {
				return Lang.msg("124");
			}
		});
		spinnerDificuldadeUltrapassagem = new JSlider(000, 500);
		spinnerDificuldadeUltrapassagem.setValue(new Integer(Util.intervalo(
				000, 500)));
		Hashtable labelTable = new Hashtable();
		labelTable.put(new Integer(000), new JLabel("") {
			@Override
			public String getText() {
				return Lang.msg("FACIL");
			}
		});
		labelTable.put(new Integer(500), new JLabel("") {
			@Override
			public String getText() {
				return Lang.msg("DIFICIL");
			}
		});
		spinnerDificuldadeUltrapassagem.setLabelTable(labelTable);
		spinnerDificuldadeUltrapassagem.setPaintLabels(true);
		painelInicio.add(spinnerDificuldadeUltrapassagem);
		painelInicio.add(new JLabel() {
			@Override
			public String getText() {
				return Lang.msg("126");
			}
		});
		sliderTempoCiclo = new JSlider(Constantes.MIN_CICLO,
				Constantes.MAX_CICLO);
		sliderTempoCiclo.setValue(new Integer(Util.intervalo(
				Constantes.MIN_CICLO, Constantes.MAX_CICLO)));
		labelTable = new Hashtable();
		labelTable.put(new Integer(Constantes.MIN_CICLO), new JLabel("") {
			@Override
			public String getText() {
				return Lang.msg("RAPIDOS");
			}
		});
		labelTable.put(new Integer(Constantes.MAX_CICLO), new JLabel("") {
			@Override
			public String getText() {
				return Lang.msg("LENTOS");
			}
		});
		sliderTempoCiclo.setLabelTable(labelTable);
		sliderTempoCiclo.setPaintLabels(true);
		painelInicio.add(sliderTempoCiclo);
		spinnerSkillPadraoPilotos = new JSpinner();
		spinnerSkillPadraoPilotos.setValue(new Integer(0));
		spinnerPotenciaPadraoCarros = new JSpinner();
		spinnerPotenciaPadraoCarros.setValue(new Integer(0));

	}

	public List getListaPilotosCombo() {
		return listaPilotosCombo;
	}

	public List getListaCarrosCombo() {
		return listaCarrosCombo;
	}

	private void gerarPainelJogoMulti(JPanel incialPanel) {
		final CarregadorRecursos carregadorRecursos = new CarregadorRecursos(
				true);
		final Map circuitosPilotos = carregadorRecursos
				.carregarTemporadasPilotos();
		comboBoxTemporadas = new JComboBox(carregadorRecursos.getVectorTemps());

		final DefaultListModel defaultListModelPilotosSelecionados = new DefaultListModel();
		listPilotosSelecionados = new JList(defaultListModelPilotosSelecionados);
		listaPilotosCombo = new ArrayList();
		comboBoxTemporadas.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				listaPilotosCombo.clear();
				String temporarada = (String) carregadorRecursos
						.getTemporadas().get(arg0.getItem());
				listaPilotosCombo.addAll((Collection) circuitosPilotos
						.get(temporarada));
				Collections.sort(listaPilotosCombo, new Comparator() {

					@Override
					public int compare(Object o1, Object o2) {
						Piloto p1 = (Piloto) o1;
						Piloto p2 = (Piloto) o2;
						return p1.getCarro().getNome().compareTo(
								p2.getCarro().getNome());
					}

				});
				defaultListModelPilotosSelecionados.clear();
				for (Iterator iterator = listaPilotosCombo.iterator(); iterator
						.hasNext();) {
					Piloto piloto = (Piloto) iterator.next();
					defaultListModelPilotosSelecionados.addElement(piloto);
				}
				try {
					listaCarrosCombo = carregadorRecursos
							.carregarListaCarros(temporarada);
				} catch (IOException e) {
					Logger.logarExept(e);
				}

			}
		});
		comboBoxTemporadas.setSelectedIndex(1);
		comboBoxTemporadas.setSelectedIndex(0);

		final JPanel pilotoPanel = new JPanel(new BorderLayout());

		pilotoPanel.setBorder(new TitledBorder(Lang.msg("274")));

		pilotoPanel.add(comboBoxTemporadas, BorderLayout.NORTH);
		pilotoPanel.add(new JScrollPane(listPilotosSelecionados) {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(210, 270);
			}
		}, BorderLayout.CENTER);

		JPanel grid = new JPanel();
		grid.setLayout(new GridLayout(7, 2, 5, 5));
		JLabel label = new JLabel() {

			public String getText() {
				return Lang.msg("110", new String[] {
						String.valueOf(Constantes.MIN_VOLTAS),
						String.valueOf(Constantes.MAX_VOLTAS) });
			}
		};
		grid.add(label);
		spinnerQtdeVoltas = new JSpinner();
		spinnerQtdeVoltas.setValue(new Integer(12));
		if (!ControleJogoLocal.VALENDO) {
			spinnerQtdeVoltas.setValue(new Integer(30));
		}

		grid.add(spinnerQtdeVoltas);

		boxPilotoSelecionado = new JComboBox();
		boxPilotoSelecionado.addItem(Lang.msg("119"));

		comboBoxNivelCorrida = new JComboBox();
		comboBoxNivelCorrida.addItem(Lang.msg(ControleJogoLocal.NORMAL));
		comboBoxNivelCorrida.addItem(Lang.msg(ControleJogoLocal.FACIL));
		comboBoxNivelCorrida.addItem(Lang.msg(ControleJogoLocal.DIFICIL));
		grid.add(new JLabel() {
			public String getText() {
				return Lang.msg("212");
			}
		});
		grid.add(comboBoxNivelCorrida);

		comboBoxClimaInicial = new JComboBox();
		comboBoxClimaInicial.addItem(new Clima(Clima.SOL));
		comboBoxClimaInicial.addItem(new Clima(Clima.NUBLADO));
		comboBoxClimaInicial.addItem(new Clima(Clima.CHUVA));
		comboBoxClimaInicial.addItem(new Clima(Clima.ALEATORIO));

		for (int i = 0; i < comboBoxClimaInicial.getItemCount(); i++) {
			Clima clima = (Clima) comboBoxClimaInicial.getItemAt(i);

			if (clima.getClima().equals(controleJogo.getClima())) {
				comboBoxClimaInicial.setSelectedIndex(i);
			}
		}

		grid.add(new JLabel() {
			public String getText() {

				return Lang.msg("123");
			}
		});
		grid.add(comboBoxClimaInicial);

		grid.add(new JLabel() {
			public String getText() {
				return Lang.msg("124");
			}
		});
		spinnerDificuldadeUltrapassagem = new JSlider(000, 500);
		spinnerDificuldadeUltrapassagem.setValue(new Integer(Util.intervalo(
				000, 500)));
		Hashtable labelTable = new Hashtable();
		labelTable.put(new Integer(000), new JLabel("") {
			@Override
			public String getText() {
				return Lang.msg("FACIL");
			}
		});
		labelTable.put(new Integer(500), new JLabel("") {
			@Override
			public String getText() {
				return Lang.msg("DIFICIL");
			}
		});
		spinnerDificuldadeUltrapassagem.setLabelTable(labelTable);
		spinnerDificuldadeUltrapassagem.setPaintLabels(true);
		grid.add(spinnerDificuldadeUltrapassagem);
		grid.add(new JLabel() {
			@Override
			public String getText() {
				return Lang.msg("126");
			}
		});
		sliderTempoCiclo = new JSlider(Constantes.MIN_CICLO,
				Constantes.MAX_CICLO);
		sliderTempoCiclo.setValue(new Integer(Util.intervalo(
				Constantes.MIN_CICLO, Constantes.MAX_CICLO)));
		labelTable = new Hashtable();
		labelTable.put(new Integer(Constantes.MIN_CICLO), new JLabel("") {
			@Override
			public String getText() {
				return Lang.msg("RAPIDOS");
			}
		});
		labelTable.put(new Integer(Constantes.MAX_CICLO), new JLabel("") {
			@Override
			public String getText() {
				return Lang.msg("LENTOS");
			}
		});
		sliderTempoCiclo.setLabelTable(labelTable);
		sliderTempoCiclo.setPaintLabels(true);
		grid.add(sliderTempoCiclo);

		spinnerSkillPadraoPilotos = new JSpinner();
		spinnerSkillPadraoPilotos.setValue(new Integer(0));
		spinnerPotenciaPadraoCarros = new JSpinner();
		spinnerPotenciaPadraoCarros.setValue(new Integer(0));

		JPanel p1 = new JPanel(new GridLayout(1, 2));

		p1.add(new JLabel() {
			@Override
			public String getText() {
				return Lang.msg("302");
			}
		});
		semReabastacimento = new JCheckBox();
		p1.add(semReabastacimento);
		grid.add(p1);

		JPanel p2 = new JPanel(new GridLayout(1, 2));

		p2.add(new JLabel() {
			@Override
			public String getText() {
				return Lang.msg("303");
			}
		});
		semTrocaPneu = new JCheckBox();
		p2.add(semTrocaPneu);
		grid.add(p2);

		JPanel p3 = new JPanel(new GridLayout(1, 2));
		p3.add(new JLabel() {
			@Override
			public String getText() {
				return Lang.msg("kers");
			}
		});
		kers = new JCheckBox();
		p3.add(kers);
		grid.add(p3);

		JPanel p4 = new JPanel(new GridLayout(1, 2));
		p4.add(new JLabel() {
			@Override
			public String getText() {
				return Lang.msg("drs");
			}
		});
		drs = new JCheckBox();
		p4.add(drs);
		grid.add(p4);

		grid.setBorder(new TitledBorder(Lang.msg("273")));

		incialPanel.add(grid, BorderLayout.CENTER);
		incialPanel.add(gerarSeletorCircuito(), BorderLayout.NORTH);
		incialPanel.add(pilotoPanel, BorderLayout.EAST);

	}

	private Component gerarSeletorCircuito() {
		JPanel grid = new JPanel();
		comboBoxCircuito = new JComboBox();
		List circuitosList = new ArrayList();
		for (Iterator iter = controleJogo.getCircuitos().keySet().iterator(); iter
				.hasNext();) {
			String key = (String) iter.next();
			circuitosList.add(key);
		}
		Collections.shuffle(circuitosList);
		for (Iterator iterator = circuitosList.iterator(); iterator.hasNext();) {
			String object = (String) iterator.next();
			comboBoxCircuito.addItem(object);
		}
		grid.add(new JLabel() {
			public String getText() {
				return Lang.msg("121");
			}
		});
		grid.add(comboBoxCircuito);
		final JLabel circuitosLabel = new JLabel() {
			public Dimension getPreferredSize() {
				return new Dimension(400, 200);
			}
		};
		comboBoxCircuito.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					desenhaMiniCircuito(circuitosLabel);
				}
			}
		});

		JPanel retPanel = new JPanel(new BorderLayout());
		retPanel.add(grid, BorderLayout.WEST);
		retPanel.add(circuitosLabel, BorderLayout.CENTER);
		desenhaMiniCircuito(circuitosLabel);
		return retPanel;
	}

	protected void desenhaMiniCircuito(JLabel circuitosLabel) {
		BufferedImage bufferedImage = new BufferedImage(400, 200,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();

		setarHints(g2d);
		g2d.setStroke(new BasicStroke(3.0f));
		g2d.setColor(Color.BLACK);
		String circuitoStr = (String) controleJogo.getCircuitos().get(
				comboBoxCircuito.getSelectedItem());
		CarregadorRecursos carregadorRecursos = new CarregadorRecursos(false);
		ObjectInputStream ois;
		Circuito circuito = null;
		try {
			ois = new ObjectInputStream(carregadorRecursos.getClass()
					.getResourceAsStream(circuitoStr));
			circuito = (Circuito) ois.readObject();
			circuito.vetorizarPista();
		} catch (Exception e) {
			e.printStackTrace();
		}

		List pista = circuito.getPista();
		ArrayList pistaMinimizada = new ArrayList();
		double doubleMulti = 25;
		Map map = new HashMap();
		for (Iterator iterator = pista.iterator(); iterator.hasNext();) {
			No no = (No) iterator.next();
			Point p = new Point(no.getX(), no.getY());
			p.x /= doubleMulti;
			p.y /= doubleMulti;
			if (!pistaMinimizada.contains(p)) {
				map.put(p, no);
				pistaMinimizada.add(p);
			}

		}
		Point o = new Point(10, 10);
		Point oldP = null;
		for (Iterator iterator = pistaMinimizada.iterator(); iterator.hasNext();) {
			Point p = (Point) iterator.next();
			if (oldP != null) {
				No no = (No) map.get(oldP);
				if (no.verificaCruvaBaixa()) {
					g2d.setColor(Color.red);
				} else if (no.verificaCruvaAlta()) {
					g2d.setColor(Color.orange);
				} else if (no.verificaRetaOuLargada()) {
					g2d.setColor(new Color(0, 200, 0));
				}
				g2d.drawLine(o.x + oldP.x, o.y + oldP.y, o.x + p.x, o.y + p.y);
			}
			oldP = p;
		}
		Point p0 = (Point) pistaMinimizada.get(0);
		g2d.drawLine(o.x + oldP.x, o.y + oldP.y, o.x + p0.x, o.y + p0.y);

		ArrayList boxMinimizado = new ArrayList();
		List box = circuito.getBox();
		for (Iterator iterator = box.iterator(); iterator.hasNext();) {
			No no = (No) iterator.next();
			Point p = new Point(no.getX(), no.getY());
			p.x /= doubleMulti;
			p.y /= doubleMulti;
			if (!boxMinimizado.contains(p))
				boxMinimizado.add(p);
		}
		g2d.setStroke(new BasicStroke(2.0f));
		oldP = null;
		g2d.setColor(Color.lightGray);
		for (Iterator iterator = boxMinimizado.iterator(); iterator.hasNext();) {
			Point p = (Point) iterator.next();
			if (oldP != null) {
				g2d.drawLine(o.x + oldP.x, o.y + oldP.y, o.x + p.x, o.y + p.y);
			}
			oldP = p;
		}

		circuitosLabel.setIcon(new ImageIcon(bufferedImage));

	}

	private void setarHints(Graphics2D g2d) {
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_DITHERING,
				RenderingHints.VALUE_DITHER_ENABLE);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	}

	public JCheckBox getSemTrocaPneu() {
		return semTrocaPneu;
	}

	public JCheckBox getSemReabastacimento() {
		return semReabastacimento;
	}

	public JCheckBox getKers() {
		return kers;
	}

	public JCheckBox getDrs() {
		return drs;
	}

	public boolean iniciarJogoSingle() {
		JPanel painelInicio = new JPanel();
		gerarPainelJogoSingle(painelInicio);
		spinnerQtdeVoltas.setValue(new Integer(12));
		int ret = JOptionPane.showConfirmDialog(controleJogo.getMainFrame(),
				painelInicio, Lang.msg("127"), JOptionPane.YES_NO_OPTION);
		if (ret != JOptionPane.YES_OPTION) {
			return false;
		}

		Integer qtdeVoltas = (Integer) spinnerQtdeVoltas.getValue();
		if (qtdeVoltas < 12) {
			spinnerQtdeVoltas.setValue(new Integer(12));
		}
		Integer combustivelInicial = (Integer) spinnerCombustivel.getValue();
		if (combustivelInicial <= 0) {
			if (semReabastacimento.isSelected()) {
				spinnerCombustivel.setValue(new Integer(1));
			} else {
				spinnerCombustivel.setValue(new Integer(20));
			}

		}

		Object selec = listPilotosSelecionados.getSelectedValue();
		controleJogo.setTemporada("t" + comboBoxTemporadas.getSelectedItem());
		if (!ControleJogoLocal.VALENDO) {
			selec = "";
		}

		if (selec instanceof Piloto) {
			controleJogo
					.efetuarSelecaoPilotoJogador(selec, Lang.key(boxPneuInicial
							.getSelectedItem().toString()), spinnerCombustivel
							.getValue(), nomeJogador.getText(), Lang
							.key((String) comboBoxAsaInicial.getSelectedItem()));
		}
		return true;
	}

	public boolean iniciarJogoMulti(Campeonato campeonato) {
		JPanel painelInicio = new JPanel(new BorderLayout());
		gerarPainelJogoMulti(painelInicio);
		spinnerQtdeVoltas.setValue(new Integer(12));
		if (campeonato != null) {

			semReabastacimento.setSelected(campeonato.isSemReabasteciemnto());
			semReabastacimento.setEnabled(false);

			semTrocaPneu.setSelected(campeonato.isSemTrocaPneus());
			semTrocaPneu.setEnabled(false);

			kers.setSelected(campeonato.isKers());
			kers.setEnabled(false);

			drs.setSelected(campeonato.isDrs());
			drs.setEnabled(false);

			comboBoxTemporadas.setSelectedItem(campeonato.getTemporada());
			comboBoxTemporadas.setEnabled(false);

			spinnerQtdeVoltas.setValue(campeonato.getQtdeVoltas());
			spinnerQtdeVoltas.setEnabled(false);
			comboBoxNivelCorrida.setSelectedItem(Lang
					.msg(campeonato.getNivel()));
			comboBoxNivelCorrida.setEnabled(false);
			List indices = new ArrayList();
			DefaultListModel defaultListModel = (DefaultListModel) listPilotosSelecionados
					.getModel();

			ArrayList mudouCarro = new ArrayList();
			for (int i = 0; i < defaultListModel.getSize(); i++) {
				Piloto piloto = (Piloto) defaultListModel.get(i);
				if (Util.isNullOrEmpty(campeonato.getNomePiloto())) {
					if (campeonato.getPilotos().contains(piloto.toString())) {
						indices.add(new Integer(i));
					}
				} else {
					String carro = campeonato.getPilotosEquipesCampeonato()
							.get(piloto.getNome());
					if (Util.isNullOrEmpty(carro)) {
						piloto.setNome(campeonato.getNomePiloto());
						piloto.setHabilidade(campeonato.getPtsPiloto());
						piloto.setJogadorHumano(true);
						mudouCarro.add(piloto);
					} else if (!carro.equals(piloto.getCarro().getNome())) {
						mudouCarro.add(piloto);
					}
				}
			}
			if (!Util.isNullOrEmpty(campeonato.getNomePiloto())) {
				for (Iterator iterator = mudouCarro.iterator(); iterator
						.hasNext();) {
					Piloto piloto = (Piloto) iterator.next();
					String carro = campeonato.getPilotosEquipesCampeonato()
							.get(piloto.getNome());
					for (int i = 0; i < defaultListModel.getSize(); i++) {
						Piloto p = (Piloto) defaultListModel.get(i);
						if (p.getCarro().getNome().equals(carro)) {
							piloto.setCarro(CarregadorRecursos.criarCopiaCarro(
									p.getCarro(), piloto));
						}
					}
				}
				for (int i = 0; i < defaultListModel.getSize(); i++) {
					Piloto piloto = (Piloto) defaultListModel.get(i);
					if (piloto.isJogadorHumano()) {
						indices.add(new Integer(i));
					}
				}
				controleJogo.verificaDesafioCampeonatoPiloto();
			}
			int[] inds = new int[indices.size()];
			for (int i = 0; i < inds.length; i++) {
				inds[i] = ((Integer) indices.get(i)).intValue();
			}
			listPilotosSelecionados.setSelectedIndices(inds);
			listPilotosSelecionados.setEnabled(false);

			comboBoxCircuito.setSelectedItem(campeonato.getCircuitoVez());
			comboBoxCircuito.setEnabled(false);
			spinnerSkillPadraoPilotos.setEnabled(false);
			spinnerPotenciaPadraoCarros.setEnabled(false);
			spinnerDificuldadeUltrapassagem.setEnabled(false);
			int val = 1 + (int) (Math.random() * 3);

			Clima climaTmp = null;
			switch (val) {
			case 1:
				climaTmp = new Clima(Clima.SOL);

				break;

			case 2:
				climaTmp = new Clima(Clima.NUBLADO);

				break;

			case 3:
				climaTmp = new Clima(Clima.CHUVA);

				break;

			default:
				break;
			}
			comboBoxClimaInicial.setSelectedItem(climaTmp);
			comboBoxClimaInicial.setEnabled(false);

		}
		int ret = JOptionPane.showConfirmDialog(controleJogo.getMainFrame(),
				painelInicio, Lang.msg("127"), JOptionPane.YES_NO_OPTION);
		if (ret != JOptionPane.YES_OPTION) {
			return false;
		}
		Object[] selec = listPilotosSelecionados.getSelectedValues();

		for (int i = 0; i < selec.length; i++) {
			JPanel painelJogSel = new JPanel(new GridLayout(5, 2));
			painelJogSel.add(new JLabel() {
				@Override
				public String getText() {
					return Lang.msg("123");
				}
			});
			Clima clima = (Clima) comboBoxClimaInicial.getSelectedItem();
			ImageIcon icon = new ImageIcon(CarregadorRecursos
					.carregarImagem("clima/" + clima.getClima()));
			painelJogSel.add(new JLabel(icon));

			JTextField nomeJogador = new JTextField();
			painelJogSel.add(new JLabel() {
				@Override
				public String getText() {
					return Lang.msg("111");
				}
			});
			painelJogSel.add(nomeJogador);
			JLabel tipoPneu = new JLabel(Lang.msg("009"));
			boxPneuInicial = new JComboBox();
			boxPneuInicial.addItem(Lang.msg(Carro.TIPO_PNEU_MOLE));
			boxPneuInicial.addItem(Lang.msg(Carro.TIPO_PNEU_DURO));
			boxPneuInicial.addItem(Lang.msg(Carro.TIPO_PNEU_CHUVA));

			JLabel tipoAsa = new JLabel() {
				@Override
				public String getText() {
					return Lang.msg("010");
				}
			};
			comboBoxAsaInicial = new JComboBox();
			comboBoxAsaInicial.addItem(Lang.msg(Carro.ASA_NORMAL));
			comboBoxAsaInicial.addItem(Lang.msg(Carro.MAIS_ASA));
			comboBoxAsaInicial.addItem(Lang.msg(Carro.MENOS_ASA));

			JLabel qtdeComustivel = new JLabel() {
				public String getText() {
					return Lang.msg("011");
				}
			};
			spinnerCombustivel = new JSlider(0, 100);
			spinnerCombustivel.setPaintLabels(true);
			Hashtable labelTable = new Hashtable();
			labelTable.put(new Integer(000), new JLabel("") {
				@Override
				public String getText() {
					return Lang.msg("MENOS");
				}
			});
			labelTable.put(new Integer(100), new JLabel("") {
				@Override
				public String getText() {
					return Lang.msg("MAIS");
				}
			});
			spinnerCombustivel.setLabelTable(labelTable);
			spinnerCombustivel.setPaintLabels(true);

			spinnerCombustivel.setValue(new Integer(50));
			painelJogSel.add(tipoPneu);
			painelJogSel.add(boxPneuInicial);
			painelJogSel.add(tipoAsa);
			painelJogSel.add(comboBoxAsaInicial);
			painelJogSel.add(qtdeComustivel);
			painelJogSel.add(spinnerCombustivel);

			JOptionPane.showMessageDialog(controleJogo.getMainFrame(),
					painelJogSel, Lang.msg("275", new String[] { selec[i]
							.toString() }), JOptionPane.QUESTION_MESSAGE);
			controleJogo
					.efetuarSelecaoPilotoJogador(selec[i], Lang
							.key(boxPneuInicial.getSelectedItem().toString()),
							spinnerCombustivel.getValue(), nomeJogador
									.getText(), Lang
									.key((String) comboBoxAsaInicial
											.getSelectedItem()));

		}
		return true;
	}

	private void gerarPainelJogo(JPanel painelInicio) {
		painelInicio.setLayout(new GridLayout(13, 2));
		painelInicio.add(new JLabel("Quantidade de Minutos Qualificação :"));
		spinnerQtdeMinutosQualificacao = new JSpinner();
		spinnerQtdeMinutosQualificacao.setValue(new Integer(5));
		painelInicio.add(spinnerQtdeMinutosQualificacao);
	}

	public boolean iniciarJogo() {
		JPanel painelInicio = new JPanel();
		painelInicio.setBorder(new TitledBorder(
				"Modo Completo com Qualificação:"));
		gerarPainelJogoSingle(painelInicio);
		gerarPainelJogo(painelInicio);
		int ret = JOptionPane.showConfirmDialog(controleJogo.getMainFrame(),
				painelInicio, Lang.msg("127"), JOptionPane.YES_NO_OPTION);
		if (ret != JOptionPane.YES_OPTION) {
			return false;
		}

		Integer qtdeVoltas = (Integer) spinnerQtdeVoltas.getValue();
		if (qtdeVoltas < 12) {
			spinnerQtdeVoltas.setValue(new Integer(12));
		}
		Integer combustivelInicial = (Integer) spinnerCombustivel.getValue();
		if (combustivelInicial <= 0) {
			if (semReabastacimento.isSelected()) {
				spinnerCombustivel.setValue(new Integer(1));
			} else {
				spinnerCombustivel.setValue(new Integer(20));
			}

		}

		Object selec = boxPilotoSelecionado.getSelectedItem();

		if (!ControleJogoLocal.VALENDO) {
			selec = "";
		}

		if (selec instanceof Piloto) {
			controleJogo.efetuarSelecaoPilotoJogador(selec, Lang
					.key(boxPneuInicial.getSelectedItem().toString()),
					spinnerCombustivel.getValue(), nomeJogador.getText(), Lang
							.key(comboBoxAsaInicial.getSelectedItem()
									.toString()));
		}

		return true;
	}

	public void desenhaQualificacao() {
		No n = (No) controleJogo.getCircuito().getPistaFull().get(0);
		try {
			painelCircuito.centralizarPontoDireto(n.getPoint());
		} catch (Exception e) {
			Logger.logarExept(e);
		}
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				No n = (No) controleJogo.getCircuito().getPistaFull().get(0);
				try {
					painelCircuito.centralizarPontoDireto(n.getPoint());
				} catch (Exception e) {
					Logger.logarExept(e);
				}
				infoCorrida.setText(Lang.msg("213"));
				infoPiloto.setText(Lang.msg("214"));
				List pilotos = controleJogo.getPilotos();
				List ptosPilotos = new ArrayList();
				painelCircuito.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						tempoSleep = 0;
					}

				});
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					Logger.logarExept(e);
				}
				Rectangle limitesViewPort = null;

				limitesViewPort = (Rectangle) painelCircuito.limitesViewPort();

				int iniY1 = 30;
				int iniY2 = 40;
				int midPainel = 0;
				if (limitesViewPort != null)
					midPainel = (limitesViewPort.width / 2);
				else {
					midPainel = 500;
					Logger
							.logarExept(new Exception(
									"limitesViewPort == null "));
				}
				for (int i = 0; i < pilotos.size(); i++) {
					Piloto piloto = (Piloto) pilotos.get(i);
					if (piloto.getPosicao() % 2 == 0) {
						ptosPilotos.add(new Point(midPainel + 30, iniY2));
						iniY2 += 40;
					} else {
						ptosPilotos.add(new Point(midPainel - 120, iniY1));
						iniY1 += 40;
					}
				}
				Logger.logar("Iniciar Loop desenha Qualy");
				for (int i = 0; i < pilotos.size(); i++) {
					Piloto piloto = (Piloto) pilotos.get(i);
					Point point = (Point) ptosPilotos.get(i);
					int x = limitesViewPort.x + limitesViewPort.width;
					while (x > (point.x + limitesViewPort.x)) {
						Point pd = new Point(x, point.y + limitesViewPort.y);
						painelCircuito.definirDesenhoQualificacao(piloto, pd);
						if (tempoSleep != 0) {
							try {
								painelCircuito.repaint();
							} catch (Exception e) {
								Logger.logarExept(e);
							}
						} else {
							break;
						}
						x -= 3;
						try {
							Thread.sleep(tempoSleep);
						} catch (InterruptedException e) {
							Logger.logarExept(e);
						}
					}
					painelCircuito.getMapDesenharQualificacao().put(piloto,
							point);
				}
				try {
					painelCircuito.repaint();
				} catch (Exception e) {
					Logger.logarExept(e);
				}

				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					Logger.logarExept(e);
				}

				painelCircuito.setDesenhouQualificacao(true);
				Logger.logar("DesenhouQualificacao");
			}
		};
		if (thDesenhaQualificacao != null) {
			thDesenhaQualificacao.interrupt();
		}
		thDesenhaQualificacao = new Thread(runnable);
		thDesenhaQualificacao.run();
	}

	public void adicionarInfoDireto(String string) {
		if (string == null) {
			return;
		}
		try {
			synchronized (bufferTextual) {
				if (string != null && !string.startsWith("<table>"))
					string = Html.cinza(Lang.msg("082")
							+ controleJogo.getNumVoltaAtual() + " ")
							+ string + "<br>";
				if (bufferTextual.size() > 6) {
					boolean contains = false;
					for (int i = bufferTextual.size() - 1; i < bufferTextual
							.size() - 5; i--) {
						if (string.equals(bufferTextual.get(i))) {
							contains = true;
						}
					}
					if (contains) {
						return;
					}
				}

				bufferTextual.add(string);

				StringBuffer buffer = new StringBuffer();
				for (int i = bufferTextual.size() - 1; i >= 0; i--) {
					String texto = Html.sansSerif(bufferTextual.get(i)
							.toString());
					buffer.append(texto);
				}
				final StringReader reader = new StringReader(buffer.toString());

				Runnable doInfo = new Runnable() {
					public void run() {
						try {
							infoTextual.read(reader, "");
						} catch (IOException e) {
							Logger.logarExept(e);
						}
					}
				};
				SwingUtilities.invokeLater(doInfo);

			}
		} catch (Exception e) {
			Logger.logarExept(e);
		}
	}

	public int getTempoSleep() {
		return tempoSleep;
	}

	public void setTempoSleep(int tempoSleep) {
		this.tempoSleep = tempoSleep;
	}

	public JLabel getInfoCorrida() {
		return infoCorrida;
	}

	public JLabel getInfoPiloto() {
		return infoPiloto;
	}

	public JSlider getSpinnerCombustivel() {
		return spinnerCombustivel;
	}

	public JComboBox getComboBoxAsaInicial() {
		return comboBoxAsaInicial;
	}

	public void setComboBoxAsaInicial(JComboBox comboBoxAsaInicial) {
		this.comboBoxAsaInicial = comboBoxAsaInicial;
	}

	public void verificaProgramacaoBox() {
		if (controleJogo.getPilotoJogador() == null
				|| controleJogo.getPilotoJogador().isBox()) {
			return;
		}
		long volta = controleJogo.getPilotoJogador().getNumeroVolta();
		boolean ativo1 = progamacaoBox.getAtive1().isSelected();
		long voltaParada1 = ((Integer) (progamacaoBox
				.getSpinnerNumVoltaParada1().getValue())).intValue();
		if (ativo1 && volta == voltaParada1) {
			controleJogo.setBoxJogadorHumano(Lang.key(progamacaoBox
					.getBoxPneuParada1().getSelectedItem().toString()),
					progamacaoBox.getSliderPercentCombustParada1().getValue(),
					Lang.key(progamacaoBox.getComboBoxAsaParada1()
							.getSelectedItem().toString()));
			modoBox();
			if (controleJogo.getPilotoJogador().getPtosBox() != 0) {
				progamacaoBox.getAtive1().setSelected(false);
			}
		}
		boolean ativo2 = progamacaoBox.getAtive2().isSelected();
		long voltaParada2 = ((Integer) (progamacaoBox
				.getSpinnerNumVoltaParada2().getValue())).intValue();
		if (ativo2 && volta == voltaParada2
				&& controleJogo.getPilotoJogador().getPtosBox() == 0) {
			controleJogo.setBoxJogadorHumano(Lang.key(progamacaoBox
					.getBoxPneuParada2().getSelectedItem().toString()),
					progamacaoBox.getSliderPercentCombustParada2().getValue(),
					Lang.key(progamacaoBox.getComboBoxAsaParada2()
							.getSelectedItem().toString()));
			modoBox();
			if (controleJogo.getPilotoJogador().getPtosBox() != 0) {
				progamacaoBox.getAtive2().setSelected(false);
			}
		}
		boolean ativo3 = progamacaoBox.getAtive3().isSelected();
		long voltaParada3 = ((Integer) (progamacaoBox
				.getSpinnerNumVoltaParada3().getValue())).intValue();
		if (ativo3 && volta == voltaParada3
				&& controleJogo.getPilotoJogador().getPtosBox() == 0) {
			controleJogo.setBoxJogadorHumano(Lang.key(progamacaoBox
					.getBoxPneuParada3().getSelectedItem().toString()),
					progamacaoBox.getSliderPercentCombustParada3().getValue(),
					Lang.key(progamacaoBox.getComboBoxAsaParada3()
							.getSelectedItem().toString()));
			modoBox();
			if (controleJogo.getPilotoJogador().getPtosBox() != 0) {
				progamacaoBox.getAtive3().setSelected(false);
			}
		}
	}

	public boolean isProgamaBox() {
		return progamacaoBox.getAtive1().isSelected()
				|| progamacaoBox.getAtive2().isSelected()
				|| progamacaoBox.getAtive3().isSelected();
	}

	public void setZoom(double d) {
		if (painelCircuito == null) {
			return;
		}
		painelCircuito.zoom = d;
		painelCircuito.gerarGrid();
	}

	public void adicinaTravadaRoda(TravadaRoda travadaRoda) {
		if (ultimaTravavadaRodas == 0) {
			ultimaTravavadaRodas = System.currentTimeMillis();
		}
		if (System.currentTimeMillis() - ultimaTravavadaRodas < 2000) {
			return;
		}
		if (painelCircuito != null) {
			ultimaTravavadaRodas = System.currentTimeMillis();
			painelCircuito.adicionatrvadaRoda(travadaRoda);
		}
	}

	public void setPosisRec(No no) {
		if (painelCircuito == null)
			return;
		painelCircuito.setPosisRec(no);

	}

	public void setPosisAtual(Point point) {
		if (painelCircuito == null)
			return;
		painelCircuito.setPosisAtual(point);

	}

	public void carregaBackGroundCliente() {
		if (painelCircuito != null && painelCircuito.getBackGround() == null) {
			painelCircuito.carregaBackGround();
		}

	}

	public static void main(String[] args) {
		JFrame jFrame = new JFrame();
		JRadioButton mole = new JRadioButton("Mole");
		JRadioButton nomrmal = new JRadioButton("Norm");
		JRadioButton duro = new JRadioButton("Duro");
		ButtonGroup group = new ButtonGroup();
		group.add(mole);
		group.add(nomrmal);
		group.add(duro);
		JPanel jPanel = new JPanel();
		jPanel.setBorder(new TitledBorder("Pneus"));
		jPanel.add(mole);
		jPanel.add(nomrmal);
		jPanel.add(duro);
		jFrame.getContentPane().add(jPanel);
		jFrame.pack();
		jFrame.setVisible(true);
	}

	public void mostraRadioPadock() {
		getRadioPadock().setVisible(true);
		getRadioPadock().setTitle(Lang.msg("f1maneSwing"));
		getRadioPadock().setSize(500, 300);
	}
}
