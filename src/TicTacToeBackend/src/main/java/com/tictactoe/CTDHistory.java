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
@Table(name = "CTDHistory")
public class CTDHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID")
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "EMAIL", referencedColumnName = "EMAIL")
  private User user;

  @Column(name = "difficulty")
  private String difficulty;

  @Column(name = "score", columnDefinition = "DOUBLE")
  private double score;

  @Column(name = "win")
  private int win;

  @Column(name = "lose")
  private int lose;

  @Column(name = "pathNumber")
  private int pathNumber;

  @Column(name = "status")
  private int status;

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public void setPathNumber(int pathNumber) {
    this.pathNumber = pathNumber;
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

  public void setDifficulty(String difficulty) {
    this.difficulty = difficulty;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public int getPathNumber() {
    return pathNumber;
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

  public String getDifficulty() {
    return difficulty;
  }

  public Integer getId() {
    return id;
  }

  public User getUser() {
    return user;
  }
}