package com.tenniscourts.guests;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.tenniscourts.exceptions.EntityNotFoundException;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = GuestService.class)
public class GuestServiceTest {
  
  @InjectMocks
  GuestService guestService;

  @Mock
  GuestRepository guestRepository;
  
  @Before
  public void setUp() throws Exception {
    ReflectionTestUtils.setField(guestService, "guestMapper", new GuestMapperImpl());
  }
    
  @Test
  public void testFindByIdNotFoundException() {
      when(guestRepository.findById(anyLong())).thenReturn(Optional.empty());
      
      Exception exception = assertThrows(EntityNotFoundException.class, () -> {
          guestService.findById(1L);
      });
      
      assertTrue(exception.getMessage().contains("Guest 1 not found."));
  }
      
}
