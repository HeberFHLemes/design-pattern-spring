package com.dio.design_patterns_spring.repository;

import org.springframework.data.repository.CrudRepository;

import com.dio.design_patterns_spring.model.Endereco;

public interface EnderecoRepository extends CrudRepository<Endereco, String>{

}
