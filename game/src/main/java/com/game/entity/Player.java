package com.game.entity;

import com.game.entity.Race;
import com.game.entity.Profession;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

//@Transactional
@Entity
@Table(name = "player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "title")
    private String title;

    @Column(name = "race")
    @Enumerated(EnumType.STRING)
    private Race race;

    @Column(name = "profession")
    @Enumerated(EnumType.STRING)
    private Profession profession;

    @Column(name = "experience")
    private Integer experience;

    @Column(name = "level")
    private Integer level;

    @Column(name = "untilNextLevel")
    private Integer untilNextLevel;

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "banned")
    private Boolean banned;

    public Player() {

    }

    public Player(String name, String title, String race, String profession, Integer experience, Date birthday, Boolean banned) {
        this.name = name;
        this.title = title;
        this.race = Race.valueOf(race);
        this.profession = Profession.valueOf(profession);
        this.experience = experience;
        this.birthday = birthday;
        this.banned = banned;
        this.level = (int) ((Math.sqrt(2500 + (200 * experience)) - 50) / 100);
        this.untilNextLevel = (50 * (level + 1) * (level + 2)) - experience;
    }



    /*
    public Player(String name, String title, Race race, Profession profession, Date birthday, Boolean banned, Integer experience, Integer level, Integer untilNextLevel) {
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.experience = experience;
        this.level = level;
        this.untilNextLevel = untilNextLevel;
        this.birthday = birthday;
        this.banned = banned;
    }

    public Player(Long id, String name, String title, Race race, Profession profession, Integer experience, Integer level, Integer untilNextLevel, Date birthday, Boolean banned) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.experience = experience;
        this.level = level;
        this.untilNextLevel = untilNextLevel;
        this.birthday = birthday;
        this.banned = banned;
    }
     */

    //public Long birthdayL(Date birthday) {return birthday.getTime();}

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Profession getProfession() { return profession; }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public void setUntilNextLevel(Integer untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, title, race, profession, experience, banned, birthday, level, untilNextLevel);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || this.getClass() != obj.getClass()) return false;
        Player player = (Player) obj;
        return id.equals(player.id)
                && name.equals(player.name)
                && title.equals(player.title)
                && race.equals(player.race)
                && profession.equals(player.profession)
                && birthday.equals(player.birthday)
                && experience.equals(player.experience)
                && banned.equals(player.banned)
                && level.equals(player.level)
                && untilNextLevel.equals(player.untilNextLevel);
    }


    @Override
    public String toString() {
        return "{" +
                "\"id\":[" + id +
                "],\"name\":[" + name +
                "],\"title\":[" + title +
                "],\"race\":[" + race +
                "],\"profession\":[" + profession +
                "],\"birthday\":[" + birthday +
                "],\"banned\":[" + banned +
                "],\"experience\":[" + experience +
                "],\"level\":[" + level +
                "],\"untilNextLevel\":[" + untilNextLevel + ']' +
                '}';
    }



}
