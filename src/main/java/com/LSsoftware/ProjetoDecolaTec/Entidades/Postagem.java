package com.LSsoftware.ProjetoDecolaTec.Entidades;

import java.sql.Date;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Postagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String conteudo;
    private Date dataCriacao;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "postagem")
    private List<Comentario> comentarios;

    @ManyToOne
    @JoinColumn(name = "forum_id")
    private Forum forum;

	public Postagem() {
		super();
	}

	public Postagem(Long id, String conteudo, Date dataCriacao, Usuario usuario, List<Comentario> comentarios,
			Forum forum) {
		super();
		this.id = id;
		this.conteudo = conteudo;
		this.dataCriacao = dataCriacao;
		this.usuario = usuario;
		this.comentarios = comentarios;
		this.forum = forum;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Comentario> getComentarios() {
		return comentarios;
	}

	public void setComentarios(List<Comentario> comentarios) {
		this.comentarios = comentarios;
	}

	public Forum getForum() {
		return forum;
	}

	public void setForum(Forum forum) {
		this.forum = forum;
	}

	@Override
	public int hashCode() {
		return Objects.hash(conteudo, dataCriacao, forum, id, usuario);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Postagem other = (Postagem) obj;
		return Objects.equals(conteudo, other.conteudo) && Objects.equals(dataCriacao, other.dataCriacao)
				&& Objects.equals(forum, other.forum) && Objects.equals(id, other.id)
				&& Objects.equals(usuario, other.usuario);
	}

    
}
