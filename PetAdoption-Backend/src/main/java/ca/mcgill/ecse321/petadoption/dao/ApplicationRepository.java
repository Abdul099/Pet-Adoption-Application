package ca.mcgill.ecse321.petadoption.dao;


import ca.mcgill.ecse321.petadoption.model.Application;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;


public interface ApplicationRepository extends CrudRepository<Application, Long> {

    Application findApplicationByApplicationId(String id);

    void deleteApplicationByApplicationId(String id);

    Application findApplicationByAdvertisement_AdvertisementIdAndApplicant_Email(String id, String email);

    List<Application> findApplicationByAdvertisement_AdvertisementId(String id);
}
