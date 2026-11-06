package com.financorp.serf.repository;

import com.financorp.serf.entity.Filial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilialRepository extends JpaRepository<Filial, Long> {

    List<Filial> findByPais(String pais);

    boolean existsByNombreAndPais(String nombre, String pais);
}