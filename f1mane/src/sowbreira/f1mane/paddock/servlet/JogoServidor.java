package sowbreira.f1mane.paddock.servlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sowbreira.f1mane.MainFrame;
import sowbreira.f1mane.controles.ControleCorrida;
import sowbreira.f1mane.controles.ControleEstatisticas;
import sowbreira.f1mane.controles.ControleJogoLocal;
import sowbreira.f1mane.controles.InterfaceJogo;
import sowbreira.f1mane.entidades.Carro;
import sowbreira.f1mane.entidades.Piloto;
import sowbreira.f1mane.paddock.entidades.BufferTexto;
import sowbreira.f1mane.paddock.entidades.Comandos;
import sowbreira.f1mane.paddock.entidades.TOs.DadosCriarJogo;
import sowbreira.f1mane.paddock.entidades.TOs.DetalhesJogo;
import sowbreira.f1mane.paddock.entidades.TOs.MsgSrv;
import sowbreira.f1mane.paddock.entidades.TOs.VoltaJogadorOnline;

/**
 * @author Paulo Sobreira Criado em 29/07/2007 as 18:28:27
 */
public class JogoServidor extends ControleJogoLocal implements InterfaceJogo {

	private String nomeJogoServidor;
	private String nomeCriador;
	private long tempoCriacao, tempoInicio, tempoFim;
	/* mapJogadoresOnline.put(apelido, dadosParticiparJogo) */
	private Map mapJogadoresOnline = new HashMap();
	private Map mapJogadoresOnlineTexto = new HashMap();
	/* Chave numVolta , valor lista de VoltaJogadorOnline */
	private Map mapVoltasJogadoresOnline = new HashMap();
	private int contadorVolta = 0;
	private DadosCriarJogo dadosCriarJogo;
	private String estado = Comandos.ESPERANDO_JOGO_COMECAR;
	private int luzes = 5;
	private ControleJogosServer controleJogosServer;
	private ControleClassificacao controleClassificacao;
	private boolean disparouInicio;

	public void processaNovaVolta() {
		super.processaNovaVolta();
		List voltasJogadoresOnline = new ArrayList();
		for (Iterator iter = pilotos.iterator(); iter.hasNext();) {
			Piloto piloto = (Piloto) iter.next();
			if (piloto.isJogadorHumano()) {
				VoltaJogadorOnline voltaJogadorOnline = new VoltaJogadorOnline();
				voltaJogadorOnline.setJogador(piloto.getNomeJogador());
				voltaJogadorOnline.setPiloto(piloto.getNome());
				voltasJogadoresOnline.add(voltaJogadorOnline);
			}
		}
		mapVoltasJogadoresOnline.put(new Integer(contadorVolta++),
				voltasJogadoresOnline);
	}

	public String getNomeCriador() {
		return nomeCriador;
	}

	public void setNomeCriador(String nomeCriador) {
		this.nomeCriador = nomeCriador;
	}

	public JogoServidor(String temporada) throws Exception {
		super(temporada);
	}

	public String getEstado() {
		return estado;
	}

	public Map getMapJogadoresOnlineTexto() {
		return mapJogadoresOnlineTexto;
	}

	public void setControleJogosServer(ControleJogosServer controleJogosServer) {
		this.controleJogosServer = controleJogosServer;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public DadosCriarJogo getDadosCriarJogo() {
		return dadosCriarJogo;
	}

	public void setDadosCriarJogo(DadosCriarJogo dadosCriarJogo) {
		this.dadosCriarJogo = dadosCriarJogo;
	}

	public Map getMapJogadoresOnline() {
		return mapJogadoresOnline;
	}

	public void setMapJogadoresOnline(Map mapJogadoresOnline) {
		this.mapJogadoresOnline = mapJogadoresOnline;
	}

	public String getNomeJogoServidor() {
		return nomeJogoServidor;
	}

	public void setNomeJogoServidor(String nomeJogoServidor) {
		this.nomeJogoServidor = nomeJogoServidor;
	}

	public long getTempoCriacao() {
		return tempoCriacao;
	}

	public void setTempoCriacao(long tempoCriacao) {
		this.tempoCriacao = tempoCriacao;
	}

	public Object adicionarJogador(String apelido,
			DadosCriarJogo dadosParticiparJogo) {
		if (mapJogadoresOnline.containsKey(apelido)) {
			return new MsgSrv("Voc� j� esta neste Jogo");
		}
		for (Iterator iter = mapJogadoresOnline.keySet().iterator(); iter
				.hasNext();) {
			String key = (String) iter.next();
			DadosCriarJogo valor = (DadosCriarJogo) mapJogadoresOnline.get(key);
			if (dadosParticiparJogo.getPiloto().equals(valor.getPiloto())) {
				return new MsgSrv(dadosParticiparJogo.getPiloto()
						+ " j� escolhido por " + key);
			}
		}
		for (Iterator iter = pilotos.iterator(); iter.hasNext();) {
			Piloto piloto = (Piloto) iter.next();
			if (piloto.getNome().equals(dadosParticiparJogo.getPiloto())
					&& piloto.isDesqualificado()) {
				return new MsgSrv(dadosParticiparJogo.getPiloto()
						+ " est� Fora da Corrida");

			}
		}
		mapJogadoresOnline.put(apelido, dadosParticiparJogo);
		mapJogadoresOnlineTexto.put(apelido, new BufferTexto());
		return null;
	}

	public void prepararJogoOnline(DadosCriarJogo dadosCriarJogo) {
		this.dadosCriarJogo = dadosCriarJogo;
		qtdeVoltas = null;
		diffultrapassagem = null;
		tempoCiclo = null;
		veloMaxReta = null;
		habilidade = null;
		potencia = null;
		tempoQualificacao = null;
		circuitoSelecionado = null;

	}

	public void preencherDetalhes(DetalhesJogo detalhesJogo) {
		Map detMap = detalhesJogo.getJogadoresPilotos();
		for (Iterator iter = mapJogadoresOnline.keySet().iterator(); iter
				.hasNext();) {
			String key = (String) iter.next();
			DadosCriarJogo valor = (DadosCriarJogo) mapJogadoresOnline.get(key);
			detMap.put(key, valor.getPiloto());
		}
		detalhesJogo.setDadosCriarJogo(getDadosCriarJogo());
		detalhesJogo.setTempoCriacao(getTempoCriacao());
		detalhesJogo.setNomeCriador(getNomeCriador());
	}

	protected void processarEntradaDados() throws Exception {
		try {
			this.nivelCorrida = dadosCriarJogo.getNivelCorrida();

			qtdeVoltas = dadosCriarJogo.getQtdeVoltas();
			if (qtdeVoltas.intValue() < 22) {
				qtdeVoltas = new Integer(22);
			}
			diffultrapassagem = dadosCriarJogo.getDiffultrapassagem();
			tempoCiclo = dadosCriarJogo.getTempoCiclo();
			if (tempoCiclo.intValue() < 70) {
				tempoCiclo = new Integer(70);
			}
			veloMaxReta = dadosCriarJogo.getVeloMaxReta();
			habilidade = dadosCriarJogo.getHabilidade();
			circuitoSelecionado = dadosCriarJogo.getCircuitoSelecionado();
			if (habilidade.intValue() != 0) {
				if (habilidade.intValue() < 50) {
					habilidade = new Integer(50);
				}
				if (habilidade.intValue() > 99) {
					habilidade = new Integer(99);
				}

				definirHabilidadePadraoPilotos(habilidade.intValue());
			}

			potencia = dadosCriarJogo.getPotencia();
			if (potencia.intValue() != 0) {
				if (potencia.intValue() < 500) {
					potencia = new Integer(500);
				}
				if (potencia.intValue() > 999) {
					potencia = new Integer(999);
				}
				definirPotenciaPadraoCarros(potencia.intValue());
			}

		} catch (Exception e) {
			throw new Exception("Erro na Entrada de dados."
					+ " Preencha os tipos numericos com numeros.");
		}

	}

	public void iniciarJogo() throws Exception {
		if (disparouInicio) {
			return;
		}
		disparouInicio = true;
		controleEstatisticas = new ControleEstatisticas(JogoServidor.this);
		gerenciadorVisual = null;
		this.estado = Comandos.MOSTRANDO_QUALIFY;
		processarEntradaDados();

		carregaRecursos((String) getCircuitos().get(circuitoSelecionado));
		controleCorrida = new ControleCorrida(this, qtdeVoltas.intValue(),
				diffultrapassagem.intValue(), veloMaxReta.intValue(),
				tempoCiclo.intValue());
		setarNivelCorrida();
		controleCorrida.getControleClima().gerarClimaInicial(
				dadosCriarJogo.getClima());
		atualizarJogadoresOnline();
		controleCorrida.gerarGridLargadaSemQualificacao();

		this.estado = Comandos.MOSTRANDO_QUALIFY;
		Thread timer = new Thread(new Runnable() {

			public void run() {
				try {
					Thread.sleep(30000);
					estado = Comandos.CORRIDA_INICIADA;
					tempoInicio = System.currentTimeMillis();
					controleCorrida.iniciarCiclos();
					controleEstatisticas.inicializarThreadConsumidoraInfo(2000);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		});
		timer.start();
	}

	public String getNivelCorrida() {
		return dadosCriarJogo.getNivelCorrida();
	}

	private void atualizarJogadoresOnline() {
		for (Iterator iter = mapJogadoresOnline.keySet().iterator(); iter
				.hasNext();) {
			String key = (String) iter.next();
			DadosCriarJogo dadosParticiparJogo = (DadosCriarJogo) mapJogadoresOnline
					.get(key);
			for (Iterator iterator = pilotos.iterator(); iterator.hasNext();) {
				Piloto piloto = (Piloto) iterator.next();
				if (piloto.getNome().equals(dadosParticiparJogo.getPiloto())) {
					piloto.setNomeJogador(key);
					piloto.setJogadorHumano(true);
					if (Comandos.ESPERANDO_JOGO_COMECAR.equals(estado)) {
						piloto.getCarro()
								.setCombustivel(
										dadosParticiparJogo.getCombustivel()
												.intValue());
						piloto.getCarro().trocarPneus(
								dadosParticiparJogo.getTpPnueu(),
								controleCorrida.getDistaciaCorrida());

					}
				}
				if (piloto.isJogadorHumano()
						&& mapJogadoresOnline.get(piloto.getNomeJogador()) == null) {
					piloto.setNomeJogador("IA");
					piloto.setJogadorHumano(false);
				}
			}

		}

	}

	public String getTipoPeneuBox(Piloto piloto) {
		DadosCriarJogo dadosParticiparJogo = (DadosCriarJogo) mapJogadoresOnline
				.get(piloto.getNomeJogador());
		if (dadosParticiparJogo == null) {
			piloto.setNomeJogador("IA");
			piloto.setJogadorHumano(false);
			return Carro.TIPO_PNEU_DURO;
		}
		return dadosParticiparJogo.getTpPnueu();
	}

	public String getAsaBox(Piloto piloto) {
		DadosCriarJogo dadosParticiparJogo = (DadosCriarJogo) mapJogadoresOnline
				.get(piloto.getNomeJogador());
		if (dadosParticiparJogo == null) {
			piloto.setNomeJogador("IA");
			piloto.setJogadorHumano(false);
			return Carro.ASA_NORMAL;
		}
		return dadosParticiparJogo.getAsa();
	}

	public Integer getCombustBox(Piloto piloto) {
		DadosCriarJogo dadosParticiparJogo = (DadosCriarJogo) mapJogadoresOnline
				.get(piloto.getNomeJogador());
		if (dadosParticiparJogo == null) {
			piloto.setNomeJogador("IA");
			piloto.setJogadorHumano(false);
			return new Integer(100);
		}
		return dadosParticiparJogo.getCombustivel();
	}

	public int setUpJogadorHumano(Piloto pilotoJogador, Object tpPneu,
			Object combust, Object asa) {
		if (Comandos.ESPERANDO_JOGO_COMECAR.equals(estado)) {
			return 0;
		} else {
			return super
					.setUpJogadorHumano(pilotoJogador, tpPneu, combust, asa);
		}
	}

	public void apagarLuz() {
		switch (luzes) {
		case 5:
			estado = Comandos.LUZES5;
			break;
		case 4:
			estado = Comandos.LUZES4;
			break;
		case 3:
			estado = Comandos.LUZES3;
			break;
		case 2:
			estado = Comandos.LUZES2;
			break;
		case 1:
			estado = Comandos.LUZES1;
			break;

		default:
			break;
		}
		if (luzes == 1) {
			Thread thread = new Thread(new Runnable() {

				public void run() {
					try {
						Thread.sleep(100);
						estado = Comandos.CORRIDA_INICIADA;
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			});
			thread.start();

		}
		this.luzes--;
	}

	public void adicionarInfoDireto(String info) {
	}

	public void info(String info) {
		if (Comandos.CORRIDA_INICIADA.equals(estado)) {
			for (Iterator iter = mapJogadoresOnline.keySet().iterator(); iter
					.hasNext();) {
				String key = (String) iter.next();
				BufferTexto bufferTexto = (BufferTexto) mapJogadoresOnlineTexto
						.get(key);
				if (bufferTexto != null) {
					bufferTexto.adicionarTexto(info);
				}

			}
		}
	}

	public void infoPrioritaria(String info) {
		if (Comandos.CORRIDA_INICIADA.equals(estado)) {
			for (Iterator iter = mapJogadoresOnline.keySet().iterator(); iter
					.hasNext();) {
				String key = (String) iter.next();
				BufferTexto bufferTexto = (BufferTexto) mapJogadoresOnlineTexto
						.get(key);
				if (bufferTexto != null) {
					bufferTexto.adicionarTextoPrio(info);
				}

			}
		}
	}

	public void exibirResultadoFinal() {
		controleCorrida.pararThreads();
		Thread timer = new Thread(new Runnable() {

			public void run() {
				try {
					estado = Comandos.MOSTRA_RESULTADO_FINAL;
					tempoFim = System.currentTimeMillis();
					if (dadosCriarJogo.getQtdeVoltas().intValue() > 9
							&& !FACIL.equals(dadosCriarJogo.getNivelCorrida())) {
						controleClassificacao.processaCorrida(tempoInicio,
								tempoFim, mapVoltasJogadoresOnline, pilotos,
								dadosCriarJogo);
					}
					Thread.sleep(60000);
					controleEstatisticas.setConsumidorAtivo(false);
					controleJogosServer.removerJogo(JogoServidor.this);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		});
		timer.start();

	}

	public void atualizaPainel() {
		atualizarJogadoresOnline();
	}

	public void informaMudancaClima() {
	}

	public void atulizaTabelaPosicoes() {
	}

	public void saiuBox(Piloto piloto) {
	}

	public void removerJogador(String apelido) {
		if (apelido == null) {
			return;
		}
		List pilots = getPilotos();
		for (Iterator iter = pilots.iterator(); iter.hasNext();) {
			Piloto piloto = (Piloto) iter.next();
			if (apelido.equals(piloto.getNomeJogador())) {
				piloto.setNomeJogador(null);
				piloto.setJogadorHumano(false);
				mapJogadoresOnline.remove(apelido);
			}
		}

	}

	public Map getMapVoltasJogadoresOnline() {
		return mapVoltasJogadoresOnline;
	}

	public void setControleClassificacao(
			ControleClassificacao controleClassificacao) {
		this.controleClassificacao = controleClassificacao;
	}
}
