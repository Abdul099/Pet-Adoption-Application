package ca.mcgill.ecse321.petadoption.dao;

import ca.mcgill.ecse321.petadoption.model.Advertisement;
import org.springframework.data.repository.CrudRepository;

public interface AdvertisementRepository extends CrudRepository<Advertisement, Integer> {

    Advertisement findAdvertisementById(Integer id);

}