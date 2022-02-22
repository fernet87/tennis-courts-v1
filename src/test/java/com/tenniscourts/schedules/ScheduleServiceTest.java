package com.tenniscourts.schedules;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.tenniscourts.TennisCourt;
import com.tenniscourts.tenniscourts.TennisCourtMapperImpl;
import com.tenniscourts.tenniscourts.TennisCourtRepository;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = ScheduleService.class)
public class ScheduleServiceTest {

  @InjectMocks
  ScheduleService scheduleService;

  @Mock
  ScheduleRepository scheduleRepository;
  
  @Mock
  TennisCourtRepository tennisCourtRepository;
  
  @Before
  public void setUp() throws Exception {
    ReflectionTestUtils.setField(scheduleService, "scheduleMapper", new ScheduleMapperImpl());
    ReflectionTestUtils.setField(scheduleService, "tennisCourtMapper", new TennisCourtMapperImpl());
  }

  @Test
  public void testAddSchedule() {
    TennisCourt tennisCourt = TennisCourt.builder().name("Philippe-Chatrier").build();
    tennisCourt.setId(1L);
    when(tennisCourtRepository.findById(anyLong())).thenReturn(Optional.of(tennisCourt));
    when(scheduleRepository.saveAndFlush(Mockito.any(Schedule.class))).thenAnswer(i -> i.getArguments()[0]);

    LocalDateTime startDateTime = LocalDateTime.now().plusHours(5);
    CreateScheduleRequestDTO createDTO = CreateScheduleRequestDTO.builder().tennisCourtId(1L).startDateTime(startDateTime).build();
    ScheduleDTO schedule = scheduleService.addSchedule(1L, createDTO);
    assertTrue(schedule != null);
    assertTrue(schedule.getTennisCourt() != null);
    assertEquals(tennisCourt.getId(), schedule.getTennisCourtId());
    assertEquals(startDateTime, schedule.getStartDateTime());
    assertEquals(startDateTime.plusHours(1), schedule.getEndDateTime());
  }
  
  @Test
  public void testFindScheduleNotFoundException() {
    when(scheduleRepository.findById(anyLong())).thenReturn(Optional.empty());
    
    Exception exception = assertThrows(EntityNotFoundException.class, () -> {
      scheduleService.findSchedule(1L);
    });
    
    assertTrue(exception.getMessage().contains("Schedule 1 not found."));
  }

}
