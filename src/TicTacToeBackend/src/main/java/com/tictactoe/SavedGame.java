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
@Table(name = "SAVEDGAME")
public class SavedGame {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID")
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "EMAIL", referencedColumnName = "EMAIL")
  private User user;

  @Column(name = "SAVEGAME")
  private String[][] board;

  @Column(name = "previousMove")
  private String previousMove;

  @Column(name = "difficulty")
  private String difficulty;

  @Column(name = "game")
  private String game;

  @Column(name = "name")
  private String name;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String[][] getBoard() {
    return board;
  }

  public void setBoard(String[][] board) {
    this.board = board;
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

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPreviousMove() {
    return this.previousMove;
  }

  public void setPreviousMove(String previousMove) {
    this.previousMove = previousMove;
  }
}