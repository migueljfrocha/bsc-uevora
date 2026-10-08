package pt.uevora.tweb.roomrent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import pt.uevora.tweb.roomrent.model.Advertisement;
import pt.uevora.tweb.roomrent.model.Message;
import pt.uevora.tweb.roomrent.model.User;

@Repository
public interface MessagesRepository extends JpaRepository<Message, Long> {

    // Get all messages received by a specific user
    Page<Message> findAllByUserTo(User to, Pageable pageable);

    // Get all messages received by a specific user, ordered by sent date descending (from most recent to oldest)
    Page<Message> findAllByUserToOrderBySentAtDesc(User to, Pageable pageable);
    
    // Get all messages related to a specific advertisement
    Page<Message> findAllByAdvertisement(Advertisement advertisement, Pageable pageable);
}
