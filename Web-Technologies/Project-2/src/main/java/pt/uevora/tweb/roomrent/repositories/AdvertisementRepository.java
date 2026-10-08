package pt.uevora.tweb.roomrent.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import pt.uevora.tweb.roomrent.model.Advertisement;
import pt.uevora.tweb.roomrent.model.Advertisement.AdvertisementType;
import pt.uevora.tweb.roomrent.model.Status;
import pt.uevora.tweb.roomrent.model.User;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

    // Find advertisements by type
    List<Advertisement> findByAdType(AdvertisementType adType);

    // Find top 3 latest advertisements by type
    List<Advertisement> findTop3ByAdTypeAndStatusOrderByCreatedAtDesc(AdvertisementType adType, Status status);

    // Find advertisements by advertiser with pagination
    Page<Advertisement> findByAdvertiser(User advertiser, Pageable pageable);

    // Find advertisements by type and status with pagination
    Page<Advertisement> findByAdTypeAndStatus(AdvertisementType adType, Status status, Pageable pageable);

    // Find advertisement by ID
    Optional<Advertisement> findById(Long id);

    // Find all advertisements ordered by creation date descending with pagination
    Page<Advertisement> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // Find advertisements by type, status, and location containing a substring (case-insensitive) with pagination
    @Query("SELECT a FROM Advertisement a " +
           "WHERE a.adType = :adType AND a.status = :status "+
                 "AND LOWER(a.location) LIKE LOWER(CONCAT('%', :location, '%'))")
    Page<Advertisement> findByAdTypeAndStatusAndLocationContaining (
        @Param("adType") AdvertisementType adType,
        @Param("status") Status status,
        @Param("location") String location,
        Pageable pageables
    );

    // Find advertisements by type, status, and advertiser name containing a substring (case-insensitive) with pagination
    @Query("SELECT a FROM Advertisement a " +
           "WHERE a.adType = :adType AND a.status = :status "+
                 "AND LOWER(a.advertiser.name) LIKE LOWER(CONCAT('%', :name, '%'))") // % for containing match
    Page<Advertisement> findByAdTypeAndStatusAndAdvertiserNameContaining(
        @Param("adType") AdvertisementType adType,
        @Param("status") Status status,
        @Param("name") String name,
        Pageable pageable
    );

}