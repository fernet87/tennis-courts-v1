package com.tenniscourts.guests;

import java.util.List;

import com.tenniscourts.config.BaseRestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/guest")
public class GuestController extends BaseRestController {

  private final GuestService guestService;

  @GetMapping("/all")
  public ResponseEntity<List<GuestDTO>> findAll() {
    return ResponseEntity.ok(guestService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<GuestDTO> findById(@PathVariable("id") Long id) {
    return ResponseEntity.ok(guestService.findById(id));
  }

  @GetMapping()
  public ResponseEntity<List<GuestDTO>> findByName(@RequestParam("name") String name) {
    return ResponseEntity.ok(guestService.findByName(name));
  }

  @PostMapping("/add")
  public ResponseEntity<Void> addGuest(@RequestBody GuestDTO guestDTO) {
      return ResponseEntity.created(locationByEntity(guestService.addGuest(guestDTO).getId())).build();
  }
  
  @PutMapping("/update")
  public ResponseEntity<Void> updateGuest(@RequestBody GuestDTO guestDTO) {
      return ResponseEntity.created(locationByEntity(guestService.updateGuest(guestDTO).getId())).build();
  }
  

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    return (guestService.delete(id)) ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.CONFLICT).build();
  }
  

}
