package io.ourfit.api.domain.user.service;

import io.ourfit.api.domain.user.dto.internal.*;
import io.ourfit.api.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  void save(UserSignUpDto signUpDto);

  Page<UserInfoDto> findAllByConditions(UserSearchDto searchDto, Pageable pageable);

  Optional<User> findByIdWithFavorites(final long id);

  Optional<User> findByEmail(String email);

  void updateBasicInfo(final long id, UserBasicInfoUpdateDto updateDto);

  void updateWorkoutPreferences(final long id, UserWorkoutPreferencesUpdateDto updateDto);

  void updateProfileImage(final long id, MultipartFile profileImage);

  void updateOpenChatUrl(final long id, UserProfileUpdateDto updateDto);
}
