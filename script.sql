create schema websitefacultate;

use aplicatieDB;

create table departament
(
    id       int auto_increment
        primary key,
    denumire varchar(50) not null
);

create table rol
(
    id   int auto_increment
        primary key,
    nume varchar(50) not null
);

create table user
(
    id          int auto_increment
        primary key,
    CNP         varchar(15)  not null,
    nume        varchar(50)  not null,
    prenume     varchar(50)  not null,
    adresa      varchar(100) not null,
    nr_telefon  varchar(12)  not null,
    email       varchar(50)  not null,
    IBAN        varchar(30)  not null,
    nr_contract varchar(30)  not null,
    username    varchar(50)  not null,
    parola      varchar(50)  not null,
    idRol       int          null,
    constraint user_username_uindex
        unique (username),
    constraint user_rol_id_fk
        foreign key (idRol) references rol (id)
);

create table mesaj_privat
(
    idFrom        int      not null,
    idTo          int      not null,
    text          text     null,
    id            int auto_increment
        primary key,
    dataTrimitere datetime null,
    constraint mesaj_privat_user_id_fk
        foreign key (idFrom) references user (id),
    constraint mesaj_privat_user_id_fk_2
        foreign key (idTo) references user (id)
);

create table profesori
(
    id            int not null
        primary key,
    minOre        int not null,
    maxOre        int not null,
    idDepartament int null,
    constraint profesori_departament_id_fk
        foreign key (idDepartament) references departament (id),
    constraint profesori_user_id_fk
        foreign key (id) references user (id)
);

create table materie
(
    id            int auto_increment
        primary key,
    nume          varchar(50)      not null,
    idDepartament int              null,
    idTitular     int              null,
    curs          double default 0 null,
    seminar       double default 0 null,
    lab           double default 0 null,
    constraint materie_departament_id_fk
        foreign key (idDepartament) references departament (id),
    constraint materie_profesori_id_fk
        foreign key (idTitular) references profesori (id)
);

create table activitate_didactica
(
    id                int auto_increment
        primary key,
    tip               enum ('CURS', 'SEMINAR', 'LABORATOR') not null,
    denumire          varchar(50)                           not null,
    data_incepere     datetime                              not null,
    data_sfarsit      datetime                              not null,
    nr_maxim          int                                   not null,
    durata_activitate int                                   not null,
    idMaterie         int                                   null,
    constraint activitate_didactica_materie_id_fk
        foreign key (idMaterie) references materie (id)
);

create table didactic
(
    idProf    int  not null,
    idActDic  int  not null,
    dataAsign date null,
    primary key (idProf, idActDic),
    constraint didactic_activitate_didactica_id_fk
        foreign key (idActDic) references activitate_didactica (id),
    constraint didactic_profesori_id_fk
        foreign key (idProf) references profesori (id)
);

create definer = root@localhost trigger t_zi_alert_profesori
    after insert
    on didactic
    for each row
begin
    declare data_var date;
    declare data_incheiere_var date;

    set data_var = (select data_incepere from activitate_didactica where id = new.idActDic);
    set data_incheiere_var = (select data_sfarsit from activitate_didactica where id = new.idActDic);

    while datediff(data_incheiere_var, data_var) >= 0
        do
            insert into zi(idActivitate, dataCurenta, idUser)
            values (new.idActDic, data_var, new.idProf);
            select date_add(data_var, interval 7 day) into data_var;
        end while;

end;

create table grup
(
    id        int auto_increment
        primary key,
    idMaterie int         null,
    idProf    int         null,
    descriere text        null,
    denumire  varchar(50) null,
    constraint Grup_materie_id_fk
        foreign key (idMaterie) references materie (id),
    constraint grup_profesori_id_fk
        foreign key (idProf) references profesori (id)
);

create table mesaj
(
    idStudent     int      not null,
    idGrup        int      not null,
    textMesaj     text     null,
    dataTrimitere datetime null,
    id            int auto_increment
        primary key,
    constraint mesaj_grup_id_fk
        foreign key (idGrup) references grup (id),
    constraint mesaj_user_id_fk
        foreign key (idStudent) references user (id)
);

create table student
(
    id            int not null
        primary key,
    an_studiu     int not null,
    ore_sustinute int not null,
    constraint student_user_id_fk
        foreign key (id) references user (id)
);

create table examen
(
    idMaterie int    not null,
    idStudent int    not null,
    valoare   double null,
    primary key (idMaterie, idStudent),
    constraint examen_materie_id_fk
        foreign key (idMaterie) references materie (id),
    constraint examen_student_id_fk
        foreign key (idStudent) references student (id)
);

create table inscrierestudent
(
    idStudent     int  not null,
    idMaterie     int  not null,
    dataInscriere date not null,
    primary key (idStudent, idMaterie),
    constraint inscrierestudent_materie_id_fk
        foreign key (idMaterie) references materie (id),
    constraint inscrierestudent_student_id_fk
        foreign key (idStudent) references student (id)
);

create definer = root@localhost trigger t_delete_after_delete
    after delete
    on inscrierestudent
    for each row
begin
    delete
    from zi
    where old.idStudent = zi.idUser
      and zi.idActivitate in (select id from activitate_didactica where idMaterie = old.idMaterie);

    delete
    from note
    where note.idStudent = OLD.idStudent
      and note.idActDic in (select id from activitate_didactica where idMaterie = old.idMaterie);

    delete
    from examen
    where examen.idStudent = OLD.idStudent
      and examen.idMaterie = OLD.idMaterie;
end;

create definer = root@localhost trigger t_zi_alert_studenti
    after insert
    on inscrierestudent
    for each row
begin
    declare data_var_curs date;
    declare data_var_seminar date;
    declare data_var_lab date;
    declare data_incheiere_var_curs date;
    declare data_incheiere_var_seminar date;
    declare data_incheiere_var_lab date;
    declare id_lab int;
    declare id_seminar int;
    declare id_curs int;

    set id_lab = (select ad.id
                  from activitate_didactica ad
                           inner join materie m on m.id = ad.idMaterie
                  where m.id = new.idMaterie
                    and tip = 'LABORATOR');
    set id_seminar = (select ad.id
                      from activitate_didactica ad
                               inner join materie m on m.id = ad.idMaterie
                      where m.id = new.idMaterie
                        and tip = 'SEMINAR');
    set id_curs = (select ad.id
                   from activitate_didactica ad
                            inner join materie m on m.id = ad.idMaterie
                   where m.id = new.idMaterie
                     and tip = 'CURS');
    if id_curs is not null then
        set data_var_curs = (select data_incepere from activitate_didactica where id = id_curs);
        set data_incheiere_var_curs = (select data_sfarsit from activitate_didactica where id = id_curs);

        while datediff(data_incheiere_var_curs, data_var_curs) >= 0
            do
                insert into zi(idActivitate, dataCurenta, idUser)
                values (id_curs, data_var_curs, new.idStudent);
                select date_add(data_var_curs, interval 7 day) into data_var_curs;
            end while;
    end if;

    if id_seminar is not null then
        set data_var_seminar = (select data_incepere from activitate_didactica where id = id_seminar);
        set data_incheiere_var_seminar = (select data_sfarsit from activitate_didactica where id = id_seminar);

        while datediff(data_incheiere_var_seminar, data_var_seminar) >= 0
            do
                insert into zi(idActivitate, dataCurenta, idUser)
                values (id_seminar, data_var_seminar, new.idStudent);
                select date_add(data_var_seminar, interval 7 day) into data_var_seminar;
            end while;
    end if;

    if id_lab is not null then
        set data_var_lab = (select data_incepere from activitate_didactica where id = id_lab);
        set data_incheiere_var_lab = (select data_sfarsit from activitate_didactica where id = id_lab);

        while datediff(data_incheiere_var_lab, data_var_lab) >= 0
            do
                insert into zi(idActivitate, dataCurenta, idUser)
                values (id_lab, data_var_lab, new.idStudent);
                select date_add(data_var_lab, interval 7 day) into data_var_lab;
            end while;
    end if;

end;

create table intalnire
(
    id              int auto_increment
        primary key,
    titlu           varchar(100) not null,
    nr_participanti int          not null,
    durata          int          not null,
    dataIncepere    datetime     not null,
    dataExpirare    datetime     not null,
    idCreator       int          not null,
    idGrup          int          not null,
    constraint intalnire_grup_id_fk
        foreign key (idGrup) references grup (id),
    constraint intalnire_student_id_fk
        foreign key (idCreator) references student (id)
);

create table note
(
    valoare        double not null,
    data_atribuire date   not null,
    idStudent      int    not null,
    idActDic       int    not null,
    primary key (idStudent, idActDic),
    constraint note_activitate_didactica_id_fk
        foreign key (idActDic) references activitate_didactica (id),
    constraint note_student_id_fk
        foreign key (idStudent) references student (id)
);

create definer = root@localhost trigger t_actualizare_nota_examen_insert
    after insert
    on note
    for each row
begin
    declare nota_curs double;
    declare nota_seminar double;
    declare nota_laborator double;
    declare tip_nota enum ('CURS','SEMINAR','LABORATOR');
    declare id_materie int;
    declare procent_curs double;
    declare procent_seminar double;
    declare procent_laborator double;
    declare nota_examen double;
    set tip_nota = (select tip from activitate_didactica where id = NEW.idActDic);
    set id_materie = (select idMaterie from activitate_didactica where id = new.idActDic);
    set procent_curs = (select curs from materie where id = id_materie);
    set procent_seminar = (select seminar from materie where id = id_materie);
    set procent_laborator = (select lab from materie where id = id_materie);
    if tip_nota = 'CURS' then
        set nota_curs = new.valoare;
        set nota_laborator = (
            select valoare
            from note n
                     inner join activitate_didactica ad on n.idActDic = ad.id
                     inner join materie m on ad.idMaterie = m.id
            where n.idStudent = NEW.idStudent
              and m.id = id_materie
              and tip = 'LABORATOR'
        );
        set nota_seminar = (
            select valoare
            from note n
                     inner join activitate_didactica ad on n.idActDic = ad.id
                     inner join materie m on ad.idMaterie = m.id
            where n.idStudent = NEW.idStudent
              and m.id = id_materie
              and tip = 'SEMINAR'
        );
    elseif tip_nota = 'SEMINAR' then
        set nota_seminar = new.valoare;
        set nota_curs = (
            select valoare
            from note n
                     inner join activitate_didactica ad on n.idActDic = ad.id
                     inner join materie m on ad.idMaterie = m.id
            where n.idStudent = NEW.idStudent
              and m.id = id_materie
              and tip = 'CURS'
        );
        set nota_laborator = (
            select valoare
            from note n
                     inner join activitate_didactica ad on n.idActDic = ad.id
                     inner join materie m on ad.idMaterie = m.id
            where n.idStudent = NEW.idStudent
              and m.id = id_materie
              and tip = 'LABORATOR'
        );
    else
        set nota_laborator = new.valoare;
        set nota_curs = (
            select valoare
            from note n
                     inner join activitate_didactica ad on n.idActDic = ad.id
                     inner join materie m on ad.idMaterie = m.id
            where n.idStudent = NEW.idStudent
              and m.id = id_materie
              and tip = 'CURS'
        );
        set nota_seminar = (
            select valoare
            from note n
                     inner join activitate_didactica ad on n.idActDic = ad.id
                     inner join materie m on ad.idMaterie = m.id
            where n.idStudent = NEW.idStudent
              and m.id = id_materie
              and tip = 'SEMINAR'
        );
    end if;

    if isnull(nota_seminar) then
        set nota_seminar = 0;
    end if;

    if isnull(nota_curs) then
        set nota_curs = 0;
    end if;

    if isnull(nota_laborator) then
        set nota_laborator = 0;
    end if;

    set nota_examen = nota_laborator * procent_laborator + nota_curs * procent_curs + nota_seminar * procent_seminar;
    -- debug

    if (select count(*) from (select * from examen where idMaterie = id_materie and idStudent = NEW.idStudent) as T) =
       0 then
        insert into examen(idMaterie, idStudent, valoare)
        values (id_materie, NEW.idStudent, nota_examen);
    else
        update examen
        set valoare=nota_examen
        where idMaterie = id_materie
          and idStudent = NEW.idStudent;
    end if;
end;

create definer = root@localhost trigger t_actualizare_nota_examen_update
    after update
    on note
    for each row
begin
    declare nota_curs double;
    declare nota_seminar double;
    declare nota_laborator double;
    declare tip_nota enum ('CURS','SEMINAR','LABORATOR');
    declare id_materie int;
    declare procent_curs double;
    declare procent_seminar double;
    declare procent_laborator double;
    declare nota_examen double;
    set tip_nota = (select tip from activitate_didactica where id = NEW.idActDic);
    set id_materie = (select idMaterie from activitate_didactica where id = new.idActDic);
    set procent_curs = (select curs from materie where id = id_materie);
    set procent_seminar = (select seminar from materie where id = id_materie);
    set procent_laborator = (select lab from materie where id = id_materie);
    if tip_nota = 'CURS' then
        set nota_curs = new.valoare;
        set nota_laborator = (
            select valoare
            from note n
                     inner join activitate_didactica ad on n.idActDic = ad.id
                     inner join materie m on ad.idMaterie = m.id
            where n.idStudent = NEW.idStudent
              and m.id = id_materie
              and tip = 'LABORATOR'
        );
        set nota_seminar = (
            select valoare
            from note n
                     inner join activitate_didactica ad on n.idActDic = ad.id
                     inner join materie m on ad.idMaterie = m.id
            where n.idStudent = NEW.idStudent
              and m.id = id_materie
              and tip = 'SEMINAR'
        );
    elseif tip_nota = 'SEMINAR' then
        set nota_seminar = new.valoare;
        set nota_curs = (
            select valoare
            from note n
                     inner join activitate_didactica ad on n.idActDic = ad.id
                     inner join materie m on ad.idMaterie = m.id
            where n.idStudent = NEW.idStudent
              and m.id = id_materie
              and tip = 'CURS'
        );
        set nota_laborator = (
            select valoare
            from note n
                     inner join activitate_didactica ad on n.idActDic = ad.id
                     inner join materie m on ad.idMaterie = m.id
            where n.idStudent = NEW.idStudent
              and m.id = id_materie
              and tip = 'LABORATOR'
        );
    else
        set nota_laborator = new.valoare;
        set nota_curs = (
            select valoare
            from note n
                     inner join activitate_didactica ad on n.idActDic = ad.id
                     inner join materie m on ad.idMaterie = m.id
            where n.idStudent = NEW.idStudent
              and m.id = id_materie
              and tip = 'CURS'
        );
        set nota_seminar = (
            select valoare
            from note n
                     inner join activitate_didactica ad on n.idActDic = ad.id
                     inner join materie m on ad.idMaterie = m.id
            where n.idStudent = NEW.idStudent
              and m.id = id_materie
              and tip = 'SEMINAR'
        );
    end if;

    if isnull(nota_seminar) then
        set nota_seminar = 0;
    end if;

    if isnull(nota_curs) then
        set nota_curs = 0;
    end if;

    if isnull(nota_laborator) then
        set nota_laborator = 0;
    end if;

    set nota_examen = nota_laborator * procent_laborator + nota_curs * procent_curs + nota_seminar * procent_seminar;
    -- debug

    if (select count(*) from (select * from examen where idMaterie = id_materie and idStudent = NEW.idStudent) as T) =
       0 then
        insert into examen(idMaterie, idStudent, valoare)
        values (id_materie, NEW.idStudent, nota_examen);
    else
        update examen
        set valoare=nota_examen
        where idMaterie = id_materie
          and idStudent = NEW.idStudent;
    end if;
end;

create table student_grupe
(
    idStudent     int  not null,
    idGrupa       int  not null,
    dataInscriere date null,
    primary key (idStudent, idGrupa),
    constraint `student-grupe_grup_id_fk`
        foreign key (idGrupa) references grup (id),
    constraint `student-grupe_student_id_fk`
        foreign key (idStudent) references student (id)
);

create table studentiintalnire
(
    idIntalnire  int  not null,
    idStudent    int  not null,
    dataInrolare date null,
    primary key (idIntalnire, idStudent),
    constraint studentiIntalnire_intalnire_id_fk
        foreign key (idIntalnire) references intalnire (id),
    constraint studentiintalnire_student_id_fk
        foreign key (idStudent) references student (id)
);

create index studentiIntalnire_student_id_fk
    on studentiintalnire (idStudent);

create table zi
(
    idUser       int      null,
    id           int auto_increment
        primary key,
    idActivitate int      null,
    dataCurenta  datetime not null,
    constraint zi_activitate_didactica_id_fk
        foreign key (idActivitate) references activitate_didactica (id),
    constraint zi_user_id_fk
        foreign key (idUser) references user (id)
);

create
    definer = root@localhost procedure get_all_mesaje_by_id(IN p_idFrom int, IN p_idTo int, IN p_flag binary(1))
begin
    if p_flag = 0 then
        select *
        from mesaj_privat
        where idFrom = p_idFrom and idto = p_idTo
           or idFrom = p_idTo and idTo = p_idFrom
        order by dataTrimitere asc;
    else
        select m.*, u.*
        from mesaj m
                 inner join user u on m.idStudent = u.id
        where m.idGrup = p_idTo;
    end if;

end;

create
    definer = root@localhost procedure get_materie_by_id(IN p_idMaterie int)
begin

    SELECT m.*, u.*, p.*
    FROM user u
             inner join profesori p on u.id = p.id
             inner join materie m on p.id = m.idTitular
    where m.id = p_idMaterie;

end;

create
    definer = root@localhost procedure get_profesor_by_id(IN p_idProf int)
begin

    SELECT u.*, p.*
    FROM user u
             inner join profesori p on u.id = p.id
    where p.id = p_idProf;

end;

create
    definer = root@localhost procedure p_afisare_activitate_didactica_by_id_materie(IN p_idMaterie int)
begin

    select a_d.*
    from activitate_didactica a_d
    where a_d.idMaterie = p_idMaterie;
end;

create
    definer = root@localhost procedure p_afisare_activitati_didactice(IN pUsername varchar(50))
begin
    declare vRol varchar(50);
    set vRol = (select nume
                from rol
                where id = (select idRol from user where id = (select id from user where username = pUsername)));
    if vRol = 'Student' then
        select a_d.*, u2.nume, u2.prenume
        from user u
                 inner join
             student s on u.id = s.id
                 inner join inscrierestudent i_s on s.id = i_s.idStudent
                 inner join materie m on i_s.idMaterie = m.id
                 inner join activitate_didactica a_d on a_d.idMaterie = m.id
                 inner join didactic d on d.idActDic = a_d.id
                 inner join profesori p on d.idProf = p.id
                 inner join user u2 on p.id = u2.id
        where u.username = pUsername;
    elseif vRol = 'Profesor' then
        select a_d.*, u.nume, u.prenume
        from user u
                 inner join profesori p on u.id = p.id
                 inner join didactic d on d.idProf = p.id
                 inner join activitate_didactica a_d on d.idActDic = a_d.id
        where u.username = pUsername;
    end if;
end;

create
    definer = root@localhost procedure p_afisare_conversatii(IN p_username varchar(50))
begin
    select distinct u.*
    from user u
             inner join mesaj_privat mp on u.id = mp.idTo
    where idFrom = (SELECT id from user u2 where u2.username = p_username);
end;

create
    definer = root@localhost procedure p_afisare_grupuri(IN pUsername varchar(50))
begin
    select g.*, m.*
    from materie m
             inner join grup g on m.id = g.idMaterie
    where g.id not in (select g2.id
                       from grup g2
                                inner join student_grupe sg on g2.id = sg.idGrupa
                                inner join user u on u.id = sg.idStudent
                       where u.username = pUsername);

end;

create
    definer = root@localhost procedure p_afisare_intalniri_from_grup(IN p_idGrup int, IN p_Username varchar(50))
begin
    select distinct i.*, u2.*
    from intalnire i
             left join studentiintalnire s on i.id = s.idIntalnire
             inner join user u2 on u2.id = i.idCreator
    where i.idGrup = p_idGrup
      and i.id not in (select i2.id
                       from intalnire i2
                                inner join studentiintalnire s2 on i2.id = s2.idIntalnire
                                inner join user u on u.id = s2.idStudent
                       where u.username = p_Username);

end;

create
    definer = root@localhost procedure p_afisare_materii(IN pUsername varchar(50))
begin

    declare vRol varchar(50);
    set vRol = (select nume
                from rol
                where id = (select idRol from user where id = (select id from user where username = pUsername)));
    if vRol = 'Admin' or vRol = 'Super admin' then
        select m.*, p.*, u.*
        from materie m
                 inner join profesori p on m.idTitular = p.id
                 inner join user u on p.id = u.id;
    elseif vRol = 'Profesor' then
        select m.*, p.*, u.*
        from materie m
                 inner join profesori p on m.idTitular = p.id
                 inner join user u on p.id = u.id
        where u.username = pUsername;
    elseif vRol = 'Student' then
        select m.*, p.*, u.*
        from user u
                 inner join profesori p on u.id = p.id
                 inner join materie m on p.id = m.idTitular
                 inner join inscrierestudent i on m.id = i.idMaterie
                 inner join student s on i.idStudent = s.id
                 inner join user u2 on s.id = u2.id
        where u2.username = pUsername;

    end if;

end;

create
    definer = root@localhost procedure p_afisare_materii_not_stud(IN pUsername varchar(50))
begin

    select distinct m2.*, u2.*, p.*
    from user u2
             inner join profesori p on u2.id = p.id
             inner join materie m2 on p.id = m2.idTitular
    where m2.id not in (select distinct m.id
                        from user u
                                 inner join student s on u.id = s.id
                                 inner join inscrierestudent i on s.id = i.idStudent
                                 inner join materie m on i.idMaterie = m.id
                        where u.username = pUsername)
    order by m2.nume;

end;

create
    definer = root@localhost procedure p_afisare_medie_student_by_id_materie(IN p_idStudent int, IN p_idMaterie int)
begin
    select valoare from examen where idMaterie = p_idMaterie and idStudent = p_idStudent;
end;

create
    definer = root@localhost procedure p_afisare_membrii_grup(IN p_idGrup int)
begin

    SELECT *
    from user u
             inner join student_grupe sg on u.id = sg.idStudent
    where sg.idGrupa = p_idGrup;

end;

create
    definer = root@localhost procedure p_afisare_note_student_by_id_activitate(IN p_idStudent int, IN p_idActivitate int)
begin
    select valoare from note where idActDic = p_idActivitate and idStudent = p_idStudent;
end;

create
    definer = root@localhost procedure p_afisare_profi()
begin
    select p.*, u.*
    from profesori p
             inner join user u on p.id = u.id;
end;

create
    definer = root@localhost procedure p_afisare_studenti_by_activitate_didactica(IN pidActivitateDidactica int)
begin

    select u.*, s.*
    from user u
             inner join student s on u.id = s.id
             inner join inscrierestudent i on s.id = i.idStudent
             inner join materie m on i.idMaterie = m.id
             inner join activitate_didactica a on m.id = a.idMaterie
    where a.id = pidActivitateDidactica;

end;

create
    definer = root@localhost procedure p_afisare_toate_activitatile_didactice()
begin
    select ad.*, u.nume, u.prenume
    from user u
             inner join profesori p on u.id = p.id
             inner join didactic d on d.idProf = p.id
             right join activitate_didactica ad on ad.id = d.idActDic;
end;

create
    definer = root@localhost procedure p_afisare_toate_materiile()
begin
    select m.*, p.*, u.*
    from materie m
             inner join profesori p on m.idTitular = p.id
             inner join user u on p.id = u.id;
end;

create
    definer = root@localhost procedure p_cautare_dupa_tip(IN p_rol varchar(50))
begin
    declare vRol int;
    set vRol = (select id from rol where nume = p_rol);

    select * from user where idRol = vRol;

end;

create
    definer = root@localhost procedure p_delete_inscrierestudent(IN p_idStudent int, IN p_idMaterie int)
begin
    delete
    from inscrierestudent
    where idStudent = p_idStudent
      and idMaterie = p_idMaterie;
end;

create
    definer = root@localhost procedure p_delete_intalnire(IN p_id int)
begin
    delete from intalnire where id = p_id;
end;

create
    definer = root@localhost procedure p_delete_studenti_intalnire(IN p_idStudent int, IN p_idGrup int)
begin
    delete
    from student_grupe
    where idStudent = p_idStudent
      and idGrupa = p_idGrup;
    delete
    from studentiintalnire
    where idStudent = p_idStudent
      and idIntalnire in (select id from intalnire where idGrup = p_idGrup);
end;

create
    definer = root@localhost procedure p_delete_studentiintalnire(IN p_idIntalnire int)
begin

    delete from studentiintalnire where idIntalnire = p_idIntalnire;

end;

create
    definer = root@localhost procedure p_getActivitatiByDate(IN pUsername varchar(50), IN pDate date)
begin

    select ad.*
    from zi z
             inner join activitate_didactica ad on z.idActivitate = ad.id
             inner join user u on z.idUser = u.id
    where u.username = pUsername
      and z.dataCurenta = pDate;
end;

create
    definer = root@localhost procedure p_getActivitatiFromZi(IN pUsername varchar(50))
begin

    select ad.*, z.*
    from zi z
             inner join activitate_didactica ad on z.idActivitate = ad.id
             inner join user u on z.idUser = u.id
    where u.username = pUsername;
end;

create
    definer = root@localhost procedure p_get_date_by_user(IN pUsername varchar(50))
begin

    select z.*
    from zi z
             inner join activitate_didactica ad on z.idActivitate = ad.id
             inner join user u on z.idUser = u.id
    where u.username = pUsername;

end;

create
    definer = root@localhost procedure p_get_grup_by_user(IN p_username varchar(50))
begin

    declare vRol varchar(50);
    set vRol = (select nume
                from rol
                where id = (select idRol from user where id = (select id from user where username = p_username)));

    if vRol = 'Student' then

        select g.*
        from grup g
                 inner join student_grupe sg on g.id = sg.idGrupa
                 inner join student s on sg.idStudent = s.id
                 inner join user u on s.id = u.id
        where u.username = p_username;
    else
        if vRol = 'Profesor' then

            select g.*
            from grup g
                     inner join user u on u.id = g.idProf
            where u.username = p_username;

        end if;
    end if;
end;

create
    definer = root@localhost procedure p_get_id_grup(IN p_idMaterie int, IN p_idProf int)
begin
    select id from grup where idMaterie = p_idMaterie and idProf = p_idProf;
end;

create
    definer = root@localhost procedure p_get_id_intalnire(IN p_titlu varchar(50), IN p_nrParticipanti int,
                                                          IN p_durata int, IN p_dataIncepere date,
                                                          IN p_dataTerminare date, IN p_idCreator int, IN p_idGrup int)
begin
    SELECT id
    from intalnire
    WHERE titlu = p_titlu
      and nr_participanti = p_nrParticipanti
      and durata = p_durata
      and dataIncepere = p_dataIncepere
      and dataExpirare = p_dataTerminare
      and idCreator = p_idCreator
      and idGrup = p_idGrup;
end;

create
    definer = root@localhost procedure p_get_meetings()
begin
    select i.*, u.*, g.*, u2.username
    from intalnire i
             inner join grup g on i.idGrup = g.id
             inner join user u2 on g.idProf = u2.id
             inner join studentiintalnire s on i.id = s.idIntalnire
             inner join user u on s.idStudent = u.id;
end;

create
    definer = root@localhost procedure p_get_sugestii(IN p_idMaterie int)
begin
    select u.*
    from user u
             inner join inscrierestudent i on u.id = i.idStudent
    where i.idMaterie = p_idMaterie;
end;

create
    definer = root@localhost procedure p_insert_activitate_didactica(IN p_tip enum ('CURS', 'SEMINAR', 'LABORATOR'),
                                                                     IN p_denumire varchar(50),
                                                                     IN p_dataIncepere datetime,
                                                                     IN p_dataTerminare datetime, IN p_nrMaxim int,
                                                                     IN p_durata int, IN p_idMaterie int)
begin
    INSERT INTO activitate_didactica(tip, denumire, data_incepere, data_sfarsit, nr_maxim, durata_activitate, idMaterie)
    VALUES (p_tip, p_denumire, p_dataIncepere, p_dataTerminare, p_nrMaxim, p_durata, p_idMaterie);
end;

create
    definer = root@localhost procedure p_insert_didactic(IN p_dataAsign date, IN p_idActDic int, IN p_idProf int)
begin
    insert into didactic(dataAsign, idActDic, idProf)
    values (p_dataAsign, p_idActDic, p_idProf);
end;

create
    definer = root@localhost procedure p_insert_grup(IN p_idMaterie int, IN p_idProf int, IN p_descriere text,
                                                     IN p_denumire varchar(50))
begin
    insert into grup(idMaterie, idProf, descriere, denumire)
    values (p_idMaterie, p_idProf, p_descriere, p_denumire);
end;

create
    definer = root@localhost procedure p_insert_inscrierestudent(IN p_dataInscriere date, IN p_idMaterie int, IN p_idStudent int)
begin
    insert into inscrierestudent(dataInscriere, idMaterie, idStudent)
    values (p_dataInscriere, p_idMaterie, p_idStudent);
end;

create
    definer = root@localhost procedure p_insert_intalnire(IN p_titlu varchar(50), IN p_nrParticipanti int,
                                                          IN p_durata int, IN p_dataIncepere date,
                                                          IN p_dataTerminare date, IN p_idCreator int, IN p_idGrup int)
begin
    INSERT INTO intalnire (titlu, nr_participanti, durata, dataIncepere, dataExpirare, idCreator, idGrup)
    VALUES (p_titlu, p_nrParticipanti, p_durata, p_dataIncepere, p_dataTerminare, p_idCreator, p_idGrup);
end;

create
    definer = root@localhost procedure p_insert_mesaj_privat(IN p_idFrom int, IN p_idTo int, IN p_text text,
                                                             IN p_dataTrimitere varchar(50), IN p_flag int)
begin

    if p_flag = 0 then
        insert into mesaj_privat(idFrom, idTo, text, dataTrimitere)
        values (p_idFrom, p_idTo, p_text, p_dataTrimitere);
    else

        insert into mesaj(idStudent, idGrup, textMesaj, dataTrimitere)
        values (p_idFrom, p_idTo, p_text, p_dataTrimitere);

    end if;

end;

create
    definer = root@localhost procedure p_insert_nota(IN p_valoare double, IN p_dataAtribuire date, IN p_idStudent int,
                                                     IN p_idActivitate int)
begin

    INSERT INTO note(valoare, data_atribuire, idStudent, idActDic)
    VALUES (p_valoare, p_dataAtribuire, p_idStudent, p_idActivitate);

end;

create
    definer = root@localhost procedure p_insert_student_grupe(IN p_dataInscriere date, IN p_idGrupa int, IN p_idStudent int)
begin
    insert into student_grupe(dataInscriere, idGrupa, idStudent)
    values (p_dataInscriere, p_idGrupa, p_idStudent);
end;

create
    definer = root@localhost procedure p_insert_studentiintalnire(IN p_idIntalnire int, IN p_idStudent int, IN p_dataInrolare date)
begin

    INSERT INTO studentiintalnire(idIntalnire, idStudent, dataInrolare)
    VALUES (p_idIntalnire, p_idStudent, p_dataInrolare);

end;

create
    definer = root@localhost procedure p_num_activitati(IN pid int)
begin
    select count(*) as num
    from student s
             inner join inscrierestudent i on s.id = i.idStudent
             inner join activitate_didactica ad on i.idMaterie = ad.idMaterie
    where ad.id = pid;
end;

create
    definer = root@localhost procedure p_num_materii(IN pid int)
begin
    select count(*) as num
    from student s
             inner join inscrierestudent i on s.id = i.idStudent
    where i.idMaterie = pid;
end;

create
    definer = root@localhost procedure p_num_stud_intalnire(IN p_idIntalnire int)
begin
    select count(*) num
    from studentiintalnire
    where idIntalnire = p_idIntalnire;
end;

create
    definer = root@localhost procedure p_save_formula(IN p_idMaterie int, IN p_curs double, IN p_seminar double,
                                                      IN p_lab double)
begin
    UPDATE materie
    SET curs    = p_curs,
        seminar = p_seminar,
        lab     = p_lab
    where id = p_idMaterie;
end;

create
    definer = root@localhost procedure p_update_profesori(IN pId int, IN p_idDepartament int, IN p_maxOre int, IN p_minOre int)
begin
    if (not isnull(p_idDepartament)) then
        update profesori
        set idDepartament=p_idDepartament
        where id = pId;
    end if;

    if (not isnull(p_maxOre)) then
        update profesori
        set maxOre=p_maxOre
        where id = pId;
    end if;

    if (not isnull(p_minOre)) then
        update profesori
        set minOre=p_minOre
        where id = pId;
    end if;

end;

create
    definer = root@localhost procedure p_update_student(IN pId int, IN p_an_studiu int, IN p_ore_sustinute int)
begin
    if (not isnull(p_an_studiu)) then
        update student
        set an_studiu=p_an_studiu
        where id = pId;
    end if;

    if (not isnull(p_ore_sustinute)) then
        update student
        set ore_sustinute=p_ore_sustinute
        where id = pId;
    end if;

end;

create
    definer = root@localhost procedure p_update_user(IN pId int, IN p_adresa varchar(100), IN p_CNP varchar(15),
                                                     IN p_email varchar(50), IN p_IBAN varchar(30), IN p_idRol int,
                                                     IN p_nr_contract varchar(30), IN p_nr_telefon varchar(12),
                                                     IN p_nume varchar(50), IN p_parola varchar(50),
                                                     IN p_prenunme varchar(50), IN p_username varchar(50))
begin
    if (not isnull(p_adresa)) then
        update user
        set adresa=p_adresa
        where id = pId;
    end if;

    if (not isnull(p_CNP)) then
        update user
        set CNP = p_CNP
        where id = pId;
    end if;

    if (not isnull(p_email)) then
        update user
        set email = p_email
        where id = pId;
    end if;

    if (not isnull(p_IBAN)) then
        update user
        set IBAN = p_IBAN
        where id = pId;
    end if;

    if (not isnull(p_idRol)) then
        update user
        set idRol = p_idRol
        where id = pId;
    end if;

    if (not isnull(p_nr_contract)) then
        update user
        set nr_contract = p_nr_contract
        where id = pId;
    end if;

    if (not isnull(p_nr_telefon)) then
        update user
        set nr_telefon=p_nr_telefon
        where id = pId;
    end if;

    if (not isnull(p_nume)) then
        update user
        set nume = p_nume
        where id = pId;
    end if;

    if (not isnull(p_parola)) then
        update user
        set parola=p_parola
        where id = pId;
    end if;

    if (not isnull(p_prenunme)) then
        update user
        set prenume = p_prenunme
        where id = pId;
    end if;

    if (not isnull(p_username)) then
        update user
        set username = p_username
        where id = pId;
    end if;

end;

create
    definer = root@localhost procedure sp_InsertProfesor(IN id int, IN minOre int, IN maxOre int, IN idDepartament int)
begin

    INSERT INTO profesori(id, minOre, maxOre, idDepartament)
    VALUES (id, minOre, maxOre, idDepartament);

end;

create
    definer = root@localhost procedure sp_InsertStudent(IN id int, IN an_studiu int, IN ore_sustinute int)
begin

    INSERT INTO student(id, an_studiu, ore_sustinute)
    VALUES (id, an_studiu, ore_sustinute);

end;

create
    definer = root@localhost procedure sp_InsertUser(IN nume varchar(50), IN prenume varchar(50), IN cnp varchar(15),
                                                     IN adresa varchar(100), IN nr_telefon varchar(12),
                                                     IN email varchar(50), IN IBAN varchar(30),
                                                     IN nr_contract varchar(30), IN username varchar(50),
                                                     IN parola varchar(50), IN idRol int)
begin

    INSERT INTO user(CNP, nume, prenume, adresa, nr_telefon, email, IBAN, nr_contract, username, parola, idRol)
    VALUES (cnp, nume, prenume, adresa, nr_telefon, email, IBAN, nr_contract, username, parola, idRol);

end;

create
    definer = root@localhost procedure sp_get_AllActivitati(IN p_username varchar(50))
BEGIN
    SELECT *
    FROM activitate_didactica
             JOIN materie m on activitate_didactica.idMaterie = m.id
             JOIN inscrierestudent i on m.id = i.idMaterie
             JOIN student s on i.idStudent = s.id
             JOIN user u on s.id = u.id
    WHERE u.username = p_username;
END;

insert into rol(nume)
values('Student'),
('Profesor'),
('Admin'),
('Super admin');

insert into departament(denumire)
values('Automatica'),
('Calculatoare'),
('Matematica');
