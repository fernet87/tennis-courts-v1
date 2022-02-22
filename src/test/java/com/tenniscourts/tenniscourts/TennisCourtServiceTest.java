package com.tenniscourts.tenniscourts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.tenniscourts.MockDataProvider;
import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.schedules.CreateScheduleRequestDTO;
import com.tenniscourts.schedules.ScheduleDTO;
import com.tenniscourts.schedules.ScheduleService;

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
@ContextConfiguration(classes = TennisCourtService.class)
public class TennisCourtServiceTest {

  @InjectMocks
  TennisCourtService tennisCourtService;

  @Mock
  ScheduleService scheduleService;
  
  @Mock
  TennisCourtRepository tennisCourtRepository;
  
  @Before
  public void setUp() throws Exception {
    ReflectionTestUtils.setField(tennisCourtService, "tennisCourtMapper", new TennisCourtMapperImpl());
  }

  @Test
  public void testAddTennisCourt() {
    TennisCourtDTO tennisCourtMock = MockDataProvider.buildTennisCourt1();

    when(tennisCourtRepository.saveAndFlush(any(TennisCourt.class))).thenAnswer(i -> i.getArguments()[0]);

    TennisCourtDTO tennisCourtDTO = tennisCourtService.addTennisCourt(tennisCourtMock);

    assertEquals(tennisCourtMock.getId(), tennisCourtDTO.getId());
    assertEquals(tennisCourtMock.getName(), tennisCourtDTO.getName());
  }

  @Test
  public void testAddScheduleSlotsToTennisCourt() {
    TennisCourt tennisCourtMock = TennisCourt.builder().name("Philippe-Chatrier").build();
    tennisCourtMock.setId(1L);

    LocalDateTime startDateTime = LocalDateTime.now().plusDays(1);   
    ScheduleDTO scheduleDTO1 = MockDataProvider.buildSchedule1();
    ScheduleDTO scheduleDTO2 = MockDataProvider.buildSchedule1();
    ScheduleDTO scheduleDTO3 = MockDataProvider.buildSchedule1();

    when(tennisCourtRepository.findById(anyLong())).thenReturn(Optional.of(tennisCourtMock));
    when(scheduleService.addSchedule(anyLong(), any(CreateScheduleRequestDTO.class))).thenReturn(scheduleDTO1, scheduleDTO2, scheduleDTO3);

    TennisCourtDTO tennisCourtResponse = tennisCourtService.addScheduleSlotsToTennisCourt(1L, startDateTime, startDateTime.plusHours(2));
    assertEquals(3, tennisCourtResponse.getTennisCourtSchedules().size());
  }
      
  @Test
  public void testFindTennisCourtByIdNotFoundException() {
    when(tennisCourtRepository.findById(anyLong())).thenReturn(Optional.empty());
    
    Exception exception = assertThrows(EntityNotFoundException.class, () -> {
      tennisCourtService.findTennisCourtById(1L);
    });
    
    assertTrue(exception.getMessage().contains("Tennis Court 1 not found."));
  }
      
  @Test
  public void testFindTennisCourtById() {
    TennisCourt tennisCourtMock = TennisCourt.builder().name("Philippe-Chatrier").build();
    tennisCourtMock.setId(1L);
    when(tennisCourtRepository.findById(anyLong())).thenReturn(Optional.of(tennisCourtMock));

    TennisCourtDTO tennisCourtDTO = tennisCourtService.findTennisCourtById(1L);
    
    assertEquals(tennisCourtMock.getId(), tennisCourtDTO.getId());
    assertEquals(tennisCourtMock.getName(), tennisCourtDTO.getName());
  }

  @Test
  public void testFindTennisCourtWithSchedulesById() {
    TennisCourt tennisCourtMock = TennisCourt.builder().name("Philippe-Chatrier").build();
    tennisCourtMock.setId(1L);
    List<ScheduleDTO> schedulesMock = MockDataProvider.buildScheduleList();       
    ScheduleDTO scheduleMock0 = schedulesMock.get(0);
    ScheduleDTO scheduleMock1 = schedulesMock.get(1);
    ScheduleDTO scheduleMock2 = schedulesMock.get(2);

    when(tennisCourtRepository.findById(anyLong())).thenReturn(Optional.of(tennisCourtMock));
    when(scheduleService.findSchedulesByTennisCourtId(anyLong())).thenReturn(schedulesMock);

    TennisCourtDTO tennisCourtDTO = tennisCourtService.findTennisCourtWithSchedulesById(1L);

    ScheduleDTO tennisCourtDTO0 = tennisCourtDTO.getTennisCourtSchedules().get(0);
    ScheduleDTO tennisCourtDTO1 = tennisCourtDTO.getTennisCourtSchedules().get(1);
    ScheduleDTO tennisCourtDTO2 = tennisCourtDTO.getTennisCourtSchedules().get(2);
    
    assertEquals(tennisCourtMock.getId(), tennisCourtDTO.getId());
    assertEquals(tennisCourtMock.getName(), tennisCourtDTO.getName());
    assertEquals(schedulesMock.size(), tennisCourtDTO.getTennisCourtSchedules().size());
    assertEquals(scheduleMock0.getStartDateTime(), tennisCourtDTO0.getStartDateTime());
    assertEquals(scheduleMock0.getEndDateTime(), tennisCourtDTO0.getEndDateTime());
    assertEquals(scheduleMock1.getStartDateTime(), tennisCourtDTO1.getStartDateTime());
    assertEquals(scheduleMock1.getEndDateTime(), tennisCourtDTO1.getEndDateTime());
    assertEquals(scheduleMock2.getStartDateTime(), tennisCourtDTO2.getStartDateTime());
    assertEquals(scheduleMock2.getEndDateTime(), tennisCourtDTO2.getEndDateTime());
  }

}
