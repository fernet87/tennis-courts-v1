package com.tenniscourts.guests;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class GuestMapperImpl implements GuestMapper {

  public Guest map(GuestDTO source) {
    Guest target = null;
    if (source != null) {
      target = Guest.builder().name(source.getName()).build();
      target.setId(source.getId());
    }
    return target;
  }

  public GuestDTO map(Guest source) {
    return (source != null) ? GuestDTO.builder().id(source.getId()).name(source.getName()).build() : null;
  }

}
