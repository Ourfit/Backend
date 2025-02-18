create table legal_district
(
    region1   varchar(30)  not null comment '특별시/광역시/도',
    region2   varchar(30)  not null comment '시군구',
    region3   varchar(30)  not null comment '읍면동',
    full_name varchar(100) not null comment '전체 이름',
    constraint uq_region unique (region1, region2, region3),
    fulltext index idx_region (region1, region2, region3)
) engine = InnoDB
    comment '법정동 정보';

