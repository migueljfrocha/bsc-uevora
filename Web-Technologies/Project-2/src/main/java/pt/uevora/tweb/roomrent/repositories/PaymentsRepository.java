package pt.uevora.tweb.roomrent.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pt.uevora.tweb.roomrent.model.Advertisement;
import pt.uevora.tweb.roomrent.model.Payment;
import pt.uevora.tweb.roomrent.model.User;

public interface PaymentsRepository extends JpaRepository<Payment, Long>{
    
    // Find payment by advertisement
    Payment findByAdvertisement(Advertisement advertisement);
    
    // Find payments by advertiser
    List<Payment> findByAdvertisementAdvertiser(User advertiser);
}
