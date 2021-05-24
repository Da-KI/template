package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.Date;
import java.util.Map;
import java.util.Optional;

public interface PlayerService {

    Page<Player> getAll(Pageable pageable);

    Page<Player> getAll(Map<String, String> params);

    Player createPlayer(String name, String title, String race, String profession, Integer experience, Date birthday, Boolean banned);

    Player createPlayer(Player player);

    Integer getCount();

    Optional<Player> get(Long id);

    Player getP(Long id);

    //void update(String name, String title, String race, String profession, Date birthday, Boolean banned, Integer experience);

    void save(Player player);

    Player updatePlayer(Long id, Player player);

     Player update(Long id, Map<String, String> params);

    void delete(Long id);

    boolean exists(Long id);

    boolean isIdValid (Long id);

    Map<String, String> validateParams(Map<String, String> params);

    boolean isParamsValid(@RequestBody Player player);
}