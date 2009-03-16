package sowbreira.f1mane.controles;

import java.util.Iterator;

import br.nnpe.Html;

import sowbreira.f1mane.entidades.Piloto;

/**
 * @author Paulo Sobreira
 */
public class ControleCiclo extends Thread {
	private InterfaceJogo controleJogo;
	private ControleCorrida controleCorrida;
	private int contadorCiclos;
	private long tempoCiclo;
	private boolean processadoCilcos = true;

	public long getTempoCiclo() {
		return tempoCiclo;
	}

	/**
	 * @param controleJogo
	 * @param circuito
	 */
	public ControleCiclo(InterfaceJogo controleJogo,
			ControleCorrida controleCorrida, long tempoCiclo) {
		super();
		this.tempoCiclo = tempoCiclo;
		this.controleJogo = controleJogo;
		this.controleCorrida = controleCorrida;
	}

	public int getContadorCiclos() {
		return contadorCiclos;
	}

	public void setContadorCiclos(int contadorCiclos) {
		this.contadorCiclos = contadorCiclos;
	}

	public boolean isProcessadoCilcos() {
		return processadoCilcos;
	}

	public void setProcessadoCilcos(boolean alive) {
		this.processadoCilcos = alive;
	}

	public void run() {
		try {
			if (ControleJogoLocal.VALENDO) {
				controleJogo.desenhaQualificacao();
				controleJogo
						.infoPrioritaria(Html
								.superGreen("A corrida inicia quando as 5 luzes apagarem"));
				Thread.sleep(5000);
				controleJogo.apagarLuz();
				Thread.sleep(2000);
				controleJogo.apagarLuz();
				Thread.sleep(2000);
				controleJogo.apagarLuz();
				Thread.sleep(2000);
				controleJogo.apagarLuz();
				Thread.sleep(2000);
				controleJogo.apagarLuz();
				Thread.sleep(1000);
			}
			while (processadoCilcos) {
				if (InterfaceJogo.VALENDO && controleCorrida.isCorridaPausada()) {
					Thread.sleep(tempoCiclo);
					continue;
				}
				controleCorrida.processaVoltaSafetyCar();
				for (Iterator iter = controleJogo.getPilotos().iterator(); iter
						.hasNext();) {
					Piloto piloto = (Piloto) iter.next();

					if (piloto.decrementaParadoBox()) {
						continue;
					}

					if (piloto.getPtosBox() == 0) {
						piloto.processarCiclo(controleJogo);
					}

					if (piloto.isBox()) {
						controleCorrida.processarPilotoBox(piloto);
					}
				}

				controleCorrida.atualizaClassificacao();
				controleCorrida.verificaFinalCorrida();
				controleJogo.atualizaPainel();

				if (InterfaceJogo.VALENDO) {
					Thread.sleep(tempoCiclo);
				} else {
					setPriority(Thread.MIN_PRIORITY);
				}

				contadorCiclos++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println((int) (.7 * 5));
	}
}
