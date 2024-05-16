package service.impl;

import entity.Actor;
import entity.Film;
import entity.dto.ActorDto;
import exception.NotFoundException;
import exception.ValidateException;
import mapper.ActorMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.ActorRepository;
import service.impl.ActorServiceImpl;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class ActorServiceImplTest {

    private ActorServiceImpl actorService;

    @Mock
    private ActorRepository actorRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        actorService = new ActorServiceImpl(actorRepository);
    }

    @Test
    public void testPostActor() {
        ActorDto actorDto = new ActorDto(1L, "John Doe", Collections.emptyList());
        Actor actor = ActorMapper.toEntity(actorDto);

        when(actorRepository.postActor(any(Actor.class))).thenReturn(actor);

        ActorDto result = actorService.postActor(actorDto);

        assertNotNull(result);
        assertEquals(actorDto.getName(), result.getName());
    }

    @Test
    public void testPatchActor() {
        ActorDto actorDto = new ActorDto(1L, "Jane Smith", Collections.emptyList());
        Actor actor = ActorMapper.toEntity(actorDto);

        when(actorRepository.patchActor(any(Actor.class))).thenReturn(actor);

        ActorDto result = actorService.patchActor(actorDto);

        assertNotNull(result);
        assertEquals(actorDto.getName(), result.getName());
    }

    @Test
    public void testGetActorById() {
        long actorId = 1L;
        Actor actor = new Actor(actorId, "Alice Johnson", Collections.emptyList());

        when(actorRepository.getActorById(actorId)).thenReturn(Optional.of(actor));

        ActorDto result = actorService.getActorById(actorId);

        assertNotNull(result);
        assertEquals(actor.getName(), result.getName());
    }

    @Test(expected = NotFoundException.class)
    public void testGetActorByIdNotFound() {
        long actorId = 2L;

        when(actorRepository.getActorById(actorId)).thenReturn(Optional.empty());

        actorService.getActorById(actorId);
    }

    @Test
    public void testRemoveActor() {
        long actorId = 1L;

        actorService.removeActor(actorId);

        verify(actorRepository, times(1)).removeActor(actorId);
    }

    @Test(expected = ValidateException.class)
    public void testPostActorInvalidName() {
        ActorDto actorDto = new ActorDto(1L, "",Collections.emptyList());

        actorService.postActor(actorDto);
    }
}
