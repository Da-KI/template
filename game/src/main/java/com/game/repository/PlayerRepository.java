package com.game.repository;


import com.game.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;
import java.util.List;

public interface PlayerRepository extends CrudRepository<Player, Long> {

    //(p.name IS NULL OR ... "ORDER BY ?10"

    /*
"(p.name IS NULL OR p.name LIKE ?1 )" +
            "AND (p.title IS NULL OR p.title LIKE ?2) " +
            "AND (p.race IS NULL OR p.race = ?3) " +
            "AND (p.profession IS NULL OR p.profession = ?4) " +
            "AND (p.birthday BETWEEN ?5 AND ?6) " +
            "AND (p.banned IS NULL OR p.banned = ?7) " +
            "AND (p.experience BETWEEN ?8 AND ?9) " +
            "AND (p.level BETWEEN ?10 AND ?11) "
*/

    @Query("SELECT p FROM Player p WHERE " +
            "(?1 IS NULL OR p.name LIKE %?1% )" +
            "AND (?2 IS NULL OR p.title LIKE %?2%) " +
            "AND (?3 IS NULL OR p.race = ?3) " +
            "AND (?4 IS NULL OR p.profession = ?4) " +
            "AND (p.birthday BETWEEN ?5 AND ?6) " +
            "AND (?7 IS NULL OR p.banned = ?7) " +
            "AND (p.experience BETWEEN ?8 AND ?9) " +
            "AND (p.level BETWEEN ?10 AND ?11) "
    )
    Page<Player> findByParams(String name,
                              String title,
                              String race,
                              String profession,
                              Date after,
                              Date before,
                              Boolean banned,
                              Integer minExperience,
                              Integer maxExperience,
                              Integer minLevel,
                              Integer maxLevel,
                              Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Player p SET p.name = ?2, p.title = ?3, p.race = ?4, p.profession = ?5, p.birthday = ?6," +
            "p.banned = ?7, p.experience = ?8, p.level = ?9, p.untilNextLevel = ?10 WHERE p.id = ?1")
    void updatePlayer(Long id,
                      String name,
                      String title,
                      String race,
                      String profession,
                      Date birthday,
                      Boolean banned,
                      Integer experience,
                      Integer level,
                      Integer untilNextLevel);



}

