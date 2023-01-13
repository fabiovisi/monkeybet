package macaco.coleta.denise;

import java.io.IOException;
import java.util.ArrayList;

import macaco.coleta.motor.MotorDenise;
import macaco.coleta.negocio.ManipulaArquivos;

public class ColetaLigas365 {
	
	static ArrayList<String> ligas;
	
	public static void main(String[] args) throws IOException {
	
		MotorDenise.configuraBet();
	
		MotorDenise.conectar();
		ligas = MotorDenise.carregarLigas();
		MotorDenise.desconectar();
		
		try {
			ManipulaArquivos.salvarLigas(ligas);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

}
