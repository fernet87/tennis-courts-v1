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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/guest")
@Api(description = "Set of endpoints for Creating, Retrieving, Updating and Deleting of Guests.")
public class GuestController extends BaseRestController {

  private final GuestService guestService;

  @GetMapping("/all")
  @ApiOperation("Returns list of all guests in the system.")
  public ResponseEntity<List<GuestDTO>> findAll() {
    return ResponseEntity.ok(guestService.findAll());
  }

  @GetMapping("/{id}")
  @ApiOperation("Returns a specific guest by their identifier. 404 if does not exist.")
  public ResponseEntity<GuestDTO> findById(@PathVariable("id") Long id) {
    return ResponseEntity.ok(guestService.findById(id));
  }

  @GetMapping()
  @ApiOperation("Returns a list of guests search by name, entering the full name or part of the name ignoring the case.")
  public ResponseEntity<List<GuestDTO>> findByName(@RequestParam("name") String name) {
    return ResponseEntity.ok(guestService.findByName(name));
  }

  @PostMapping("/add")
  @ApiOperation("Creates a new guest.")
  public ResponseEntity<Void> addGuest(@RequestBody GuestDTO guestDTO) {
      return ResponseEntity.created(locationByEntity(guestService.addGuest(guestDTO).getId())).build();
  }
  
  @PutMapping("/update")
  @ApiOperation("Updates an existing guest. 404 if does not exist.")
  public ResponseEntity<Void> updateGuest(@RequestBody GuestDTO guestDTO) {
      return ResponseEntity.created(locationByEntity(guestService.updateGuest(guestDTO).getId())).build();
  }
  

  @DeleteMapping("/{id}")
  @ApiOperation("Deletes a guest from the system. 404 if the guest's identifier is not found.")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    return (guestService.delete(id)) ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.CONFLICT).build();
  }
  

}
