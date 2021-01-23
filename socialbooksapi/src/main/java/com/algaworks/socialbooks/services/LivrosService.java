package com.algaworks.socialbooks.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.socialbooks.domain.Comentario;
import com.algaworks.socialbooks.domain.Livro;
import com.algaworks.socialbooks.exceptions.LivroNaoEncontradoException;
import com.algaworks.socialbooks.repository.ComentariosRepository;
import com.algaworks.socialbooks.repository.LivrosRepository;

@Service
public class LivrosService {
	
	@Autowired
	private LivrosRepository livrosRepository;
	
	@Autowired
	private ComentariosRepository comentariosRepository;
	
	public List<Livro> listar() {
		return livrosRepository.findAll();
	}
	
	public Optional<Livro> buscar(Long id) {
		Optional<Livro> livro = livrosRepository.findById(id);
		
		if (!livro.isPresent()) {
			throw new LivroNaoEncontradoException("O livro não foi localizado.");
		}
		
		return livro;
	}
	
	public Livro salvar(Livro livro) {
		livro.setId(null);
		return  livrosRepository.save(livro);
	}
	
	public void deletar(Long id) {
		try {
			livrosRepository.deleteById(id);			
		} catch(EmptyResultDataAccessException ex) {
			throw new LivroNaoEncontradoException("O livro não foi localizado.");
		}
	}
	
	public void atualizar(Livro livro) {
		verificarExistencia(livro);
		livrosRepository.save(livro);
	}

	private void verificarExistencia(Livro livro) {
		buscar(livro.getId());
	}
	
	public Comentario salvarComentario(Long livroId, Comentario comentario) {
		Optional<Livro> livro = buscar(livroId); 
		
		comentario.setLivro(livro.get());
		comentario.setData(new Date());
		
		return comentariosRepository.save(comentario);
	}
	
	public List<Comentario> listarComentario(Long livroId) {
		Optional<Livro> livro = buscar(livroId); 
		
		return livro.get().getComentarios();
	}

}
