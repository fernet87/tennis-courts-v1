package com.tenniscourts.reservations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
@ApiModel(description = "Class representing a request object to create a new reservation.")
public class CreateReservationRequestDTO {

    @NotNull
    @ApiModelProperty(notes = "Guest identifier required to create a reservation.", example = "1", required = true, position = 0)
    private Long guestId;

    @NotNull
    @ApiModelProperty(notes = "Schedule identifier required to create a reservation.", example = "1", required = true, position = 1)
    private Long scheduleId;

}
