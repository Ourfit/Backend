use ourfit;

create table user
(
    id                   int unsigned auto_increment comment '서비스 내 고유 ID',
    oauth_id             varchar(255)                                     not null comment '가입 시 사용한 OAuth 제공자가 부여한 고유 ID',
    oauth_type           enum ('KAKAO')                                   not null comment 'OAuth 제공자',
    role_type            enum ('SUPER_ADMIN', 'ADMIN', 'MANAGER', 'USER') not null default 'USER' comment '권한',
    email                varchar(255)                                     not null comment '이메일',
    nick_name            varchar(50)                                      not null comment '닉네임',
    age                  tinyint unsigned                                 not null comment '나이',
    gender_type          enum ('F', 'M')                                  not null comment '성별',
    introduction         text                                                      default null comment '간단 소개',
    skill_level_type     enum ('BEGINNER', 'INTERMEDIATE', 'ADVANCED')    not null comment '나의 운동 실력',
    preference_type      enum (
        'WEEKDAY_MORNING', 'WEEKDAY_AFTERNOON', 'WEEKDAY_EVENING',
        'WEEKEND_MORNING', 'WEEKEND_AFTERNOON', 'WEEKEND_EVENING')
                                                                          not null comment '선호하는 운동 시간대',
    profile_image_url    varchar(255)                                              default null comment '프로필 이미지 URL',
    open_chat_url        varchar(255)                                              default null comment '오픈 채팅 URL',
    region1              varchar(50)                                      not null comment '시/도 단위 지역',
    region2              varchar(50)                                      not null comment '시/군/구 단위 지역',
    region3              varchar(50)                                      not null comment '읍/면/동 단위 지역',
    nick_name_updated_at datetime                                                  default null comment '닉네임 변경 일시',
    created_at           datetime                                                  default current_timestamp comment '생성일시',
    created_by           int unsigned comment '생성자',
    updated_at           datetime                                                  default current_timestamp on update current_timestamp comment '수정일시',
    updated_by           int unsigned                                              default null comment '수정자',
    deleted_at           datetime                                                  default null comment '탈퇴일시',
    primary key (id),
    constraint uq_oauth unique (oauth_id, oauth_type),
    constraint uq_email unique (email),
    constraint uq_nick_name unique (nick_name),
    index idx_region (region1, region2, region3)
) engine = InnoDB
  row_format = dynamic
    comment '사용자 정보';

create table workout_type
(
    id         int unsigned auto_increment,
    code       varchar(50) not null comment '운동 유형 코드',
    name       varchar(50) not null comment '운동 유형 이름',
    created_at datetime default current_timestamp comment '생성일시',
    updated_at datetime default current_timestamp on update current_timestamp comment '수정일시',
    primary key (id),
    constraint uq_workout_type_name unique (code)
) engine = InnoDB
    comment '운동 종목 정보';

insert into workout_type (code, name)
values ('GYM', '헬스'),
       ('PILATES', '필라테스'),
       ('SWIMMING', '수영'),
       ('DANCE', '댄스'),
       ('SQUASH', '스쿼시'),
       ('BOXING', '복싱'),
       ('GOLF', '골프'),
       ('TENNIS', '테니스'),
       ('YOGA', '요가'),
       ('CROSSFIT', '크로스핏'),
       ('CLIMBING', '클라이밍'),
       ('TAEKWONDO', '태권도'),
       ('HAPKIDO', '합기도'),
       ('AEROBICS', '에어로빅'),
       ('BALLET', '발레'),
       ('MUAY_THAI', '무에타이'),
       ('BADMINTON', '배드민턴'),
       ('BOWLING', '볼링'),
       ('TABLE_TENNIS', '탁구'),
       ('BASKETBALL', '농구'),
       ('SOCCER', '축구'),
       ('VOLLEYBALL', '배구'),
       ('RUGBY', '럭비'),
       ('JUDO', '유도'),
       ('KENDO', '검도');

create table user_favorite_workout
(
    user_id    int unsigned not null comment '사용자 ID',
    workout_id int unsigned not null comment '운동 유형 ID',
    created_at datetime default current_timestamp comment '생성일시',
    updated_at datetime default current_timestamp on update current_timestamp comment '수정일시',
    primary key (user_id, workout_id),
    constraint fk_user_favorite_workout_user_id foreign key (user_id) references user (id),
    constraint fk_user_favorite_workout_workout_id foreign key (workout_id) references workout_type (id)
) engine = InnoDB
  row_format = dynamic
    comment '사용자가 선호하는 운동 종목 정보';

create table user_favorite_workout_place
(
    id         int unsigned auto_increment,
    user_id    int unsigned not null comment '사용자 ID',
    place_name varchar(100) not null comment '장소(시설)명',
    address    varchar(255) not null comment '주소',
    created_at datetime default current_timestamp comment '생성일시',
    updated_at datetime default current_timestamp on update current_timestamp comment '수정일시',
    primary key (id),
    constraint fk_user_favorite_workout_place_user_id foreign key (user_id) references user (id)
) engine = InnoDB
  row_format = dynamic
    comment '사용자가 선호하는 운동 장소(시설) 정보';

create table mate
(
    id          int unsigned auto_increment,
    me_id       int unsigned                                                  not null comment '요청자 ID',
    my_mate_id  int unsigned                                                  not null comment '요청 대상자 ID',
    status_type enum ('PENDING', 'MATCHED', 'CANCELED','REJECTED', 'UNMATED') not null comment '메이트 상태',
    accepted_at datetime default null comment '메이트 매칭 일시',
    created_at  datetime default current_timestamp comment '생성일시',
    updated_at  datetime default current_timestamp on update current_timestamp comment '수정일시',
    deleted_at  datetime default null comment '삭제일시',
    primary key (id),
    constraint fk_mate_me foreign key (me_id) references user (id),
    constraint fk_mate_my_mate foreign key (my_mate_id) references user (id),
    index idx_mate_me (status_type, me_id),
    index idx_mate_my_mate (status_type, my_mate_id)
) engine = InnoDB
  row_format = dynamic
    comment '운동 메이트 매칭 정보';

create table mate_history
(
    id          int unsigned auto_increment,
    mate_id     int unsigned                                  not null comment '메이트 ID',
    actor_id    int unsigned                                  not null comment '행동을 수행한 사용자 ID',
    target_id   int unsigned                                  not null comment '행동의 대상이 되는 사용자 ID',
    action_type enum ('APPLY', 'RECEIVE', 'ACCEPT', 'UNMATE') not null comment '로그 타입',
    actor_read  boolean                                       not null default false comment '행동을 수행한 사용자가 이 이력을 읽었는지 여부',
    target_read boolean                                       not null default false comment '대상이 되는 사용자가 이 이력을 읽었는지 여부',
    created_at  datetime                                               default current_timestamp comment '생성일시',
    updated_at  datetime                                               default current_timestamp on update current_timestamp comment '수정일시',
    primary key (id),
    constraint fk_mate_history_mate_id foreign key (mate_id) references mate (id),
    constraint fk_mate_history_actor_id foreign key (actor_id) references user (id),
    constraint fk_mate_history_target_id foreign key (target_id) references user (id)
) engine = InnoDB
  row_format = dynamic
    comment '운동 메이트 이력';

create table mate_workout
(
    mate_id             int unsigned                                                                       not null comment '메이트 ID',
    place_name          varchar(100)                                                                       null comment '운동 장소(시설)명',
    address             varchar(255)                                                                       null comment '주소',
    workout_day_of_week set ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY') null comment '함께 운동하는 요일',
    workout_start_at    time                                                                               null comment '함께 운동 시작 시간',
    workout_end_at      time                                                                               null comment '함께 운동 종료 시간',
    created_at          datetime     default current_timestamp comment '생성일시',
    created_by          int unsigned comment '생성자',
    updated_at          datetime     default current_timestamp on update current_timestamp comment '수정일시',
    updated_by          int unsigned default NULL comment '수정자',
    primary key (mate_id),
    constraint fk_mate_workout_mate_id foreign key (mate_id) references mate (id)
) engine = InnoDB
  row_format = dynamic
    comment '운동 메이트 매칭 운동 정보';

create table challenge
(
    id                           int unsigned auto_increment,
    mate_id                      int unsigned                                                                       not null comment '메이트 ID',
    user_id                      int unsigned                                                                       not null comment '도전자 ID',
    goal_workout_count           tinyint unsigned                                                                   not null comment '매주 목표 운동 횟수',
    goal_workout_day_of_week     set ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY') not null comment '매주 목표 운동 요일',
    challenge_duration_in_months tinyint unsigned                                                                   not null comment '매주 목표 운동 기간(월)',
    start_at                     date                                                                               not null comment '챌린지 시작일시',
    end_at                       date                                                                               not null comment '챌린지 종료일시',
    created_at                   datetime default current_timestamp comment '생성일시',
    updated_at                   datetime default current_timestamp on update current_timestamp comment '수정일시',
    deleted_at                   datetime default null comment '삭제일시',
    primary key (id),
    constraint uq_challenge_mate_user unique (mate_id, user_id),
    constraint fk_challenge_mate_id foreign key (mate_id) references mate (id),
    constraint fk_challenge_user_id foreign key (user_id) references user (id),
    check ( goal_workout_count > 0 )
) engine = InnoDB
  row_format = dynamic
    comment '챌린지 정보';

create table challenge_record
(
    id              int unsigned auto_increment,
    challenge_id    int unsigned not null comment '챌린지 ID',
    record_date     date         not null comment '운동 기록(예정) 날짜',
    is_completed    boolean          default false comment '운동 완료 여부',
    intensity_level tinyint unsigned default null comment '강도',
    note            tinytext         default null comment '메모',
    created_at      datetime         default current_timestamp comment '생성일시',
    updated_at      datetime         default current_timestamp on update current_timestamp comment '수정일시',
    primary key (id),
    constraint uq_challenge_record unique (challenge_id, record_date),
    constraint fk_challenge_record_challenge_id foreign key (challenge_id) references challenge (id),
    check ( intensity_level between 1 and 3 )
) engine = InnoDB
  row_format = dynamic
    comment '챌린지 운동 기록';

create table terms
(
    id          int unsigned auto_increment,
    terms_type  enum ('OVER_14_POLICY', 'TERMS_OF_SERVICE', 'PRIVACY_POLICY', 'MARKETING_POLICY', 'MARKETING_KAKAO_POLICY') not null COMMENT '타입',
    is_required boolean       default false                                                                                 not null comment '필수 여부',
    title       varchar(150)                                                                                                not null comment '제목',
    content     text                                                                                                        not null comment '내용',
    version     decimal(3, 1) default 1.0                                                                                   not null comment '버전',
    created_at  datetime      default current_timestamp comment '생성일시',
    created_by  int unsigned comment '생성자',
    updated_at  datetime      default current_timestamp on update current_timestamp comment '수정일시',
    updated_by  int unsigned  default null comment '수정자',
    primary key (id),
    constraint uq_terms unique (terms_type, version)
) engine = InnoDB
  row_format = dynamic
    comment '약관 정보';

create table terms_revision_history
(
    id            int unsigned auto_increment,
    terms_id      int unsigned comment '약관 ID',
    terms_type    enum ('OVER_14_POLICY', 'TERMS_OF_SERVICE', 'PRIVACY_POLICY', 'MARKETING_POLICY', 'MARKETING_KAKAO_POLICY') not null COMMENT '타입',
    title         varchar(150)                                                                                                not null comment '제목',
    content       text                                                                                                        not null comment '내용',
    version       decimal(3, 1) default 1.0                                                                                   not null comment '버전',
    revision_note text                                                                                                        not null comment '개정 이유',
    created_at    datetime      default current_timestamp comment '생성일시',
    created_by    int unsigned comment '생성자',
    updated_at    datetime      default current_timestamp on update current_timestamp comment '수정일시',
    updated_by    int unsigned  default null comment '수정자',
    primary key (id),
    constraint fk_terms_revision_history_terms_id foreign key (terms_id) references terms (id)
) engine = InnoDB
  row_format = dynamic
    comment '약관 개정 이력(SNAPSHOT) 정보';

create table user_terms_agreement
(
    id         int unsigned auto_increment,
    user_id    int unsigned not null comment '사용자 ID',
    terms_id   int unsigned not null comment '약관 ID',
    created_at datetime default current_timestamp comment '생성일시',
    updated_at datetime default current_timestamp on update current_timestamp comment '수정일시',
    primary key (id),
    constraint uq_user_terms unique (user_id, terms_id),
    constraint fk_user_terms_user_id foreign key (user_id) references user (id),
    constraint fk_user_terms_terms_id foreign key (terms_id) references terms (id)
) engine = InnoDB
  row_format = dynamic
    comment '사용자 약관 동의 정보';