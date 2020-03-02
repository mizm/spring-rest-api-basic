package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Integer> {
   Optional<Board> findByDeletedAndId(boolean deleted, int id);
}
