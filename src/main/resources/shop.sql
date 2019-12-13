create table item (
   id int primary key,
   name varchar(30),
   price int,
   description varchar(100),
   pictureUrl varchar(30)
);
insert into item values (1, '레몬', 50,  '레몬에 포함된 구연산은 피로회복에 좋습니다.   비타민C 도 풍부합니다.','lemon.jpg');
insert into item values (2, '오렌지', 100, '비타민C 가 풍부합니다. 맛도 좋습니다.','orange.jpg');
insert into item values (3, '키위', 200,  '비타민C 가 풍부합니다. 다이어트에 좋습니다.','kiui.jpg');
insert into item values (4, '포도', 300,  '폴리페놀을 다량 함유하고 있어 항산화 작용을 합니다.',  'podo.jpg');
insert into item values (5, '딸기', 800,  '비타민C를 다량 함유하고 있습니다.',  'ichigo.jpg');
insert into item values (6, '귤', 1000,  '시네피린을 다량 함유하고 있어 감기예방에 좋습니다.',  'mikan.jpg');
commit;