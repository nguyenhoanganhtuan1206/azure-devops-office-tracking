package com.openwt.officetracking.persistent.user;

import com.openwt.officetracking.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.openwt.officetracking.persistent.user.UserEntityMapper.*;
import static org.apache.commons.collections4.IterableUtils.toList;

@Repository
@RequiredArgsConstructor
public class UserStore {

    private final UserRepository userRepository;

    public List<User> findAll() {
        return toUsers(toList(userRepository.findAllByOrderByCreatedAtDesc()));
    }

    public List<User> findAllMentors() {
        return toUsers(toList(userRepository.findAllMentors()));
    }

    public List<User> findByName(final String name) {
        return toUsers(userRepository.findByName(name));
    }

    public Optional<User> findById(final UUID userId) {
        return userRepository.findById(userId)
                .map(UserEntityMapper::toUser);
    }

    public List<User> findAllMentees() {
        return toUsers(toList(userRepository.findAllMentees()));
    }

    public List<User> findAllCoaches() {
        return toUsers(toList(userRepository.findAllCoaches()));
    }

    public Optional<User> findByQRCode(final String qrCode) {
        return userRepository.findByQrCode(qrCode)
                .map(UserEntityMapper::toUser);
    }

    public Optional<User> findByIdentifier(final String identifier) {
        return userRepository.findByIdentifier(identifier)
                .map(UserEntityMapper::toUser);
    }

    public User save(final User user) {
        return toUser(userRepository.save(toUserEntity(user)));
    }

    public Optional<User> findByPersonalEmail(final String personalEmail) {
        return userRepository.findByPersonalEmail(personalEmail).map(UserEntityMapper::toUser);
    }

    public Optional<User> findByCompanyEmail(final String companyEmail) {
        return userRepository.findByCompanyEmail(companyEmail).map(UserEntityMapper::toUser);
    }

    public Optional<User> findByResetPasswordCode(final String code) {
        return userRepository.findByResetPasswordCode(code).map(UserEntityMapper::toUser);
    }
}
