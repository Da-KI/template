package com.game.controller;


import com.game.entity.Player;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/rest")
public class PlayerRestController {

    //final Logger logger = LoggerFactory.getLogger(PlayerRestController.class);

    private PlayerService playerService;

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping(path = "/players", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Player>> getAll(@RequestParam Map<String, String> params
            /*
            @RequestParam(name = "name",required = false) String name,
            @RequestParam(name = "title",required = false) String title,
            @RequestParam(name = "race",required = false) Race race,
            @RequestParam(name = "profession",required = false) Profession profession,
            @RequestParam(name = "after",required = false, defaultValue = "0") Long after,
            @RequestParam(name = "before",required = false, defaultValue = "9223372036854775807") Long before,
            @RequestParam(name = "banned",required = false) Boolean banned,
            @RequestParam(name = "minExperience",required = false, defaultValue = "0") Integer minExperience,
            @RequestParam(name = "maxExperience",required = false, defaultValue = "10000000") Integer maxExperience,
            @RequestParam(name = "minLevel",required = false, defaultValue = "0") Integer minLevel,
            @RequestParam(name = "maxLevel",required = false, defaultValue = "214748364") Integer maxLevel,
            @RequestParam(name = "order",required = false, defaultValue = "ID") PlayerOrder order,
            @RequestParam(name = "pageNumber",required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize",required = false, defaultValue = "3") Integer pageSize
            */
            ) {
        //System.out.println(params);
        Page<Player> players = playerService.getAll(params);
        System.out.println(players.getContent());
        return new ResponseEntity<>(players.getContent(), HttpStatus.OK);
    }

    @GetMapping(path = "/players/count")
    public ResponseEntity<Integer> getCount(@RequestParam Map<String, String> params) {
        Page<Player> players = playerService.getAll(params);
        return new ResponseEntity<>(Math.toIntExact(players.getTotalElements()), HttpStatus.OK);
    }

    @PostMapping(path = "/players")
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        if (
                player.getName() == null ||
                player.getTitle() == null ||
                player.getRace() == null ||
                player.getProfession() == null ||
                player.getBirthday() == null ||
                player.getExperience() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!playerService.isParamsValid(player)) {return new ResponseEntity<>(HttpStatus.BAD_REQUEST);}



        return new ResponseEntity<>(playerService.createPlayer(player), HttpStatus.OK);
    }



    /*
    @PostMapping(path = "/players")
    public ResponseEntity<Player> createPlayer(@RequestParam Map<String, String> params) {
        if (
                !params.containsKey("name") ||
                !params.containsKey("title") ||
                !params.containsKey("race") ||
                !params.containsKey("profession") ||
                !params.containsKey("birthday") ||
                !params.containsKey("experience")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String name = params.get("name");
        String title = params.get("title");
        String race = params.get("race");
        String profession = params.get("profession");
        Long date = Long.valueOf(params.get("birthday"));

        //System.out.println(name +" "+ title +" "+ race +" "+ profession );

        if (date < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Date birthday = new Date(date);
        Boolean banned = Boolean.valueOf(params.getOrDefault("banned", "false"));
        Integer experience = Integer.parseInt(params.get("experience"));

        //System.out.println(name +" "+ title +" "+ race +" "+ profession +" "+ experience +" "+ birthday +" "+ banned);

        if (
                name.isEmpty() ||
                name.length() > 12 ||
                title.length() > 30 ||
                experience < 0 ||
                experience > 10000000 ||
                birthday.before(new Date((946684800000L))) ||
                birthday.after(new Date(32535215999000L))
        ) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(playerService.createPlayer(name, title, race, profession, experience, birthday, banned), HttpStatus.OK);

    }
    */

    @GetMapping(value = "/players/{id}")
    public ResponseEntity<Player> get(@PathVariable ("id") Long id) {
        if (this.playerService.isIdValid(id)) {
            Optional<Player> optional = this.playerService.get(id);
            return optional.map(player -> new ResponseEntity<>(player, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        /*try {
            long idL = Long.parseLong(id);
            if (idL <= 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            if (this.playerService.exists(idL)) {
                Player player = this.playerService.get(idL);
                return new ResponseEntity<>(player, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (NumberFormatException nfe) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } */
    }

    @PostMapping(value = "/players/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Player> updatePlayer(@PathVariable (name = "id") Long id, @RequestBody Player player) {

        if (id == null) { return new ResponseEntity<>(player, HttpStatus.OK); }
        if (!this.playerService.isIdValid(id)) { return new ResponseEntity<>(HttpStatus.BAD_REQUEST);}

        if (player.getName() == null &&
                player.getTitle() == null &&
                player.getRace() == null &&
                player.getProfession() == null &&
                player.getBirthday() == null &&
                player.getExperience() == null) {
            return new ResponseEntity<>(this.playerService.getP(id), HttpStatus.OK);
        }

        if (!playerService.isParamsValid(player)) {return new ResponseEntity<>(HttpStatus.BAD_REQUEST);}

        if (!this.playerService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Player playerNew = this.playerService.updatePlayer(id, player);

        return new ResponseEntity<>(playerNew, HttpStatus.OK);

        /*

        }

        if (params.isEmpty()) {
            return new ResponseEntity<>(playerService.getP(id), HttpStatus.OK);
        }

        if (params.get("name").length() > 12
                || params.get("title").length() > 30
                || !new java.sql.Date(Long.parseLong(params.get("birthday"))).after(new java.sql.Date(946684800000L))
                || !new java.sql.Date(Long.parseLong(params.get("birthday"))).before(new java.sql.Date(32535215999000L))
                || Integer.parseInt(params.get("experience")) < 0
                || Integer.parseInt(params.get("experience")) > 10000000
        ) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(playerService.update(id, params), HttpStatus.OK);

         */

        /*
        if (!playerService.isIdValid(id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if((player.getName() == null
                && player.getTitle() == null
                && player.getRace() == null
                && player.getProfession() == null
                && player.getBirthday() == null
                && player.getExperience() == null
                && player.getBanned() == null)) {
            return new ResponseEntity<>(playerService.getP(id), HttpStatus.OK);
        }

        if (!playerService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if ((player.getName() != null && player.getName().length() > 12)
                || (player.getRace() != null && player.getTitle().length() > 30)
                || (player.getBirthday() != null && (!player.getBirthday().after(new java.sql.Date(946684800000L)) || !player.getBirthday().before(new java.sql.Date(32535215999000L))))
                || (player.getExperience() != null && (player.getExperience() < 0 || player.getExperience() > 10000000))) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(playerService.update(id, player), HttpStatus.OK);
         */

        /*
        if (player.getName() != null && player.getName().length() <= 12) {
            //playerService.getP(id).setName(player.getName());
        }
        //else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (player.getTitle() != null && player.getTitle().length() <= 30) {
            //playerService.getP(id).setTitle(player.getTitle());
        }
        //else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        */



        /*
        @RequestParam Map<String, String> params
        if (!playerService.isIdValid(id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!playerService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Player old = playerService.getP(id);

        String name = params.getOrDefault("name", old.getName());
        String title = params.getOrDefault("title", old.getTitle());
        String race = params.getOrDefault("race", old.getRace().name());
        String profession = params.getOrDefault("profession",old.getProfession().name());
        java.sql.Date birthday;

        if (params.containsKey("birthday")) {
            birthday = new java.sql.Date(Long.parseLong(params.get("birthday")));
        }
        else {
            birthday = playerService.getP(id).getBirthday();
        }

        Boolean banned = Boolean.valueOf(params.getOrDefault("banned",  old.getBanned().toString()));
        Integer experience;

        if (params.containsKey("experience")) {
            experience = Integer.parseInt(params.get("experience"));
        }
        else {
            experience = old.getExperience();
        }

        if (
                name.length() > 12
                || title.length() > 30
                || !birthday.after(new java.sql.Date(946684800000L))
                || !birthday.before(new java.sql.Date(32535215999000L))
                || experience < 0
                || experience > 10000000
        ) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        playerService.update(id, name, title, race, profession, birthday, banned, experience);

        return new ResponseEntity<>(playerService.getP(id), HttpStatus.OK);
        */
    }

    @DeleteMapping(value = "/players/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Player> delete(@PathVariable("id") Long id) {
        //logger.info("Delete player ID" + id);
        if (this.playerService.isIdValid(id)) {
            if (this.playerService.exists(id)) {
                this.playerService.delete(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

}
