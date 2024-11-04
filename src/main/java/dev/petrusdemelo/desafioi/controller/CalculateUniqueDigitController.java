package dev.petrusdemelo.desafioi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.petrusdemelo.desafioi.controller.dto.CalculateUniqueDigitRequestDTO;
import dev.petrusdemelo.desafioi.services.CalculateUniqueDigitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/calculate")
@RequiredArgsConstructor
public class CalculateUniqueDigitController {
  private final CalculateUniqueDigitService service;
  @PostMapping
  public ResponseEntity<Integer> calculateUniqueDigit(
    @RequestBody @Valid CalculateUniqueDigitRequestDTO calculateUniqueDigitRequestDTO) {
    var response = this.service.calculateUniqueDigit(
      calculateUniqueDigitRequestDTO.getBigIntegerNumber(), 
      calculateUniqueDigitRequestDTO.getK(), 
      calculateUniqueDigitRequestDTO.userID()
    );

    return ResponseEntity.ok(response);
  }
}
