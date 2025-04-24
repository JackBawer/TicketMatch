create table utilisateur
(
    id_utilisateur serial
        primary key,
    nom            varchar(100) not null,
    email          varchar(150) not null
        unique,
    mot_de_passe   text         not null,
    role           varchar(10)  not null
        constraint utilisateur_role_check
            check ((role)::text = ANY ((ARRAY ['client'::character varying, 'admin'::character varying])::text[]))
);

create table match
(
    id_match       serial
        primary key,
    equipe1        varchar(100) not null,
    equipe2        varchar(100) not null,
    date_match     date         not null,
    heure_match    time         not null,
    lieu           varchar(150) not null,
    capacite_stade integer      not null
        constraint match_capacite_stade_check
            check (capacite_stade > 0)
);

create table ticket
(
    id_ticket      serial
        primary key,
    id_utilisateur integer        not null
        references public.utilisateur
            on delete cascade,
    id_match       integer        not null
        references public.match
            on delete cascade,
    prix           numeric(10, 2) not null
        constraint ticket_prix_check
            check (prix >= (0)::numeric),
    statut         varchar(10)    not null
        constraint ticket_statut_check
            check ((statut)::text = ANY
                   ((ARRAY ['disponible'::character varying, 'vendu'::character varying])::text[])),
    numero_place   integer        not null
        constraint ticket_numero_place_check
            check (numero_place > 0)
);

create table paiement
(
    id_paiement    serial
        primary key,
    id_ticket      integer                             not null
        references public.ticket
            on delete cascade,
    id_utilisateur integer                             not null
        references public.utilisateur
            on delete cascade,
    mode_paiement  varchar(50)                         not null,
    statut         varchar(10)                         not null
        constraint paiement_statut_check
            check ((statut)::text = ANY ((ARRAY ['validé'::character varying, 'refusé'::character varying])::text[])),
    date_paiement  timestamp default CURRENT_TIMESTAMP not null
);

