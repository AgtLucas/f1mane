package sowbreira.f1mane.entidades;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import sowbreira.f1mane.controles.ControleQualificacao;
import sowbreira.f1mane.controles.InterfaceJogo;
import sowbreira.f1mane.recursos.idiomas.Lang;
import br.nnpe.Html;
import br.nnpe.Logger;

/**
 * @author Paulo Sobreira
 */
public class Piloto implements Serializable {
	private static final long serialVersionUID = 698992658460848522L;
	public static final String AGRESSIVO = "AGRESSIVO";
	public static final String NORMAL = "NORMAL";
	public static final String LENTO = "LENTO";
	private transient double anguloRotacaoCarro;
	private transient double zoom;
	private transient BufferedImage ultimaRotacaoCarro;
	private List ultsConsumosCombustivel = new LinkedList();
	private Integer ultimoConsumoCombust;
	private List ultsConsumosPneu = new LinkedList();
	private Integer ultimoConsumoPneu;
	protected String tipoPeneuJogador;
	protected String asaJogador;
	protected Integer combustJogador;
	private int id;
	private int velocidade;
	private int velocidadeLargada;
	private transient String setUpIncial;
	private String nome;
	private String nomeCarro;
	private String nomeJogador;
	private transient int habilidade;
	private transient double notaQualificacaoAleatoria;
	private int carX;
	private int carY;
	private int ptosPista;
	private int ultimoIndice;
	private List mediaIndices;

	public int getCarX() {
		return carX;
	}

	public void setCarX(int carX) {
		this.carX = carX;
	}

	public int getCarY() {
		return carY;
	}

	public void setCarY(int carY) {
		this.carY = carY;
	}

	public double getZoom() {
		return zoom;
	}

	public void setZoom(double zoom) {
		this.zoom = zoom;
	}

	public int getUltimoIndice() {
		return ultimoIndice;
	}

	public void setUltimoIndice(int ultimoIndice) {
		this.ultimoIndice = ultimoIndice;
	}

	private transient int ptosBox;
	private int posicao;
	private transient int paradoBox;
	private int qtdeParadasBox;
	private boolean desqualificado;
	private boolean jogadorHumano;
	private transient boolean recebeuBanderada;
	private boolean box;
	private boolean agressivo = true;
	private Carro carro = new Carro();
	private No noAtual = new No();
	private int numeroVolta;
	private int stress;
	private transient int ciclosDesconcentrado;
	private transient int porcentagemCombustUltimaParadaBox;
	private transient Map msgsBox = new HashMap();
	private List voltas = new ArrayList();
	private String modoPilotagem = NORMAL;
	private Volta voltaAtual;
	private int ciclosVoltaQualificacao;
	private Volta ultimaVolta;
	private Volta melhorVolta;
	private String segundosParaLider;
	private String tipoPneuBox;
	private String asaBox;
	private int qtdeCombustBox;
	private long parouNoBoxMilis;
	private long saiuDoBoxMilis;
	private int msgTentativaNumVolta = 2;

	public double getAnguloRotacaoCarro() {
		return anguloRotacaoCarro;
	}

	public void setAnguloRotacaoCarro(double anguloRotacaoCarro) {
		this.anguloRotacaoCarro = anguloRotacaoCarro;
	}

	public BufferedImage getUltimaRotacaoCarro() {
		return ultimaRotacaoCarro;
	}

	public void setUltimaRotacaoCarro(BufferedImage ultimaRotacaoCarro) {
		this.ultimaRotacaoCarro = ultimaRotacaoCarro;
	}

	@Override
	public boolean equals(Object obj) {
		Piloto outro = (Piloto) obj;
		return getNome().equals(outro.getNome());
	}

	@Override
	public int hashCode() {
		return getNome().hashCode();
	}

	public String getTipoPeneuJogador() {
		return tipoPeneuJogador;
	}

	public void setTipoPeneuJogador(String tipoPeneuJogador) {
		this.tipoPeneuJogador = tipoPeneuJogador;
	}

	public String getAsaJogador() {
		return asaJogador;
	}

	public void setAsaJogador(String asaJogador) {
		this.asaJogador = asaJogador;
	}

	public Integer getCombustJogador() {
		return combustJogador;
	}

	public void setCombustJogador(Integer combustJogador) {
		this.combustJogador = combustJogador;
	}

	public int getVelocidade() {
		return velocidade;
	}

	public String getNomeJogador() {
		return nomeJogador;
	}

	public int getId() {
		return id;
	}

	public int getQtdeCombustBox() {
		return qtdeCombustBox;
	}

	public void setQtdeCombustBox(int qtdeCombustBox) {
		this.qtdeCombustBox = qtdeCombustBox;
	}

	public String getTipoPneuBox() {
		return tipoPneuBox;
	}

	public void setTipoPneuBox(String tipoPneuBox) {
		this.tipoPneuBox = tipoPneuBox;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNomeJogador(String nomeJogador) {
		this.nomeJogador = nomeJogador;
	}

	public void setVoltas(List voltas) {
		this.voltas = voltas;
	}

	public long getParouNoBoxMilis() {
		return parouNoBoxMilis;
	}

	public int getCiclosVoltaQualificacao() {
		return ciclosVoltaQualificacao;
	}

	public void setCiclosVoltaQualificacao(int ciclosVoltaQualificacao) {
		this.ciclosVoltaQualificacao = ciclosVoltaQualificacao;
	}

	public void setParouNoBoxMilis(long entrouNoBoxMilis) {
		this.parouNoBoxMilis = entrouNoBoxMilis;
	}

	public long getSaiuDoBoxMilis() {
		return saiuDoBoxMilis;
	}

	public void setSaiuDoBoxMilis(long saiuDoBoxMilis) {
		this.saiuDoBoxMilis = saiuDoBoxMilis;
	}

	public Volta getUltimaVolta() {
		return ultimaVolta;
	}

	public boolean isDesqualificado() {
		return desqualificado;
	}

	public void setDesqualificado(boolean desqualificado) {
		this.desqualificado = desqualificado;
	}

	public String getSegundosParaLider() {
		return ((segundosParaLider == null) ? Lang.msg("Lider")
				: segundosParaLider);
	}

	public void setSegundosParaLider(String segundosParaLider) {
		if (segundosParaLider == null) {
			segundosParaLider = "";
		}

		this.segundosParaLider = segundosParaLider;
	}

	public Volta getVoltaAtual() {
		return voltaAtual;
	}

	public void setVoltaAtual(Volta voltaAtual) {
		if (voltaAtual != null)
			voltaAtual.setPiloto(this.getId());
		this.voltaAtual = voltaAtual;
	}

	public List getVoltas() {
		return voltas;
	}

	public int getQtdeParadasBox() {
		return qtdeParadasBox;
	}

	public boolean isRecebeuBanderada() {
		return recebeuBanderada;
	}

	public void setRecebeuBanderada(boolean recebueBanderada,
			InterfaceJogo controleJogo) {
		if (!this.recebeuBanderada) {
			if (this.getPosicao() == 1) {
				controleJogo.infoPrioritaria(Html.superBlack(getNome())
						+ Html.superGreen(Lang.msg("044",
								new Object[] { getNumeroVolta() })));
			} else {
				controleJogo.info(Html.superBlack(getNome())
						+ Html.verde(Lang.msg("044",
								new Object[] { getNumeroVolta() })));
			}
		}

		this.recebeuBanderada = recebueBanderada;
	}

	public void setQtdeParadasBox(int qtdeParadasBox) {
		this.qtdeParadasBox = qtdeParadasBox;
	}

	public String getSetUpIncial() {
		return setUpIncial;
	}

	public void setSetUpIncial(String setUpIncial) {
		this.setUpIncial = setUpIncial;
	}

	public int getCiclosDesconcentrado() {
		return ciclosDesconcentrado;
	}

	public int getPtosBox() {
		return ptosBox;
	}

	public void setPtosBox(int ptosBox) {
		this.ptosBox = ptosBox;
	}

	public boolean isBox() {
		return box;
	}

	public void setBox(boolean box) {
		this.box = box;
	}

	public void setCiclosDesconcentrado(int ciclosDelay) {
		this.ciclosDesconcentrado = ciclosDelay;
	}

	public boolean isAgressivo() {
		return agressivo;
	}

	public void setAgressivo(boolean regMaximo) {
		this.agressivo = regMaximo;
	}

	public boolean isJogadorHumano() {
		return jogadorHumano;
	}

	public void setJogadorHumano(boolean jogadorHumano) {
		this.jogadorHumano = jogadorHumano;
	}

	public int getNumeroVolta() {
		return numeroVolta;
	}

	public void setNumeroVolta(int numeroVolta) {
		this.numeroVolta = numeroVolta;
	}

	public double getNotaQualificacaoAleatoria() {
		return notaQualificacaoAleatoria;
	}

	public void setNotaQualificacaoAleatoria(double notaQualificacao) {
		this.notaQualificacaoAleatoria = notaQualificacao;
	}

	public Carro getCarro() {
		return carro;
	}

	public int getPtosPista() {
		return ptosPista;
	}

	public void setPtosPista(int ptosPista) {
		this.ptosPista = ptosPista;
	}

	public No getNoAtual() {
		return noAtual;
	}

	public void setNoAtual(No no) {
		this.noAtual = no;
	}

	public void setCarro(Carro carro) {
		this.carro = carro;
	}

	public String getNomeCarro() {
		return nomeCarro;
	}

	public void setNomeCarro(String carro) {
		this.nomeCarro = carro;
	}

	public int getHabilidade() {
		return habilidade;
	}

	public void setHabilidade(int habilidade) {
		this.habilidade = habilidade;
	}

	public String getNome() {
		return nome;
	}

	public void setUltimaVolta(Volta ultimaVolta) {
		this.ultimaVolta = ultimaVolta;
	}

	public void setNome(String nome) {
		if (nome == null || "".equals(nome))
			nome = "H";
		this.nome = nome;
	}

	public String toString() {
		return nome + " - " + getCarro().getNome();
	}

	public void setVelocidade(int velocidade) {
		this.velocidade = velocidade;
	}

	/**
	 * Fechado n�o mexa mais em index aqui.
	 */
	public void processarCiclo(InterfaceJogo controleJogo) {
		List pista = controleJogo.getNosDaPista();
		int index = calcularNovoIndex(controleJogo);
		int diff = index - pista.size();

		/**
		 * Completou Volta
		 */
		if (diff >= 0) {
			int pCombust = getCarro().porcentagemCombustivel();
			if (ultimoConsumoCombust == null) {
				ultimoConsumoCombust = new Integer(pCombust);
			} else {
				if (ultimoConsumoCombust.intValue() > pCombust) {
					ultsConsumosCombustivel.add(ultimoConsumoCombust.intValue()
							- pCombust);
					ultimoConsumoCombust = new Integer(pCombust);

				}
			}
			int pPneu = getCarro().porcentagemDesgastePeneus();
			if (ultimoConsumoPneu == null) {
				ultimoConsumoPneu = new Integer(pPneu);
			} else {
				if (ultimoConsumoPneu.intValue() > pPneu) {
					ultsConsumosPneu.add(ultimoConsumoCombust.intValue()
							- pPneu);
					ultimoConsumoPneu = new Integer(pPneu);
				}
			}

			index = diff;
			controleJogo.processaVoltaRapida(this);
			/**
			 * calback de nova volta para corrida Toda
			 */
			if (posicao == 1) {
				controleJogo.processaNovaVolta();
			}

			if (controleJogo.isCorridaTerminada()) {
				setRecebeuBanderada(true, controleJogo);
			}

			if (numeroVolta > controleJogo.totalVoltasCorrida()) {
				numeroVolta = controleJogo.totalVoltasCorrida();

				return;
			}
		}

		calcularVolta(controleJogo);
		verificaIrBox(controleJogo);

		this.setNoAtual((No) pista.get(index));

	}

	public void processaVelocidade(int index, No no) {
		if (velocidadeLargada < 50) {
			velocidade += ((int) (Math.random() * (20 * index)));
			velocidadeLargada = velocidade;
			return;
		}
		int fatorAcel = 10;
		if (getCarro().testePotencia()) {
			fatorAcel = 20;
		}
		switch (index) {
		case 0:
			velocidade -= 10 + ((int) (Math.random() * (fatorAcel + 5)));
			break;

		case 1:
			if (no.verificaRetaOuLargada()) {
				velocidade--;
			} else {
				if (velocidade > 200)
					velocidade -= 10 + ((int) (Math.random() * (fatorAcel + 25)));
				else if (velocidade > 150)
					velocidade -= 10 + ((int) (Math.random() * (fatorAcel + 15)));
				else if (velocidade > 80) {
					velocidade -= ((int) (Math.random() * (fatorAcel + 10)));
				} else {
					velocidade += ((int) (Math.random() * fatorAcel + 5));
				}
			}
			if (velocidade < 50) {
				velocidade = 40 + ((int) (Math.random() * 20));
			}

			break;
		case 2:
			if (no.verificaRetaOuLargada()) {
				velocidade += ((int) (Math.random() * fatorAcel / 2));
			} else {
				if (velocidade > 200 && no.verificaCruvaBaixa())
					velocidade -= ((int) (Math.random() * (fatorAcel + 30)));
				else if (velocidade > 150 && no.verificaCruvaBaixa())
					velocidade -= ((int) (Math.random() * (fatorAcel + 10)));
				else if (velocidade > 270)
					velocidade -= ((int) (Math.random() * (fatorAcel)));
				else {
					velocidade += ((int) (Math.random() * fatorAcel));
				}
			}
			break;
		case 3:
			velocidade += ((int) (Math.random() * (fatorAcel / 2)));
			break;

		default:
			break;
		}

		if (velocidade < 0) {
			velocidade = 10 + ((int) (Math.random() * 10));
		}
		if (velocidade > 300) {
			velocidade = 300 + ((int) (Math.random() * 30));
		}

	}

	private void verificaIrBox(InterfaceJogo controleJogo) {
		if (jogadorHumano || recebeuBanderada || ptosPista < 0) {
			return;
		}

		int pneus = getCarro().porcentagemDesgastePeneus();
		int combust = getCarro().porcentagemCombustivel();

		if ((combust < 15) && !controleJogo.isCorridaTerminada()) {
			box = true;
		} else {
			box = false;
		}

		double consumoMedioPneus = calculaConsumoMedioPneu();
		if (pneus < 1.8 * consumoMedioPneus
				&& Carro.TIPO_PNEU_MOLE.equals(carro.getTipoPneu())) {
			box = true;
		}

		if (Carro.TIPO_PNEU_DURO.equals(carro.getTipoPneu()) && (pneus < 10)) {
			box = true;
		}

		if ((Carro.TIPO_PNEU_MOLE.equals(carro.getTipoPneu()) || Carro.TIPO_PNEU_CHUVA
				.equals(carro.getTipoPneu()))
				&& (pneus < 25)) {
			box = true;
		}

		if (box && controleJogo.verificaBoxOcupado(getCarro()) && (combust > 5)
				&& (pneus > 12)) {
			if (!Messagens.BOX_OCUPADO.equals(msgsBox
					.get(Messagens.BOX_OCUPADO))) {
				if (isJogadorHumano()) {
					controleJogo.infoPrioritaria(Html.orange(Lang.msg("046",
							new String[] { Html.bold(getNome()) })));
				} else if (getPosicao() < 9) {
					controleJogo.info(Html.orange(Lang.msg("046",
							new String[] { Html.bold(getNome()) })));
				}

				msgsBox.put(Messagens.BOX_OCUPADO, Messagens.BOX_OCUPADO);
			}

			box = false;
		}
		if (controleJogo.isSafetyCarNaPista()
				&& !controleJogo.isSafetyCarVaiBox()) {
			if (combust < 20 || pneus < 50) {
				box = true;
			}

		}

		if (carro.verificaPneusIncompativeisClima(controleJogo)) {
			box = true;
		}

		if (controleJogo.verificaUltimasVoltas() && (combust > 3)
				&& (combust <= 5) && (pneus <= 12) && (pneus > 5)) {
			controleJogo.info(Html.orange(Lang.msg("047",
					new String[] { getNome() })));
			msgsBox.put(Messagens.IR_BOX_FINAL_CORRIDA,
					Messagens.IR_BOX_FINAL_CORRIDA);
			box = false;
		}

		if (carro.verificaDano()) {
			box = true;
		}

		if (controleJogo.verificaUltima()) {
			box = false;
		}
		if (controleJogo.getNumVoltaAtual() < 1) {
			box = false;
		}
		if (controleJogo.isCorridaTerminada()) {
			box = false;
		}
	}

	public int getPosicao() {
		return posicao;
	}

	public void setPosicao(int posicao) {
		this.posicao = posicao;
	}

	public void calcularVolta(InterfaceJogo controleJogo) {
		int tamanhoCircuito = controleJogo.getNosDaPista().size();

		if ((ptosPista == 0) || (tamanhoCircuito == 0)) {
			numeroVolta = 0;
		}

		numeroVolta = ptosPista / tamanhoCircuito;

		if (numeroVolta > controleJogo.totalVoltasCorrida()) {
			numeroVolta = controleJogo.totalVoltasCorrida();
		}
	}

	private int calcularNovoIndex(InterfaceJogo controleJogo) {
		int index = noAtual.getIndex();
		boolean fator = Math.random() > controleJogo.getNiveljogo();
		if (fator && NORMAL.equals(modoPilotagem)) {
			decStress(Math.random() > .5 ? 1 : 0);
		} else if (fator && LENTO.equals(modoPilotagem)) {
			decStress(1);
		} else if (fator && !agressivo && getPosicao() != 1) {
			decStress(1);
		}
		/**
		 * Devagarinho qdo a corrida termina
		 */
		if ((controleJogo.isCorridaTerminada() && isRecebeuBanderada())) {
			index += ((Math.random() > 0.4) ? 1 : 0);
			ptosPista += 1;
			return index;
		}
		if (!desqualificado) {
			if (getCarro().isPaneSeca()) {
				desqualificado = true;
				controleJogo.infoPrioritaria(Html.txtRedBold(getNome()
						+ Lang.msg("118")));
			}
		} else {
			return index;
		}
		verificaMudancaRegime(controleJogo);
		tentarPassaPilotoDaFrente(controleJogo);
		int novoModificador = calcularNovoModificador(controleJogo);
		novoModificador = getCarro().calcularModificadorCarro(novoModificador,
				agressivo, noAtual, controleJogo);
		if (!controleJogo.isModoQualify()) {
			novoModificador = controleJogo.verificaUltraPassagem(this,
					novoModificador);

			novoModificador = controleJogo.verificaRetardatario(this,
					novoModificador);
		}

		if (noAtual.verificaCruvaBaixa() || noAtual.verificaCruvaAlta()) {
			if (carro.verificaPneusIncompativeisClima(controleJogo)
					&& novoModificador > 1) {
				novoModificador = ((Math.random() > 0.4) ? 1 : 0);
			}
		}
		if (novoModificador > 6) {
			novoModificador = 6;
		} else if (novoModificador < 0) {
			novoModificador = 0;
		}

		if (danificado()) {
			novoModificador = 1;
		}

		novoModificador = controleJogo.calculaModificadorComSafetyCar(this,
				novoModificador);
		double ganho = ((novoModificador * controleJogo.getCircuito()
				.getMultiplciador()) * controleJogo.getIndexVelcidadeDaPista());
		ganho = processaGanhoMedio(ganho);

		processaVelocidade(novoModificador, noAtual);
		index += ganho;
		ptosPista += ganho;

		return index;
	}

	private double processaGanhoMedio(double ganho) {
		if (mediaIndices == null)
			mediaIndices = new ArrayList();
		if (mediaIndices.size() > 5) {
			mediaIndices.remove(0);
		}
		mediaIndices.add(ganho);
		double soma = 0;
		for (Iterator iterator = mediaIndices.iterator(); iterator.hasNext();) {
			Double val = (Double) iterator.next();
			soma += val.doubleValue();
		}
		return soma / mediaIndices.size();
	}

	private void tentarPassaPilotoDaFrente(InterfaceJogo controleJogo) {
		if (jogadorHumano || danificado()) {
			return;
		}
		if (ControleQualificacao.modoQualify) {
			return;
		}
		int diff = calculaDiffParaProximo(controleJogo);
		int distBrigaMax = (int) (120 * controleJogo.getNiveljogo());
		int distBrigaMin = 0;
		if (controleJogo.getNiveljogo() == .3) {
			distBrigaMin = 30;
		} else if (controleJogo.getNiveljogo() == .5) {
			distBrigaMin = 20;
		} else if (controleJogo.getNiveljogo() == .7) {
			distBrigaMin = 15;
		}
		if (controleJogo.porcentagemCorridaCompletada() > distBrigaMax) {
			distBrigaMax = controleJogo.porcentagemCorridaCompletada();
		}
		Carro carroPilotoDaFrente = controleJogo.obterCarroNaFrente(this);
		if (diff > distBrigaMin && diff < distBrigaMax
				&& testeHabilidadePilotoCarro()) {
			if (carroPilotoDaFrente != null) {
				Piloto pilotoFrente = carroPilotoDaFrente.getPiloto();
				if (!pilotoFrente.entrouNoBox()
						&& !controleJogo.isSafetyCarNaPista()
						&& Math.random() < controleJogo.getNiveljogo()) {
					getCarro().setGiro(Carro.GIRO_MAX_VAL);
					if (controleJogo.verificaNivelJogo()
							&& testeHabilidadePiloto()) {
						No no = getNoAtual();
						if ((no.verificaCruvaAlta() || no
								.verificaRetaOuLargada())
								&& Carro.MAIS_ASA.equals(getCarro().getAsa())) {
							getCarro().setGiro(Carro.GIRO_NOR_VAL);
						}
						if (no.verificaCruvaBaixa()
								&& (Carro.MENOS_ASA.equals(getCarro().getAsa()))) {
							getCarro().setGiro(Carro.GIRO_NOR_VAL);
						}
					}
					if (testeHabilidadePiloto()
							&& Math.random() < controleJogo.getNiveljogo()) {
						setAgressivo(true);
						if (Math.random() < controleJogo
								.obterIndicativoCorridaCompleta()
								&& Math.random() > .9
								&& getPosicao() < 9
								&& msgTentativaNumVolta == getNumeroVolta()) {

							int val = 1 + (int) (Math.random() * 4);
							msgTentativaNumVolta = getNumeroVolta() + val;
							String txt = "";
							switch (val) {

							case 1:
								txt = Lang.msg("048", new String[] {
										Html.bold(getNome()),
										Html.bold(carroPilotoDaFrente
												.getPiloto().getNome()) });
								controleJogo.info(Html.silver(txt));
								break;
							case 2:
								txt = Lang.msg("049", new String[] {
										Html.bold(getNome()),
										Html.bold(carroPilotoDaFrente
												.getPiloto().getNome()) });
								controleJogo.info(Html.silver(txt));
								break;
							case 3:
								txt = Lang.msg("050", new String[] {
										Html.bold(getNome()),
										Html.bold(carroPilotoDaFrente
												.getPiloto().getNome()) });
								controleJogo.info(Html.silver(txt));
								break;
							case 4:
								txt = Lang.msg("051", new String[] {
										Html.bold(getNome()),
										Html.bold(carroPilotoDaFrente
												.getPiloto().getNome()) });
								controleJogo.info(Html.silver(txt));
								break;

							default:
								break;
							}
						}
					}
				}
			}
		} else {
			getCarro().setGiro(Carro.GIRO_NOR_VAL);
		}
		if (getCarro().verificaCondicoesCautelaGiro()
				|| controleJogo.isSafetyCarNaPista() || entrouNoBox()) {
			getCarro().setGiro(Carro.GIRO_MIN_VAL);
		}

	}

	public static void main(String[] args) {
		Logger.logar(1 + (int) (Math.random() * 3));
	}

	private void verificaMudancaRegime(InterfaceJogo controleJogo) {
		if (verificaPilotoDesconcentrado()) {
			return;
		}

		if (jogadorHumano) {
			if (carro.isFritouPneuNaUltimaCurvaBaixa()
					&& !testeHabilidadePilotoHumanoCarro(controleJogo)
					&& !getNoAtual().verificaCruvaBaixa()
					&& Math.random() < controleJogo.getNiveljogo()) {
				if (controleJogo.getNiveljogo() == InterfaceJogo.DIFICIL_NV) {
					setAgressivo(false);
				}
				carro.setFritouPneuNaUltimaCurvaBaixa(false);
				if (AGRESSIVO.equals(modoPilotagem)) {
					if (controleJogo.isChovendo() && Math.random() > 0.950) {
						controleJogo.info(Html.txtRedBold(getNome())
								+ Html.bold(Lang.msg("052")));
					} else if (Math.random() > 0.950) {
						if (Math.random() > 0.5) {
							controleJogo.info(Html.txtRedBold(getNome())
									+ Html.bold(Lang.msg("053")));
						} else {
							controleJogo.info(Html.txtRedBold(getNome())
									+ Html.bold(Lang.msg("054")));
						}
					}
					if (controleJogo.verificaNivelJogo())
						incStress(Math.random() > .5 ? 1 : 0);
				} else if (Math.random() > 0.950) {
					if (controleJogo.isChovendo()) {
						controleJogo.info(Html.bold(getNome())
								+ Html.verde(Lang.msg("055")));
					} else {
						controleJogo.info(Html.bold(getNome())
								+ Html.verde(Lang.msg("056")));
					}
				}

			}
			carro.setFritouPneuNaUltimaCurvaBaixa(false);
			if (AGRESSIVO.equals(modoPilotagem)) {
				setAgressivo(true);
				return;
			}
			if (LENTO.equals(modoPilotagem)) {
				setAgressivo(false);
				return;
			}
		}

		if (testeHabilidadePilotoCarro() && controleJogo.verificaNivelJogo()) {
			if (carro.verificaCondicoesCautela()) {
				agressivo = false;
				if (!Messagens.PILOTO_EM_CAUTELA.equals(msgsBox
						.get(Messagens.PILOTO_EM_CAUTELA))) {
					controleJogo.info(Html
							.superRed(getNome() + Lang.msg("057")));
					msgsBox.put(Messagens.PILOTO_EM_CAUTELA,
							Messagens.PILOTO_EM_CAUTELA);
				}
			} else if (No.CURVA_BAIXA.equals(noAtual.getTipo())) {
				agressivo = false;
			} else {
				agressivo = true;
			}
		} else if (!testeHabilidadePilotoCarro()) {
			if (No.CURVA_BAIXA.equals(noAtual.getTipo())) {
				agressivo = false;
				ciclosDesconcentrado = gerarDesconcentracao((int) (14 * controleJogo
						.getNiveljogo()));
				if (Math.random() > .991 && getPosicao() < 9) {
					controleJogo.info(Html.bold(getNome()) + Lang.msg("058"));
				}
			} else if (No.CURVA_ALTA.equals(noAtual.getTipo())) {
				ciclosDesconcentrado = gerarDesconcentracao((int) (10 * controleJogo
						.getNiveljogo()));
			} else {
				ciclosDesconcentrado = gerarDesconcentracao((int) (4 * controleJogo
						.getNiveljogo()));
			}

			ciclosDesconcentrado *= controleJogo.getNiveljogo();
		}
	}

	private boolean testeHabilidadePilotoHumanoCarro(InterfaceJogo controleJogo) {
		if (danificado()) {
			return false;
		}
		if (Math.random() < controleJogo.getNiveljogo()) {
			return false;
		}
		return carro.testePotencia() && testeHabilidadePiloto();
	}

	public int gerarDesconcentracao(int fator) {
		return (fator + (int) (Math.random() * 5));
	}

	public boolean verificaPilotoDesconcentrado() {
		if (ciclosDesconcentrado <= 0) {
			ciclosDesconcentrado = 0;

			return false;
		}

		ciclosDesconcentrado--;

		return true;
	}

	private int calcularNovoModificador(InterfaceJogo controleJogo) {

		double bonusSecundario = getCarro().getGiro() / 10.0;
		if (controleJogo.isChovendo()) {
			bonusSecundario -= .5;
		}
		if (testeHabilidadePilotoCarro() && agressivo
				&& noAtual.verificaRetaOuLargada()
				&& (Math.random() < controleJogo.getIndexVelcidadeDaPista())
				&& (Math.random() < bonusSecundario)) {
			return 3;
		} else if (getCarro().testePotencia()
				&& noAtual.verificaRetaOuLargada()) {
			return (Math.random() < bonusSecundario ? 3 : 2);
		} else if (testeHabilidadePilotoCarro() && agressivo
				&& noAtual.verificaCruvaAlta()) {
			return 2;
		} else if (getCarro().testePotencia() && !agressivo
				&& noAtual.verificaCruvaAlta()) {
			return (Math.random() < bonusSecundario ? 2 : 1);
		} else if (testeHabilidadePilotoCarro() && agressivo
				&& noAtual.verificaCruvaBaixa()) {
			return (Math.random() < bonusSecundario ? 2 : 1);
		} else {
			if (!carro.testePotencia() && (Math.random() > bonusSecundario)) {
				return 0;
			} else {
				return 1;
			}
		}
	}

	public boolean testeHabilidadePilotoCarro() {
		if (danificado()) {
			return false;
		}

		return carro.testePotencia() && testeHabilidadePiloto();
	}

	public boolean testeHabilidadePiloto() {
		if (danificado()) {
			return false;
		}
		boolean teste = Math.random() < (habilidade / 1000.0);
		return teste;
	}

	private boolean danificado() {
		return carro.verificaDano();
	}

	public void gerarCiclosPadoBox(int porcentCombust, long ciclos, long penal) {
		paradoBox = (int) ((((porcentCombust + penal) * 100) / ciclos)) + 90;
		porcentagemCombustUltimaParadaBox = porcentCombust;
	}

	public int getPorcentagemCombustUltimaParadaBox() {
		if (porcentagemCombustUltimaParadaBox < 0) {
			porcentagemCombustUltimaParadaBox = 0;
		}

		return porcentagemCombustUltimaParadaBox;
	}

	public void processaVoltaNovaBox(InterfaceJogo interfaceJogo) {
		if (getVoltaAtual() == null) {
			Volta volta = new Volta();
			volta.setCiclosInicio(System.currentTimeMillis()
					- (getPtosBox() * interfaceJogo.getTempoCiclo()));
			setVoltaAtual(volta);

			return;
		}
		Volta volta = getVoltaAtual();
		volta.setCiclosFim(System.currentTimeMillis());
		volta.setVoltaBox(true);
		setUltimaVolta(volta);
		voltas.add(volta);
		volta = new Volta();
		volta.setCiclosInicio(System.currentTimeMillis()
				- (getPtosBox() * interfaceJogo.getTempoCiclo()));
		setVoltaAtual(volta);
	}

	public boolean decrementaParadoBox() {
		if (paradoBox < 0) {
			paradoBox = 0;
		}

		if (paradoBox > 0) {
			paradoBox--;
		}

		if (paradoBox == 0) {
			if (saiuDoBoxMilis == 0) {
				saiuDoBoxMilis = System.currentTimeMillis();
			}
			return false;
		}

		return true;
	}

	public boolean entrouNoBox() {
		if (ptosBox == 0) {
			return false;
		}

		return true;
	}

	public void efetuarSaidaBox(InterfaceJogo interfaceJogo) {
		qtdeParadasBox++;
		ptosBox = 0;
		box = false;
		carro.setDanificado(null);
		if (carro.getDurabilidadeAereofolio() <= 0) {
			carro.setDurabilidadeAereofolio(3);
		}
		msgsBox.put(Messagens.BOX_OCUPADO, null);
		msgsBox.put(Messagens.PILOTO_EM_CAUTELA, null);
	}

	public String obterTempoVoltaAtual() {
		if (voltaAtual == null) {
			return "";
		}

		return voltaAtual.obterTempoVoltaFormatado();
	}

	public Volta obterVoltaMaisRapida() {
		if (melhorVolta != null && (voltas.isEmpty())) {
			return melhorVolta;
		}
		if (voltas == null) {
			return null;
		}

		if (voltas.isEmpty()) {
			return null;
		}
		List ordenaVoltas = new ArrayList();
		for (Iterator iterator = voltas.iterator(); iterator.hasNext();) {
			Volta volta = (Volta) iterator.next();
			ordenaVoltas.add(volta);
		}
		Collections.sort(ordenaVoltas, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				Volta v0 = (Volta) arg0;
				Volta v1 = (Volta) arg1;

				return Double.compare(v0.obterTempoVolta(), v1
						.obterTempoVolta());
			}
		});

		return (Volta) ordenaVoltas.get(0);
	}

	public void abandonar() {
		setDesqualificado(true);
		carro.abandonou();

	}

	public Volta getMelhorVolta() {
		return melhorVolta;
	}

	public void setMelhorVolta(Volta melhorVolta) {
		this.melhorVolta = melhorVolta;
	}

	public int calculaDiffParaProximo(InterfaceJogo controleJogo) {
		return controleJogo.calculaDiferencaParaProximo(this);

	}

	public String getAsaBox() {
		return asaBox;
	}

	public void setAsaBox(String asaBox) {
		this.asaBox = asaBox;
	}

	public String getModoPilotagem() {
		return modoPilotagem;
	}

	public void setModoPilotagem(String modoPilotagem) {
		this.modoPilotagem = modoPilotagem;
	}

	public int getStress() {
		return stress;
	}

	public void decStress(int val) {

		if (stress > 0 && (stress - val) > 0
				&& (Math.random() > ((900.0 - getPosicao() * 40) / 1000.0))) {
			stress -= val;
		}
	}

	public void incStress(int val) {
		if (stress < 100 && (stress + val) < 100) {
			if ((Math.random() < ((900.0 - getPosicao() * 40) / 1000.0)))
				stress += val;
		} else {
			setModoPilotagem(NORMAL);
		}
	}

	public void setStress(int stress) {
		this.stress = stress;
	}

	public double calculaConsumoMedioCombust() {
		double valmed = 0;
		for (Iterator iterator = ultsConsumosCombustivel.iterator(); iterator
				.hasNext();) {
			Integer longVal = (Integer) iterator.next();
			valmed += longVal.doubleValue();
		}
		if (ultsConsumosCombustivel.isEmpty())
			return 0;
		return valmed / ultsConsumosCombustivel.size();
	}

	public double calculaConsumoMedioPneu() {
		double valmed = 0;
		for (Iterator iterator = ultsConsumosPneu.iterator(); iterator
				.hasNext();) {
			Integer longVal = (Integer) iterator.next();
			valmed += longVal.doubleValue();
		}
		if (ultsConsumosPneu.isEmpty())
			return 0;
		return valmed / ultsConsumosCombustivel.size();
	}

	public void limparConsumoMedioCombust() {
		ultimoConsumoCombust = null;
		ultsConsumosCombustivel.clear();
	}

	public void limparConsumoMedioPneus() {
		ultimoConsumoPneu = null;
		ultsConsumosPneu.clear();
	}

}
