package sowbreira.f1mane.controles;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sowbreira.f1mane.MainFrame;
import sowbreira.f1mane.entidades.Carro;
import sowbreira.f1mane.entidades.Circuito;
import sowbreira.f1mane.entidades.Clima;
import sowbreira.f1mane.entidades.Piloto;
import sowbreira.f1mane.entidades.SafetyCar;
import sowbreira.f1mane.entidades.Volta;
import sowbreira.f1mane.recursos.idiomas.Lang;
import sowbreira.f1mane.visao.GerenciadorVisual;
import sowbreira.f1mane.visao.PainelTabelaResultadoFinal;
import br.nnpe.Html;

/**
 * @author Paulo Sobreira
 */
public class ControleJogoLocal extends ControleRecursos implements
		InterfaceJogo {
	protected Piloto pilotoSelecionado;
	protected Piloto pilotoJogador;
	protected String tipoPeneuJogador;
	protected String asaJogador;
	protected Integer combustJogador;
	protected double niveljogo = InterfaceJogo.MEDIO_NV;
	protected String nivelCorrida;
	protected boolean corridaTerminada;

	protected ControleCorrida controleCorrida;
	protected GerenciadorVisual gerenciadorVisual;
	protected ControleEstatisticas controleEstatisticas;

	protected Integer qtdeVoltas = null;
	protected Integer diffultrapassagem = null;
	protected Integer tempoCiclo = null;
	protected Integer veloMaxReta = null;
	protected Integer habilidade = null;
	protected Integer potencia = null;
	protected Integer tempoQualificacao = null;
	protected String circuitoSelecionado = null;
	private MainFrame mainFrame;

	public ControleJogoLocal(String temporada) throws Exception {
		super(temporada);
		gerenciadorVisual = new GerenciadorVisual(this);
		controleEstatisticas = new ControleEstatisticas(this);

	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#getCombustBox(sowbreira.f1mane.entidades.Piloto)
	 */
	public Integer getCombustBox(Piloto piloto) {
		return combustJogador;
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#getTipoPeneuBox(sowbreira.f1mane.entidades.Piloto)
	 */
	public String getTipoPeneuBox(Piloto piloto) {
		return tipoPeneuJogador;
	}

	protected void setarNivelCorrida() {
		if (ControleJogoLocal.FACIL.equals(getNivelCorrida())) {
			niveljogo = FACIL_NV;
		} else if (ControleJogoLocal.DIFICIL.equals(getNivelCorrida())) {
			niveljogo = DIFICIL_NV;
		}
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#setNiveljogo(double)
	 */
	public void setNiveljogo(double niveljogo) {
		this.niveljogo = niveljogo;
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#isCorridaTerminada()
	 */
	public boolean isCorridaTerminada() {
		return corridaTerminada;
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#setCorridaTerminada(boolean)
	 */
	public void setCorridaTerminada(boolean corridaTerminada) {
		this.corridaTerminada = corridaTerminada;
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#getNosDoBox()
	 */
	public List getNosDoBox() {
		return nosDoBox;
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#getMainFrame()
	 */
	public MainFrame getMainFrame() {
		return mainFrame;
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#getNivelCorrida()
	 */
	public String getNivelCorrida() {
		return nivelCorrida;
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#setNivelCorrida(java.lang.String)
	 */
	public void setNivelCorrida(String nivelCorrida) {
		this.nivelCorrida = nivelCorrida;
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#getCircuito()
	 */
	public Circuito getCircuito() {
		return circuito;
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#getNosDaPista()
	 */
	public List getNosDaPista() {
		return nosDaPista;
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#getCarros()
	 */
	public List getCarros() {
		return carros;
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#setCircuito(sowbreira.f1mane.entidades.Circuito)
	 */
	public void setCircuito(Circuito circuito) {
		this.circuito = circuito;
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#getPilotos()
	 */
	public List getPilotos() {
		return pilotos;
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#matarTodasThreads()
	 */
	public void matarTodasThreads() {
		try {
			if (controleCorrida != null) {
				controleCorrida.finalize();
			}

			if (gerenciadorVisual != null) {
				gerenciadorVisual.finalize();
			}

			if (controleEstatisticas != null) {
				controleEstatisticas.finalize();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#verificaNivelJogo()
	 */
	public boolean verificaNivelJogo() {
		return controleCorrida.verificaNivelCorrida();
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#getClima()
	 */
	public String getClima() {
		if (controleCorrida != null)
			return controleCorrida.getControleClima().getClima();
		return null;
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#atualizaPainel()
	 */
	public void atualizaPainel() {
		gerenciadorVisual.atualizaPainel();
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#info(java.lang.String)
	 */
	public void info(String info) {
		controleEstatisticas.info(info);
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#infoPrioritaria(java.lang.String)
	 */
	public void infoPrioritaria(String info) {
		controleEstatisticas.info(info, true);
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#porcentagemCorridaCompletada()
	 */
	public int porcentagemCorridaCompletada() {
		if (controleCorrida == null) {
			return 0;
		}
		return controleCorrida.porcentagemCorridaCompletada();
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#getNumVoltaAtual()
	 */
	public int getNumVoltaAtual() {
		if (controleCorrida == null) {
			return 0;
		}
		return controleCorrida.voltaAtual();
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#totalVoltasCorrida()
	 */
	public int totalVoltasCorrida() {
		if (controleCorrida == null) {
			return 0;
		}
		return controleCorrida.getQtdeTotalVoltas();
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#verificaUltimasVoltas()
	 */
	public boolean verificaUltimasVoltas() {
		return ((controleCorrida.getQtdeTotalVoltas() - 3) < getNumVoltaAtual());
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#verificaBoxOcupado(sowbreira.f1mane.entidades.Carro)
	 */
	public boolean verificaBoxOcupado(Carro carro) {
		return controleCorrida.verificaBoxOcupado(carro);
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#calculaSegundosParaLider(sowbreira.f1mane.entidades.Piloto)
	 */
	public String calculaSegundosParaLider(Piloto pilotoSelecionado) {
		long tempo = controleCorrida.obterTempoCilco();
		return controleEstatisticas.calculaSegundosParaLider(pilotoSelecionado,
				tempo);
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#verificaUltima()
	 */
	public boolean verificaUltima() {
		return ((controleCorrida.getQtdeTotalVoltas() - 1) == getNumVoltaAtual());
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#processaVoltaRapida(sowbreira.f1mane.entidades.Piloto)
	 */
	public void processaVoltaRapida(Piloto piloto) {
		controleEstatisticas.processaVoltaRapida(piloto);
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#getCicloAtual()
	 */
	public int getCicloAtual() {
		return controleCorrida.getCicloAtual();
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#verificaVoltaMaisRapidaCorrida(sowbreira.f1mane.entidades.Piloto)
	 */
	public void verificaVoltaMaisRapidaCorrida(Piloto piloto) {
		controleEstatisticas.verificaVoltaMaisRapida(piloto);
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#obterIndicativoCorridaCompleta()
	 */
	public double obterIndicativoCorridaCompleta() {
		return (porcentagemCorridaCompletada() / 100.0) + 1;
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#obterMelhorVolta()
	 */
	public Volta obterMelhorVolta() {
		return controleEstatisticas.getVoltaMaisRapida();
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#verificaUltraPassagem(sowbreira.f1mane.entidades.Piloto,
	 *      int)
	 */
	public int verificaUltraPassagem(Piloto piloto, int novoModificador) {
		return controleCorrida.verificaUltraPassagem(piloto, novoModificador);
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#getNiveljogo()
	 */
	public double getNiveljogo() {
		return niveljogo;
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#efetuarSelecaoPilotoJogador(java.lang.Object,
	 *      java.lang.Object, java.lang.Object, java.lang.String)
	 */
	public void efetuarSelecaoPilotoJogador(Object selec, Object tpneu,
			Object combust, String nomeJogador, Object asa) {
		pilotoJogador = (Piloto) selec;
		pilotoJogador.setJogadorHumano(true);
		pilotoJogador.setNomeJogador(nomeJogador);

		this.tipoPeneuJogador = (String) tpneu;
		this.combustJogador = (Integer) combust;
		this.asaJogador = (String) asa;
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#mudarModoAgressivo()
	 */
	public boolean mudarModoAgressivo() {
		if (pilotoJogador == null)
			return false;
		pilotoJogador.setAgressivo(!pilotoJogador.isAgressivo());
		pilotoJogador.setCiclosDesconcentrado(40);
		return pilotoJogador.isAgressivo();
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#mudarModoBox()
	 */
	public boolean mudarModoBox() {
		if (pilotoJogador != null) {
			pilotoJogador.setBox(!pilotoJogador.isBox());

			return pilotoJogador.isBox();

		}
		return false;
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#setBoxJogadorHumano(java.lang.Object,
	 *      java.lang.Object)
	 */
	public void setBoxJogadorHumano(Object tpneu, Object combust, Object asa) {
		this.tipoPeneuJogador = (String) tpneu;
		this.combustJogador = (Integer) combust;
		this.asaJogador = (String) asa;
		pilotoJogador.setTipoPneuBox(tipoPeneuJogador);
		pilotoJogador.setQtdeCombustBox(combustJogador);
		pilotoJogador.setAsaBox(asaJogador);
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#selecionaPilotoJogador()
	 */
	public void selecionaPilotoJogador() {
		pilotoSelecionado = pilotoJogador;
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#apagarLuz()
	 */
	public void apagarLuz() {
		gerenciadorVisual.apagarLuz();
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#processaNovaVolta()
	 */
	public void processaNovaVolta() {
		int qtdeDesqualificados = 0;
		if (getNumVoltaAtual() == (totalVoltasCorrida() - 1)
				&& !isCorridaTerminada()) {
			Piloto piloto = (Piloto) pilotos.get(0);
			infoPrioritaria(Html.superBlack(piloto.getNome())
					+ Html.superGreen(Lang.msg("045")));
		}

		for (Iterator iter = pilotos.iterator(); iter.hasNext();) {
			Piloto piloto = (Piloto) iter.next();
			if (piloto.isDesqualificado()) {
				qtdeDesqualificados++;
			}
		}
		if (qtdeDesqualificados >= 14) {
			setCorridaTerminada(true);
			controleCorrida.terminarCorrida();
			infoPrioritaria(Html.superDarkRed(Lang.msg("024",
					new Object[] { getNumVoltaAtual() })));
		}
		controleCorrida.getControleClima().processaPossivelMudancaClima();
		if (!isSafetyCarNaPista()) {
			Thread nvolta = new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					controleEstatisticas.tabelaComparativa();

				}

			});
			nvolta.start();
		}

	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#isChovendo()
	 */
	public boolean isChovendo() {
		return Clima.CHUVA.equals(getClima());
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#informaMudancaClima()
	 */
	public void informaMudancaClima() {
		gerenciadorVisual.informaMudancaClima();

	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#pausarJogo()
	 */
	public void pausarJogo() {
		info(Html.cinza(controleCorrida.isCorridaPausada() ? Lang.msg("025")
				: Lang.msg("026")));
		controleCorrida.setCorridaPausada(!controleCorrida.isCorridaPausada());

	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#obterResultadoFinal()
	 */
	public PainelTabelaResultadoFinal obterResultadoFinal() {

		return gerenciadorVisual.getResultadoFinal();
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#isSafetyCarNaPista()
	 */
	public boolean isSafetyCarNaPista() {
		if (controleCorrida == null)
			return false;
		return controleCorrida.isSafetyCarNaPista();
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#getSafetyCar()
	 */
	public SafetyCar getSafetyCar() {
		return controleCorrida.getSafetyCar();
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#calculaModificadorComSafetyCar(sowbreira.f1mane.entidades.Piloto,
	 *      int)
	 */
	public int calculaModificadorComSafetyCar(Piloto piloto, int novoModificador) {
		return controleCorrida.calculaModificadorComSafetyCar(piloto,
				novoModificador);
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#isSafetyCarVaiBox()
	 */
	public boolean isSafetyCarVaiBox() {

		return controleCorrida.isSafetyCarVaiBox();
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#obterCarroNaFrente(sowbreira.f1mane.entidades.Piloto)
	 */
	public Carro obterCarroNaFrente(Piloto piloto) {
		return controleCorrida.obterCarroNaFrente(piloto);
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#obterCarroAtraz(sowbreira.f1mane.entidades.Piloto)
	 */
	public Carro obterCarroAtraz(Piloto piloto) {
		return controleCorrida.obterCarroAtraz(piloto);
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#calculaSegundosParaProximo(sowbreira.f1mane.entidades.Piloto)
	 */
	public String calculaSegundosParaProximo(Piloto psel) {
		long tempo = controleCorrida.obterTempoCilco();
		return controleEstatisticas.calculaSegundosParaProximo(psel, tempo);
	}

	public double calculaSegundosParaProximoDouble(Piloto psel) {
		long tempo = controleCorrida.obterTempoCilco();
		return controleEstatisticas.calculaSegundosParaProximoDouble(psel,
				tempo);
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#getIndexVelcidadeDaPista()
	 */
	public double getIndexVelcidadeDaPista() {

		return controleCorrida.getIndexVelcidadeDaPista();
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#iniciarJogoSingle()
	 */
	public void iniciarJogo() throws Exception {
		if (gerenciadorVisual.iniciarJogoSingle()) {
			processarEntradaDados();
			carregaRecursos((String) getCircuitos().get(circuitoSelecionado));
			this.nivelCorrida = Lang.key(gerenciadorVisual
					.getComboBoxNivelCorrida().getSelectedItem().toString());
			controleCorrida = new ControleCorrida(this, qtdeVoltas.intValue(),
					diffultrapassagem.intValue(), veloMaxReta.intValue(),
					tempoCiclo.intValue());
			setarNivelCorrida();
			controleCorrida.getControleClima().gerarClimaInicial(
					(Clima) gerenciadorVisual.getComboBoxClimaInicial()
							.getSelectedItem());
			controleCorrida.gerarGridLargadaSemQualificacao();
			gerenciadorVisual.iniciarInterfaceGraficaJogo();
			controleCorrida.iniciarCorrida();
			controleEstatisticas.inicializarThreadConsumidoraInfo(1500);
		}
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#getCircuitos()
	 */
	public Map getCircuitos() {
		return circuitos;
	}

	protected void processarEntradaDados() throws Exception {
		try {
			qtdeVoltas = (Integer) gerenciadorVisual.getSpinnerQtdeVoltas()
					.getValue();
			diffultrapassagem = (Integer) gerenciadorVisual
					.getSpinnerDificuldadeUltrapassagem().getValue();
			tempoCiclo = (Integer) gerenciadorVisual.getSpinnerTempoCiclo()
					.getValue();
			veloMaxReta = (Integer) gerenciadorVisual
					.getSpinnerIndexVelcidadeEmReta().getValue();
			habilidade = (Integer) gerenciadorVisual
					.getSpinnerSkillPadraoPilotos().getValue();
			circuitoSelecionado = (String) gerenciadorVisual
					.getComboBoxCircuito().getSelectedItem();
			if (habilidade.intValue() != 0) {
				if (habilidade.intValue() < 50) {
					habilidade = new Integer(50);
				}
				if (habilidade.intValue() > 99) {
					habilidade = new Integer(99);
				}
				definirHabilidadePadraoPilotos(habilidade.intValue());
			}

			potencia = (Integer) gerenciadorVisual
					.getSpinnerPotenciaPadraoCarros().getValue();
			if (potencia.intValue() != 0) {
				if (potencia.intValue() < 800) {
					potencia = new Integer(800);
				}
				if (potencia.intValue() > 999) {
					potencia = new Integer(999);
				}
				definirPotenciaPadraoCarros(potencia.intValue());
			}

			if (gerenciadorVisual.getSpinnerQtdeMinutosQualificacao() != null) {
				tempoQualificacao = (Integer) gerenciadorVisual
						.getSpinnerQtdeMinutosQualificacao().getValue();
				if (tempoQualificacao.intValue() < 3) {
					tempoQualificacao = new Integer(3);
				}
				if (tempoQualificacao.intValue() > 15) {
					tempoQualificacao = new Integer(15);
				}
			}
		} catch (Exception e) {
			throw new Exception(Lang.msg("027"));
		}

	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#exibirResultadoFinal()
	 */
	public void exibirResultadoFinal() {
		mainFrame
				.exibirResiltadoFinal(gerenciadorVisual.exibirResultadoFinal());
		controleCorrida.pararThreads();
		controleEstatisticas.setConsumidorAtivo(false);
		if (!VALENDO) {
			for (int i = 0; i < pilotos.size(); i++) {
				Piloto piloto = (Piloto) pilotos.get(i);
				System.out.println(i + 1 + "-" + piloto.getNome() + "-"
						+ piloto.getCarro().getNome() + "-"
						+ piloto.getNumeroVolta() + "-"
						+ piloto.getQtdeParadasBox() + "-"
						+ piloto.getCarro().getTipoPneu() + " ## "
						+ piloto.getCarro().getMotor() + " # "
						+ piloto.getCarro().porcentagemDesgasteMotor());

			}
		}
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#abandonar()
	 */
	public void abandonar() {
		if (pilotoJogador != null) {
			pilotoJogador.abandonar();
		}
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#desenhaQualificacao()
	 */
	public void desenhaQualificacao() {
		if (gerenciadorVisual != null) {
			gerenciadorVisual.desenhaQualificacao();
		}

	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#getTempoCiclo()
	 */
	public long getTempoCiclo() {

		return controleCorrida.getTempoCiclo();
	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#zerarMelhorVolta()
	 */
	public void zerarMelhorVolta() {
		controleEstatisticas.zerarMelhorVolta();

	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#adicionarInfoDireto(java.lang.String)
	 */
	public void adicionarInfoDireto(String string) {
		gerenciadorVisual.adicionarInfoDireto(string);

	}

	/**
	 * @see sowbreira.f1mane.controles.InterfaceJogo#atulizaTabelaPosicoes()
	 */
	public void atulizaTabelaPosicoes() {
		gerenciadorVisual.atulizaTabelaPosicoes();

	}

	public void selecionouPiloto(Piloto pilotoSelecionado) {
		this.pilotoSelecionado = pilotoSelecionado;

	}

	public Piloto getPilotoSelecionado() {
		return pilotoSelecionado;
	}

	public int setUpJogadorHumano(Piloto pilotoJogador, Object tpPneu,
			Object combust, Object asa) {
		String tipoPneu = (String) tpPneu;
		Integer qtdeCombustPorcent = (Integer) combust;

		pilotoJogador.getCarro().trocarPneus(tipoPneu,
				controleCorrida.getDistaciaCorrida());

		int undsComnustAbastecer = (controleCorrida.getTanqueCheio() * qtdeCombustPorcent
				.intValue()) / 100;

		pilotoJogador.getCarro().setCombustivel(
				undsComnustAbastecer
						+ pilotoJogador.getCarro().getCombustivel());
		String strAsa = (String) asa;
		if (!strAsa.equals(pilotoJogador.getCarro().getAsa())) {
			infoPrioritaria(Html.orange(Lang.msg("028",
					new String[] { pilotoJogador.getNome() })));
		}
		pilotoJogador.getCarro().setAsa(strAsa);
		if (undsComnustAbastecer < 0) {
			undsComnustAbastecer = 0;
		}
		return undsComnustAbastecer;
	}

	public void saiuBox(Piloto piloto) {

	}

	public Volta obterMelhorVolta(Piloto pilotoSelecionado) {
		return pilotoSelecionado.obterVoltaMaisRapida();
	}

	public Piloto getPilotoJogador() {
		return pilotoJogador;
	}

	public void mudarGiroMotor(Object selectedItem) {
		String giroMotor = (String) selectedItem;
		if (pilotoJogador != null) {
			pilotoJogador.getCarro().mudarGiroMotor(giroMotor);
		}

	}

	public int calculaDiferencaParaProximo(Piloto piloto) {
		return controleEstatisticas.calculaDiferencaParaProximo(piloto);
	}

	public void mudarModoPilotagem(String modo) {
		if (pilotoJogador != null)
			pilotoJogador.setModoPilotagem(modo);
	}

	public String getAsaBox(Piloto piloto) {
		return asaJogador;
	}

	public void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public int verificaRetardatario(Piloto piloto, int novoModificador) {
		return controleCorrida.verificaRetardatario(piloto, novoModificador);
	}

	public boolean isModoQualify() {
		return controleCorrida.getControleQualificacao().isModoQualify();
	}

	public void tabelaComparativa() {
		controleEstatisticas.tabelaComparativa();

	}

	public int getQtdeTotalVoltas() {
		return controleCorrida.getQtdeTotalVoltas();
	}

}
