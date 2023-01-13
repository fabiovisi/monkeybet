package macaco.coleta.test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import macaco.coleta.bean.Equilibrio;
import macaco.coleta.negocio.ManipulaOdds;

public class TesteEquilibrio {

	public static void main(String[] args) throws IOException {
		
		Equilibrio equilibrio = ManipulaOdds.calculaEquilibrio("2.35", "2.90", "2.90");
		
		Double eq = Double.parseDouble(equilibrio.getDiferença());
		
		//Double roundDbl = Math.round(eq*100.00)/100.00;
		//System.out.println(roundDbl);
		
		BigDecimal bd = new BigDecimal(eq).setScale(2, RoundingMode.HALF_UP);
		
		
		System.out.println(equilibrio.getEquilibrio());
		System.out.println(bd);
		
		
	}
}
