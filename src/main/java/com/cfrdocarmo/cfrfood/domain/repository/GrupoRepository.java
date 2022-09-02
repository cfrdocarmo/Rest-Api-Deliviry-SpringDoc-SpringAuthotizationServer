package com.cfrdocarmo.cfrfood.domain.repository;

import com.cfrdocarmo.cfrfood.domain.model.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {
}
