package com.tenniscourts.guests;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.tenniscourts.exceptions.BusinessException;
import com.tenniscourts.exceptions.EntityNotFoundException;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GuestService {
  
  private final GuestRepository guestRepository;
    
  private final GuestMapper guestMapper;

  public List<GuestDTO> findAll() {
    return guestRepository.findAll().stream().map(guest -> { return guestMapper.map(guest); }).collect(Collectors.toList());
  }
  
  public GuestDTO findById(Long id) {
    return guestRepository.findById(id).map(guestMapper::map).orElseThrow(() -> {
      throw new EntityNotFoundException("Guest " + id + " not found.");
    });
  }
    
  public List<GuestDTO> findByName(String name) {
    return guestRepository.findByNameContainingIgnoreCase(name).stream().map(guest -> { return guestMapper.map(guest); }).collect(Collectors.toList());
  }

  public GuestDTO addGuest(GuestDTO guest) {
    if (guest.getId() > 0) {
      throw new BusinessException("Guest id should be zero.");
    }
    return guestMapper.map(guestRepository.saveAndFlush(guestMapper.map(guest)));
  }
  
  public GuestDTO updateGuest(GuestDTO guest) {
    GuestDTO guestUpdated = null;
    if (guest.getId() == 0) {
      throw new BusinessException("Guest " + guest.getId() + " is zero.");
    }
    else {
      // if not found findById throws an exception
      GuestDTO guestToUpdate = findById(guest.getId());
      guestUpdated = guestMapper.map(guestRepository.saveAndFlush(guestMapper.map(guestToUpdate)));
    }
    return guestUpdated;
  }

  public boolean delete(Long id) {
    try {
      guestRepository.delete(guestRepository.findById(id).get());
      return true;
    }
    catch (Exception exception) {
      return false;
    }
  }
}
