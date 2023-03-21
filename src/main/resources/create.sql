DROP TABLE IF EXISTS votante CASCADE;

DROP TABLE IF EXISTS pauta CASCADE;

DROP TABLE IF EXISTS assembleia CASCADE;

DROP TABLE IF EXISTS pauta_votantes CASCADE;

DROP TABLE IF EXISTS assembleia_pauta CASCADE;
    
    
create table votante (
     id uuid not null,
     voto varchar(3) not null, 
     primary key (id)
     );


create table pauta (
     id uuid not null, 
     hash varchar(255) not null,
     titulo varchar(30) not null,
     descricao varchar(255),
     autor_pauta uuid not null,
     resultado varchar(10) not null,
     primary key (id)
);

create table assembleia (
     id uuid not null, 
     status varchar(10) not null,
     primary key (id)
);

create table assembleia_pauta(
    pauta_id uuid not null,
    assembleia_id uuid not null);

create table pauta_votantes (
    pauta_id uuid not null,
    votantes_id uuid not null);
