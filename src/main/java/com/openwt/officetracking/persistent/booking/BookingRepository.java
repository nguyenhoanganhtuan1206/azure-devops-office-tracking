package com.openwt.officetracking.persistent.booking;

import com.openwt.officetracking.persistent.booking.BookingEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookingRepository extends CrudRepository<BookingEntity, UUID> {
}
