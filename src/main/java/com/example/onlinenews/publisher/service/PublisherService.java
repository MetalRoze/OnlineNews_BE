package com.example.onlinenews.publisher.service;

import com.example.onlinenews.error.StateResponse;
import com.example.onlinenews.publisher.dto.PublisherCreateRequestDTO;
import com.example.onlinenews.publisher.dto.PublisherDto;
import com.example.onlinenews.publisher.entity.Publisher;
import com.example.onlinenews.publisher.repository.PublisherRepository;
import com.example.onlinenews.request.dto.RequestDto;
import jakarta.persistence.Column;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublisherService {
    private final PublisherRepository publisherRepository;

    public List<Publisher> getAllPublisher() {return publisherRepository.findAll();}

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public Publisher getPublisherByName(String publisher_name){
        return publisherRepository.findByName(publisher_name);
    }
    public StateResponse publisherCreate(PublisherCreateRequestDTO requestDTO){
        Publisher publisher = Publisher.builder()
                .name(requestDTO.getPublisher_name())
                .url(requestDTO.getPublisher_url())
                .img(requestDTO.getPublisher_url())
                .type(requestDTO.getPublisher_type()).build();
        publisherRepository.save(publisher);
        return StateResponse.builder().code("success!!!!!").message("신문사 추가 성공").build();
    }

    public List<PublisherDto> list(){
        return publisherRepository.findAll().stream()
                .map(PublisherDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PublisherDto> getByTypeList(String pub_type){
        return publisherRepository.findByType(pub_type).stream()
                .map(PublisherDto::fromEntity)
                .collect(Collectors.toList());
    }
}
