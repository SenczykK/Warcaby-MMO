package com.senczyk.lordOfGame.entities;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerListRepository extends JpaRepository<PlayerEntity, Integer> {

}
