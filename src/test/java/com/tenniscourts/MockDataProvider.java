package com.tenniscourts;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.tenniscourts.guests.GuestDTO;
import com.tenniscourts.reservations.ReservationDTO;
import com.tenniscourts.reservations.ReservationStatus;
import com.tenniscourts.schedules.CreateScheduleRequestDTO;
import com.tenniscourts.schedules.ScheduleDTO;
import com.tenniscourts.tenniscourts.TennisCourtDTO;

public class MockDataProvider {

  public static GuestDTO buildGuest1() {
    return GuestDTO.builder()
      .id(1L)
      .name("Roger Federer")
      .build();
  }

  public static GuestDTO buildGuest2() {
    return GuestDTO.builder()
      .id(1L)
      .name("Rafael Nadal")
      .build();
  }

  public static TennisCourtDTO buildTennisCourt1() {
    return TennisCourtDTO.builder()
      .id(1L)
      .name("Philippe-Chatrier")
      // .tennisCourtSchedule(buildSchedule1())
      .build();
  }

  public static TennisCourtDTO buildTennisCourt2() {
    return TennisCourtDTO.builder()
      .id(2L)
      .name("Rod Laver Arena")
      // .tennisCourtSchedule(buildSchedule2())
      // .tennisCourtSchedule(buildSchedule3())
      .build();
  }
  
  public static ScheduleDTO buildSchedule1() {
    return ScheduleDTO.builder()
      .id(1L)
      .tennisCourt(buildTennisCourt1())
      .tennisCourtId(1L)
      .startDateTime(LocalDateTime.now().plusDays(1))
      .endDateTime(LocalDateTime.now().plusDays(1).plusHours(1))
      .build();
  }

  public static ScheduleDTO buildSchedule2() {
    return ScheduleDTO.builder()
      .id(2L)
      .tennisCourt(buildTennisCourt2())
      .tennisCourtId(2L)
      .startDateTime(LocalDateTime.now().plusDays(2))
      .endDateTime(LocalDateTime.now().plusDays(2).plusHours(1))
      .build();
  }
  
  public static ScheduleDTO buildSchedule3() {
    return ScheduleDTO.builder()
      .id(3L)
      .tennisCourt(buildTennisCourt2())
      .tennisCourtId(3L)
      .startDateTime(LocalDateTime.now().plusDays(2).plusHours(1))
      .endDateTime(LocalDateTime.now().plusDays(2).plusHours(2))
      .build();
  }
  
  public static List<ScheduleDTO> buildScheduleList() {
    List<ScheduleDTO> schedules = new ArrayList<ScheduleDTO>();
    schedules.add(buildSchedule1());
    schedules.add(buildSchedule2());
    schedules.add(buildSchedule3());
    return schedules;
  }

  public static ReservationDTO buildReservation1() {
    return ReservationDTO.builder()
      .id(1L)
      .schedule(buildSchedule1())
      .scheduledId(1L)
      .guest(buildGuest1())
      .guestId(1l)
      .reservationStatus(ReservationStatus.READY_TO_PLAY.name())
      .build();
  }

  public static CreateScheduleRequestDTO buildCreateScheduleRequest(Long tennisCourtId, LocalDateTime startDateTime) {
    return CreateScheduleRequestDTO.builder().tennisCourtId(tennisCourtId).startDateTime(startDateTime).build();
  }
}
