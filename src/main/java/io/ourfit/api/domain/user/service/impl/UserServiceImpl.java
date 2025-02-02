package io.ourfit.api.domain.user.service.impl;

import io.ourfit.api.domain.auth.data.dto.OAuth2UserInfo;
import io.ourfit.api.domain.auth.service.OAuth2Service;
import io.ourfit.api.domain.user.dto.internal.UserSignUpDto;
import io.ourfit.api.domain.user.entity.User;
import io.ourfit.api.domain.user.service.UserService;
import io.ourfit.api.infra.persistence.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository repository;
  private final OAuth2Service oAuth2Service;

  @Override
  public void save(UserSignUpDto signUpDto) {
    OAuth2UserInfo oAuth2UserInfo =
        this.oAuth2Service.authenticate(signUpDto.providerType(), signUpDto.code());
    this.repository.save(User.of(oAuth2UserInfo, signUpDto));
    // TODO: Workout -> 선호하는 운동도 같이 등록 처리
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findByEmail(String email) {
    return this.repository.findByEmail(email);
  }
}
