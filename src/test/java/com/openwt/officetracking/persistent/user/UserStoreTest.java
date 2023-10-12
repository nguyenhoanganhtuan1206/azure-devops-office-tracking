package com.openwt.officetracking.persistent.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static com.openwt.officetracking.fake.UserFakes.*;
import static com.openwt.officetracking.persistent.user.UserEntityMapper.toUser;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserStoreTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserStore userStore;

    @Test
    void shouldFindByPersonalEmail_OK() {
        final var user = buildUserEntity();
        final var userOptional = Optional.of(user);

        when(userRepository.findByPersonalEmail(user.getPersonalEmail()))
                .thenReturn(Optional.of(user));

        final var expected = userOptional.get();
        final var actual = userStore.findByPersonalEmail(user.getPersonalEmail()).get();

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getPersonalEmail(), actual.getPersonalEmail());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getRole(), actual.getRole());
        assertEquals(expected.getQrCode(), actual.getQrCode());
        assertEquals(expected.getPhoto(), actual.getPhoto());
        assertEquals(expected.getGender(), actual.getGender());
        assertEquals(expected.getContractType(), actual.getContractType());

        verify(userRepository).findByPersonalEmail(user.getPersonalEmail());
    }

    @Test
    void shouldFindByPersonalEmail_IsEmpty() {
        final var email = randomAlphabetic(3, 10);

        when(userRepository.findByPersonalEmail(email)).thenReturn(Optional.empty());

        final var actual = userRepository.findByPersonalEmail(email);

        assertFalse(actual.isPresent());

        verify(userRepository).findByPersonalEmail(email);
    }

    @Test
    void shouldFindByCompanyEmail_OK() {
        final var user = buildUserEntity();
        final var userOptional = Optional.of(user);

        when(userRepository.findByCompanyEmail(user.getCompanyEmail()))
                .thenReturn(Optional.of(user));

        final var expected = userOptional.get();
        final var actual = userStore.findByCompanyEmail(user.getCompanyEmail()).get();

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getPersonalEmail(), actual.getPersonalEmail());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getRole(), actual.getRole());
        assertEquals(expected.getQrCode(), actual.getQrCode());
        assertEquals(expected.getPhoto(), actual.getPhoto());
        assertEquals(expected.getGender(), actual.getGender());
        assertEquals(expected.getContractType(), actual.getContractType());

        verify(userRepository).findByCompanyEmail(user.getCompanyEmail());
    }

    @Test
    void shouldFindByCompanyEmail_IsEmpty() {
        final var email = randomAlphabetic(3, 10);

        when(userRepository.findByCompanyEmail(email)).thenReturn(Optional.empty());

        final var actual = userRepository.findByCompanyEmail(email);

        assertFalse(actual.isPresent());

        verify(userRepository).findByCompanyEmail(email);
    }

    @Test
    void shouldFindAll_OK() {
        final var users = buildUserEntities();

        when(userRepository.findAllByOrderByCreatedAtDesc()).thenReturn(users);

        final var actual = userStore.findAll();

        assertEquals(users.size(), actual.size());

        verify(userRepository).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void shouldFindById_OK() {
        final var user = buildUserEntity();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        final var actual = userRepository.findById(user.getId()).get();

        assertEquals(user.getId(), actual.getId());
        assertEquals(user.getFirstName(), actual.getFirstName());
        assertEquals(user.getLastName(), actual.getLastName());
        assertEquals(user.getPersonalEmail(), actual.getPersonalEmail());
        assertEquals(user.getIdentifier(), actual.getIdentifier());
        assertEquals(user.getPositionId(), actual.getPositionId());
        assertEquals(user.getRole(), actual.getRole());
        assertEquals(user.getQrCode(), actual.getQrCode());
        assertEquals(user.getPhoto(), actual.getPhoto());
        assertEquals(user.getGender(), actual.getGender());
        assertEquals(user.getContractType(), actual.getContractType());

        verify(userRepository).findById(user.getId());
    }

    @Test
    void shouldFindById_Empty() {
        final var userId = randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        final var actual = userStore.findById(userId);

        assertFalse(actual.isPresent());

        verify(userRepository).findById(userId);
    }

    @Test
    void findByQRCode_OK() {
        final var user = buildUserEntity();

        when(userRepository.findByQrCode(user.getQrCode()))
                .thenReturn(Optional.of(user));

        final var actual = userRepository.findByQrCode(user.getQrCode()).get();

        assertEquals(user.getId(), actual.getId());
        assertEquals(user.getFirstName(), actual.getFirstName());
        assertEquals(user.getLastName(), actual.getLastName());
        assertEquals(user.getLastName(), actual.getLastName());
        assertEquals(user.getPersonalEmail(), actual.getPersonalEmail());
        assertEquals(user.getIdentifier(), actual.getIdentifier());
        assertEquals(user.getPositionId(), actual.getPositionId());
        assertEquals(user.getRole(), actual.getRole());
        assertEquals(user.getContractType(), actual.getContractType());
        assertEquals(user.getQrCode(), actual.getQrCode());

        verify(userRepository).findByQrCode(user.getQrCode());
    }

    @Test
    void shouldFindByQRCode_Empty() {
        final var qrCode = randomAlphabetic(6);

        when(userRepository.findByQrCode(qrCode)).thenReturn(Optional.empty());

        final var actual = userStore.findByQRCode(qrCode);

        assertFalse(actual.isPresent());
        verify(userRepository).findByQrCode(qrCode);
    }

    @Test
    void findByIdentifier_OK() {
        final var user = buildUserEntity();

        when(userRepository.findByIdentifier(user.getIdentifier()))
                .thenReturn(Optional.of(user));

        final var actual = userRepository.findByIdentifier(user.getIdentifier()).get();

        assertEquals(user.getId(), actual.getId());
        assertEquals(user.getFirstName(), actual.getFirstName());
        assertEquals(user.getLastName(), actual.getLastName());
        assertEquals(user.getLastName(), actual.getLastName());
        assertEquals(user.getPersonalEmail(), actual.getPersonalEmail());
        assertEquals(user.getIdentifier(), actual.getIdentifier());
        assertEquals(user.getPositionId(), actual.getPositionId());
        assertEquals(user.getRole(), actual.getRole());
        assertEquals(user.getQrCode(), actual.getQrCode());
        assertEquals(user.getPhoto(), actual.getPhoto());
        assertEquals(user.getGender(), actual.getGender());
        assertEquals(user.getContractType(), actual.getContractType());

        verify(userRepository).findByIdentifier(user.getIdentifier());
    }

    @Test
    void shouldFindByIdentifier_Empty() {
        final var userIdentifier = randomAlphabetic(3, 10);

        when(userRepository.findByIdentifier(userIdentifier)).thenReturn(Optional.empty());

        final var actual = userStore.findByIdentifier(userIdentifier);

        assertFalse(actual.isPresent());
        verify(userRepository).findByIdentifier(userIdentifier);
    }

    @Test
    void shouldFindByResetPasswordCode_OK() {
        final var user = buildUserEntity();
        final var userOptional = Optional.of(user);

        when(userRepository.findByResetPasswordCode(user.getResetPasswordCode()))
                .thenReturn(Optional.of(user));

        final var expected = userOptional.get();
        final var actual = userStore.findByResetPasswordCode(user.getResetPasswordCode()).get();

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getPersonalEmail(), actual.getPersonalEmail());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getRole(), actual.getRole());
        assertEquals(expected.getQrCode(), actual.getQrCode());
        assertEquals(expected.getPhoto(), actual.getPhoto());
        assertEquals(expected.getGender(), actual.getGender());
        assertEquals(expected.getContractType(), actual.getContractType());
        assertEquals(expected.getResetPasswordCode(), actual.getResetPasswordCode());

        verify(userRepository).findByResetPasswordCode(user.getResetPasswordCode());
    }

    @Test
    void shouldFindByResetPasswordCode_IsEmpty() {
        final var code = randomAlphabetic(3, 10);

        when(userRepository.findByResetPasswordCode(code)).thenReturn(Optional.empty());

        final var actual = userRepository.findByResetPasswordCode(code);

        assertFalse(actual.isPresent());

        verify(userRepository).findByResetPasswordCode(code);
    }

    @Test
    void shouldSave_OK() {
        final var user = buildUserEntity();

        when(userRepository.save(any()))
                .thenReturn(user);

        final var actual = userStore.save(toUser(user));

        assertEquals(user.getId(), actual.getId());
        assertEquals(user.getFirstName(), actual.getFirstName());
        assertEquals(user.getLastName(), actual.getLastName());
        assertEquals(user.getLastName(), actual.getLastName());
        assertEquals(user.getPersonalEmail(), actual.getPersonalEmail());
        assertEquals(user.getIdentifier(), actual.getIdentifier());
        assertEquals(user.getPositionId(), actual.getPositionId());
        assertEquals(user.getRole(), actual.getRole());
        assertEquals(user.getQrCode(), actual.getQrCode());
        assertEquals(user.getPhoto(), actual.getPhoto());
        assertEquals(user.getGender(), actual.getGender());
        assertEquals(user.getContractType(), actual.getContractType());

        verify(userRepository).save(any());
    }

    @Test
    void shouldFindByName_OK() {
        final var user = buildUser();
        final var users = buildUserEntities();

        when(userRepository.findByName(user.getFirstName()))
                .thenReturn(users);

        final var actual = userStore.findByName(user.getFirstName());

        assertEquals(users.get(0).getId().toString(), actual.get(0).getId().toString());
        assertEquals(users.get(0).getFirstName(), actual.get(0).getFirstName());
        assertEquals(users.get(0).getLastName(), actual.get(0).getLastName());
        assertEquals(users.get(0).getPersonalEmail(), actual.get(0).getPersonalEmail());
        assertEquals(users.get(0).getPositionId().toString(), actual.get(0).getPositionId().toString());
        assertEquals(users.get(0).getIdentifier(), actual.get(0).getIdentifier());
        assertEquals(users.get(0).getQrCode(), actual.get(0).getQrCode());
        assertEquals(users.get(0).getGender(), actual.get(0).getGender());
        assertEquals(users.get(0).getContractType(), actual.get(0).getContractType());

        verify(userRepository).findByName(user.getFirstName());
    }

    @Test
    void shouldFindByName_IsEmpty() {
        final var firstName = randomAlphabetic(9, 10);

        when(userRepository.findByName(firstName))
                .thenReturn(Collections.emptyList());

        final var actual = userStore.findByName(firstName);

        assertTrue(actual.isEmpty());

        verify(userRepository).findByName(firstName);
    }
}
