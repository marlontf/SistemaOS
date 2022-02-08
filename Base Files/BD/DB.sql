-- COMENTÁRIOS
-- A linha abaixo cria um banco de dados
create database dbinfox;
-- A linha abaixo escolhe o banco de dados
use dbinfox;
-- o bloco de instruções abaixo cria uma tabela
create table tbusuarios(
	iduser int primary key,
    usuario varchar(50) not null,
    fone varchar(15),
    login varchar(15) not null unique,
    senha varchar(15) not null
);
-- a linha abaixo insere dados na tabela (CRUD)
-- create
insert into tbusuarios(iduser, usuario, fone, login, senha)
values (1,'José de Assis','9999-9999','joseassis','123456');
insert into tbusuarios(iduser, usuario, fone, login, senha)
values(2, 'Marlon Tavares', '8888-8888','marlontf','naocadastrei');
insert into tbusuarios(iduser, usuario, fone, login, senha)
values(3, 'Administrador','9999-9999','admin','admin');

create table tbclientes(
	idcli int primary key auto_increment,
    nomecli varchar(50) not null,
    endcli varchar(100),
    fonecli varchar(50) not null,
    emailcli varchar(50)
);

insert into tbclientes(nomecli,endcli,fonecli,emailcli)
values ('Linus Torvalds','Rua Tux , 2015','9999-9999','linus@linux.com');

create table tbos(
	os int primary key auto_increment,
    data_os timestamp default current_timestamp,
    equipamento varchar(150) not null,
    defeito varchar(150) not null,
    servico varchar(150),
    tecnico varchar(30),
    valor decimal(10,2),
    idcli int not null,
    foreign key (idcli) references tbclientes(idcli)
);

insert into tbos(equipamento, defeito, servico, tecnico, valor,idcli)
values('Notebook','Não liga','Troca da fonte','Zé',87.50,1);

/* UM SELECT INTERESSANTE
select
O.os,equipamento,defeito,servico,valor,
C.nomecli,fonecli
from tbos as O
inner join tbclientes as C
on (O.idcli = C.idcli);
*/