package ca.mcgill.ecse321.petadoption.TestSuits.UnitTests;

import ca.mcgill.ecse321.petadoption.dao.AdvertisementRepository;
import ca.mcgill.ecse321.petadoption.dao.AppUserRepository;
import ca.mcgill.ecse321.petadoption.model.Advertisement;
import ca.mcgill.ecse321.petadoption.model.AppUser;
import ca.mcgill.ecse321.petadoption.model.Sex;
import ca.mcgill.ecse321.petadoption.model.Species;
import ca.mcgill.ecse321.petadoption.service.AdvertisementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class AdvertisementUnitTest {

    @Mock
    private AdvertisementRepository advertisementDao;
    @Mock
    private AppUserRepository appUserDao;

    @InjectMocks
    private AdvertisementService advertisementService;

    // Constants to create test appUser and advertisement objects
    private static final String USER_EMAIL_1 = "user1@mcgill.ca";
    private static final String USER_EMAIL_2 = "user2@mcgill.ca";
    private static final String NON_EXISTING_APP_USER = "user3@mcgill.ca";

    //TODO: Check/ Put constraint that advertisement cannot be updated if it is expired
    private static final boolean IS_EXPIRED_1 = false;
    private static final String PET_NAME_1 = "Rusty";
    private static final Integer PET_AGE_1 = 10;
    private static final String PET_DESCRIPTION_1 = "Not Rusty at all";
    private static final Sex PET_SEX_1 = Sex.M;
    private static final Species PET_SPECIES_1 = Species.dog;

    private static final boolean IS_EXPIRED_2 = false;
    private static final String PET_NAME_2 = "Snow";
    private static final Integer PET_AGE_2 = 15;
    private static final String PET_DESCRIPTION_2 = "Not Snowy at all";
    private static final Sex PET_SEX_2 = Sex.F;
    private static final Species PET_SPECIES_2 = Species.cat;

    private static final boolean IS_EXPIRED_3 = false;
    private static final String PET_NAME_3 = "Chirpy";
    private static final Integer PET_AGE_3 = 20;
    private static final String PET_DESCRIPTION_3 = "Not Chirpy at all";
    private static final Sex PET_SEX_3 = Sex.F;
    private static final Species PET_SPECIES_3 = Species.bird;

    private static final Integer NEGATIVE_AGE = -7;

    private static final Date DATE_1 = Date.valueOf("2020-07-02");
    private static final Date DATE_2 = Date.valueOf("2020-06-07");

    //Mock unique ID keys for advertisements
    private static final String ADVERTISEMENT_1_ID = "zwVC9mb";
    private static final String ADVERTISEMENT_2_ID = "3klFrbp";
    private static final String ADVERTISEMENT_3_ID = "MEM6jFl";
    private static final String INVALID_ID = "a47sxj9";

    private static final AppUser APP_USER_1 = new AppUser();
    private static final AppUser APP_USER_2 = new AppUser();

    //Create Mock Advertisement Objects
    private static final Advertisement ADVERTISEMENT_1 = createAdvertisement(APP_USER_1, DATE_1, ADVERTISEMENT_1_ID,
            IS_EXPIRED_1, PET_NAME_1, PET_AGE_1, PET_DESCRIPTION_1, PET_SEX_1, PET_SPECIES_1);
    private static final Advertisement ADVERTISEMENT_2 = createAdvertisement(APP_USER_2, DATE_1, ADVERTISEMENT_2_ID,
            IS_EXPIRED_2, PET_NAME_2, PET_AGE_2, PET_DESCRIPTION_2, PET_SEX_2, PET_SPECIES_2);
    private static final Advertisement ADVERTISEMENT_3 = createAdvertisement(APP_USER_2, DATE_2, ADVERTISEMENT_3_ID,
            IS_EXPIRED_3, PET_NAME_3, PET_AGE_3, PET_DESCRIPTION_3, PET_SEX_3, PET_SPECIES_3);

    private static final String INVALID_AD_ID_MESSAGE = "Invalid advertisement requested. Please check advertisement ID.";
    private static final String APP_USER_ERROR_MESSAGE = "User not found. Cannot make advertisement without user profile!";
    private static final String DATE_ERROR_MESSAGE = "Date posted cannot be an empty field!";
    private static final String PET_NAME_ERROR_MESSAGE = "Pet name cannot be empty! ";
    private static final String PET_AGE_ERROR_MESSAGE = "Pet age cannot be less than or equal to 0! ";
    private static final String PET_DESCRIPTION_ERROR_MESSAGE = "Pet description cannot be empty! ";
    private static final String PET_SEX_ERROR_MESSAGE = "Pet sex must be specified! ";
    private static final String PET_SPECIES_ERROR_MESSAGE = "Pet Species is invalid! ";
    private static final String AD_REQUIRE_ID_MESSAGE = "Advertisement must have an ID";
    private static final String PROFILE_NOT_FOUND_MESSAGE = "User profile not found. App user does not exist!";

    @BeforeEach
    public void setMockOutput() {
        //Set up Mock data base with App User account and Advertisements
        configureAppUser(APP_USER_1, USER_EMAIL_1, ADVERTISEMENT_1);
        configureAppUser(APP_USER_2, USER_EMAIL_2, ADVERTISEMENT_2);
        APP_USER_2.addAdvertisement(ADVERTISEMENT_3);

        // Mock Actions of data base
        lenient().when(appUserDao.findAppUserByEmail(anyString())).thenAnswer(
                (InvocationOnMock invocation) -> {
                    if (invocation.getArgument(0).equals(USER_EMAIL_1)) {
                        return APP_USER_1;
                    } else if (invocation.getArgument(0).equals((USER_EMAIL_2))) {
                        return APP_USER_2;
                    }
                    return null;
                });

        lenient().when(advertisementDao.findAdvertisementsByPostedBy(any(AppUser.class))).thenAnswer(
                (InvocationOnMock invocation) -> {
                    if (((AppUser) invocation.getArgument(0)).getEmail().equals(USER_EMAIL_1)) {
                        //Return HashSet data as ArrayList because method in DAO class returns List<Advertisement>
                        return new ArrayList<>(APP_USER_1.getAdvertisements());
                    } else if (((AppUser) invocation.getArgument(0)).getEmail().equals(USER_EMAIL_2)) {
                        return new ArrayList<>(APP_USER_2.getAdvertisements());
                    }
                    return null;
                });

        lenient().when(advertisementDao.findAdvertisementByAdvertisementId(anyString())).thenAnswer(
                (InvocationOnMock invocation) -> {
                    if (invocation.getArgument(0).equals(ADVERTISEMENT_1_ID)) {
                        return ADVERTISEMENT_1;
                    } else if (invocation.getArgument(0).equals(ADVERTISEMENT_2_ID)) {
                        return ADVERTISEMENT_2;
                    } else if (invocation.getArgument(0).equals(ADVERTISEMENT_3_ID)) {
                        return ADVERTISEMENT_3;
                    }
                    return null;
                });

        lenient().when(advertisementDao.findAll()).thenAnswer(
                (InvocationOnMock invocation) -> {
                    List<Advertisement> advertisementList = new ArrayList<>();
                    advertisementList.add(ADVERTISEMENT_1);
                    advertisementList.add(ADVERTISEMENT_2);
                    advertisementList.add(ADVERTISEMENT_3);

                    return advertisementList;
                });

        //Returning Parameter object upon save
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> invocation.getArgument(0);

        lenient().when(advertisementDao.save(any(Advertisement.class))).thenAnswer(returnParameterAsAnswer);
    }

    private void setMockOutputAdvertisementsEmpty() {
        // create mock of no advertisements in database
        when(advertisementDao.findAll()).thenAnswer((InvocationOnMock invocation) -> new ArrayList<>());
    }

    @Test
    public void testCreateAdvertisement() {
        setMockOutputAdvertisementsEmpty();
        assertEquals(0, advertisementService.getAllAdvertisements().size());

        Advertisement advertisement = null;
        try {
            advertisement = advertisementService.createAdvertisement(USER_EMAIL_1, DATE_1, PET_NAME_1,
                    PET_AGE_1, PET_DESCRIPTION_1, PET_SEX_1, PET_SPECIES_1);
            advertisement.setAdvertisementId(ADVERTISEMENT_1_ID);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertNotNull(advertisement);
        assertEquals(USER_EMAIL_1, advertisement.getPostedBy().getEmail());
        assertEquals(DATE_1, advertisement.getDatePosted());
        assertEquals(ADVERTISEMENT_1_ID, advertisement.getAdvertisementId());
        assertEquals(PET_NAME_1, advertisement.getPetName());
        assertEquals(PET_AGE_1, advertisement.getPetAge());
        assertEquals(PET_DESCRIPTION_1, advertisement.getPetDescription());
        assertEquals(PET_SEX_1, advertisement.getPetSex());
        assertEquals(PET_SPECIES_1, advertisement.getPetSpecies());
    }

    @Test
    public void testCreateAdvertisementEmailEmpty() {
        Advertisement advertisement = null;
        String error = null;
        try {
            advertisement = advertisementService.createAdvertisement(" ", DATE_1, PET_NAME_1,
                    PET_AGE_1, PET_DESCRIPTION_1, PET_SEX_1, PET_SPECIES_1);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(advertisement);
        assertEquals(APP_USER_ERROR_MESSAGE, error);
    }

    @Test
    public void testCreateAdvertisementEmailNull() {
        Advertisement advertisement = null;
        String error = null;
        try {
            advertisement = advertisementService.createAdvertisement(null, DATE_1, PET_NAME_1,
                    PET_AGE_1, PET_DESCRIPTION_1, PET_SEX_1, PET_SPECIES_1);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(advertisement);
        assertEquals(APP_USER_ERROR_MESSAGE, error);
    }

    @Test
    public void testCreateAdvertisementNullDate() {
        Advertisement advertisement = null;
        String error = null;
        try {
            advertisement = advertisementService.createAdvertisement(USER_EMAIL_1, null, PET_NAME_1,
                    PET_AGE_1, PET_DESCRIPTION_1, PET_SEX_1, PET_SPECIES_1);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(advertisement);
        assertEquals(DATE_ERROR_MESSAGE, error);
    }

    @Test
    public void testCreateAdvertisementNoAppUser() {
        Advertisement advertisement = null;
        String error = null;
        try {
            advertisement = advertisementService.createAdvertisement(NON_EXISTING_APP_USER, DATE_1, PET_NAME_1,
                    PET_AGE_1, PET_DESCRIPTION_1, PET_SEX_1, PET_SPECIES_1);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(advertisement);
        assertEquals(APP_USER_ERROR_MESSAGE, error);
    }

    @Test
    public void testCreateAdvertisementNullPetName() {
        Advertisement advertisement = null;
        String error = null;
        try {
            advertisement = advertisementService.createAdvertisement(USER_EMAIL_1, DATE_1, null,
                    PET_AGE_1, PET_DESCRIPTION_1, PET_SEX_1, PET_SPECIES_1);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(advertisement);
        assertEquals(PET_NAME_ERROR_MESSAGE, error);
    }

    @Test
    public void testCreateAdvertisementEmptyPetName() {
        Advertisement advertisement = null;
        String error = null;
        try {
            advertisement = advertisementService.createAdvertisement(USER_EMAIL_1, DATE_1, " ",
                    PET_AGE_1, PET_DESCRIPTION_1, PET_SEX_1, PET_SPECIES_1);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(advertisement);
        assertEquals(PET_NAME_ERROR_MESSAGE, error);
    }

    @Test
    public void testCreateAdvertisementNullAge() {
        Advertisement advertisement = null;
        String error = null;
        try {
            advertisement = advertisementService.createAdvertisement(USER_EMAIL_1, DATE_1, PET_NAME_1,
                    null, PET_DESCRIPTION_1, PET_SEX_1, PET_SPECIES_1);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(advertisement);
        assertEquals(PET_AGE_ERROR_MESSAGE, error);
    }

    @Test
    public void testCreateAdvertisementNegativeAge() {
        Advertisement advertisement = null;
        String error = null;
        try {
            advertisement = advertisementService.createAdvertisement(USER_EMAIL_1, DATE_1, PET_NAME_1,
                    NEGATIVE_AGE, PET_DESCRIPTION_1, PET_SEX_1, PET_SPECIES_1);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(advertisement);
        assertEquals(PET_AGE_ERROR_MESSAGE, error);
    }

    @Test
    public void testCreateAdvertisementNullDescription() {
        Advertisement advertisement = null;
        String error = null;
        try {
            advertisement = advertisementService.createAdvertisement(USER_EMAIL_1, DATE_1, PET_NAME_1,
                    PET_AGE_1, null, PET_SEX_1, PET_SPECIES_1);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(advertisement);
        assertEquals(PET_DESCRIPTION_ERROR_MESSAGE, error);
    }

    @Test
    public void testCreateAdvertisementEmptyDescription() {
        Advertisement advertisement = null;
        String error = null;
        try {
            advertisement = advertisementService.createAdvertisement(USER_EMAIL_1, DATE_1, PET_NAME_1,
                    PET_AGE_1, " ", PET_SEX_1, PET_SPECIES_1);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(advertisement);
        assertEquals(PET_DESCRIPTION_ERROR_MESSAGE, error);
    }

    @Test
    public void testCreateAdvertisementNullSex() {
        Advertisement advertisement = null;
        String error = null;
        try {
            advertisement = advertisementService.createAdvertisement(USER_EMAIL_1, DATE_1, PET_NAME_1,
                    PET_AGE_1, PET_DESCRIPTION_1, null, PET_SPECIES_1);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(advertisement);
        assertEquals(PET_SEX_ERROR_MESSAGE, error);
    }

    @Test
    public void testCreateAdvertisementNullSpecies() {
        Advertisement advertisement = null;
        String error = null;
        try {
            advertisement = advertisementService.createAdvertisement(USER_EMAIL_1, DATE_1, PET_NAME_1,
                    PET_AGE_1, PET_DESCRIPTION_1, PET_SEX_1, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(advertisement);
        assertEquals(PET_SPECIES_ERROR_MESSAGE, error);
    }

    @Test
    public void testCreateAdvertisementAllEmpty() {
        Advertisement advertisement = null;
        String error = null;
        try {
            advertisement = advertisementService.createAdvertisement(null, null, null,
                    null, null, null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(advertisement);
        assertEquals(APP_USER_ERROR_MESSAGE, error);
    }

    @Test
    public void testCreateAdvertisementAllPetFieldsEmpty() {
        Advertisement advertisement = null;
        String error = null;
        try {
            advertisement = advertisementService.createAdvertisement(USER_EMAIL_1, DATE_1, null,
                    null, null, null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(advertisement);
        assertEquals(PET_NAME_ERROR_MESSAGE + PET_AGE_ERROR_MESSAGE + PET_DESCRIPTION_ERROR_MESSAGE +
                PET_SEX_ERROR_MESSAGE + PET_SPECIES_ERROR_MESSAGE, error);
    }

    @Test
    public void testUpdateAdvertisement() {
        Advertisement advertisement = ADVERTISEMENT_1;
        try {
            advertisement = advertisementService.updateAdvertisement(ADVERTISEMENT_1_ID, PET_NAME_2, PET_AGE_2,
                    PET_DESCRIPTION_2, PET_SEX_2, PET_SPECIES_2);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertNotNull(advertisement);
        assertEquals(ADVERTISEMENT_1_ID, advertisement.getAdvertisementId());
        assertEquals(PET_NAME_2, advertisement.getPetName());
        assertEquals(PET_AGE_2, advertisement.getPetAge());
        assertEquals(PET_DESCRIPTION_2, advertisement.getPetDescription());
        assertEquals(PET_SEX_2, advertisement.getPetSex());
        assertEquals(PET_SPECIES_2, advertisement.getPetSpecies());
    }

    @Test
    public void testUpdateAdvertisementInvalidId() {
        Advertisement advertisement = null;
        String error = null;
        try {
            advertisement = advertisementService.updateAdvertisement(INVALID_ID, PET_NAME_2, PET_AGE_2,
                    PET_DESCRIPTION_2, PET_SEX_2, PET_SPECIES_2);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(advertisement);
        assertEquals(INVALID_AD_ID_MESSAGE, error);
    }

    @Test
    public void testUpdateAdvertisementNullId() {
        Advertisement advertisement = null;
        String error = null;
        try {
            advertisement = advertisementService.updateAdvertisement(null, PET_NAME_2, PET_AGE_2,
                    PET_DESCRIPTION_2, PET_SEX_2, PET_SPECIES_2);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(advertisement);
        assertEquals(INVALID_AD_ID_MESSAGE, error);
    }

    @Test
    public void testUpdateAdvertisementEmptyId() {
        Advertisement advertisement = null;
        String error = null;
        try {
            advertisement = advertisementService.updateAdvertisement(" ", PET_NAME_2, PET_AGE_2,
                    PET_DESCRIPTION_2, PET_SEX_2, PET_SPECIES_2);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(advertisement);
        assertEquals(INVALID_AD_ID_MESSAGE, error);
    }

    @Test
    public void testUpdateAdvertisementNullPetName() {
        Advertisement advertisement = ADVERTISEMENT_3;
        String error = null;
        try {
            advertisement = advertisementService.updateAdvertisement(ADVERTISEMENT_3_ID, null, PET_AGE_3,
                    PET_DESCRIPTION_3, PET_SEX_3, PET_SPECIES_3);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNotNull(advertisement);
        assertEquals(ADVERTISEMENT_3, advertisement);
        assertEquals(PET_NAME_3, advertisement.getPetName());
        assertEquals(PET_NAME_ERROR_MESSAGE, error);
    }

    @Test
    public void testUpdateAdvertisementEmptyPetName() {
        Advertisement advertisement = ADVERTISEMENT_3;
        String error = null;
        try {
            advertisement = advertisementService.updateAdvertisement(ADVERTISEMENT_3_ID, " ", PET_AGE_3,
                    PET_DESCRIPTION_3, PET_SEX_3, PET_SPECIES_3);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNotNull(advertisement);
        assertEquals(ADVERTISEMENT_3, advertisement);
        assertEquals(PET_NAME_3, advertisement.getPetName());
        assertEquals(PET_NAME_ERROR_MESSAGE, error);
    }

    @Test
    public void testUpdateAdvertisementNullPetAge() {
        Advertisement advertisement = ADVERTISEMENT_3;
        String error = null;
        try {
            advertisement = advertisementService.updateAdvertisement(ADVERTISEMENT_3_ID, PET_NAME_3, null,
                    PET_DESCRIPTION_3, PET_SEX_3, PET_SPECIES_3);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNotNull(advertisement);
        assertEquals(ADVERTISEMENT_3, advertisement);
        assertEquals(PET_AGE_3, advertisement.getPetAge());
        assertEquals(PET_AGE_ERROR_MESSAGE, error);
    }

    @Test
    public void testUpdateAdvertisementNegativePetAge() {
        Advertisement advertisement = ADVERTISEMENT_3;
        String error = null;
        try {
            advertisement = advertisementService.updateAdvertisement(ADVERTISEMENT_3_ID, PET_NAME_3, NEGATIVE_AGE,
                    PET_DESCRIPTION_3, PET_SEX_3, PET_SPECIES_3);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNotNull(advertisement);
        assertEquals(ADVERTISEMENT_3, advertisement);
        assertEquals(PET_AGE_3, advertisement.getPetAge());
        assertEquals(PET_AGE_ERROR_MESSAGE, error);
    }

    @Test
    public void testUpdateAdvertisementNullPetDescription() {
        Advertisement advertisement = ADVERTISEMENT_3;
        String error = null;
        try {
            advertisement = advertisementService.updateAdvertisement(ADVERTISEMENT_3_ID, PET_NAME_3, PET_AGE_3,
                    null, PET_SEX_3, PET_SPECIES_3);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNotNull(advertisement);
        assertEquals(ADVERTISEMENT_3, advertisement);
        assertEquals(PET_DESCRIPTION_3, advertisement.getPetDescription());
        assertEquals(PET_DESCRIPTION_ERROR_MESSAGE, error);
    }

    @Test
    public void testUpdateAdvertisementEmptyPetDescription() {
        Advertisement advertisement = ADVERTISEMENT_3;
        String error = null;
        try {
            advertisement = advertisementService.updateAdvertisement(ADVERTISEMENT_3_ID, PET_NAME_3, PET_AGE_3,
                    " ", PET_SEX_3, PET_SPECIES_3);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNotNull(advertisement);
        assertEquals(ADVERTISEMENT_3, advertisement);
        assertEquals(PET_DESCRIPTION_3, advertisement.getPetDescription());
        assertEquals(PET_DESCRIPTION_ERROR_MESSAGE, error);
    }

    @Test
    public void testUpdateAdvertisementNullSex() {
        Advertisement advertisement = ADVERTISEMENT_3;
        String error = null;
        try {
            advertisement = advertisementService.updateAdvertisement(ADVERTISEMENT_3_ID, PET_NAME_3, PET_AGE_3,
                    PET_DESCRIPTION_3, null, PET_SPECIES_3);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNotNull(advertisement);
        assertEquals(ADVERTISEMENT_3, advertisement);
        assertEquals(PET_SEX_3, advertisement.getPetSex());
        assertEquals(PET_SEX_ERROR_MESSAGE, error);
    }

    @Test
    public void testUpdateAdvertisementNullSpecies() {
        Advertisement advertisement = ADVERTISEMENT_3;
        String error = null;
        try {
            advertisement = advertisementService.updateAdvertisement(ADVERTISEMENT_3_ID, PET_NAME_3, PET_AGE_3,
                    PET_DESCRIPTION_3, PET_SEX_3, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNotNull(advertisement);
        assertEquals(ADVERTISEMENT_3, advertisement);
        assertEquals(PET_SPECIES_3, advertisement.getPetSpecies());
        assertEquals(PET_SPECIES_ERROR_MESSAGE, error);
    }

    //makes sure that we can set the advertisement to expired
    @Test
    public void testUpdateAdvertisementExpire() {
        Advertisement advertisement = ADVERTISEMENT_1;
        try {
            advertisement = advertisementService.updateAdvertisementIsExpired(advertisement.getAdvertisementId(), true);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertNotNull(advertisement);
        assertTrue(advertisement.isIsExpired());
    }

    @Test
    public void testUpdateAdvertisementExpireInvalidId() {
        Advertisement advertisement = null;
        String error = null;
        try {
            advertisement = advertisementService.updateAdvertisementIsExpired(INVALID_ID, true);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(advertisement);
        assertEquals(INVALID_AD_ID_MESSAGE, error);
    }

    @Test
    public void testUpdateAdvertisementExpireNullId(){
        Advertisement advertisement = null;
        String error = null;
        try {
            advertisement = advertisementService.updateAdvertisementIsExpired(null, true);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(advertisement);
        assertEquals(INVALID_AD_ID_MESSAGE, error);
    }

    @Test
    public void testUpdateAdvertisementExpireEmptyId() {
        Advertisement advertisement = null;
        String error = null;
        try {
            advertisement = advertisementService.updateAdvertisementIsExpired(" ", true);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(advertisement);
        assertEquals(INVALID_AD_ID_MESSAGE, error);
    }

    @Test
    public void testGetAdvertisementById() {
        Advertisement advertisement = null;
        try {
            advertisement = advertisementService.getAdvertisementByID(ADVERTISEMENT_3_ID);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertNotNull(advertisement);
        assertAdvertisementEquality(ADVERTISEMENT_3, advertisement);
    }

    @Test
    public void testGetAdvertisementByInvalidId() {
        Advertisement advertisement = null;
        String error = null;
        try {
            advertisement = advertisementService.getAdvertisementByID(INVALID_ID);
            System.out.println(advertisement);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertNull(advertisement);
    }

    @Test
    public void testGetAdvertisementByNullId() {
        Advertisement advertisement = null;
        String error = null;
        try {
            advertisement = advertisementService.getAdvertisementByID(null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(advertisement);
        assertEquals(AD_REQUIRE_ID_MESSAGE, error);
    }

    @Test
    public void testGetAdvertisementByEmptyId(){
        Advertisement advertisement = null;
        String error = null;
        try {
            advertisement = advertisementService.getAdvertisementByID(" ");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(advertisement);
        assertEquals(AD_REQUIRE_ID_MESSAGE, error);
    }

    @Test
    public void testGetAllAdvertisementsOfAppUser() {
        List<Advertisement> advertisements = null;
        try {
            advertisements = advertisementService.getAdvertisementsOfAppUser(USER_EMAIL_2);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertNotNull(advertisements);
        if (advertisements.get(0).getAdvertisementId().equals(ADVERTISEMENT_2_ID)) {
            assertAdvertisementEquality(ADVERTISEMENT_2, advertisements.get(0));
        } else if (advertisements.get(0).getAdvertisementId().equals(ADVERTISEMENT_3_ID)) {
            assertAdvertisementEquality(ADVERTISEMENT_3, advertisements.get(0));
        } else if (advertisements.get(1).getAdvertisementId().equals(ADVERTISEMENT_2_ID)) {
            assertAdvertisementEquality(ADVERTISEMENT_2, advertisements.get(1));
        } else {
            assertAdvertisementEquality(ADVERTISEMENT_3, advertisements.get(1));
        }
    }

    @Test
    public void testGetAllAdvertisementsOfInvalidAppUser() {
        List<Advertisement> advertisements = null;
        String error = null;
        try{
            advertisements = advertisementService.getAdvertisementsOfAppUser(NON_EXISTING_APP_USER);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertNull(advertisements);
        assertEquals(PROFILE_NOT_FOUND_MESSAGE, error);
    }

    @Test
    public void testGetAllAdvertisementsOfNullAppUser() {
        List<Advertisement> advertisements = null;
        String error = null;
        try{
            advertisements = advertisementService.getAdvertisementsOfAppUser(null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertNull(advertisements);
        assertEquals(PROFILE_NOT_FOUND_MESSAGE, error);
    }

    @Test
    public void testGetAllAdvertisementsOfEmptyAppUser() {
        List<Advertisement> advertisements = null;
        String error = null;
        try{
            advertisements = advertisementService.getAdvertisementsOfAppUser(" ");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertNull(advertisements);
        assertEquals(PROFILE_NOT_FOUND_MESSAGE, error);
    }

    @Test
    public void testDeleteAdvertisement() {
        String error = null;
        try {
            advertisementService.deleteAdvertisement(ADVERTISEMENT_3_ID);
        } catch (IllegalArgumentException e){
            error = e.getMessage();
        }
        assertNull(error);
    }

    @Test
    public void testDeleteAdvertisementNullId() {
        String error = null;
        try {
            advertisementService.deleteAdvertisement(null);
        } catch (IllegalArgumentException e){
            error = e.getMessage();
        }
        assertEquals(INVALID_AD_ID_MESSAGE, error);
    }

    @Test
    public void testDeleteAdvertisementByEmptyId() {
        String error = null;
        try {
            advertisementService.deleteAdvertisement(" ");
        } catch (IllegalArgumentException e){
            error = e.getMessage();
        }
        assertEquals(INVALID_AD_ID_MESSAGE, error);
    }

    //Helper methods - Need to take out of here and put them in separate Resource class
    private static Advertisement createAdvertisement(AppUser appUser, Date datePosted, String id, boolean isExpired, String petName,
                                                     Integer petAge, String petDescription, Sex petSex, Species petSpecies) {
        Advertisement advertisement = new Advertisement();
        advertisement.setDatePosted(datePosted);
        advertisement.setAdvertisementId(id);
        advertisement.setIsExpired(isExpired);
        advertisement.setPetName(petName);
        advertisement.setPetAge(petAge);
        advertisement.setPetDescription(petDescription);
        advertisement.setPetSex(petSex);
        advertisement.setPetSpecies(petSpecies);

        advertisement.setPostedBy(appUser);
        advertisement.setPetImages(new HashSet<>());
        advertisement.setApplications(new HashSet<>());

        return advertisement;
    }

    private static void configureAppUser(AppUser appUser, String userEmail, Advertisement advertisement) {
        appUser.setEmail(userEmail);
        appUser.addAdvertisement(advertisement);
    }

    private static void assertAdvertisementEquality(Advertisement expected, Advertisement actual) {
        assertEquals(expected.getPostedBy().getEmail(), actual.getPostedBy().getEmail());
        assertEquals(expected.getDatePosted(), actual.getDatePosted());
        assertEquals(expected.getAdvertisementId(), actual.getAdvertisementId());
        assertEquals(expected.getPetName(), actual.getPetName());
        assertEquals(expected.getPetAge(), actual.getPetAge());
        assertEquals(expected.getPetDescription(),actual.getPetDescription());
        assertEquals(expected.getPetSex(), actual.getPetSex());
        assertEquals(expected.getPetSpecies(), actual.getPetSpecies());
    }
}
