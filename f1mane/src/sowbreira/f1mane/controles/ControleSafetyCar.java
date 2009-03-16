package sowbreira.f1mane.controles;

import java.util.List;

import br.nnpe.Html;

import sowbreira.f1mane.entidades.No;
import sowbreira.f1mane.entidades.Piloto;
import sowbreira.f1mane.entidades.SafetyCar;

/**
 * @author Paulo Sobreira
 */
public class ControleSafetyCar {
	private ControleCorrida controleCorrida;
	private ControleJogoLocal controleJogo;
	private SafetyCar safetyCar;
	private ThreadRecolihimentoCarro recolihimentoCarro;

	/**
	 * @param controleCorrida
	 * @param controleJogo
	 */
	public ControleSafetyCar(ControleJogoLocal controleJogo,
			ControleCorrida controleCorrida) {
		super();
		this.controleCorrida = controleCorrida;
		this.controleJogo = controleJogo;
		safetyCar = new SafetyCar();
	}

	public boolean isSaftyCarNaPista() {
		return (safetyCar.isNaPista());
	}

	public void safetyCarNaPista(Piloto piloto) {
		if (safetyCar.isNaPista()) {
			return;
		}
		int pts = controleCorrida.calculaQtdePtsPistaPoleParaSaidaBox();
		safetyCar.setNoAtual(controleCorrida.getNoSaidaBox());
		safetyCar.setPtosPista(pts);
		safetyCar.setNaPista(true);
		safetyCar.setVaiProBox(false);
		controleJogo
				.infoPrioritaria(Html
						.saftyCar("Bandeira amarela em toda pista. SafetyCar na pista.Volta "
								+ controleCorrida.voltaAtual()));
		recolihimentoCarro = new ThreadRecolihimentoCarro(controleJogo, piloto,
				safetyCar);
		recolihimentoCarro.start();

	}

	public void processarCiclo() {
		if (!safetyCar.isNaPista()) {
			return;
		}
		if (safetyCar.getNoAtual().isNoEntradaBox() && safetyCar.isVaiProBox()) {
			controleJogo.infoPrioritaria(Html
					.saftyCar("Safety Car no Box. Corrida re-iniciada."));
			safetyCar.setNaPista(false);
			safetyCar.setSaiuVolta(controleJogo.getNumVoltaAtual());
		}

		List pista = controleJogo.getNosDaPista();
		int index = safetyCar.getNoAtual().getIndex();
		No noAtual = safetyCar.getNoAtual();
		int bonus = noAtual.verificaCruvaBaixa() || noAtual.verificaCruvaAlta() ? 1
				: (Math.random() > .8) ? 2 : 1;
		Piloto pole = (Piloto) controleJogo.getPilotos().get(0);
		if (pole.getPtosPista() > safetyCar.getPtosPista()
				&& noAtual.verificaRetaOuLargada()) {
			bonus = (Math.random() > .7) ? 2 : 1;
		}
		if (safetyCar.getPtosPista() > (pole.getPtosPista() + 50)) {
			bonus = (Math.random() > .5) ? 0 : 1;
		}
		index += bonus;
		int diff = index - pista.size();

		/**
		 * Completou Volta
		 */
		if (diff >= 0) {
			index = diff;
		}
		safetyCar.setPtosPista(safetyCar.getPtosPista() + bonus);
		safetyCar.setNoAtual((No) pista.get(index));
	}

	public SafetyCar getSafetyCar() {
		return safetyCar;
	}

	public int calculaModificadorComSafetyCar(Piloto piloto, int novoModificador) {
		if (!isSaftyCarNaPista()) {
			return novoModificador;
		}
		if (piloto.getPosicao() == 1 && controleJogo.isSafetyCarVaiBox()) {
			return 1;
		}
		if (piloto.getPosicao() == 1
				&& piloto.getPtosPista() >= safetyCar.getPtosPista()) {
			piloto.gerarDesconcentracao((22 - piloto.getPosicao()) * 4);
			piloto.setAgressivo(false);
			return Math.random() > .5 ? 1 : 0;
		}
		Piloto pilotoFrente = controleCorrida.acharPilotoDaFrente(piloto);
		if (pilotoFrente.equals(piloto) || pilotoFrente.entrouNoBox()
				|| pilotoFrente.getCarro().verificaDano()) {
			return novoModificador;
		}
		if (pilotoFrente.isDesqualificado()
				|| pilotoFrente.getCarro().verificaDano()) {
			return novoModificador;
		}
		if (piloto.getPtosPista() + novoModificador >= pilotoFrente
				.getPtosPista()) {
			piloto.gerarDesconcentracao((22 - piloto.getPosicao()) * 4);
			piloto.setAgressivo(false);
			return 0;
		}
		return novoModificador;
	}

	public boolean verificaPoleFrenteSafety(Piloto piloto) {
		return piloto.getPtosPista() >= safetyCar.getPtosPista();
	}

	public boolean isSafetyCarVaiBox() {
		return safetyCar.isVaiProBox();
	}

	public boolean safetyCarUltimas3voltas() {
		if (safetyCar.getSaiuVolta() == 0) {
			return false;
		}
		return (controleJogo.getNumVoltaAtual() - safetyCar.getSaiuVolta()) < 3;
	}

	public void matarThreads() {
		if (recolihimentoCarro != null) {
			recolihimentoCarro.interrupt();
		}

	}
}
