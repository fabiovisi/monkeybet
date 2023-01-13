package macaco.coleta.test;

import java.io.IOException;

import macaco.coleta.negocio.ManipulaOdds;

public class TesteBomEmpate {

	public static void main(String[] args) throws IOException {
		
		Float odd1 = (float) 2.43;
		Float oddx = (float) 3.10;
		Float odd2 = (float) 2.65;
		
		boolean bomEmpate = ManipulaOdds.bomEmpate(odd1, oddx, odd2);
		
		System.out.println(bomEmpate);
		
	}
}
