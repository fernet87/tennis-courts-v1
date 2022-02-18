package com.tenniscourts.guests;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
@ApiModel(description = "Class representing a guest tracked by the application.")
public class GuestDTO {

  @ApiModelProperty(notes = "Unique identifier of the guest. No two guests can have the same id.", example = "1", required = true, position = 0)
  private Long id;
  
  @NotNull
  @ApiModelProperty(notes = "Name of the guest.", example = "Roger Federer", required = true, position = 1)
  private String name;

}
