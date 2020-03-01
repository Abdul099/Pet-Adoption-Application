package ca.mcgill.ecse321.petadoption.TestSuits.Utils;

import ca.mcgill.ecse321.petadoption.model.*;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestUtils {

    public static Donation createDonation(AppUser donor, Integer amount, Date dateOfPayment) {
        Donation newDonation = new Donation();
        newDonation.setDonor(donor);
        newDonation.setAmount(amount);
        newDonation.setTransactionID();
        newDonation.setDateOfPayment(dateOfPayment);
        return newDonation;
    }

    public static void assertDonation(Donation donation, String userEmail, Integer amount, Date dateOfPayment) {
        assertNotNull(donation);
        assertEquals(userEmail, donation.getDonor().getEmail());
        assertEquals(amount, donation.getAmount());
        assertEquals(dateOfPayment, donation.getDateOfPayment());
    }

    public static AppUser createAppUser(String name, String email, String password,
                                String biography, String homeDescription, Integer age, boolean isAdmin, Sex sex) {
        AppUser new_user = new AppUser();
        new_user.setSex(sex);
        new_user.setPassword(password);
        new_user.setIsAdmin(isAdmin);
        new_user.setHomeDescription(homeDescription);
        new_user.setBiography(biography);
        new_user.setAge(age);
        new_user.setEmail(email);
        new_user.setName(name);

        Advertisement ad = new Advertisement();
        ad.setAdvertisementId();;
        new_user.addAdvertisement(ad);

        Application app = new Application();
        app.setApplicationId();
        new_user.addApplication(app);

        // TODO: ZAK: use createDonation AFTER CONSTRUCTING a user
//        Donation don = new Donation();
//        don.setTransactionID();
//        new_user.addDonation(don);
          return new_user;
    }

    public static void assertAppUser(AppUser user,String name, String email, String password,
                                     String biography, String homeDescription, Integer age, boolean isAdmin, Sex sex ){
        assertNotNull(user);
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(biography, user.getBiography());
        assertEquals(homeDescription, user.getHomeDescription());
        assertEquals(age, user.getAge());
        assertEquals(sex, user.getSex());
        assertEquals(isAdmin, user.isIsAdmin());
    }


}
