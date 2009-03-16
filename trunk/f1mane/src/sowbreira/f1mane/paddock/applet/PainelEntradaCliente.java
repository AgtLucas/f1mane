package sowbreira.f1mane.paddock.applet;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import sowbreira.f1mane.MainFrame;
import sowbreira.f1mane.controles.ControleJogoLocal;
import sowbreira.f1mane.entidades.Carro;
import sowbreira.f1mane.entidades.Clima;
import sowbreira.f1mane.entidades.Piloto;
import sowbreira.f1mane.paddock.entidades.TOs.DadosCriarJogo;
import sun.net.www.content.image.png;

/**
 * @author Paulo Sobreira Criado em 29/07/2007 as 17:41:24
 */
public class PainelEntradaCliente {
	private JSpinner spinnerQtdeVoltas;
	private JTextField nomeJogador;
	private JTextField senhaJogo;
	private JComboBox comboBoxPilotoSelecionado;
	private JComboBox comboBoxCircuito;
	private JComboBox comboBoxNivelCorrida;
	private JComboBox comboBoxClimaInicial;
	private JComboBox comboBoxPneuInicial;
	private JComboBox comboBoxAsa;
	private JSpinner spinnerCombustivelInicial;
	private JSpinner spinnerDificuldadeUltrapassagem;
	private JSpinner spinnerIndexVelcidadeEmReta;
	private JSpinner spinnerTempoCiclo;
	private JSpinner spinnerSkillPadraoPilotos;
	private JSpinner spinnerPotenciaPadraoCarros;
	private List pilotos;
	private Map circuitos;
	private MainFrame mainFrame;
	private DadosCriarJogo dadosCriarJogo;
	private String nomeCriador;

	public PainelEntradaCliente(List pilotos, Map circuitos,
			MainFrame mainFrame, String nomeCriador) {
		this.pilotos = pilotos;
		this.circuitos = circuitos;
		this.mainFrame = mainFrame;
		this.nomeCriador = nomeCriador;
	}

	private void gerarPainelCriarJogo(JPanel painelInicio) {
		painelInicio.setLayout(new GridLayout(15, 2));
		JLabel label = new JLabel("N�mero de voltas da corrida (2-72):");
		painelInicio.add(label);
		spinnerQtdeVoltas = new JSpinner();
		spinnerQtdeVoltas.setValue(new Integer(22));
		if (!ControleJogoLocal.VALENDO) {
			spinnerQtdeVoltas.setValue(new Integer(30));
		}

		painelInicio.add(spinnerQtdeVoltas);

		painelInicio.add(new JLabel("Apelido :"));
		nomeJogador = new JTextField(nomeCriador);
		nomeJogador.setEditable(false);
		painelInicio.add(nomeJogador);

		painelInicio.add(new JLabel("Senha(Jogo Privado):"));
		senhaJogo = new JTextField();
		senhaJogo.setEditable(false);
		painelInicio.add(senhaJogo);

		comboBoxPilotoSelecionado = new JComboBox();
		for (Iterator iter = pilotos.iterator(); iter.hasNext();) {
			Piloto piloto = (Piloto) iter.next();
			comboBoxPilotoSelecionado.addItem(piloto);
		}

		painelInicio.add(new JLabel("Selecionar Piloto :"));
		painelInicio.add(comboBoxPilotoSelecionado);

		comboBoxCircuito = new JComboBox();
		for (Iterator iter = circuitos.keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			comboBoxCircuito.addItem(key);
		}
		painelInicio.add(new JLabel("Selecionar Circuito :"));
		painelInicio.add(comboBoxCircuito);

		comboBoxNivelCorrida = new JComboBox();
		comboBoxNivelCorrida.addItem(ControleJogoLocal.NORMAL);
		comboBoxNivelCorrida.addItem(ControleJogoLocal.FACIL);
		comboBoxNivelCorrida.addItem(ControleJogoLocal.DIFICIL);
		painelInicio.add(new JLabel("Nivel da corrida :"));
		painelInicio.add(comboBoxNivelCorrida);

		comboBoxClimaInicial = new JComboBox();
		comboBoxClimaInicial.addItem(new Clima(Clima.SOL));
		comboBoxClimaInicial.addItem(new Clima(Clima.NUBLADO));
		comboBoxClimaInicial.addItem(new Clima(Clima.CHUVA));
		comboBoxClimaInicial.addItem(new Clima(Clima.ALEATORIO));

		painelInicio.add(new JLabel("Clima :"));
		painelInicio.add(comboBoxClimaInicial);

		JLabel tipoPneu = new JLabel("Tipo Pneu :");
		comboBoxPneuInicial = new JComboBox();
		comboBoxPneuInicial.addItem(Carro.TIPO_PNEU_MOLE);
		comboBoxPneuInicial.addItem(Carro.TIPO_PNEU_DURO);
		comboBoxPneuInicial.addItem(Carro.TIPO_PNEU_CHUVA);

		JLabel qtdeComustivel = new JLabel("% Combustivel :");
		spinnerCombustivelInicial = new JSpinner();
		spinnerCombustivelInicial.setValue(new Integer(50));

		JLabel tipoAsa = new JLabel("Ajuste da Asa :");
		comboBoxAsa = new JComboBox();
		comboBoxAsa.addItem(Carro.ASA_NORMAL);
		comboBoxAsa.addItem(Carro.MAIS_ASA);
		comboBoxAsa.addItem(Carro.MENOS_ASA);

		painelInicio.add(tipoPneu);
		painelInicio.add(comboBoxPneuInicial);
		painelInicio.add(tipoAsa);
		painelInicio.add(comboBoxAsa);
		painelInicio.add(qtdeComustivel);
		painelInicio.add(spinnerCombustivelInicial);

		painelInicio.add(new JLabel("Dificuldade Utrapassagem (0-999):"));
		spinnerDificuldadeUltrapassagem = new JSpinner();
		spinnerDificuldadeUltrapassagem.setValue(new Integer(500));
		painelInicio.add(spinnerDificuldadeUltrapassagem);
		painelInicio.add(new JLabel("Index velocidade em reta (0-999):"));
		spinnerIndexVelcidadeEmReta = new JSpinner();
		spinnerIndexVelcidadeEmReta.setValue(new Integer(500));
		painelInicio.add(spinnerIndexVelcidadeEmReta);

		painelInicio.add(new JLabel("Tempo Ciclo (50ms-240ms):"));
		spinnerTempoCiclo = new JSpinner();
		spinnerTempoCiclo.setValue(new Integer(120));
		painelInicio.add(spinnerTempoCiclo);

		painelInicio.add(new JLabel(
				"Habilidade pilotos (0 para realista (0-99)):"));
		spinnerSkillPadraoPilotos = new JSpinner();
		spinnerSkillPadraoPilotos.setValue(new Integer(0));
		painelInicio.add(spinnerSkillPadraoPilotos);

		painelInicio.add(new JLabel(
				"Potencia Carros (0 para realista (0-999)):"));
		spinnerPotenciaPadraoCarros = new JSpinner();
		spinnerPotenciaPadraoCarros.setValue(new Integer(0));
		painelInicio.add(spinnerPotenciaPadraoCarros);

	}

	public boolean gerarDadosCriarJogo(DadosCriarJogo dadosCriarJogo) {
		this.dadosCriarJogo = dadosCriarJogo;
		JPanel painelInicio = new JPanel();
		gerarPainelCriarJogo(painelInicio);

		int ret = JOptionPane.showConfirmDialog(mainFrame, painelInicio,
				"Setup Inicial", JOptionPane.YES_NO_OPTION);
		if (ret != JOptionPane.YES_OPTION) {
			return false;
		}
		while ((((Integer) spinnerQtdeVoltas.getValue()).intValue() < 2)
				|| (((Integer) spinnerCombustivelInicial.getValue()).intValue() == 0)) {
			JOptionPane.showMessageDialog(mainFrame,
					"N�MERO DE VOLTAS DEVE SER INFORMADO!!!",
					"SetUP N�o preenchido corretamente.",
					JOptionPane.INFORMATION_MESSAGE);
			ret = JOptionPane.showConfirmDialog(mainFrame, painelInicio,
					"Setup Inicial", JOptionPane.YES_NO_OPTION);
			spinnerQtdeVoltas.requestFocus();
			if (ret == JOptionPane.NO_OPTION) {
				return false;
			}
		}
		preecherDados();
		return true;
	}

	private void preecherDados() {
		Integer qtdeVoltas = (Integer) spinnerQtdeVoltas.getValue();
		if (qtdeVoltas.intValue() > 72) {
			qtdeVoltas = new Integer(72);
		}
		dadosCriarJogo.setQtdeVoltas(qtdeVoltas);
		dadosCriarJogo
				.setDiffultrapassagem((Integer) spinnerDificuldadeUltrapassagem
						.getValue());
		Integer integerTempoCiclo = (Integer) spinnerTempoCiclo.getValue();
		if (integerTempoCiclo.intValue() < 85) {
			integerTempoCiclo = new Integer(85);
		}
		if (integerTempoCiclo.intValue() > 240) {
			integerTempoCiclo = new Integer(240);
		}
		dadosCriarJogo.setTempoCiclo(integerTempoCiclo);
		dadosCriarJogo.setVeloMaxReta((Integer) spinnerIndexVelcidadeEmReta
				.getValue());
		Integer habilidade = (Integer) spinnerSkillPadraoPilotos.getValue();
		if (habilidade.intValue() > 99) {
			habilidade = new Integer(99);
		}
		dadosCriarJogo.setHabilidade(habilidade);
		Integer potencia = (Integer) spinnerPotenciaPadraoCarros.getValue();
		if (potencia.intValue() > 999) {
			potencia = new Integer(999);
		}
		dadosCriarJogo.setPotencia(potencia);
		String circuitoSelecionado = (String) comboBoxCircuito
				.getSelectedItem();
		dadosCriarJogo.setCircuitoSelecionado(circuitoSelecionado);
		dadosCriarJogo.setNivelCorrida((String) comboBoxNivelCorrida
				.getSelectedItem());
		dadosCriarJogo.setClima((Clima) comboBoxClimaInicial.getSelectedItem());
		preecherDadosCriarJogo(dadosCriarJogo);
	}

	private void preecherDadosCriarJogo(DadosCriarJogo dadosParticiparJogo) {
		String tpPnueu = (String) comboBoxPneuInicial.getSelectedItem();
		Piloto piloto = (Piloto) comboBoxPilotoSelecionado.getSelectedItem();
		String asa = (String) comboBoxAsa.getSelectedItem();
		Integer combustivel = (Integer) spinnerCombustivelInicial.getValue();
		if (combustivel.intValue() > 100) {
			combustivel = new Integer(100);
		}

		dadosParticiparJogo.setSenha(senhaJogo.getText());
		dadosParticiparJogo.setTpPnueu(tpPnueu);
		dadosParticiparJogo.setAsa(asa);
		dadosParticiparJogo.setCombustivel(combustivel);
		dadosParticiparJogo.setPiloto(piloto.getNome());

	}

	public DadosCriarJogo getDadosCriarJogo() {
		return dadosCriarJogo;
	}

	public boolean gerarDadosEntrarJogo(DadosCriarJogo dadosParticiparJogo) {
		JPanel painelInicio = new JPanel();
		gerarPainelParticiparJogo(painelInicio);

		int ret = JOptionPane.showConfirmDialog(mainFrame, painelInicio,
				"Setup Inicial", JOptionPane.YES_NO_OPTION);
		if (ret == JOptionPane.NO_OPTION) {
			return false;
		}
		preecherDadosCriarJogo(dadosParticiparJogo);
		return true;
	}

	private void gerarPainelParticiparJogo(JPanel painelInicio) {
		painelInicio.setLayout(new GridLayout(6, 2));

		painelInicio.add(new JLabel("Apelido :"));
		nomeJogador = new JTextField(nomeCriador);
		nomeJogador.setEditable(false);
		painelInicio.add(nomeJogador);

		painelInicio.add(new JLabel("Senha(Jogo Privado) :"));
		senhaJogo = new JTextField();
		senhaJogo.setEditable(false);
		painelInicio.add(senhaJogo);

		comboBoxPilotoSelecionado = new JComboBox();
		for (Iterator iter = pilotos.iterator(); iter.hasNext();) {
			Piloto piloto = (Piloto) iter.next();
			comboBoxPilotoSelecionado.addItem(piloto);
		}

		painelInicio.add(new JLabel("Selecionar Piloto :"));
		painelInicio.add(comboBoxPilotoSelecionado);

		comboBoxCircuito = new JComboBox();
		for (Iterator iter = circuitos.keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			comboBoxCircuito.addItem(key);
		}

		JLabel tipoPneu = new JLabel("Tipo Pneu :");
		comboBoxPneuInicial = new JComboBox();
		comboBoxPneuInicial.addItem(Carro.TIPO_PNEU_MOLE);
		comboBoxPneuInicial.addItem(Carro.TIPO_PNEU_DURO);
		comboBoxPneuInicial.addItem(Carro.TIPO_PNEU_CHUVA);
		JLabel tipoAsa = new JLabel("Ajuste da Asa :");
		comboBoxAsa = new JComboBox();
		comboBoxAsa.addItem(Carro.ASA_NORMAL);
		comboBoxAsa.addItem(Carro.MAIS_ASA);
		comboBoxAsa.addItem(Carro.MENOS_ASA);
		JLabel qtdeComustivel = new JLabel("% Combustivel :");
		spinnerCombustivelInicial = new JSpinner();
		spinnerCombustivelInicial.setValue(new Integer(50));

		painelInicio.add(tipoPneu);
		painelInicio.add(comboBoxPneuInicial);
		painelInicio.add(tipoAsa);
		painelInicio.add(comboBoxAsa);
		painelInicio.add(qtdeComustivel);
		painelInicio.add(spinnerCombustivelInicial);

	}
}
