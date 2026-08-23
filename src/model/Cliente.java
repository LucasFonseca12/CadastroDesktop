package model;

import java.util.Date;

public class Cliente {
	private int id;
	private String nome;
	private String cpf;
	private String email;
	private Date data_nascimento;
	private String senha_hash;
	private String senha_salt;
	private boolean ativo;
	
	
	public Cliente(boolean ativo) {
		super();
		this.setAtivo(true);
	}
	
	
	
	public Cliente(int id, String nome, String cpf, String email, Date data_nascimento, String senha_hash,
			String senha_salt) {
		super();
		this.id = id;
		this.nome = nome;
		this.cpf = cpf;
		this.email = email;
		this.data_nascimento = data_nascimento;
		this.senha_hash = senha_hash;
		this.senha_salt = senha_salt;
	}



	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getCpf() {
		return cpf;
	}


	public void setCpf(String cpf) {
		this.cpf = cpf;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public Date getData_nascimento() {
		return data_nascimento;
	}


	public void setData_nascimento(Date data_nascimento) {
		this.data_nascimento = data_nascimento;
	}


	public String getSenha_hash() {
		return senha_hash;
	}


	public void setSenha_hash(String senha_hash) {
		this.senha_hash = senha_hash;
	}


	public String getSenha_salt() {
		return senha_salt;
	}


	public void setSenha_salt(String senha_salt) {
		this.senha_salt = senha_salt;
	}


	public boolean isAtivo() {
		return ativo;
	}


	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
}