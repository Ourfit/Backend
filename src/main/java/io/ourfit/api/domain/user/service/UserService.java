package io.ourfit.api.domain.user.service;

import io.ourfit.api.domain.user.data.dto.internal.*;
import io.ourfit.api.domain.user.data.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  void save(UserSignUpDto signUpDto);

  Page<UserInfoDto> findMateCandidates(MatesCandidateSearchDto searchDto, Pageable pageable);

  Optional<User> findById(final long id);

  Optional<User> findByIdWithFavorites(final long id);

  Optional<User> findByOAuthId(String oAuthId);

  boolean existsByOAuthId(String oAuthId);

  void updateBasicInfo(final long id, UserBasicInfoUpdateDto updateDto);

  void updateWorkoutPreferences(final long id, UserWorkoutPreferencesUpdateDto updateDto);

  void updateProfile(final long id, UserProfileUpdateDto updateDto);

  void updateProfileImage(final long id, MultipartFile profileImage);

  void delete(final long id);
}
