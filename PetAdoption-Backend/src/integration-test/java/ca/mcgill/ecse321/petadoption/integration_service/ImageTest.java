package ca.mcgill.ecse321.petadoption.integration_service;

import ca.mcgill.ecse321.petadoption.TestSuits.Utils.TestUtils;
import ca.mcgill.ecse321.petadoption.dao.AdvertisementRepository;
import ca.mcgill.ecse321.petadoption.dao.AppUserRepository;
import ca.mcgill.ecse321.petadoption.dao.ImageRepository;
import ca.mcgill.ecse321.petadoption.model.*;
import ca.mcgill.ecse321.petadoption.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class ImageTest {

    private static final String USER_NAME_1 = "user 1";
    private static final String USER_EMAIL_1 = "user1@mcgill.ca";
    private static final String USER_PASSWORD_1 = "password 1";
    private static final String USER_BIO_1 = "empty";
    private static final String USER_HOME_1 = "its nice";
    private static final Integer USER_AGE_1 = 34;
    private static final Sex USER_SEX_1 = Sex.M;
    private static final boolean USER_ADMIN_1 = true;

    private static final String IMAGE_NAME_1 = "doggy 1";
    private static final String IMAGE_LINK_1 = "doggy_1.com";
    private static final String IMAGE_ID_1 = "id1";

    private static final String IMAGE_NAME_2 = "doggy 2";
    private static final String IMAGE_LINK_2 = "doggy_2.com";
    private static final String IMAGE_ID_2 = "id2";

    private static final Date ADVERTISEMENT_POSTDATE_1 = Date.valueOf(LocalDate.of(2020, Month.FEBRUARY, 7));
    private static final String ADVERTISEMENT_ID_1 = "";
    private static final boolean ADVERTISEMENT_ISEXPIRED_1 = false;
    private static final String PET_NAME = "";
    private static final int  PET_AGE = 3;
    private static final String PET_DESCRIPTION = "cute lovely";
    private static final Sex PET_SEX = Sex.M;
    private static final Species PET_SPECIE = Species.bird;

    private static final AppUser user = TestUtils.createAppUser(USER_NAME_1, USER_EMAIL_1, USER_PASSWORD_1, USER_BIO_1, USER_HOME_1, USER_AGE_1, USER_ADMIN_1, USER_SEX_1);
    private static Advertisement ad = TestUtils.createAdvertisement(user, ADVERTISEMENT_POSTDATE_1, ADVERTISEMENT_ID_1, ADVERTISEMENT_ISEXPIRED_1, PET_NAME, PET_AGE, PET_DESCRIPTION, PET_SEX, PET_SPECIE );
    private static Image image1 = TestUtils.createImage(ad, IMAGE_NAME_1, IMAGE_LINK_1, IMAGE_ID_1);
    private static Image image2 = TestUtils.createImage(ad, IMAGE_NAME_2, IMAGE_LINK_2, IMAGE_ID_2);


    @Autowired
    private ImageService imageservice;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @BeforeEach
    public void cleanAndSetup(){
        imageRepository.deleteAll();
        advertisementRepository.deleteAll();
        appUserRepository.deleteAll();
    }
    @Test
    public void createImage(){
        
    }
}
