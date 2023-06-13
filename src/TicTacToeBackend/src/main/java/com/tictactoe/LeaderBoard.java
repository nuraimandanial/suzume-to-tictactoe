package com.tictactoe;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "LEADERBOARD")
public class LeaderBoard {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID")
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "EMAIL", referencedColumnName = "EMAIL")
  private User user;

  @Column(name = "username")
  private String userName;

  @Column(name = "game")
  private String game;

  @Column(name = "difficulty")
  private String difficulty;

  @Column(name = "win")
  private int win = 0;

  @Column(name = "lose")
  private int lose = 0;

  @Column(name = "score", columnDefinition = "double default 0")
  private double score = 0;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getDifficulty() {
    return this.difficulty;
  }

  public void setDifficulty(String difficulty) {
    this.difficulty = difficulty;
  }

  public String getGame() {
    return this.game;
  }

  public void setGame(String game) {
    this.game = game;
  }

  public int getWin() {
    return this.win;
  }

  public void setWin(int win) {
    this.win = win;
  }

  public int getLose() {
    return this.lose;
  }

  public void setLose(int lose) {
    this.lose = lose;
  }

  public Double getScore() {
    return this.score;
  }

  public void setScore(double score) {
    this.score = score;
  }
}