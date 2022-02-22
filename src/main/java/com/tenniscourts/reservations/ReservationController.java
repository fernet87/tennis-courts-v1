package com.tenniscourts.reservations;

import java.math.BigDecimal;
import java.util.List;

import com.tenniscourts.config.BaseRestController;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@AllArgsConstructor
@RestController
@RequestMapping("/reservation")
@Api(description = "Set of endpoints for Creating, Retrieving, Cancelling, Rescheduling and changing the deposit value of Reservations.")
public class ReservationController extends BaseRestController {

    private final ReservationService reservationService;

    @PostMapping("/book")
    @ApiOperation("Creates a new reservation.")
    public ResponseEntity<Void> bookReservation(@RequestBody CreateReservationRequestDTO createReservationRequestDTO) {
        return ResponseEntity.created(locationByEntity(reservationService.bookReservation(createReservationRequestDTO).getId())).build();
    }

    @GetMapping("/{id}")
    @ApiOperation("Returns a specific reservation by their identifier. 404 if does not exist.")
    public ResponseEntity<ReservationDTO> findReservation(@PathVariable("id") Long id) {
        return ResponseEntity.ok(reservationService.findReservation(id));
    }

    @PutMapping("/cancel/{id}")
    @ApiOperation("Cancel a specific reservation by their identifier. 404 if does not exist.")
    public ResponseEntity<ReservationDTO> cancelReservation(@PathVariable("id") Long id) {
        return ResponseEntity.ok(reservationService.cancelReservation(id));
    }

    @PutMapping("/reschedule/{id}")
    @ApiOperation("Reschedule a specific reservation by their identifier. 404 if does not exist.")
    public ResponseEntity<ReservationDTO> rescheduleReservation(@PathVariable("id") Long id, @RequestParam("scheduleId") Long scheduleId) {
        return ResponseEntity.ok(reservationService.rescheduleReservation(id, scheduleId));
    }

    @PutMapping("/chargeDeposit/{id}")
    @ApiOperation("Charge a deposit to a specific reservation by their identifier. 404 if does not exist. It accepts as optional parameter the deposit value. $10 is the default if this value is not sent.")
    public ResponseEntity<ReservationDTO> chargeReservationDeposit(@PathVariable("id") Long id, @RequestParam(name = "deposit", required = false, defaultValue = "10") BigDecimal deposit) {
        return ResponseEntity.ok(reservationService.chargeDeposit(id, deposit));
    }
    
    // @PutMapping("/keepDeposit/{id}")
    // @ApiOperation("Keep a deposit from a specific reservation by their identifier. 404 if does not exist.")
    // public ResponseEntity<ReservationDTO> keepReservationDeposit(@PathVariable("id") Long id, @RequestParam(name = "deposit", required = false, defaultValue = "10") BigDecimal deposit) {
    //     return ResponseEntity.ok(reservationService.keepDeposit(id));
    // }
    
    @GetMapping("/history")
    @ApiOperation("Returns a list of past reservations.")
    public ResponseEntity<List<ReservationDTO>> history() {
        return ResponseEntity.ok(reservationService.getPastReservations());
    }
}
