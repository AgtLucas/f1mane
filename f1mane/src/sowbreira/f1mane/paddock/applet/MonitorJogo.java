package sowbreira.f1mane.paddock.applet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.hibernate.property.MapAccessor;

import com.jhlabs.image.OilFilter;

import sowbreira.f1mane.controles.InterfaceJogo;
import sowbreira.f1mane.entidades.Carro;
import sowbreira.f1mane.entidades.No;
import sowbreira.f1mane.entidades.Piloto;
import sowbreira.f1mane.paddock.entidades.Comandos;
import sowbreira.f1mane.paddock.entidades.TOs.ClientPaddockPack;
import sowbreira.f1mane.paddock.entidades.TOs.DadosJogo;
import sowbreira.f1mane.paddock.entidades.TOs.DadosParciais;
import sowbreira.f1mane.paddock.entidades.TOs.ErroServ;
import sowbreira.f1mane.paddock.entidades.TOs.MsgSrv;
import sowbreira.f1mane.paddock.entidades.TOs.Posis;
import sowbreira.f1mane.paddock.entidades.TOs.PosisPack;
import sowbreira.f1mane.paddock.entidades.TOs.SessaoCliente;
import sowbreira.f1mane.paddock.entidades.TOs.SrvJogoPack;
import sowbreira.f1mane.paddock.entidades.TOs.TravadaRoda;
import sowbreira.f1mane.paddock.entidades.persistencia.CarreiraDadosSrv;
import sowbreira.f1mane.recursos.idiomas.Lang;
import br.nnpe.Constantes;
import br.nnpe.Logger;
import br.nnpe.Util;

/**
 * @author Paulo Sobreira Criado em 05/08/2007 as 11:43:33
 */
public class MonitorJogo implements Runnable {
	private JogoCliente jogoCliente;
	private ControlePaddockCliente controlePaddockCliente;
	private String estado = Comandos.ESPERANDO_JOGO_COMECAR;
	private SessaoCliente sessaoCliente;
	private Thread monitorQualificacao;
	private Thread atualizadorPainel;
	private Thread consumidorPosis = null;
	private Thread threadCmd;
	private boolean jogoAtivo = true;
	private int luz = 5;
	public long lastPosis = 0;
	public boolean procPosis = false;
	private boolean atualizouDados;
	private Vector posisBuffer = new Vector();
	private boolean consumidorAtivo = false;
	private Object[] posisArrayBuff;
	private int sleepConsumidorPosis = 15;
	private boolean lagLongo = false;
	private long ultPoisis;
	private boolean apagarLuzes;
	private long ultLuzApagada;

	public boolean isJogoAtivo() {
		return jogoAtivo;
	}

	public void setJogoAtivo(boolean jogoAtivo) {
		this.jogoAtivo = jogoAtivo;
	}

	public MonitorJogo(JogoCliente local,
			ControlePaddockCliente controlePaddockCliente,
			SessaoCliente sessaoCliente) {
		this.jogoCliente = local;
		this.controlePaddockCliente = controlePaddockCliente;
		this.sessaoCliente = sessaoCliente;
	}

	public void run() {
		boolean interrupt = false;
		while (!interrupt && controlePaddockCliente.isComunicacaoServer()
				&& jogoAtivo) {
			try {
				long tempoCiclo = jogoCliente.getTempoCiclo();
				if (tempoCiclo < controlePaddockCliente.getLatenciaMinima()) {
					tempoCiclo = controlePaddockCliente.getLatenciaMinima();
				}
				jogoCliente.preparaGerenciadorVisual();
				esperaJogoComecar();
				mostraQualify();
				apagaLuzesLargada();
				processaCiclosCorrida(tempoCiclo);
				mostraResultadoFinal(tempoCiclo);
				verificaEstadoJogo();
				Thread.sleep(controlePaddockCliente.getLatenciaMinima());
			} catch (InterruptedException e) {
				interrupt = true;
				if (jogoCliente != null) {
					jogoCliente.matarTodasThreads();
				}
				Logger.logarExept(e);
			}
		}
		if (jogoCliente != null) {
			jogoCliente.matarTodasThreads();
		}

	}

	private void apagaLuzesLargada() {
		if (luz < 0) {
			return;
		}
		if (Comandos.LUZES.equals(estado)
				|| Comandos.CORRIDA_INICIADA.equals(estado)) {
			apagarLuzes = true;
		}
		if (apagarLuzes) {
			int intervalo = 2000;
			intervalo += (luz * controlePaddockCliente.getLatenciaReal());
			if ((System.currentTimeMillis() - ultLuzApagada) < intervalo) {
				return;
			}
			apagarLuz();
			luz--;
			ultLuzApagada = System.currentTimeMillis();
		}
	}

	private void mostraResultadoFinal(long tempoCiclo) {
		boolean interrupt = false;
		while (!interrupt && Comandos.MOSTRA_RESULTADO_FINAL.equals(estado)
				&& controlePaddockCliente.isComunicacaoServer() && jogoAtivo) {
			try {

				List pilotos = jogoCliente.getPilotos();
				for (Iterator iterator = pilotos.iterator(); iterator.hasNext();) {
					Piloto piloto = (Piloto) iterator.next();
					// jogoCliente.adicionarInfoDireto(piloto.getPosicao() + " "
					// + piloto.getNome() + " " + piloto.getCarro().getNome());
					atualizarDadosParciais(jogoCliente.getDadosJogo(), piloto);
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						Logger.logarExept(e);
					}
				}
				atualizarDados();
				jogoCliente.exibirResultadoFinal();
				jogoAtivo = false;
				Thread.sleep(tempoCiclo);
			} catch (InterruptedException e) {
				interrupt = true;
				Logger.logarExept(e);
			}
		}
	}

	private void processaCiclosCorrida(long tempoCiclo) {
		int delayVerificaStado = 20;
		boolean interrupt = false;
		while (!interrupt && Comandos.CORRIDA_INICIADA.equals(estado)
				&& controlePaddockCliente.isComunicacaoServer() && jogoAtivo) {
			try {
				iniciaJalena();
				disparaAtualizadorPainel();
				apagaLuzesLargada();
				if (!atualizouDados) {
					atualizarDados();
					atualizaModoCarreira();
					atualizouDados = true;
				}
				if (monitorQualificacao != null) {
					jogoCliente.pularQualificacao();
					monitorQualificacao = null;
				}

				delayVerificaStado--;
				if (delayVerificaStado <= 0) {
					if (((Piloto) jogoCliente.getPilotos().get(0))
							.getNumeroVolta() != 0) {
						for (Iterator iterator = jogoCliente.getPilotos()
								.iterator(); iterator.hasNext();) {
							Piloto piloto = (Piloto) iterator.next();
							piloto.setVelocidade(1);
						}
					}

					atualizarDadosParciais(jogoCliente.getDadosJogo(),
							jogoCliente.getPilotoSelecionado());
					if (controlePaddockCliente.getLatenciaReal() > 240) {
						jogoCliente.autoDrs();
					}

					if (controlePaddockCliente.getLatenciaReal() > 2000) {
						delayVerificaStado = 5;
					} else {
						delayVerificaStado = 7;
					}
					continue;
				}
				atualizaPosicoes();
				Thread.sleep(tempoCiclo);
			} catch (InterruptedException e) {
				interrupt = true;
				Logger.logarExept(e);
			}
		}
	}

	private void disparaAtualizadorPainel() {
		if (atualizadorPainel == null) {
			atualizadorPainel = new Thread(new Runnable() {
				public void run() {
					while (jogoAtivo) {
						try {
							if (jogoCliente.getPilotoSelecionado() == null)
								jogoCliente.selecionaPilotoJogador();
							jogoCliente.atualizaPainel();
							List pilotos = jogoCliente.getPilotos();
							for (Iterator iterator = pilotos.iterator(); iterator
									.hasNext();) {
								Piloto piloto = (Piloto) iterator.next();
								piloto.decIndiceTracado();
							}
							jogoCliente.verificaProgramacaoBox();
							Thread.sleep(70);
						} catch (Exception e) {
							Logger.logarExept(e);
						}
					}

				}

			});
			atualizadorPainel.start();
		}
	}

	private void mostraQualify() {
		boolean interrupt = false;
		while (!interrupt && Comandos.MOSTRANDO_QUALIFY.equals(estado)
				&& controlePaddockCliente.isComunicacaoServer() && jogoAtivo) {
			verificaEstadoJogo();
			iniciaJalena();
			try {
				if (monitorQualificacao == null) {
					monitorQualificacao = new Thread(new MonitorQualificacao(
							jogoCliente));
					Thread.sleep(1000);
					atualizarDados();
					jogoCliente.atualizaPainel();
					if (jogoCliente.getPilotos() != null) {
						Piloto p = (Piloto) jogoCliente.getPilotos().get(
								Util.intervalo(0, jogoCliente.getPilotos()
										.size() - 1));
						int cont = 0;
						while (p.getPosicao() == 0
								|| p.getCiclosVoltaQualificacao() == 0) {
							if (cont > 5) {
								break;
							}
							Thread.sleep(250);
							atualizarDados();
							cont++;
						}
					}
					monitorQualificacao.start();
				}
				Thread.sleep(100);
			} catch (InterruptedException e) {
				interrupt = true;
				Logger.logarExept(e);
			}

		}
		if (monitorQualificacao != null) {
			jogoCliente.interruptDesenhaQualificao();
			monitorQualificacao.interrupt();
		}
	}

	private void esperaJogoComecar() {
		boolean interupt = false;
		while (!interupt && Comandos.ESPERANDO_JOGO_COMECAR.equals(estado)
				&& controlePaddockCliente.isComunicacaoServer() && jogoAtivo) {
			verificaEstadoJogo();
			jogoCliente.carregaBackGroundCliente();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				interupt = true;
				Logger.logarExept(e);
			}
		}
	}

	private void atualizaModoCarreira() {
		try {
			ClientPaddockPack clientPaddockPack = new ClientPaddockPack(
					Comandos.VER_CARREIRA, sessaoCliente);

			clientPaddockPack.setNomeJogo(jogoCliente.getNomeJogoCriado());
			Object ret = controlePaddockCliente.enviarObjeto(clientPaddockPack);
			if (retornoNaoValido(ret)) {
				return;
			}
			if (ret != null) {
				CarreiraDadosSrv carreiraDadosSrv = (CarreiraDadosSrv) ret;
				if (carreiraDadosSrv.isModoCarreira()) {
					jogoCliente.setNomePilotoJogador(carreiraDadosSrv
							.getNomePiloto());
				}
			}
			clientPaddockPack = new ClientPaddockPack(
					Comandos.DADOS_PILOTOS_JOGO, sessaoCliente);
			clientPaddockPack.setNomeJogo(jogoCliente.getNomeJogoCriado());
			ret = controlePaddockCliente.enviarObjeto(clientPaddockPack);
			if (retornoNaoValido(ret)) {
				return;
			}
			if (ret != null) {
				clientPaddockPack = (ClientPaddockPack) ret;
				if (clientPaddockPack.getDadosJogoCriado().getPilotosCarreira() != null) {
					Logger
							.logar(" Dentro dadosParticiparJogo.getPilotosCarreira()");
					List pilots = clientPaddockPack.getDadosJogoCriado()
							.getPilotosCarreira();
					List carros = new ArrayList();
					for (Iterator iterator = pilots.iterator(); iterator
							.hasNext();) {
						Piloto piloto = (Piloto) iterator.next();
						if (!carros.contains(piloto.getCarro())) {
							carros.add(piloto.getCarro());
						}
					}
					Logger.logar("Tamanho da lista Cliente " + carros.size());
					jogoCliente.geraBoxesEquipes(carros);
				}
			}
		} catch (Exception e) {
			Logger.logarExept(e);
			jogoAtivo = false;
			JOptionPane.showMessageDialog(jogoCliente.getMainFrame(), e
					.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void atualizaPosicoes() {
		try {
			Object ret = controlePaddockCliente.enviarObjeto(jogoCliente
					.getNomeJogoCriado(), true);
			if (retornoNaoValido(ret)) {
				return;
			}
			if (ret != null) {
				String enc = (String) ret;
				PosisPack posisPack = new PosisPack();
				posisPack.decode(enc);
				if (posisPack.safetyNoId != 0) {
					jogoCliente.setSafetyCarBol(true);
					jogoCliente.atualizaPosSafetyCar(posisPack.safetyNoId,
							posisPack.safetySair);
				} else {
					jogoCliente.setSafetyCarBol(false);
				}
				atualizarListaPilotos(posisPack.posis);
			}
		} catch (Exception e) {
			Logger.logarExept(e);
			jogoAtivo = false;
			JOptionPane.showMessageDialog(jogoCliente.getMainFrame(), e
					.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}

	}

	private boolean retornoNaoValido(Object ret) {
		if (ret instanceof ErroServ || ret instanceof MsgSrv) {
			return true;
		}
		return false;
	}

	public void atualizarListaPilotos(Object[] posisArray) {
		if (jogoCliente.getMainFrame().isAtualizacaoSuave()) {
			posisBuffer.add(posisArray);
			if (ultPoisis != 0
					&& (System.currentTimeMillis() - ultPoisis) > 10000) {
				lagLongo = true;
			}

			ultPoisis = System.currentTimeMillis();
			iniciaConsumidorPosis();
		} else {
			consumidorAtivo = false;
			if (posisArray != null) {
				for (int i = 0; i < posisArray.length; i++) {
					Posis posis = (Posis) posisArray[i];
					jogoCliente.atualizaPosicaoPiloto(posis);
				}
			}

		}
	}

	private void iniciaConsumidorPosis() {
		if (consumidorPosis != null && consumidorPosis.isAlive()) {
			return;
		}
		posisArrayBuff = (Object[]) posisBuffer.remove(0);
		if (posisArrayBuff != null) {
			for (int i = 0; i < posisArrayBuff.length; i++) {
				Posis posis = (Posis) posisArrayBuff[i];
				try {
					jogoCliente.atualizaPosicaoPiloto(posis);
				} catch (Exception e) {
					Logger.logarExept(e);
				}
			}
		}
		ultPoisis = 0;
		consumidorAtivo = true;
		consumidorPosis = new Thread(new Runnable() {
			@Override
			public void run() {
				boolean interrupt = false;
				while (!interrupt && jogoAtivo && consumidorAtivo) {
					if (!posisBuffer.isEmpty()) {
						posisArrayBuff = (Object[]) posisBuffer.remove(0);
					}
					if (posisArrayBuff != null) {
						for (int i = 0; i < posisArrayBuff.length; i++) {
							Posis posis = (Posis) posisArrayBuff[i];
							try {
								atualizaPosicaoPiloto(posis);
							} catch (Exception e) {
								Logger.logarExept(e);
							}
						}
					}
					try {
						Thread.sleep(sleepConsumidorPosis);
					} catch (InterruptedException e) {
						interrupt = true;
						Logger.logarExept(e);
					}
				}
			}
		});
		consumidorPosis.start();

	}

	public void atualizaPosicaoPiloto(Posis posis) {
		List pilotos = jogoCliente.getPilotos();
		Map mapaIdsNos = jogoCliente.getMapaIdsNos();
		Map mapaNosIds = jogoCliente.getMapaNosIds();
		for (Iterator iter = pilotos.iterator(); iter.hasNext();) {
			Piloto piloto = (Piloto) iter.next();
			if (piloto.getId() == posis.idPiloto) {
				piloto.setAgressivo(posis.agressivo, jogoCliente);
				piloto.setJogadorHumano(posis.humano);
				piloto.setAutoPos(posis.autoPos);
				if (posis.idNo >= -1) {
					No no = (No) mapaIdsNos.get(new Integer(posis.idNo));
					if (piloto.getNoAtual() == null) {
						piloto.setNoAtual(no);
					} else {
						if (piloto.isJogadorHumano()
								&& jogoCliente.getPilotoJogador()
										.equals(piloto)) {
							jogoCliente.setPosisRec(no);
							jogoCliente.setPosisAtual(piloto.getNoAtual()
									.getPoint());
						}
						if (lagLongo) {
							Logger.logar("lag longo");
							piloto.setNoAtual(no);
							lagLongo = false;
							return;
						}
						int indexPiloto = piloto.getNoAtual().getIndex();
						No noNovo = null;
						int diffINdex = no.getIndex() - indexPiloto;
						if (diffINdex < 0) {
							diffINdex = (no.getIndex() + jogoCliente
									.getNosDaPista().size())
									- indexPiloto;
							if (piloto.isJogadorHumano()) {
								Logger.logar("no.getIndex() " + no.getIndex());
								Logger.logar("indexPiloto " + indexPiloto);
								Logger.logar("diffINdex " + diffINdex);
							}
						}

						double ganhoSuave = 0;
						int maxLoop = 500;
						int incremento = 30;

						if (controlePaddockCliente.getLatenciaReal() > Constantes.LATENCIA_MIN) {
							incremento = 40;
							maxLoop += (2 * (controlePaddockCliente
									.getLatenciaReal()));
						}
						if (controlePaddockCliente.getLatenciaReal() > Constantes.LATENCIA_MAX) {
							incremento = 50;
							maxLoop += (3 * (controlePaddockCliente
									.getLatenciaReal()));
						}

						for (int i = 0; i < maxLoop; i += incremento) {
							if (diffINdex >= i && diffINdex < i + incremento) {
								break;
							}
							ganhoSuave += 1;
						}
						if (diffINdex >= 2000
								&& !(jogoCliente.getNosDoBox().contains(no) && jogoCliente
										.getNosDaPista().contains(
												piloto.getNoAtual()))
								&& !(jogoCliente.getNosDaPista().contains(no) && jogoCliente
										.getNosDoBox().contains(
												piloto.getNoAtual()))) {
							piloto.setNoAtual(no);
							return;
						}
						No noAtual = piloto.getNoAtual();
						boolean entrouNoBox = false;
						if (jogoCliente.getNosDoBox().contains(no)
								&& jogoCliente.getNosDaPista()
										.contains(noAtual)) {
							entrouNoBox = true;
						}
						boolean saiuNoBox = false;
						if (jogoCliente.getNosDaPista().contains(no)
								&& jogoCliente.getNosDoBox().contains(noAtual)) {
							saiuNoBox = true;
						}

						double ganho = ganhoSuave;

						if (entrouNoBox || saiuNoBox) {
							ganho = Math.random() > 0.95 ? 5 : 4;
						}
						if (piloto
								.verificaColisaoCarroFrente(jogoCliente, true)) {
							int tracado = piloto.obterNovoTracadoPossivel();
							if (!piloto.isAutoPos())
								piloto.mudarTracado(tracado, jogoCliente, true);
						} else {
							if (piloto.getIndiceTracado() <= 0) {
								piloto.setTracadoAntigo(piloto.getTracado());
							}
							piloto.setTracado(posis.tracado);
							if (piloto.getIndiceTracado() <= 0
									&& piloto.getTracado() != piloto
											.getTracadoAntigo()) {
								if (piloto.verificaColisaoCarroFrente(
										jogoCliente, true)) {
									piloto.setIndiceTracado(0);
								} else {
									piloto
											.setIndiceTracado((int) (Carro.ALTURA * jogoCliente
													.getCircuito()
													.getMultiplicadorLarguraPista()));
								}
							}
						}

						indexPiloto += ganho;
						if (jogoCliente.getNosDaPista().contains(noAtual)) {
							int diff = indexPiloto
									- jogoCliente.getNosDaPista().size();

							if (diff >= 0) {
								indexPiloto = diff;
							}
							noNovo = (No) jogoCliente.getNosDaPista().get(
									indexPiloto);
						} else if (jogoCliente.getNosDoBox().contains(noAtual)) {
							int diff = indexPiloto
									- jogoCliente.getNosDoBox().size();
							if (diff >= 0) {
								indexPiloto = jogoCliente.getNosDoBox().size() - 1;
							}
							noNovo = (No) jogoCliente.getNosDoBox().get(
									indexPiloto);
						}
						if (entrouNoBox) {
							if ((jogoCliente.getNoEntradaBox().getIndex() - noNovo
									.getIndex()) < 5)
								noNovo = (No) jogoCliente.getNosDoBox().get(0);

						}
						if (saiuNoBox) {
							No ultNoBox = (No) jogoCliente.getNosDoBox().get(
									jogoCliente.getNosDoBox().size() - 1);
							if ((ultNoBox.getIndex() - noNovo.getIndex()) < 5)
								noNovo = (No) jogoCliente.getNosDaPista().get(
										jogoCliente.getCircuito()
												.getSaidaBoxIndex());
						}
						if (noNovo != null)
							piloto.setNoAtual(noNovo);
					}
				}
				break;
			}

		}
	}

	public static void main(String[] args) {
		// int valor = 2000;
		// System.out.println(valor > 1500 && valor <= 2000);

		for (int i = 0; i < 200; i += 5) {
			System.out.println("if (diffINdex >=" + i + "&& diffINdex <"
					+ (i + 5));
		}
		// int cont = 0;
		// for (int i = 0; i < 2000; i += 20) {
		// cont++;
		// }
		// System.out.println(cont);
	}

	private void apagarLuz() {
		iniciaJalena();
		jogoCliente.apagarLuz();
	}

	public void atualizarDados() {
		try {
			ClientPaddockPack clientPaddockPack = new ClientPaddockPack(
					Comandos.OBTER_DADOS_JOGO, sessaoCliente);
			clientPaddockPack.setNomeJogo(jogoCliente.getNomeJogoCriado());
			Object ret = controlePaddockCliente.enviarObjeto(clientPaddockPack);
			if (retornoNaoValido(ret)) {
				return;
			}
			if (ret != null) {
				DadosJogo dadosJogo = (DadosJogo) ret;
				jogoCliente.setDadosJogo(dadosJogo);
			}
		} catch (Exception e) {
			Logger.logarExept(e);
			jogoAtivo = false;
			JOptionPane.showMessageDialog(jogoCliente.getMainFrame(), e
					.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void iniciaJalena() {
		if (jogoAtivo) {
			jogoCliente.iniciaJanela();
		}
	}

	private void verificaEstadoJogo() {
		try {
			ClientPaddockPack clientPaddockPack = new ClientPaddockPack(
					Comandos.VERIFICA_ESTADO_JOGO, sessaoCliente);
			clientPaddockPack.setNomeJogo(jogoCliente.getNomeJogoCriado());
			Object ret = controlePaddockCliente.enviarObjeto(clientPaddockPack);
			if (retornoNaoValido(ret)) {
				return;
			}
			if (ret != null) {
				SrvJogoPack jogoPack = (SrvJogoPack) ret;
				estado = jogoPack.getEstadoJogo();
			}
		} catch (Exception e) {
			Logger.logarExept(e);
			jogoAtivo = false;
			JOptionPane.showMessageDialog(jogoCliente.getMainFrame(), e
					.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}

	}

	public String getEstado() {
		return estado;
	}

	public void abandonar() {
		try {
			jogoCliente.getMainFrame().setVisible(false);
			jogoCliente.matarTodasThreads();
			ClientPaddockPack clientPaddockPack = new ClientPaddockPack(
					Comandos.SAIR_JOGO, sessaoCliente);
			clientPaddockPack.setNomeJogo(jogoCliente.getNomeJogoCriado());
			Object ret = controlePaddockCliente.enviarObjeto(clientPaddockPack);

		} catch (Exception e) {
			Logger.logarExept(e);
			jogoAtivo = false;
		}

	}

	public void atualizarDadosParciais(DadosJogo dadosJogo,
			Piloto pilotoSelecionado) {
		try {
			String dataSend = jogoCliente.getNomeJogoCriado() + "#"
					+ sessaoCliente.getNomeJogador();
			if (pilotoSelecionado != null) {
				dataSend += "#" + pilotoSelecionado.getId();
			}

			Object ret = controlePaddockCliente.enviarObjeto(dataSend, true);
			if (retornoNaoValido(ret)) {
				return;
			}
			if (ret != null) {
				// dec dadosParciais
				String enc = (String) ret;
				DadosParciais dadosParciais = new DadosParciais();
				dadosParciais.decode(enc);
				estado = dadosParciais.estado;
				jogoCliente.verificaMudancaClima(dadosParciais.clima);
				dadosJogo.setClima(dadosParciais.clima);
				dadosJogo.setMelhoVolta(dadosParciais.melhorVolta);
				if (dadosParciais.texto != null
						&& !"".equals(dadosParciais.texto))
					dadosJogo.setTexto(dadosParciais.texto);
				dadosJogo.setVoltaAtual(dadosParciais.voltaAtual);
				List pilotos = dadosJogo.getPilotosList();
				for (Iterator iter = pilotos.iterator(); iter.hasNext();) {
					Piloto piloto = (Piloto) iter.next();
					piloto.setPtosPista(dadosParciais.pilotsPonts[piloto
							.getId() - 1]);
					piloto.setNumeroVolta((int) Math.floor(piloto
							.getPtosPista()
							/ jogoCliente.getNosDaPista().size()));
					long valTsFinal = dadosParciais.pilotsTs[piloto.getId() - 1];
					if (valTsFinal == -1) {
						piloto.getCarro().setRecolhido(true);
					} else if (valTsFinal == -2) {
						if (!piloto.decContTravouRodas()) {
							piloto.setContTravouRodas(2);
							TravadaRoda travadaRoda = new TravadaRoda();
							travadaRoda.setIdNo(this.jogoCliente
									.obterIdPorNo(piloto.getNoAtual()));
							travadaRoda.setTracado(piloto.getTracado());
							jogoCliente.travouRodas(travadaRoda);
						}

					} else {
						piloto.setTimeStampChegeda(valTsFinal);
					}
					if (pilotoSelecionado != null
							&& pilotoSelecionado.equals(piloto)) {
						piloto.setMelhorVolta(dadosParciais.peselMelhorVolta);
						piloto.getVoltas().clear();
						piloto.getVoltas().add(dadosParciais.peselUltima5);
						piloto.getVoltas().add(dadosParciais.peselUltima4);
						piloto.getVoltas().add(dadosParciais.peselUltima3);
						piloto.getVoltas().add(dadosParciais.peselUltima2);
						piloto.getVoltas().add(dadosParciais.peselUltima1);
						piloto.setNomeJogador(dadosParciais.nomeJogador);
						piloto.setQtdeParadasBox(dadosParciais.pselParadas);
						if (piloto.getNomeJogador() != null) {
							piloto.setJogadorHumano(true);
						} else {
							piloto.setJogadorHumano(false);
						}
						piloto.getCarro().setDanificado(dadosParciais.dano);
						if (!jogoCliente.isSafetyCarNaPista()
								&& piloto.isDesqualificado()) {
							piloto.getCarro().setRecolhido(true);
						}
						piloto.setBox(dadosParciais.pselBox);
						piloto.setStress(dadosParciais.pselStress);
						piloto.getCarro().setCargaKers(dadosParciais.cargaKers);
						piloto.getCarro().setTemperaturaMotor(
								dadosParciais.temperaturaMotor);
						if (piloto.getCargaKersOnline() != dadosParciais.cargaKers) {
							piloto.setAtivarKers(true);
							piloto.setCargaKersOnline(dadosParciais.cargaKers);
						} else {
							piloto.setAtivarKers(false);
						}
						piloto.getCarro().setMotor(dadosParciais.pselMotor);
						piloto.getCarro().setPneus(dadosParciais.pselPneus);
						piloto.getCarro().setDurabilidadeMaxPneus(
								dadosParciais.pselMaxPneus);
						piloto.getCarro().setDurabilidadeAereofolio(
								dadosParciais.pselDurAereofolio);
						piloto.getCarro().setCombustivel(
								dadosParciais.pselCombust);
						piloto.getCarro().setAsa(dadosParciais.pselAsaBox);
						piloto.getCarro()
								.setTipoPneu(dadosParciais.pselTpPneus);
						piloto.setVelocidade(dadosParciais.pselVelocidade);
						piloto.setQtdeCombustBox(dadosParciais.pselCombustBox);
						piloto.setTipoPneuBox(dadosParciais.pselTpPneusBox);
						piloto.setModoPilotagem(dadosParciais.pselModoPilotar);
						piloto.setAsaBox(dadosParciais.pselAsaBox);
						piloto.getCarro().setAsa(dadosParciais.pselAsa);
						piloto.getCarro().setGiro(dadosParciais.pselGiro);
					}
				}
				Collections.sort(pilotos, new Comparator() {
					public int compare(Object arg0, Object arg1) {
						Piloto piloto0 = (Piloto) arg0;
						Piloto piloto1 = (Piloto) arg1;
						long p1Val = piloto1.getPtosPista();
						long p0Val = piloto0.getPtosPista();
						if (piloto0.getTimeStampChegeda() != 0
								&& piloto1.getTimeStampChegeda() != 0) {
							Long val = new Long(Long.MAX_VALUE
									- piloto0.getTimeStampChegeda());
							val = new Long(val.toString().substring(
									val.toString().length() / 4,
									val.toString().length()));
							p0Val = (val * piloto0.getNumeroVolta());
							val = new Long(Long.MAX_VALUE
									- piloto1.getTimeStampChegeda());
							val = new Long(val.toString().substring(
									val.toString().length() / 4,
									val.toString().length()));
							p1Val = (val * piloto1.getNumeroVolta());
						}
						return ((p1Val < p0Val) ? (-1) : ((p1Val == p0Val) ? 0
								: 1));
					}
				});

				for (int i = 0; i < pilotos.size(); i++) {
					Piloto piloto = (Piloto) pilotos.get(i);
					piloto.setPosicao(i + 1);
				}
			}
		} catch (Exception e) {
			Logger.logarExept(e);
			jogoAtivo = false;
			JOptionPane.showMessageDialog(jogoCliente.getMainFrame(), e
					.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}

	}

	public void mudarGiroMotor(final Object selectedItem) {
		if (threadCmd != null && threadCmd.isAlive()) {
			return;
		}
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				try {
					String giro = (String) selectedItem;
					if (!Carro.GIRO_MAX.equals(giro)
							&& !Carro.GIRO_MIN.equals(giro)
							&& !Carro.GIRO_NOR.equals(giro)) {
						return;
					}
					ClientPaddockPack clientPaddockPack = new ClientPaddockPack(
							Comandos.MUDAR_GIRO_MOTOR, sessaoCliente);
					clientPaddockPack.setNomeJogo(jogoCliente
							.getNomeJogoCriado());
					clientPaddockPack.setGiroMotor(giro);
					deleyAtualizacaoNaoSuave();
					Object ret = controlePaddockCliente.enviarObjeto(
							clientPaddockPack, true);
				} catch (Exception e) {
					Logger.logarExept(e);
				}
			}
		};
		threadCmd = new Thread(runnable);
		threadCmd.start();

	}

	public void mudarModoBox(boolean modoBox) {
		if (threadCmd != null && threadCmd.isAlive()) {
			return;
		}
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				try {
					ClientPaddockPack clientPaddockPack = new ClientPaddockPack(
							Comandos.MUDAR_MODO_BOX, sessaoCliente);
					clientPaddockPack.setNomeJogo(jogoCliente
							.getNomeJogoCriado());
					clientPaddockPack.setTpPneuBox(jogoCliente
							.getDadosCriarJogo().getTpPnueu());
					clientPaddockPack.setCombustBox(jogoCliente
							.getDadosCriarJogo().getCombustivel().intValue());
					clientPaddockPack.setAsaBox(jogoCliente.getDadosCriarJogo()
							.getAsa());
					deleyAtualizacaoNaoSuave();
					Object ret = controlePaddockCliente.enviarObjeto(
							clientPaddockPack, true);
				} catch (Exception e) {
					Logger.logarExept(e);
				}

			}
		};
		threadCmd = new Thread(runnable);
		threadCmd.start();

	}

	public void mudarModoAgressivo(boolean modoAgressivo) {
		try {
			ClientPaddockPack clientPaddockPack = new ClientPaddockPack(
					Comandos.MUDAR_MODO_AGRESSIVO, sessaoCliente);
			clientPaddockPack.setNomeJogo(jogoCliente.getNomeJogoCriado());
			deleyAtualizacaoNaoSuave();
			Object ret = controlePaddockCliente.enviarObjeto(clientPaddockPack,
					true);
		} catch (Exception e) {
			Logger.logarExept(e);
		}

	}

	public void mudarModoPilotagem(final String modo) {
		if (threadCmd != null && threadCmd.isAlive()) {
			return;
		}
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				try {
					ClientPaddockPack clientPaddockPack = new ClientPaddockPack(
							Comandos.MUDAR_MODO_PILOTAGEM, sessaoCliente);
					clientPaddockPack.setNomeJogo(jogoCliente
							.getNomeJogoCriado());
					clientPaddockPack.setModoPilotagem(modo);
					deleyAtualizacaoNaoSuave();
					Object ret = controlePaddockCliente.enviarObjeto(
							clientPaddockPack, true);
				} catch (Exception e) {
					Logger.logarExept(e);
				}

			}
		};
		threadCmd = new Thread(runnable);
		threadCmd.start();

	}

	public void mudarAutoPos() {
		if (threadCmd != null && threadCmd.isAlive()) {
			return;
		}
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				try {
					ClientPaddockPack clientPaddockPack = new ClientPaddockPack(
							Comandos.MUDAR_MODO_AUTOPOS, sessaoCliente);
					clientPaddockPack.setNomeJogo(jogoCliente
							.getNomeJogoCriado());
					deleyAtualizacaoNaoSuave();
					Object ret = controlePaddockCliente.enviarObjeto(
							clientPaddockPack, true);
				} catch (Exception e) {
					Logger.logarExept(e);
				}

			}
		};
		threadCmd = new Thread(runnable);
		threadCmd.start();

	}

	public void mudarPos(final int tracado) {
		if (threadCmd != null && threadCmd.isAlive()) {
			return;
		}
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				try {
					ClientPaddockPack clientPaddockPack = new ClientPaddockPack(
							Comandos.MUDAR_TRACADO, sessaoCliente);
					clientPaddockPack.setNomeJogo(jogoCliente
							.getNomeJogoCriado());
					clientPaddockPack.setTracado(tracado);
					deleyAtualizacaoNaoSuave();
					Object ret = controlePaddockCliente.enviarObjeto(
							clientPaddockPack, true);
				} catch (Exception e) {
					Logger.logarExept(e);
				}
			}

		};
		threadCmd = new Thread(runnable);
		threadCmd.start();

	}

	private void deleyAtualizacaoNaoSuave() throws InterruptedException {
		if (!jogoCliente.getMainFrame().isAtualizacaoSuave()) {
			Thread.sleep(Util.intervalo(300, 1000));
		}
	}

	public void mudarModoDRS(final boolean modo) {
		if (threadCmd != null && threadCmd.isAlive()) {
			return;
		}
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				try {
					ClientPaddockPack clientPaddockPack = new ClientPaddockPack(
							Comandos.MUDAR_DRS, sessaoCliente);
					clientPaddockPack.setNomeJogo(jogoCliente
							.getNomeJogoCriado());
					clientPaddockPack.setDataObject(new Boolean(modo));
					deleyAtualizacaoNaoSuave();
					Object ret = controlePaddockCliente.enviarObjeto(
							clientPaddockPack, true);
				} catch (Exception e) {
					Logger.logarExept(e);
				}
			}
		};
		threadCmd = new Thread(runnable);
		threadCmd.start();

	}

	public void mudarModoKers(final boolean modo) {
		if (threadCmd != null && threadCmd.isAlive()) {
			return;
		}
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				try {
					ClientPaddockPack clientPaddockPack = new ClientPaddockPack(
							Comandos.MUDAR_KERS, sessaoCliente);
					clientPaddockPack.setNomeJogo(jogoCliente
							.getNomeJogoCriado());
					clientPaddockPack.setDataObject(modo);
					deleyAtualizacaoNaoSuave();
					Object ret = controlePaddockCliente.enviarObjeto(
							clientPaddockPack, true);
				} catch (Exception e) {
					Logger.logarExept(e);
				}
			}
		};
		threadCmd = new Thread(runnable);
		threadCmd.start();

	}

	public void driveThru(final Piloto pilotoSelecionado) {
		if (pilotoSelecionado == null
				|| !pilotoSelecionado.isJogadorHumano()
				|| sessaoCliente.getNomeJogador().equals(
						pilotoSelecionado.getNomeJogador())) {
			jogoCliente.adicionarInfoDireto(Lang
					.msg("selecionePilotoDriveThru"));
			return;
		}
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					ClientPaddockPack clientPaddockPack = new ClientPaddockPack(
							Comandos.DRIVE_THRU, sessaoCliente);
					clientPaddockPack.setNomeJogo(jogoCliente
							.getNomeJogoCriado());
					clientPaddockPack.setDataObject(pilotoSelecionado
							.getNomeJogador());
					Object ret = controlePaddockCliente.enviarObjeto(
							clientPaddockPack, true);
				} catch (Exception e) {
					Logger.logarExept(e);
				}
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}

	public void fechaJanela() {
		if (jogoCliente != null && jogoCliente.getMainFrame() != null)
			jogoCliente.getMainFrame().setVisible(false);

	}

	public void matarTodasThreads() {
		if (monitorQualificacao != null) {
			monitorQualificacao.interrupt();
		}
		if (atualizadorPainel != null) {
			atualizadorPainel.interrupt();
		}
		if (consumidorPosis != null) {
			consumidorPosis.interrupt();
		}
		if (threadCmd != null) {
			threadCmd.interrupt();
		}
	}
}
