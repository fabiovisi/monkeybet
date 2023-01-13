package macaco.coleta.bean;

import java.util.List;

public class Liga {
	
	public Liga() {
	}
	
	private String nomeLiga;
	private List<Partida> partidas;
	private boolean processada;

	public boolean isProcessada() {
		return processada;
	}

	public void setProcessada(boolean processada) {
		this.processada = processada;
	}

	public List<Partida> getPartidas() {
		return partidas;
	}

	public void setPartidas(List<Partida> partidas) {
		this.partidas = partidas;
	}

	public String getNomeLiga() {
		return nomeLiga;
	}

	public void setNomeLiga(String nomeLiga) {
		this.nomeLiga = nomeLiga;
	}
	
}
