package io.ourfit.api.domain.user.service;

import io.ourfit.api.domain.user.dto.internal.UserBasicInfoUpdateDto;
import io.ourfit.api.domain.user.dto.internal.UserSignUpDto;
import io.ourfit.api.domain.user.dto.internal.UserWorkoutPreferencesUpdateDto;
import io.ourfit.api.domain.user.entity.User;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  void save(UserSignUpDto signUpDto);

  Optional<User> findByIdWithFavorites(final long id);

  Optional<User> findByEmail(String email);

  void updateBasicInfo(final long id, UserBasicInfoUpdateDto updateDto);

  void updateWorkoutPreferences(final long id, UserWorkoutPreferencesUpdateDto updateDto);

  void updateProfileImage(final long id, MultipartFile profileImage);
}
