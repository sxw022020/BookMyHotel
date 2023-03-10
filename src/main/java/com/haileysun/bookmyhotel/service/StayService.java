package com.haileysun.bookmyhotel.service;

import com.haileysun.bookmyhotel.entity.*;
import com.haileysun.bookmyhotel.repository.StayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class StayService {
    private StayRepository stayRepository;
    private ImageStorageService imageStorageService;

    // constructor injection
    @Autowired
    public StayService(StayRepository stayRepository, ImageStorageService imageStorageService) {
        this.stayRepository = stayRepository;
        this.imageStorageService = imageStorageService;
    }

    // 1. find list of stays by host
    public List<Stay> listByHost(String username) {
        return stayRepository.findByHost(new User.Builder().setUsername(username).build());
    }

    // 2. find stay by stayID
    public Stay findStayByID(Long stayID) {
        return stayRepository.findById(stayID).orElse(null);
    }

    // 3. add stay
    public void addStay(Stay stay, MultipartFile[] images) {
        // 1. Add availabilities
        // TODO - plusDays?
        LocalDate date = LocalDate.now().plusDays(1);
        List<StayAvailability> availabilities = new ArrayList<>();

        // TODO - 30?
        for (int i = 0; i < 30; i++) {
            availabilities.add(
                    new StayAvailability
                            .Builder()
                            .setId(new StayAvailabilityID(stay.getId(), date))
                            .setStay(stay)
                            .setState(StayAvailabilityState.AVAILABLE)
                            .build());
            date = date.plusDays(1);
        }

        stay.setAvailabilities(availabilities);

        // 2. Add stay images
        List<String> mediaURLs = Arrays
                .stream(images)
                .parallel()
                .map(image -> imageStorageService.save(image)).toList();

        List<StayImage> stayImages = new ArrayList<>();
        for (String mediaURL : mediaURLs) {
            stayImages.add(new StayImage(mediaURL, stay));
        }
        stay.setImages(stayImages);

        // 3. save stay to repository
        stayRepository.save(stay);
    }

    // 4. delete a stay
    public void deleteStay(Long stayID) {
        stayRepository.deleteById(stayID);
    }
}
