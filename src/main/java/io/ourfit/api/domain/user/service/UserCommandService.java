package io.ourfit.api.domain.user.service;

import io.ourfit.api.domain.user.data.dto.internal.UserBasicInfoUpdateDto;
import io.ourfit.api.domain.user.data.dto.internal.UserProfileUpdateDto;
import io.ourfit.api.domain.user.data.dto.internal.UserSignUpDto;
import io.ourfit.api.domain.user.data.dto.internal.UserWorkoutPreferencesUpdateDto;
import org.springframework.web.multipart.MultipartFile;

public interface UserCommandService {

  void save(UserSignUpDto signUpDto);

  void updateBasicInfo(final long id, UserBasicInfoUpdateDto updateDto);

  void updateWorkoutPreferences(final long id, UserWorkoutPreferencesUpdateDto updateDto);

  void updateProfile(final long id, UserProfileUpdateDto updateDto);

  void updateProfileImage(final long id, MultipartFile profileImage);

  void delete(final long id);
}
