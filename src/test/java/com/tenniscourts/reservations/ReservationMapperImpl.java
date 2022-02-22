package com.tenniscourts.reservations;

import com.tenniscourts.guests.Guest;
import com.tenniscourts.guests.GuestMapperImpl;
import com.tenniscourts.schedules.Schedule;
import com.tenniscourts.schedules.ScheduleMapperImpl;

public class ReservationMapperImpl implements ReservationMapper {

    public Reservation map(ReservationDTO source) {
        Reservation target = Reservation.builder()
            .guest(new GuestMapperImpl().map(source.getGuest()))
            .schedule(new ScheduleMapperImpl().map(source.getSchedule()))
            .value(source.getValue())
            .reservationStatus((source.getReservationStatus() != null) ? ReservationStatus.valueOf(source.getReservationStatus()) : ReservationStatus.READY_TO_PLAY)
            .refundValue(source.getRefundValue())
            .build();
        target.setId(source.getId());
        return target;
    };

    public ReservationDTO map(Reservation source) {
        return ReservationDTO.builder()
            .id(source.getId())
            .guest(new GuestMapperImpl().map(source.getGuest()))
            .schedule(new ScheduleMapperImpl().map(source.getSchedule()))
            .guestId((source.getGuest() != null) ? source.getGuest().getId() : 0)
            .scheduledId((source.getSchedule() != null) ? source.getSchedule().getId() : 0)
            .value(source.getValue())
            .reservationStatus(source.getReservationStatus().name())
            .refundValue(source.getRefundValue())
            .build();
    }

    public Reservation map(CreateReservationRequestDTO source) {
        Guest guest = new Guest();
        guest.setId(source.getGuestId());
        Schedule schedule = new Schedule();
        schedule.setId(source.getScheduleId());
        return Reservation.builder()
            .guest(guest)
            .schedule(schedule)
            .build();
    }
}
