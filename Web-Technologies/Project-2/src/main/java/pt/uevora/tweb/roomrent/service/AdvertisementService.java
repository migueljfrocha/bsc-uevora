package pt.uevora.tweb.roomrent.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import pt.uevora.tweb.roomrent.model.Advertisement;
import pt.uevora.tweb.roomrent.model.Advertisement.AdvertisementType;
import pt.uevora.tweb.roomrent.model.Status;
import pt.uevora.tweb.roomrent.model.User;
import pt.uevora.tweb.roomrent.repositories.AdvertisementRepository;


@Service
public class AdvertisementService {
    
    private final AdvertisementRepository adRepo;

    /**
     * Constructor
     * @param adRepo The advertisement repository
     */
    public AdvertisementService(AdvertisementRepository adRepo) {
        this.adRepo = adRepo;
    }

    /**
     * Get the 3 most recent active advertisements of a specific type.
     * @param type The type of advertisement
     * @return A list of the 3 most recent active advertisements of the specified type
     */
    public List<Advertisement> get3MostRecentActiveAds(AdvertisementType type) {
        return adRepo.findTop3ByAdTypeAndStatusOrderByCreatedAtDesc(type, Status.ACTIVE);
    }

    /**
     * Get all advertisements with pagination.
     * @param pageable Pagination information.
     * @return A page of advertisements.
     */
    public Page<Advertisement> getAllAdvertisements(Pageable pageable) {
        return adRepo.findAll(pageable);
    }

    /**
     * Get all advertisements ordered by creation date descending with pagination.
     * @param pageable Pagination information.
     * @return A page of advertisements ordered by creation date descending.
     */
    public Page<Advertisement> getAllAdvertisementsOrderByCreatedAtDesc(Pageable pageable) {
        return adRepo.findAllByOrderByCreatedAtDesc(pageable);
    }
    
    /**
     * Get active advertisements by type with pagination.
     * @param type The type of advertisement.
     * @param pageable Pagination information.
     * @return A page of active advertisements of the specified type.
     */
    public Page<Advertisement> getActiveAdsByType(AdvertisementType type, Pageable pageable) {
        return adRepo.findByAdTypeAndStatus(type, Status.ACTIVE, pageable);
    }

    /**
     * Get active advertisements by location with pagination.
     * @param location The location to filter advertisements.
     * @param type The type of advertisement.
     * @param pageable Pagination information.
     * @return A page of active advertisements matching the location and type.
     */
    public Page<Advertisement> getActiveAdsByLocation(String location, AdvertisementType type, Pageable pageable) {
        return adRepo.findByAdTypeAndStatusAndLocationContaining(type, Status.ACTIVE, location, pageable);
    }
    
    /**
     * Get active advertisements by advertiser name with pagination.
     * @param name The name of the advertiser.
     * @param type The type of advertisement.
     * @param pageable Pagination information.
     * @return A page of active advertisements matching the advertiser name and type.
     */
    public Page<Advertisement> getActiveAdsByAdvertiserName(String name, AdvertisementType type, Pageable pageable) {
        return adRepo.findByAdTypeAndStatusAndAdvertiserNameContaining(type, Status.ACTIVE, name, pageable);
    } 

    /**
     * Get advertisements by a specific advertiser with pagination.
     * @param advertiser The user who posted the advertisements.
     * @param pageable Pagination information.
     * @return A page of advertisements posted by the specified advertiser.
     */
    public Page<Advertisement> getAdsByAdvertiser(User advertiser, Pageable pageable) {
        return adRepo.findByAdvertiser(advertiser, pageable);
    }

    /**
     * Get an advertisement by its ID.
     * @param adId The ID of the advertisement.
     * @return The advertisement with the specified ID.
     * @throws IllegalArgumentException if the advertisement ID is invalid.
     */
    public Advertisement getAdById(Long adId) {
        return adRepo.findById(adId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid advertisement ID: " + adId));
    }

    /**
     * Activate an advertisement by setting its status to ACTIVE.
     * @param adId The ID of the advertisement to activate.
     * @throws IllegalArgumentException if the advertisement ID is invalid.
     */
    @Transactional
    public void activateAdvertisement(Long adId) {
        Advertisement ad = adRepo.findById(adId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid advertisement ID: " + adId));
        ad.setStatus(Status.ACTIVE);
        adRepo.save(ad);
    }

    /**
     * Deactivate an advertisement by setting its status to INACTIVE.
     * @param adId The ID of the advertisement to deactivate.
     * @throws IllegalArgumentException if the advertisement ID is invalid.
     */
    @Transactional
    public void deactivateAdvertisement(Long adId) {
        Advertisement ad = adRepo.findById(adId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid advertisement ID: " + adId));
        ad.setStatus(Status.INACTIVE);
        adRepo.save(ad);
    }
    
    /**
     * Save or update an advertisement.
     * @param advertisement The advertisement to save or update.
     * @return The saved or updated advertisement.
     */
    @Transactional
    public Advertisement saveAdvertisement(Advertisement advertisement) {
        return adRepo.save(advertisement);
    }
}