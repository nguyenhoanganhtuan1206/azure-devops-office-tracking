package com.openwt.officetracking.api.user_mobile;

import com.openwt.officetracking.api.AbstractControllerTest;
import com.openwt.officetracking.api.WithMockAdmin;
import com.openwt.officetracking.api.WithMockUser;
import com.openwt.officetracking.domain.user_mobile.UserMobile;
import com.openwt.officetracking.domain.user_mobile.UserMobileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.openwt.officetracking.fake.UserMobileFakes.*;
import static org.mockito.ArgumentMatchers.any;
import static com.openwt.officetracking.fake.UserMobileFakes.buildUserMobileDetail;
import static com.openwt.officetracking.fake.UserMobileFakes.buildUserMobiles;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserMobileController.class)
@ExtendWith(SpringExtension.class)
class UserMobileControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/api/v1/user-mobiles";

    @MockBean
    private UserMobileService userMobileService;

    @Test
    @WithMockAdmin
    void findAllUserDeviceInformation_OK() throws Exception {
        final var userDevices = buildUserMobiles();

        when(userMobileService.findAll()).thenReturn(userDevices);

        get(BASE_URL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(userDevices.size()))
                .andExpect(jsonPath("$[0].id").value(userDevices.get(0).getId().toString()))
                .andExpect(jsonPath("$[0].userId").value(userDevices.get(0).getUserId().toString()))
                .andExpect(jsonPath("$[0].deviceType").value(userDevices.get(0).getDeviceType()))
                .andExpect(jsonPath("$[0].model").value(userDevices.get(0).getModel()))
                .andExpect(jsonPath("$[0].osVersion").value(userDevices.get(0).getOsVersion()))
                .andExpect(jsonPath("$[0].active").value(userDevices.get(0).isActive()))
                .andExpect(jsonPath("$[0].registeredAt").value(userDevices.get(0).getRegisteredAt().toString()));

        verify(userMobileService).findAll();
    }

    @Test
    @WithMockUser
    void findUpdate_OK() throws Exception {
        final var userDevice = buildUserMobile();
        final var updateMobile = buildUpdateUserMobileDTO();

        when(userMobileService.update(any(UserMobile.class))).thenReturn(userDevice);

        put(BASE_URL, updateMobile)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deviceType").value(userDevice.getDeviceType()))
                .andExpect(jsonPath("$.model").value(userDevice.getModel()))
                .andExpect(jsonPath("$.osVersion").value(userDevice.getOsVersion()))
                .andExpect(jsonPath("$.fcmToken").value(userDevice.getFcmToken()));

        verify(userMobileService).update(any(UserMobile.class));
    }

    @Test
    @WithMockAdmin
    void findUserMobileDetailById_OK() throws Exception {
        final var mobileDetail = buildUserMobileDetail();

        when(userMobileService.findDetailById(mobileDetail.getId())).thenReturn(mobileDetail);

        get(BASE_URL + "/" + mobileDetail.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mobileDetail.getId().toString()))
                .andExpect(jsonPath("$.deviceType").value(mobileDetail.getDeviceType()))
                .andExpect(jsonPath("$.model").value(mobileDetail.getModel()))
                .andExpect(jsonPath("$.osVersion").value(mobileDetail.getOsVersion()))
                .andExpect(jsonPath("$.registeredAt").value(mobileDetail.getRegisteredAt().toString()));

        verify(userMobileService).findDetailById(mobileDetail.getId());
    }

    @Test
    @WithMockUser
    void findUpdateMobilePermission_OK() throws Exception {
        final var mobilePermission = buildUpdateMobilePermission();

        when(userMobileService.updateMobilePermissions(argThat(permissionArg -> permissionArg.getIsEnableBluetooth()
                == mobilePermission.getIsEnableBluetooth())))
                .thenReturn(mobilePermission);

        put(BASE_URL + "/permissions", mobilePermission)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isEnableBluetooth").value(mobilePermission.getIsEnableBluetooth()))
                .andExpect(jsonPath("$.isEnableLocation").value(mobilePermission.getIsEnableLocation()))
                .andExpect(jsonPath("$.isEnableBackground").value(mobilePermission.getIsEnableBackground()))
                .andExpect(jsonPath("$.isEnableNotification").value(mobilePermission.getIsEnableNotification()));

        verify(userMobileService).updateMobilePermissions(argThat(permissionArg -> permissionArg.getIsEnableBluetooth()
                == mobilePermission.getIsEnableBluetooth()));
    }
}