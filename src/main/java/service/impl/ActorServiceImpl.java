package service.impl;

import entity.Actor;
import entity.dto.ActorDto;
import exception.NotFoundException;
import exception.ValidateException;
import mapper.ActorMapper;
import repository.ActorRepository;
import service.ActorService;

import java.util.Optional;

public class ActorServiceImpl implements ActorService {
    private final ActorRepository actorRepository;

    public ActorServiceImpl(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @Override
    public ActorDto postActor(ActorDto actorDto) {
        checkValid(actorDto);
        Actor requestActor = ActorMapper.toEntity(actorDto);
        Actor responseActor = actorRepository.postActor(requestActor);
        return ActorMapper.toDto(responseActor);
    }

    @Override
    public ActorDto patchActor(ActorDto actorDto) {
        checkValid(actorDto);
        Actor requestActor = ActorMapper.toEntity(actorDto);
        Actor responseActor = actorRepository.patchActor(requestActor);
        return ActorMapper.toDto(responseActor);
    }

    @Override
    public ActorDto getActorById(long actorId) {
        Optional<Actor> actorOptional = actorRepository.getActorById(actorId);
        if (actorOptional.isPresent()) {
            return ActorMapper.toDto(actorOptional.get());
        } else {
            throw new NotFoundException("Actor not found");
        }
    }

    @Override
    public void removeActor(long actorId) {
        actorRepository.removeActor(actorId);
    }

    private void checkValid(ActorDto actorDto) {
        if (actorDto.getName() == null || actorDto.getName().isEmpty()) {
            throw new ValidateException("Name cannot be empty");
        }
    }
}