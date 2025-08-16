package com.forohub.forohub.domain.topico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TopicoRepository extends JpaRepository<Topico, Long> {
    boolean existsByTituloAndMensaje(String titulo, String mensaje);

    @Query("SELECT t FROM Topico t WHERE t.curso = :curso AND YEAR(t.fechaCreacion) = :year")
    Page<Topico> findByCursoAndFechaCreacion_Year(@Param("curso") String curso, @Param("year") Integer year, Pageable paginacion);
}