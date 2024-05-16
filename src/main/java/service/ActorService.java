package service;

import entity.dto.ActorDto;
import entity.dto.FilmDto;

public interface ActorService {

    ActorDto postActor(ActorDto actorDto);

    ActorDto getActorById(long actorId);

    void removeActor(long actorId);

    ActorDto patchActor(ActorDto actorDto);

}
