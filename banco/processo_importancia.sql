create table processo_importancia(
	codigo int auto_increment not null,
	cdusuario int not null,
	cdprocesso int not null,
	primary key(codigo)
);

alter table processo_importancia add constraint fk_processo_importancia foreign key(cdprocesso) references processo(cdprocesso);
alter table processo_importancia add constraint fk_usuario_importancia foreign key(cdusuario) references usuario(cdusuario);

