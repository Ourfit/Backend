alter table challenge drop constraint fk_challenge_mate_id;

alter table challenge
    drop key uq_challenge_mate_user;

alter table challenge
    add constraint uq_challenge_mate_user
        unique (user_id, mate_id, deleted_at);


alter table challenge add constraint fk_challenge_mate_id foreign key (mate_id) references mate (id);