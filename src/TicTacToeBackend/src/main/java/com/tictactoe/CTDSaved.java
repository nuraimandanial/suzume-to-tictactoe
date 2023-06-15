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
@Table(name = "CTDSaved")
public class CTDSaved {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID")
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "EMAIL", referencedColumnName = "EMAIL")
  private User user;

  @Column(name = "Map", columnDefinition = "TEXT")
  private String map;

  @Column(name = "Position")
  private int[] position;

  @Column(name = "difficulty")
  private String difficulty;

  @Column(name = "previousMove")
  private int[] previousMove;

  @Column(name = "beforeMove")
  private int beforeMove;

  @Column(name = "stations")
  private String stations;

  @Column(name = "moveIndexBeforeStation")
  private String moveIndexBeforeStation;

  @Column(name = "selectedOptions", columnDefinition = "TEXT")
  private String selectedOptions;

  @Column(name = "moveIndex")
  private int moveIndex;

  @Column(name = "score", columnDefinition = "DOUBLE")
  private double score;

  @Column(name = "win")
  private int win;

  @Column(name = "lose")
  private int lose;

  @Column(name = "name")
  private String name;

  @Column(name = "pathNumber")
  private int pathNumber;

  public void setPathNumber(int pathNumber) {
    this.pathNumber = pathNumber;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setWin(int win) {
    this.win = win;
  }

  public void setLose(int lose) {
    this.lose = lose;
  }

  public void setScore(double score) {
    this.score = score;
  }

  public void setMap(String map) {
    this.map = map;
  }

  public void setPosition(int[] position) {
    this.position = position;
  }

  public void setDifficulty(String difficulty) {
    this.difficulty = difficulty;
  }

  public void setPreviousMove(int[] previousMove) {
    this.previousMove = previousMove;
  }

  public void setBeforeMove(int beforeMove) {
    this.beforeMove = beforeMove;
  }

  public void setStations(String stations) {
    this.stations = stations;
  }

  public void setMoveIndexBeforeStation(String moveIndexBeforeStation) {
    this.moveIndexBeforeStation = moveIndexBeforeStation;
  }

  public void setSelectedOptions(String selectedOptions) {
    this.selectedOptions = selectedOptions;
  }

  public void setMoveIndex(int moveIndex) {
    this.moveIndex = moveIndex;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public int getPathNumber() {
    return pathNumber;
  }

  public String getName() {
    return name;
  }

  public int getWin() {
    return win;
  }

  public int getLose() {
    return lose;
  }

  public double getScore() {
    return score;
  }

  public String getMap() {
    return map;
  }

  public int[] getPosition() {
    return position;
  }

  public String getDifficulty() {
    return difficulty;
  }

  public int[] getPreviousMove() {
    return previousMove;
  }

  public int getBeforeMove() {
    return beforeMove;
  }

  public String getStations() {
    return stations;
  }

  public String getMoveIndexBeforeStation() {
    return moveIndexBeforeStation;
  }

  public String getSelectedOptions() {
    return selectedOptions;
  }

  public int getMoveIndex() {
    return moveIndex;
  }

  public Integer getId() {
    return id;
  }

  public User getUser() {
    return user;
  }
}