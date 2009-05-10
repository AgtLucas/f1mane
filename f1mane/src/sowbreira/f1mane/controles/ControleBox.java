package sowbreira.f1mane.controles;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sowbreira.f1mane.entidades.Carro;
import sowbreira.f1mane.entidades.No;
import sowbreira.f1mane.entidades.Piloto;
import sowbreira.f1mane.recursos.idiomas.Lang;
import br.nnpe.GeoUtil;
import br.nnpe.Html;

/**
 * @author Paulo Sobreira Criado em 09/06/2007 as 17:17:28
 */
public class ControleBox {
	public static String UMA_OU_MAIS_PARADAS = "UMA_OU_MAIS_PARADAS";
	public static String UMA_PARADA = "UMA_UMA_PARADA";
	private No entradaBox;
	private No saidaBox;
	private No paradaBox;
	private int qtdeNosPistaRefBox;
	private InterfaceJogo controleJogo;
	private ControleCorrida controleCorrida;
	private Map boxEquipes;
	private Hashtable boxEquipesOcupado;

	/**
	 * @param controleJogo
	 * @param controleCorrida
	 * @throws Exception
	 */
	public ControleBox(InterfaceJogo controleJogo,
			ControleCorrida controleCorrida) throws Exception {
		super();
		this.controleJogo = controleJogo;
		this.controleCorrida = controleCorrida;
		calculaNosBox(controleJogo.getNosDaPista(), controleJogo.getNosDoBox());
		calculaQtdeNosPistaRefBox();

		if (saidaBox == null) {
			throw new Exception("Saida box n�o encontrada!");
		}

		geraBoxesEquipes();
	}

	public ControleBox() {
	}

	private void geraBoxesEquipes() {
		boxEquipes = new HashMap();
		boxEquipesOcupado = new Hashtable();

		List carros = controleJogo.getCarros();
		Collections.sort(carros, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				Carro carro0 = (Carro) arg0;
				Carro carro1 = (Carro) arg1;

				return new Integer(carro1.getPotencia()).compareTo(new Integer(
						carro0.getPotencia()));
			}
		});

		List ptosBox = controleJogo.getNosDoBox();
		int indexParada = ptosBox.indexOf(paradaBox);

		for (Iterator iter = carros.iterator(); iter.hasNext();) {
			Carro carro = (Carro) iter.next();

			if (indexParada > (ptosBox.size() - 1)) {
				indexParada = ptosBox.size() - 1;
			}

			boxEquipes.put(carro, ptosBox.get(indexParada));
			boxEquipesOcupado.put(carro, "");
			indexParada += 4;
		}
	}

	public void calculaNosBox(List pontosPista2, List pontosBox2)
			throws Exception {
		No boxEntrada = (No) pontosBox2.get(0);
		No boxSaida = (No) pontosBox2.get(pontosBox2.size() - 1);
		boolean entrada = false;
		boolean saida = false;

		for (Iterator iter = pontosPista2.iterator(); iter.hasNext();) {
			No noPista = (No) iter.next();

			if (GeoUtil.drawBresenhamLine(noPista.getPoint(),
					boxEntrada.getPoint()).size() < 3) {
				if (!entrada) {
					entradaBox = noPista;
				}

				entrada = true;
				noPista.setNoEntradaBox(entrada);
			}

			if (GeoUtil.drawBresenhamLine(noPista.getPoint(),
					boxSaida.getPoint()).size() < 3) {
				saida = true;
				noPista.setNoSaidaBox(saida);
				saidaBox = noPista;
			}
		}

		for (Iterator iter = pontosBox2.iterator(); iter.hasNext();) {
			No noBox = (No) iter.next();

			if (No.PARADA_BOX.equals(noBox.getTipo()) && paradaBox == null) {
				paradaBox = noBox;
			}
		}

		if (paradaBox == null) {
			throw new Exception("Parada de box n�o encontrrada");
		}

		if (!entrada) {
			throw new Exception("Entrada de box n�o encontrrada");
		}

		if (!saida) {
			throw new Exception("Saida de box n�o encontrrada");
		}
	}

	public Hashtable getBoxEquipesOcupado() {
		return boxEquipesOcupado;
	}

	public void processarPilotoBox(Piloto piloto) {
		if (!piloto.getNoAtual().isNoEntradaBox() && (piloto.getPtosBox() <= 0)) {
			return;
		} else {
			if ("".equals(getBoxEquipesOcupado().get(piloto.getCarro()))) {
				boxEquipesOcupado.put(piloto.getCarro(), piloto.getCarro());
			}

			List boxList = controleJogo.getNosDoBox();
			No box = (No) boxEquipes.get(piloto.getCarro());

			if (box.equals(piloto.getNoAtual())
					|| piloto.getNoAtual().isNoEntradaBox()) {
				piloto.setPtosBox(piloto.getPtosBox() + 1);
			} else {
				box = piloto.getNoAtual();
				int ptosBox = 0;
				if (box.isBox()) {
					/**
					 * gera limite velocidade no box
					 */
					ptosBox += ((Math.random() < .7) ? 1 : 0);
				} else if (box.verificaRetaOuLargada()) {
					ptosBox += 2;
				} else if (box.verificaCruvaAlta()) {
					ptosBox += 1;
				} else {
					ptosBox += ((Math.random() < .5) ? 1 : 0);
				}
				piloto.processaVelocidade(ptosBox, piloto.getNoAtual());
				piloto.setPtosBox(ptosBox + piloto.getPtosBox());
			}

			if (piloto.getPtosBox() < boxList.size()) {
				piloto.setNoAtual((No) boxList.get(piloto.getPtosBox()));
			} else {
				processarPilotoSairBox(piloto, controleJogo);
			}
		}

		No box = (No) boxEquipes.get(piloto.getCarro());

		if (box.equals(piloto.getNoAtual()) && !piloto.decrementaParadoBox()) {
			processarPilotoPararBox(piloto);
		}
	}

	private void processarPilotoPararBox(Piloto piloto) {
		piloto.setVelocidade(0);
		int qtdeCombust = 0;
		if (!piloto.isJogadorHumano()) {
			if (piloto.getCarro().verificaDano()) {
				if (controleCorrida.porcentagemCorridaCompletada() < 35) {
					qtdeCombust = setupParadaUnica(piloto);
				} else {
					qtdeCombust = setupDuasOuMaisParadas(piloto);
				}
			} else if (UMA_OU_MAIS_PARADAS.equals(piloto.getSetUpIncial())) {
				qtdeCombust = setupDuasOuMaisParadas(piloto);
			} else {
				qtdeCombust = setupParadaUnica(piloto);
			}
		} else {
			qtdeCombust = controleJogo.setUpJogadorHumano(piloto, controleJogo
					.getTipoPeneuBox(piloto), controleJogo
					.getCombustBox(piloto), controleJogo.getAsaBox(piloto));
		}

		int porcentCombust = (100 * qtdeCombust)
				/ controleCorrida.getTanqueCheio();

		piloto.gerarCiclosPadoBox(porcentCombust, controleCorrida
				.obterTempoCilco());
		piloto.getCarro().ajusteMotorParadaBox();
		piloto.setParouNoBoxMilis(System.currentTimeMillis());
		if (piloto.getNumeroVolta() > 0)
			piloto.processaVoltaNovaBox(controleJogo);
		piloto.setSaiuDoBoxMilis(0);
		if (piloto.isJogadorHumano()) {
			controleJogo
					.infoPrioritaria(Html.orange(Lang.msg("002", new String[] {
							piloto.getNome(),
							String.valueOf(controleJogo.getNumVoltaAtual()) })));
		} else if (piloto.getPosicao() < 9) {
			controleJogo.info(Html.orange(Lang.msg("002", new String[] {
					piloto.getNome(),
					String.valueOf(controleJogo.getNumVoltaAtual()) })));
		}

	}

	private void processarPilotoSairBox(Piloto piloto,
			InterfaceJogo interfaceJogo) {
		piloto.setNoAtual(saidaBox);
		piloto.setPtosPista(piloto.getPtosPista() + qtdeNosPistaRefBox);
		long diff = piloto.getSaiuDoBoxMilis() - piloto.getParouNoBoxMilis();
		String[] strings = new String[] { piloto.getNome(),
				ControleEstatisticas.formatarTempo(diff),
				String.valueOf(piloto.getPorcentagemCombustUltimaParadaBox()),
				Lang.msg(piloto.getCarro().getTipoPneu()) };
		String info = Lang.msg("003", strings);
		if (piloto.isJogadorHumano()) {
			controleJogo.infoPrioritaria(Html.orange(info));
		} else if (piloto.getPosicao() < 9) {
			controleJogo.info(Html.orange(info));
		}

		boxEquipesOcupado.put(piloto.getCarro(), "");

		if (controleJogo.isCorridaTerminada()) {
			piloto.setRecebeuBanderada(true, controleJogo);
		}
		controleJogo.saiuBox(piloto);
		if (controleJogo.isSafetyCarNaPista() && piloto.getVoltaAtual() != null) {
			piloto.getVoltaAtual().setVoltaSafetyCar(true);
		}
		piloto.efetuarSaidaBox(interfaceJogo);

	}

	public int setupParadaUnica(Piloto piloto) {
		if (controleJogo.isChovendo()) {
			piloto.getCarro().trocarPneus(Carro.TIPO_PNEU_CHUVA,
					controleCorrida.getDistaciaCorrida());
			piloto.setSetUpIncial(UMA_OU_MAIS_PARADAS);
		} else {
			piloto.getCarro().trocarPneus(Carro.TIPO_PNEU_DURO,
					controleCorrida.getDistaciaCorrida());
		}

		int diff = controleCorrida.getTanqueCheio()
				- piloto.getCarro().getCombustivel();

		if (controleJogo.getNumVoltaAtual() < 1) {
			piloto.getCarro().setCombustivel(controleCorrida.getTanqueCheio());
		} else {
			int porCompleta = controleCorrida.porcentagemCorridaCompletada();
			int qtdeAbs = 120 - porCompleta;
			int qtdeantes = piloto.getCarro().getCombustivel();
			piloto.getCarro().setCombustivel(
					(controleCorrida.getTanqueCheio() * qtdeAbs) / 100);
			diff = piloto.getCarro().getCombustivel() - qtdeantes;
		}

		return diff;
	}

	public void setupCorridaQualificacaoAleatoria(Piloto piloto, int posicao) {

		if (piloto.isJogadorHumano()) {
			controleJogo.setUpJogadorHumano(piloto, controleJogo
					.getTipoPeneuBox(piloto), controleJogo
					.getCombustBox(piloto), controleJogo.getAsaBox(piloto));

			return;
		}

		if ((Math.random() > .5) && (posicao > 8)) {
			piloto.setSetUpIncial(UMA_PARADA);
			setupParadaUnica(piloto);

		} else {
			piloto.setSetUpIncial(UMA_OU_MAIS_PARADAS);
			setupDuasOuMaisParadas(piloto);

		}
		if (controleCorrida.getControleClima().isClimaAleatorio()) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			int val = 1 + ((int) (Math.random() * 3));
			switch (val) {
			case 1:
				piloto.getCarro().trocarPneus(Carro.TIPO_PNEU_DURO,
						controleCorrida.getDistaciaCorrida());

				break;
			case 2:
				piloto.getCarro().trocarPneus(Carro.TIPO_PNEU_MOLE,
						controleCorrida.getDistaciaCorrida());

				break;

			case 3:
				piloto.getCarro().trocarPneus(Carro.TIPO_PNEU_CHUVA,
						controleCorrida.getDistaciaCorrida());

				break;

			default:
				break;
			}
		}
		int noAlta = 0;
		int noMedia = 0;
		int noBaixa = 0;
		List list = controleJogo.getCircuito().geraPontosPista();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			No no = (No) iterator.next();
			if (no.verificaRetaOuLargada()) {
				noAlta++;
			}
			if (no.verificaCruvaAlta()) {
				noMedia++;
			}
			if (no.verificaCruvaBaixa()) {
				noBaixa++;
			}
		}
		double total = noAlta + noMedia + noBaixa;
		int alta = (int) (100 * noAlta / total);
		int media = (int) (100 * noMedia / total);
		int baixa = (int) (100 * noBaixa / total);
		if (alta >= 78 && piloto.testeHabilidadePilotoCarro()) {
			piloto.getCarro().setAsa(Carro.MENOS_ASA);
		}
		if (baixa >= 15 && piloto.testeHabilidadePilotoCarro()) {
			piloto.getCarro().setAsa(Carro.MAIS_ASA);
		}
		if (media >= 20 && piloto.testeHabilidadePilotoCarro()) {
			piloto.getCarro().setAsa(Carro.ASA_NORMAL);
		}

	}

	public int setupDuasOuMaisParadas(Piloto piloto) {
		if (controleJogo.isChovendo()) {
			piloto.getCarro().trocarPneus(Carro.TIPO_PNEU_CHUVA,
					controleCorrida.getDistaciaCorrida());
		} else {
			piloto.getCarro().trocarPneus(Carro.TIPO_PNEU_MOLE,
					controleCorrida.getDistaciaCorrida());
		}

		int percentagem = 0;

		if (piloto.getQtdeParadasBox() == 0) {
			percentagem = 40 + ((int) (Math.random() * 50));
		} else if (piloto.getQtdeParadasBox() == 1) {
			percentagem = 30 + ((int) (Math.random() * 30));
		} else {
			percentagem = 10 + ((int) (Math.random() * 30));
		}

		if (piloto.getCarro().verificaDano()) {
			percentagem = 70;
		}

		int qtddeCombust = (controleCorrida.getTanqueCheio() * percentagem) / 100;
		int diffCombust = qtddeCombust - piloto.getCarro().getCombustivel();

		if (diffCombust < 0) {
			return 0;
		}

		piloto.getCarro().setCombustivel(
				qtddeCombust + piloto.getCarro().getCombustivel());

		return diffCombust;
	}

	public void calculaQtdeNosPistaRefBox() {
		List ptosPista = controleJogo.getNosDaPista();
		int ateFim = ptosPista.size() - ptosPista.indexOf(entradaBox);
		int ateSaidaBox = ptosPista.indexOf(saidaBox);
		qtdeNosPistaRefBox = ateFim + ateSaidaBox;
	}

	public boolean verificaBoxOcupado(Carro carro) {
		if ((!"".equals(getBoxEquipesOcupado().get(carro)))) {
			return true;
		}

		return false;
	}

	public static void main(String[] args) {
		int val = 1 + ((int) (Math.random() * 3));
		System.out.println(val);
	}

	public No getSaidaBox() {
		return saidaBox;
	}

	public int calculaQtdePtsPistaPoleParaSaidaBox(Piloto pole) {
		List ptsPista = controleJogo.getNosDaPista();
		int diferenca = 0;
		int indexPole = 0;
		int indexSaidaBox = 0;
		for (int i = 0; i < ptsPista.size(); i++) {
			No no = (No) ptsPista.get(i);
			if (no.equals(pole.getNoAtual())) {
				indexPole = i;
			}
			if (no.equals(saidaBox)) {
				indexSaidaBox = i;
			}
		}
		diferenca = pole.getPtosPista() + (indexSaidaBox - indexPole);
		if (indexSaidaBox > indexPole) {
			diferenca = pole.getPtosPista() + (indexSaidaBox - indexPole);
		} else {
			diferenca = pole.getPtosPista()
					+ ((ptsPista.size() - indexPole) + indexSaidaBox);
		}
		return diferenca;
	}
}
