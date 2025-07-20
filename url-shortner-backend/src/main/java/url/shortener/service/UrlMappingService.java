package url.shortener.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import url.shortener.dtos.ClickEventDto;
import url.shortener.dtos.UrlMappingDto;
import url.shortener.models.ClickEvent;
import url.shortener.models.UrlMapping;
import url.shortener.models.User;
import url.shortener.repository.ClickEventRepository;
import url.shortener.repository.UrlMappingRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UrlMappingService {

    @Autowired
    private UrlMappingRepository urlMappingRepository;
    @Autowired
    private ClickEventRepository clickEventRepository;

    public UrlMappingDto createShortUrl(String originalUrl, User user) {
        String shortUrl = generateShortUrl();
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setUser(user);
        urlMapping.setCreatedDate(LocalDateTime.now());
        UrlMapping savedUrlMapping = urlMappingRepository.save(urlMapping);
        return convertToDto(savedUrlMapping);
    }

    private String generateShortUrl() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder shortUrl = new StringBuilder(8);
        for(int i=0; i<8; i++){
            shortUrl.append(characters.charAt(random.nextInt(characters.length())));
        }
        return shortUrl.toString();
    }

    private UrlMappingDto convertToDto(UrlMapping urlMapping){
        UrlMappingDto urlMappingDto = new UrlMappingDto();
        urlMappingDto.setOriginalUrl(urlMapping.getOriginalUrl());
        urlMappingDto.setShortUrl(urlMapping.getShortUrl());
        urlMappingDto.setId(urlMapping.getId());
        urlMappingDto.setClickCount(urlMapping.getClickCount());
        urlMappingDto.setCreatedDate(urlMapping.getCreatedDate());
        urlMappingDto.setUsername(urlMapping.getUser().getUsername());
        return urlMappingDto;
    }

    public List<UrlMappingDto> getUrls(User user) {
        return urlMappingRepository.findByUser(user).stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<ClickEventDto> getCickEventsByDate(String shortUrl, LocalDateTime start, LocalDateTime end) {
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if(urlMapping != null){
            return clickEventRepository.findByUrlMappingAndClickDateBetween(urlMapping, start, end).stream()
                    .collect(Collectors.groupingBy(click -> click.getClickDate().toLocalDate(),
                            Collectors.counting()))
                    .entrySet().stream()
                    .map(entry -> {
                       ClickEventDto clickEventDto = new ClickEventDto();
                       clickEventDto.setClickDate(entry.getKey());
                       clickEventDto.setCount(entry.getValue());
                       return clickEventDto;
                    })
                    .toList();
        }
        return null;
    }

    public Map<LocalDate, Long> getTotalClicksByUserAndDate(User user, LocalDate start, LocalDate end) {
        List<UrlMapping> urlMappingList = urlMappingRepository.findByUser(user);
        List<ClickEvent> clickEventList = clickEventRepository.findByUrlMappingInAndClickDateBetween(urlMappingList, start.atStartOfDay(),
                end.plusDays(1).atStartOfDay());
        return clickEventList.stream()
                .collect(Collectors.groupingBy(click -> click.getClickDate().toLocalDate(),
                        Collectors.counting()));
    }
}
