package com.tenniscourts.reservations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import com.tenniscourts.MockDataProvider;
import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.guests.Guest;
import com.tenniscourts.guests.GuestService;
import com.tenniscourts.schedules.Schedule;
import com.tenniscourts.schedules.ScheduleService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = ReservationService.class)
public class ReservationServiceTest {

    @InjectMocks
    ReservationService reservationService;

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    GuestService guestService;

    @Mock
    ScheduleService scheduleService;

    @Before
    public void setUp() throws Exception {
      ReflectionTestUtils.setField(reservationService, "reservationMapper", new ReservationMapperImpl());
    }
  
    @Test
    public void getRefundValueFullRefund() {
        Schedule schedule = new Schedule();

        LocalDateTime startDateTime = LocalDateTime.now().plusDays(2);

        schedule.setStartDateTime(startDateTime);

        Assert.assertEquals(reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()), new BigDecimal(10));
    }

    @Test
    public void getRefundValue25PercentRefund() {
        Schedule schedule = new Schedule();

        LocalDateTime startDateTime = LocalDateTime.now().plusHours(1);

        schedule.setStartDateTime(startDateTime);

        Assert.assertEquals(reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()), BigDecimal.valueOf(2.5));
    }  
    
    @Test
    public void getRefundValue50PercentRefund() {
        Schedule schedule = new Schedule();

        LocalDateTime startDateTime = LocalDateTime.now().plusHours(8);

        schedule.setStartDateTime(startDateTime);

        Assert.assertEquals(reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()), BigDecimal.valueOf(5.0));
    }
        
    @Test
    public void getRefundValue75PercentRefund() {
        Schedule schedule = new Schedule();

        LocalDateTime startDateTime = LocalDateTime.now().plusHours(15);

        schedule.setStartDateTime(startDateTime);

        Assert.assertEquals(reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()), BigDecimal.valueOf(7.5));
    }

    @Test
    public void testBookReservation() {
        ReservationDTO reservationMock = ReservationDTO.builder()
            .guest(MockDataProvider.buildGuest1())
            .schedule(MockDataProvider.buildSchedule1())
            .value(BigDecimal.valueOf(0))
            .reservationStatus(ReservationStatus.READY_TO_PLAY.name())
            .build();

        when(guestService.findById(anyLong())).thenReturn(reservationMock.getGuest());
        when(scheduleService.findSchedule(anyLong())).thenReturn(reservationMock.getSchedule());
        when(reservationRepository.save(Mockito.any(Reservation.class))).thenAnswer(i -> i.getArguments()[0]);

        ReservationDTO bookedReservation = reservationService.bookReservation(CreateReservationRequestDTO.builder().guestId(1L).scheduleId(1l).build());
        assertEquals(reservationMock.getReservationStatus(), bookedReservation.getReservationStatus());
        assertEquals(reservationMock.getValue(), bookedReservation.getValue());
        assertEquals(reservationMock.getGuest().getId(), bookedReservation.getGuest().getId());
        assertEquals(reservationMock.getGuest().getName(), bookedReservation.getGuest().getName());
        assertEquals(reservationMock.getSchedule().getId(), bookedReservation.getSchedule().getId());
        assertEquals(reservationMock.getSchedule().getStartDateTime(), bookedReservation.getSchedule().getStartDateTime());
        assertEquals(reservationMock.getSchedule().getEndDateTime(), bookedReservation.getSchedule().getEndDateTime());
    }
    
    @Test
    public void testFindReservationNotFoundException() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            reservationService.findReservation(1L);
        });
        
        assertTrue(exception.getMessage().contains("Reservation 1 not found."));
    }
        
    @Test
    public void testCancelReservationIllegalArgumentException() {
        Schedule schedule = Schedule.builder().startDateTime(LocalDateTime.now().minusHours(5)).build();
        Reservation reservation = Reservation.builder().reservationStatus(ReservationStatus.RESCHEDULED).schedule(schedule).build();

        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.cancelReservation(1L);
        });
        
        assertTrue(exception.getMessage().contains("Cannot cancel/reschedule because it's not in ready to play status."));

        reservation.setReservationStatus(ReservationStatus.READY_TO_PLAY);
        
        exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.cancelReservation(1L);
        });
        
        assertTrue(exception.getMessage().contains("Can cancel/reschedule only future dates."));
    }

    @Test
    public void testCancelReservation() {
        Schedule schedule = Schedule.builder().startDateTime(LocalDateTime.now().plusDays(2)).build();
        schedule.setId(1L);
        Reservation reservation = Reservation.builder()
            .reservationStatus(ReservationStatus.READY_TO_PLAY)
            .value(BigDecimal.valueOf(10))
            .schedule(schedule)
            .build();

        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(i -> i.getArguments()[0]);

        ReservationDTO cancelledReservation = reservationService.cancelReservation(1L);

        assertEquals(ReservationStatus.CANCELLED.name(), cancelledReservation.getReservationStatus());
        assertEquals(BigDecimal.valueOf(0), cancelledReservation.getValue());
        assertEquals(BigDecimal.valueOf(10), cancelledReservation.getRefundValue());
    }

    @Test
    public void testCancelReservationWith75PercentRefund() {
        Schedule schedule = Schedule.builder().startDateTime(LocalDateTime.now().plusHours(1)).build();
        schedule.setId(1L);
        Reservation reservation = Reservation.builder()
            .reservationStatus(ReservationStatus.READY_TO_PLAY)
            .value(BigDecimal.valueOf(10))
            .schedule(schedule)
            .build();

        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(i -> i.getArguments()[0]);

        ReservationDTO cancelledReservation = reservationService.cancelReservation(1L);

        assertEquals(ReservationStatus.CANCELLED.name(), cancelledReservation.getReservationStatus());
        assertEquals(BigDecimal.valueOf(7.5), cancelledReservation.getValue());
        assertEquals(BigDecimal.valueOf(2.5), cancelledReservation.getRefundValue());
    }

    @Test
    public void testRescheduleReservation() {
        Schedule schedule = Schedule.builder().startDateTime(LocalDateTime.now().plusHours(5)).build();
        schedule.setId(1L);
        Reservation reservation = Reservation.builder().value(BigDecimal.valueOf(10)).reservationStatus(ReservationStatus.READY_TO_PLAY).schedule(schedule).build();
        Guest guest = Guest.builder().name("Roger Federer").build();
        guest.setId(1L);
        reservation.setGuest(guest);

        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(i -> i.getArguments()[0]);

        ReservationDTO rescheduledReservation = reservationService.rescheduleReservation(1L, 2L);

        assertEquals(ReservationStatus.RESCHEDULED.name(), rescheduledReservation.getPreviousReservation().getReservationStatus());
        assertEquals(ReservationStatus.READY_TO_PLAY.name(), rescheduledReservation.getReservationStatus());
    }

    @Test
    public void testChargeDeposit() {

        Reservation reservation = Reservation.builder().value(BigDecimal.valueOf(10)).reservationStatus(ReservationStatus.READY_TO_PLAY).build();

        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(i -> i.getArguments()[0]);

        ReservationDTO reservatioWithDeposit = reservationService.chargeDeposit(1L, BigDecimal.valueOf(20));

        assertEquals(BigDecimal.valueOf(20), reservatioWithDeposit.getValue());
    }
}