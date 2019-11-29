package com.senczyk.lordOfGame.entities;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GamesRepository extends JpaRepository<GameEntity, Integer> {

}
