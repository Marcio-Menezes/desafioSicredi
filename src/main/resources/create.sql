
create table if not exists votante(
     id uuid not null,
     cod_associado varchar(8) not null,
     voto varchar(7) not null,
     primary key (id)
     );


create table if not exists pauta (
     id uuid not null, 
     hash varchar(255) not null,
     titulo varchar(30) not null,
     descricao varchar(255),
     autor_pauta uuid not null,
     inicio_votacao timestamp,
     termino_votacao timestamp,
     resultado varchar(10),
     andamento varchar(10) not null,
     primary key (id)
);

create table if not exists assembleia (
     id uuid not null,
     nome varchar(255) not null,
     inicio_assembleia timestamp not null,
     fim_assembleia timestamp,
     status varchar(10) not null,
     primary key (id)
);

create table if not exists assembleia_pauta(
    pauta_id uuid not null,
    assembleia_id uuid not null);

create table if not exists pauta_votantes (
    pauta_id uuid not null,
    votantes_id uuid not null);
