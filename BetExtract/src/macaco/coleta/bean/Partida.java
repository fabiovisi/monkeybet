package macaco.coleta.bean;

public class Partida {

	private String data;
	private String hora;
	private String descricao;
	private String odd1;
	private String oddx;
	private String odd2;
	private String dupla;
	private String banca;
	private String palpite;
	
	public String getDupla() {
		return dupla;
	}

	public void setDupla(String dubla) {
		this.dupla = dubla;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getOdd1() {
		return odd1;
	}

	public void setOdd1(String odd1) {
		this.odd1 = odd1;
	}

	public String getOddx() {
		return oddx;
	}

	public void setOddx(String oddx) {
		this.oddx = oddx;
	}

	public String getOdd2() {
		return odd2;
	}

	public void setOdd2(String odd2) {
		this.odd2 = odd2;
	}

	public Partida() {
	}

	public String getBanca() {
		return banca;
	}

	public void setBanca(String banca) {
		this.banca = banca;
	}

	public String getPalpite() {
		return palpite;
	}

	public void setPalpite(String palpite) {
		this.palpite = palpite;
	}
	
}
