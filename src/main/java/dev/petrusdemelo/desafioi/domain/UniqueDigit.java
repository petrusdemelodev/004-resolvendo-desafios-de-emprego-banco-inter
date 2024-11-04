package dev.petrusdemelo.desafioi.domain;

import java.math.BigInteger;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "unique_digits")
@Data
@NoArgsConstructor
public class UniqueDigit implements Cloneable {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false)
  private UUID id;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  @Column(name = "result", nullable = false, columnDefinition = "int")
  private int result;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = true)
  @Setter
  private User user;

  @Column(name = "number", nullable = false, columnDefinition = "TEXT")
  private String number;
  
  @Column(name="k", nullable = false, columnDefinition = "int")
  private int k;

  public UniqueDigit(BigInteger number, Integer k) {
    this.number = String.valueOf(number);
    this.k = k;
    this.result = this.calculateNewUniqueDigit(number, k);
  }

  public int getResult() {
    return result;
  }

  public void setUser(User user) {
    this.user = user;
  }
  
  private int calculateNewUniqueDigit(BigInteger number, Integer k){
    String numberString = String.valueOf(number)
      .repeat(k);

    int uniqueDigit = this.sumOfDigits(numberString);

    while(uniqueDigit > 9){
      uniqueDigit = this.sumOfDigits(String.valueOf(uniqueDigit));
    }

    return uniqueDigit;
  }

  private int sumOfDigits(String numberString){
    int sum = 0;
    for(char digit : numberString.toCharArray()){
      sum += Character.getNumericValue(digit);
    }
    return sum;
  }

  @Override
  public UniqueDigit clone() {
    var newUniqueDigit = new UniqueDigit();
    newUniqueDigit.setResult(this.result);
    newUniqueDigit.setUser(this.user);
    newUniqueDigit.setNumber(this.number);
    newUniqueDigit.setK(this.k);
    return newUniqueDigit;
  }
}
