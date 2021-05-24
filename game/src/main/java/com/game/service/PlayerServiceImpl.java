package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;


import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    @Override
    // неиспользуется?
    public Page<Player> getAll(Pageable pageable) {
        return null;//playerRepository.findAll(pageable);
    }

    @Override
    public Page<Player> getAll(Map<String, String> params) {

        String name = params.getOrDefault("name", null);
        String title = params.getOrDefault("title", null);
        String race = params.getOrDefault("race", null);
        String profession = params.getOrDefault("profession", null);
        java.sql.Date after = params.containsKey("after") ? new java.sql.Date(Long.parseLong(params.get("after"))) : new java.sql.Date(946684800000L);
        java.sql.Date before = params.containsKey("before") ? new java.sql.Date(Long.parseLong(params.get("before"))) : new java.sql.Date(32535215999000L);
        Boolean banned = params.containsKey("banned") ? Boolean.parseBoolean(params.get("banned")) : null;
        Integer minExperience = params.containsKey("minExperience") ? Integer.parseInt(params.get("minExperience")) : 0;
        Integer maxExperience = params.containsKey("maxExperience") ? Integer.parseInt(params.get("maxExperience")) : 10000000;
        Integer minLevel = params.containsKey("minLevel") ? Integer.parseInt(params.get("minLevel")) : 0;
        Integer maxLevel = params.containsKey("maxLevel") ? Integer.parseInt(params.get("maxLevel")) : 10000000;
        PlayerOrder order = PlayerOrder.valueOf(params.getOrDefault("order", "ID").toUpperCase());
        Integer pageNumber = params.containsKey("pageNumber") ? Integer.parseInt(params.get("pageNumber")) : 0;
        Integer pageSize = params.containsKey("pageSize") ? Integer.parseInt(params.get("pageSize")) : 3;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));

        //System.out.println(name +" "+ title +" "+ race +" "+ profession +" "+ after +" "+ before +" "+ banned +" "+ minExperience +" "+ maxExperience +" "+ minLevel +" "+ maxLevel +" "+ order +" "+ pageNumber +" "+ pageSize);

        return playerRepository.findByParams(
                name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel, pageable);
    }

    @Override
    public Player createPlayer(String name, String title, String race, String profession, Integer experience, Date birthday, Boolean banned) {
        Player player = new Player(name, title, race, profession, experience, birthday, banned);
        playerRepository.save(player);
        return player;
    }

    @Override
    public Player createPlayer(Player player) {
        if(player.getBanned() == null) {player.setBanned(false);}
        int level = (int) ((Math.sqrt(2500 + (200 * player.getExperience())) - 50) / 100);
        player.setLevel(level);
        Integer untilNextLevel = (50 * (level + 1) * (level + 2)) - player.getExperience();
        player.setUntilNextLevel(untilNextLevel);
        playerRepository.save(player);
        return player;
    }

    @Override
    public Integer getCount() {
        return Math.toIntExact(playerRepository.count());
    }

    @Override
    public Optional<Player> get(Long id) {
        return playerRepository.findById(id);
    }

    @Override
    public Player getP(Long id) {
        Optional<Player> player = playerRepository.findById(id);
        return player.orElse(null);
    }

    @Override
    public Player updatePlayer(Long id, Player player) {
        //System.out.println("playerNew" + playerRepository.getOne(id).toString());
        Player playerNew = getP(id);
        //System.out.println("player" + player.toString());

        if (player.getName() != null) playerNew.setName(player.getName());
        if (player.getTitle() != null) playerNew.setTitle(player.getTitle());
        if (player.getRace() != null) playerNew.setRace(player.getRace());
        if (player.getProfession() != null) playerNew.setProfession(player.getProfession());
        if (player.getBirthday() != null) playerNew.setBirthday(player.getBirthday());
        if (player.getBanned() != null) playerNew.setBanned(player.getBanned());
        if (player.getExperience() != null) { playerNew.setExperience(player.getExperience());
            int level = (int) ((Math.sqrt(2500 + (200 * player.getExperience())) - 50) / 100);
            playerNew.setLevel(level);
            Integer untilNextLevel = (50 * (level + 1) * (level + 2)) - player.getExperience();
            playerNew.setUntilNextLevel(untilNextLevel);
        }

        return playerRepository.save(playerNew);
        /*
        playerRepository.updatePlayer(id,
                name,
                title,
                player.getRace().name(),
                player.getProfession().name(),
                birthday,
                banned,
                experience,
                level,
                untilNextLevel);
         */

    }

    /*
    playerRepository.getOne(id).getRace().name()
     playerRepository.getOne(id).getProfession().name()
     playerRepository.getOne(id).getBirthday()
     playerRepository.getOne(id).getBanned()
     playerRepository.getOne(id).getExperience()

     */

    @Override
    public Player update(Long id, Map<String, String> params) {

        String name = params.getOrDefault("name", null);

        String title = params.getOrDefault("title", null);

        String race = params.getOrDefault("race", null);

        String profession = params.getOrDefault("profession", null);

        java.sql.Date birthday = params.containsKey("birthday") ? new java.sql.Date(Long.parseLong(params.get("birthday"))) : null;

        Boolean banned = params.containsKey("banned") ? Boolean.parseBoolean(params.get("banned")) : null;

        Integer experience = params.containsKey("experience") ? Integer.parseInt(params.get("experience")) : null;

        Integer level = (int) ((Math.sqrt(2500 + (200 * experience)) - 50) / 100);
        Integer untilNextLevel = (50 * (level + 1) * (level + 2)) - experience;

        playerRepository.updatePlayer(id,
                name,
                title,
                race,
                profession,
                birthday,
                banned,
                experience,
                level,
                untilNextLevel);

        return getP(id);
    }

    @Override
    public void save(Player player) {
        playerRepository.save(player);
    }

    @Override
    public void delete(Long id) {
        playerRepository.deleteById(id);
    }

    @Override
    public boolean exists(Long id) {
        return playerRepository.existsById(id);
    }

    @Override
    public boolean isIdValid(Long id) {
        return id > 0;
    }

    @Override
    public Map<String, String> validateParams(Map<String, String> params) {
        Map<String, String> ret = new HashMap<>();
        ret.put("name", params.getOrDefault("name", null));
        ret.put("title", params.getOrDefault("title", null));
        ret.put("race", params.getOrDefault("race", null));
        ret.put("profession", params.getOrDefault("profession", null));
        ret.put("after", params.getOrDefault("after", "0"));
        ret.put("before", params.getOrDefault("before", "9223372036854775807"));
        ret.put("banned", params.getOrDefault("banned", null));
        ret.put("minExperience", params.getOrDefault("minExperience", "0"));
        ret.put("maxExperience", params.getOrDefault("maxExperience", "10000000"));
        ret.put("minLevel", params.getOrDefault("minLevel", "0"));
        ret.put("maxLevel", params.getOrDefault("maxLevel", "214748364"));
        ret.put("order", params.getOrDefault("order", "ID"));
        ret.put("pageNumber", params.getOrDefault("pageNumber", "0"));
        ret.put("pageSize", params.getOrDefault("pageSize", "3"));

        return ret;
    }

    @Override
    public boolean isParamsValid(@RequestBody Player player) {
        if ((player.getName() != null && (player.getName().isEmpty() || player.getName().length() > 12)) ||
                (player.getTitle() != null && (player.getTitle().length() > 30)) ||
                (player.getBirthday() != null &&  (!player.getBirthday().after(new java.sql.Date(946684800000L)) || !player.getBirthday().before(new java.sql.Date(32535215999000L)))) ||
                (player.getExperience() != null && (player.getExperience() < 0 || player.getExperience() > 10000000))) {
            return false;
        }
        return true;
    }

}
