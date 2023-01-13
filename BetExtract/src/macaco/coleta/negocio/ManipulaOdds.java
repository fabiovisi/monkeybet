package macaco.coleta.negocio;

import java.math.BigDecimal;
import java.math.RoundingMode;

import macaco.coleta.bean.Equilibrio;

public class ManipulaOdds{

	public static boolean bomEmpate(Float odd1, Float oddx, Float odd2) {

		Double maior = Double.max(odd1, odd2);
		Double menor = Double.min(odd1, odd2);

		Double diferenca = maior - menor;

		if (odd1 > 3.00 || odd2 > 3.00) {
			return false;
		} else if (oddx >= 3.40 || oddx < 2.50) {
			return false;
		} else if (oddx > 3.00 || diferenca >= 0.40) {
			return false;
		}
		return true;
	}
	
	public static String calculaFavoritismo(Float odd1, Float oddx, Float odd2) {
		
		String palpite = "sem palpite";
		
		if(odd1 <= 1.50 || odd2 <= 1.50) {
			palpite = "muito favorito";
		} else if (odd1 > 1.50 || odd2 > 1.50) {
			palpite = "favorito";
		}
		
		return palpite;
		
	}

	public static String duplaHipotese(Float odd1, Float odd2) {

		String retorno;

		if (odd1 <= odd2) {
			retorno = "1X";
		} else {
			retorno = "X2";
		}

		return retorno;
	}

	public static String calculaEquilibrioInverso(String odd1, String oddx, String odd2) {

		String risco = "";

		try {
			Double Odd1 = Double.parseDouble(odd1);
			Double Oddx = Double.parseDouble(oddx);
			Double Odd2 = Double.parseDouble(odd2);

			Double maior = Double.max(Odd1, Odd2);
			Double menor = Double.min(Odd1, Odd2);

			Double diferenca = maior - menor;

			if (Oddx >= 3.50) {
				// risco 4
				risco = "4";
			} else if (Oddx >= 3.20 || diferenca >= 0.25) {
				// risco 3
				risco = "3";
			} else if (Odd1 < 3.00 && Oddx < 3.00 && Odd2 < 3.00 && diferenca < 0.15) {
				// risco 1
				risco = "1";
			} else {
				// risco 2
				risco = "2";
			}

		} catch (Exception e) {
			return risco;
		}

		return risco;

	}

	public static Equilibrio calculaEquilibrio(String odd1, String oddx, String odd2) {

		Equilibrio eq = new Equilibrio();

		try {
			Double Odd1 = Double.parseDouble(odd1);
			Double Oddx = Double.parseDouble(oddx);
			Double Odd2 = Double.parseDouble(odd2);

			Double maior = Double.max(Odd1, Odd2);
			Double menor = Double.min(Odd1, Odd2);

			Double diferenca = maior - menor;
			BigDecimal diferencaArredondada = new BigDecimal(diferenca).setScale(2, RoundingMode.HALF_UP);

			eq.setDiferença(diferencaArredondada.toString());

			if (Oddx >= 3.80 || diferenca > 1.00) {
				// risco 5
				eq.setEquilibrio("5");
			} else if (Oddx >= 3.50) {
				// risco 4
				eq.setEquilibrio("4");
			} else if (Oddx >= 3.20 || diferenca >= 0.25) {
				// risco 3
				eq.setEquilibrio("3");
			} else if (Odd1 < 3.00 && Oddx < 3.00 && Odd2 < 3.00 && diferenca < 0.15) {
				// risco 1
				eq.setEquilibrio("1");
			} else {
				// risco 2
				eq.setEquilibrio("2");
			}

		} catch (Exception e) {
			return eq;
		}

		return eq;

	}
	
	public static String calculaPalpilte(Float odd1, Float oddx, Float odd2) {
		
		String palpite;
		
		if(bomEmpate(odd1, oddx, odd2)) {
			palpite = "empate";
		} else {
			palpite = calculaFavoritismo(odd1, oddx, odd2);
		}
		
		return palpite;
	}

}
