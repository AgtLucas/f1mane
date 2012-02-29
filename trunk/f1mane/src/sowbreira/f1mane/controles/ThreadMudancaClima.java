package sowbreira.f1mane.controles;

import sowbreira.f1mane.entidades.Clima;
import br.nnpe.Logger;

/**
 * @author Paulo Sobreira
 */
public class ThreadMudancaClima extends Thread {
	private ControleClima controleClima;
	private boolean processada;

	public boolean isProcessada() {
		return processada;
	}

	public void setProcessada(boolean processada) {
		this.processada = processada;
	}

	/**
	 * @param controleClima
	 */
	public ThreadMudancaClima(ControleClima controleClima) {
		super();
		this.controleClima = controleClima;
	}

	public void run() {
		try {
			if (ControleJogoLocal.VALENDO) {
				sleep(5000 + ((int) (Math.random() * 25000)));
			}

			if (Clima.SOL.equals(controleClima.getClima())
					|| Clima.CHUVA.equals(controleClima.getClima())) {

				controleClima.intervaloNublado();

			} else if (Clima.NUBLADO.equals(controleClima.getClima())) {
				if (Math.random() > controleClima.getControleJogo()
						.getNiveljogo()) {
					controleClima.intervaloSol();
				} else {

					if (controleClima.verificaPossibilidadeChoverNaPista()) {
						controleClima.intervaloChuva();
					} else {
						controleClima.intervaloSol();
					}
				}

			}
			controleClima.informaMudancaClima();
			processada = true;
		} catch (Exception e) {
			Logger.logarExept(e);
		}
	}

}
