package com.tenniscourts.reservations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.guests.GuestService;
import com.tenniscourts.schedules.ScheduleService;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReservationService {

    private ReservationRepository reservationRepository;
    
    private ReservationMapperImpl reservationMapper;
    
    private GuestService guestService;

    private ScheduleService scheduleService;

    public ReservationDTO bookReservation(CreateReservationRequestDTO createReservationRequestDTO) {
        ReservationDTO reservationDTO = new ReservationDTO();      
        reservationDTO.setGuest(guestService.findById(createReservationRequestDTO.getGuestId()));
        reservationDTO.setSchedule(scheduleService.findSchedule(createReservationRequestDTO.getScheduleId()));
        reservationDTO.setValue(BigDecimal.valueOf(0));
        return reservationMapper.map(reservationRepository.save(reservationMapper.map(reservationDTO)));
    }

    public ReservationDTO findReservation(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservationMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation " + reservationId + " not found.");
        });
    }

    public ReservationDTO cancelReservation(Long reservationId) {
        return reservationMapper.map(this.cancel(reservationId));
    }

    private Reservation cancel(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservation -> {

            this.validateCancellation(reservation);

            BigDecimal refundValue = getRefundValue(reservation);
            return this.updateReservation(reservation, refundValue, ReservationStatus.CANCELLED);

        }).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    private Reservation updateReservation(Reservation reservation, BigDecimal refundValue,ReservationStatus status) {
        reservation.setReservationStatus(status);
        reservation.setValue(reservation.getValue().subtract(refundValue));
        reservation.setRefundValue(refundValue);

        return reservationRepository.save(reservation);
    }

    private void validateCancellation(Reservation reservation) {
        if (!ReservationStatus.READY_TO_PLAY.equals(reservation.getReservationStatus())) {
            throw new IllegalArgumentException("Cannot cancel/reschedule because it's not in ready to play status.");
        }

        if (reservation.getSchedule().getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Can cancel/reschedule only future dates.");
        }
    }

    public BigDecimal getRefundValue(Reservation reservation) {
        long hours = ChronoUnit.HOURS.between(LocalDateTime.now(), reservation.getSchedule().getStartDateTime());

        if (hours < 2) {
            // keep 75% and refund 25%
            return BigDecimal.valueOf(reservation.getValue().doubleValue() * BigDecimal.valueOf(0.25).doubleValue());
        }
        else if (hours >= 2 && hours < 12) {
            // keep 50% and refund 50%
            return BigDecimal.valueOf(reservation.getValue().doubleValue() * BigDecimal.valueOf(0.50).doubleValue());
        }
        else if (hours >= 12 && hours < 24) {
            // keep 25% and refund 75%
            return BigDecimal.valueOf(reservation.getValue().doubleValue() * BigDecimal.valueOf(0.75).doubleValue());
        }
        else if (hours >= 24) {
            // refund 100%
            return reservation.getValue();
        }

        return BigDecimal.ZERO;
    }

    public ReservationDTO rescheduleReservation(Long previousReservationId, Long scheduleId) {
        ReservationDTO previousReservationDTO = findReservation(previousReservationId);
        
        if (scheduleId.equals(previousReservationDTO.getSchedule().getId())) {
            throw new IllegalArgumentException("Cannot reschedule to the same slot.");
        }

        Reservation previousReservation = cancel(previousReservationId);

        previousReservation.setReservationStatus(ReservationStatus.RESCHEDULED);
        reservationRepository.save(previousReservation);

        ReservationDTO newReservation = bookReservation(CreateReservationRequestDTO.builder()
                .guestId(previousReservation.getGuest().getId())
                .scheduleId(scheduleId)
                .build());
        newReservation.setPreviousReservation(reservationMapper.map(previousReservation));
        return newReservation;
    }
    
    public ReservationDTO chargeDeposit(Long reservationId, BigDecimal deposit) {
        ReservationDTO reservationDTO = findReservation(reservationId);
        reservationDTO.setValue(deposit);
        return reservationMapper.map(reservationRepository.save(reservationMapper.map(reservationDTO)));
    }

    // public ReservationDTO keepDeposit(Long reservationId) {
    //     ReservationDTO reservationDTO = findReservation(reservationId);
    //     return reservationMapper.map(reservationRepository.save(reservationMapper.map(reservationDTO)));
    // }

    public List<ReservationDTO> getPastReservations() {
        List<Reservation> historicReservations = reservationRepository.findBySchedule_StartDateTimeLessThanEqual(LocalDateTime.now());
        return historicReservations.stream().map(historicReservation -> reservationMapper.map(historicReservation)).collect(Collectors.toList());
    }
}
