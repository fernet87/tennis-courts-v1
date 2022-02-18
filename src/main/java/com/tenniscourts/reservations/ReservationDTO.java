package com.tenniscourts.reservations;

import com.tenniscourts.guests.GuestDTO;
import com.tenniscourts.schedules.ScheduleDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
@ApiModel(description = "Class representing a reservation tracked by the application.")
public class ReservationDTO {

    @ApiModelProperty(notes = "Unique identifier of the reservation. No two reservations can have the same id.", example = "1", required = true, position = 0)
    private Long id;

    @ApiModelProperty(notes = "Schedule.", required = false, position = 1)
    private ScheduleDTO schedule;

    @ApiModelProperty(notes = "Guest.", required = false, position = 2)
    private GuestDTO guest;

    @ApiModelProperty(notes = "Reservation status. It can be READY_TO_PLAY, CANCELLED or RESCHEDULED.", required = false, position = 3)
    private String reservationStatus;

    @ApiModelProperty(notes = "Reservation.", required = false, position = 4)
    private ReservationDTO previousReservation;

    @ApiModelProperty(notes = "Refund reservation deposit value.", required = false, position = 5)
    private BigDecimal refundValue;

    @ApiModelProperty(notes = "Reservation deposit value.", required = false, position = 6)
    private BigDecimal value;

    @NotNull
    @ApiModelProperty(notes = "Schedule identifier.", required = true, position = 7)
    private Long scheduledId;

    @NotNull
    @ApiModelProperty(notes = "Guest identifier.", required = true, position = 8)
    private Long guestId;
}
