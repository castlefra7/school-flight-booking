/*
    db: compagnie_aerienne_p9
    user: hihi
    pwd: 123456

    ALTER USER hihi WITH SUPERUSER;


*/


DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
CREATE EXTENSION pgcrypto;

create type passengerCateg as  enum ('bébé', 'enfant', 'adulte');
create type userType as  enum ('admin', 'user');

create table users (
    id serial primary key,
    name varchar(255) not null check (name <> ''),
    password char(60) not null check (password <>''),
    user_type userType not null
);

insert into users (name, password, user_type) values ('admin', crypt('123456', gen_salt('bf')), 'admin');

create table towns (
    id serial primary key,
    name varchar(255) not null check (name <> ''),
    unique (name)
);

create table routes (
    id serial primary key,
    id_town_origin int,
    id_town_destination int check (id_town_origin <> id_town_destination),
    foreign key (id_town_origin) references towns(id),
    foreign key (id_town_destination) references towns(id),
    unique (id_town_origin, id_town_destination),
    unique (id_town_destination, id_town_origin)
);

create table planes (
    id serial primary key,
    name varchar(10) not null check (name <> ''),
    number_places smallint not null check (number_places > 0),
    unique (name)
);

create table placeTypes (
    id serial primary key,
    name varchar(100) not null check (name <> ''),
    unique (name)
);

create table planePlaceTypes (
    id serial primary key,
    id_place_type int,
    id_plane int,
    number_places smallint not null check (number_places > 0),
    foreign key (id_place_type) references placeTypes(id),
    foreign key (id_plane) references planes(id),
    unique (id_place_type, id_plane)
);

create table flights (
    id serial  primary key,
    id_route int,
    date_flight_departure timestamp not null,
    date_flight_arriving timestamp not null check (date_flight_arriving > date_flight_departure),
    id_plane int,
    foreign key (id_plane) references planes(id),
    foreign key (id_route) references routes(id)
);

create table customers (
    id serial primary key,
    full_name varchar(255) not null check (full_name <> ''),
    email varchar(255) not null check (email <> ''),
    phone_number char(13) not null check (phone_number <> ''),
    address varchar(255) not null check (address <> '')
);

create table passengers (
    id serial primary key,
    id_customer int,
    first_name varchar(255) not null check (first_name <> ''),
    last_name varchar(255) not null check (last_name <> ''),
    passenger_categ passengerCateg not null,
    id_place_type int,
    price double precision not null check (price > 0),
    foreign key (id_customer) references customers(id),
    foreign key (id_place_type) references placeTypes(id)
);

create SEQUENCE bookingNumber increment by 1 minvalue 1 start 1;
create table bookings (
    id serial primary key,
    date_booking timestamp not null,
    id_flight int,
    id_customer int,
    booking_number int not null check (booking_number > 0),
    foreign key (id_customer) references customers(id),
    foreign key (id_flight) references flights(id),
    unique (booking_number)
);

create table pricings (
    id serial primary key,
    id_place_type int,
    id_route int,
    passenger_categ passengerCateg not null,
    price double precision not null check (price > 0),
    foreign key (id_route) references routes(id),
    foreign key (id_place_type) references placeTypes(id),
    unique (id_place_type, id_route, passenger_categ)
);



/* JOUR 2 */
create type cardType as  enum ('visa', 'mastercard', 'discover', 'amex', 'diners', 'jcb');
create table payments (
    id serial primary key,
    email varchar(255) not null check (email <> ''),
    booking_number int not null check (booking_number > 0),
    date_payment timestamp not null,
    bank_card varchar(16) not null check (bank_card <> ''),
    amount double precision not null check (amount > 0),
    unique (booking_number)
);


alter table payments add column card_name cardType;

/* PAYMENT STATISTICS */
/* graph payments by date */

create view stats_payments_by_date as select date(date_payment), sum(amount) as total_amount from payments group by date(date_payment) order by date;

/* graph amount by route */

/* BASIC VIEWS */

create view all_routes as select routes.id as id_route, routes.id_town_origin, t_origin.name as origin_name, routes.id_town_destination, t_dest.name as dest_name from routes join towns t_origin on t_origin.id = routes.id_town_origin join towns t_dest on t_dest.id = routes.id_town_destination;

create view all_flights as select flights.id as id_flight, flights.date_flight_departure, flights.date_flight_arriving, all_routes.*, planes.id as id_plane, planes.name from flights join all_routes on all_routes.id_route = flights.id_route join planes on planes.id = flights.id_plane;

create view all_pricings as select pricings.id_place_type, pricings.passenger_categ, pricings.price, pricings.id, placeTypes.name,  all_routes.* from all_routes left join pricings on pricings.id_route = all_routes.id_route join placeTypes on pricings.id_place_type = placeTypes.id; 

/* VIEW FOR CALCULATING THE REMAINING SEATS IN A FLIGHT */
create view all_passengers_bookings as select customers.id as id_customer, customers.full_name, customers.email, customers.phone_number, bookings.date_booking, bookings.id_flight as id_flight_customer, bookings.booking_number, passengers.id as id_passenger, passengers.first_name, passengers.last_name, passengers.passenger_categ, placeTypes.id as id_place_type, placeTypes.name as name_placeType, passengers.price from customers join bookings on bookings.id_customer = customers.id join passengers on passengers.id_customer = customers.id join placeTypes on placeTypes.id = passengers.id_place_type;

create view all_flights_passengers as select all_flights.*, all_passengers_bookings.* from all_flights join all_passengers_bookings on all_passengers_bookings.id_flight_customer = all_flights.id_flight;

create view all_flights_plane_places_total as select all_flights.id_flight, planePlaceTypes.id_plane, planePlaceTypes.id_place_type, sum(planePlaceTypes.number_places) as number_places from all_flights join planePlaceTypes on planePlaceTypes.id_plane = all_flights.id_plane group by all_flights.id_flight, planePlaceTypes.id_plane, planePlaceTypes.id_place_type order by all_flights.id_flight;

create view all_flights_passengers_places_total as select id_flight, id_place_type, count(*) as number_places from all_flights_passengers group by id_flight, id_place_type;


create view all_flights_summary as select all_flights_plane_places_total.id_flight, all_flights_plane_places_total.id_place_type, (all_flights_plane_places_total.number_places - coalesce(all_flights_passengers_places_total.number_places,0)) as remain_places,all_flights_plane_places_total.number_places  from all_flights_plane_places_total left join all_flights_passengers_places_total on all_flights_passengers_places_total.id_flight = all_flights_plane_places_total.id_flight and all_flights_passengers_places_total.id_place_type = all_flights_plane_places_total.id_place_type;

/* FLIGHTS SUMMARY WITH DETAILS */
create view all_flights_summary_with_details as select all_flights_summary.remain_places, all_flights_summary.id_place_type, all_flights_summary.number_places, all_flights.*, placetypes.name as name_placeType from all_flights_summary join placetypes on placetypes.id = all_flights_summary.id_place_type join all_flights on all_flights.id_flight = all_flights_summary.id_flight;


select * from all_flights where id_town_origin = 1 and id_town_destination = 2 and date_flight_departure between '2021-07-01 00:01' and '2021-07-07 23:59' and id_flight in (select id_flight from all_flights_summary where id_place_type = 1 and remain_places >= 198);


/* VIEWS FOR PAYMENTS */
create view all_bookings_total_price as select booking_number, sum(price) as total_price from all_flights_passengers group by booking_number;



/* TEST DATA NEEDED FOR THE APPLICATION TO WORK */
insert into towns (name) values ('antananarivo');
insert into towns (name) values ('new york');
insert into towns (name) values ('paris');
insert into towns (name) values ('belgique');

insert into routes (id_town_origin, id_town_destination) values (1, 2);
insert into routes (id_town_origin, id_town_destination) values (2, 1);
insert into routes (id_town_origin, id_town_destination) values (3, 4);
insert into routes (id_town_origin, id_town_destination) values (4, 3);

insert into placeTypes (name) values ('économique');
insert into placeTypes (name) values ('affaire');

insert into planes (name, number_places) values ('a12', 250);
insert into planes (name, number_places) values ('a13', 300);
insert into planes (name, number_places) values ('a14', 500);

insert into planePlaceTypes (id_place_type, id_plane, number_places) values (1, 1, 200);
insert into planePlaceTypes (id_place_type, id_plane, number_places) values (2, 1, 50);
insert into planePlaceTypes (id_place_type, id_plane, number_places) values (1, 2, 250);
insert into planePlaceTypes (id_place_type, id_plane, number_places) values (2, 2, 50);
insert into planePlaceTypes (id_place_type, id_plane, number_places) values (1, 3, 400);
insert into planePlaceTypes (id_place_type, id_plane, number_places) values (2, 3, 100);

insert into pricings (id_place_type, id_route, passenger_categ, price) values (1, 1, 'adulte', 1000000);
insert into pricings (id_place_type, id_route, passenger_categ, price) values (1, 1, 'enfant', 950000);
insert into pricings (id_place_type, id_route, passenger_categ, price) values (1, 1, 'bébé', 900000);
insert into pricings (id_place_type, id_route, passenger_categ, price) values (2, 1, 'adulte', 21000000);
insert into pricings (id_place_type, id_route, passenger_categ, price) values (2, 1, 'enfant', 1900000);
insert into pricings (id_place_type, id_route, passenger_categ, price) values (2, 1, 'bébé', 1800000);

insert into pricings (id_place_type, id_route, passenger_categ, price) values (1, 2, 'adulte', 1200000);
insert into pricings (id_place_type, id_route, passenger_categ, price) values (1, 2, 'enfant', 1000000);
insert into pricings (id_place_type, id_route, passenger_categ, price) values (1, 2, 'bébé', 950000);
insert into pricings (id_place_type, id_route, passenger_categ, price) values (2, 2, 'adulte', 2400000);
insert into pricings (id_place_type, id_route, passenger_categ, price) values (2, 2, 'enfant', 2000000);
insert into pricings (id_place_type, id_route, passenger_categ, price) values (2, 2, 'bébé', 1900000);

/* TEST DATA THAT IS STATIC */
/* Route: Antananarivo -> New York */
insert into flights (id_route, date_flight_departure, date_flight_arriving, id_plane) values (1, '2021-07-01 08:00', '2021-07-01 23:00', 1);
insert into flights (id_route, date_flight_departure, date_flight_arriving, id_plane) values (1, '2021-07-02 08:00', '2021-07-02 23:00', 2);
insert into flights (id_route, date_flight_departure, date_flight_arriving, id_plane) values (1, '2021-07-03 08:00', '2021-07-03 23:00', 3);
insert into flights (id_route, date_flight_departure, date_flight_arriving, id_plane) values (1, '2021-07-04 08:00', '2021-07-04 23:00', 1);
insert into flights (id_route, date_flight_departure, date_flight_arriving, id_plane) values (1, '2021-07-05 08:00', '2021-07-05 23:00', 2);
insert into flights (id_route, date_flight_departure, date_flight_arriving, id_plane) values (1, '2021-07-06 08:00', '2021-07-06 23:00', 3);
insert into flights (id_route, date_flight_departure, date_flight_arriving, id_plane) values (1, '2021-07-07 08:00', '2021-07-07 23:00', 1);

/* Route: New York -> Antananarivo */
insert into flights (id_route, date_flight_departure, date_flight_arriving, id_plane) values (2, '2021-08-01 08:00', '2021-08-01 23:00', 1);
insert into flights (id_route, date_flight_departure, date_flight_arriving, id_plane) values (2, '2021-08-02 08:00', '2021-08-02 23:00', 2);
insert into flights (id_route, date_flight_departure, date_flight_arriving, id_plane) values (2, '2021-08-03 08:00', '2021-08-03 23:00', 3);
insert into flights (id_route, date_flight_departure, date_flight_arriving, id_plane) values (2, '2021-08-04 08:00', '2021-08-04 23:00', 1);
insert into flights (id_route, date_flight_departure, date_flight_arriving, id_plane) values (2, '2021-08-05 08:00', '2021-08-05 23:00', 2);
insert into flights (id_route, date_flight_departure, date_flight_arriving, id_plane) values (2, '2021-08-06 08:00', '2021-08-06 23:00', 3);
insert into flights (id_route, date_flight_departure, date_flight_arriving, id_plane) values (2, '2021-08-07 08:00', '2021-08-07 23:00', 1);

/* CUSTOMER BOOKS a Flight */
insert into customers (full_name, email, phone_number, address) values ('rakoto lala',  'rakoto@gmail.com', '+261331165269', 'lot 68 ak alarobia');
insert into bookings (date_booking, id_flight, id_customer) values ('2021-06-25 15:00', 4, 1);
/* PASSENGER ASSOCIATED WITH THIS CUSTOMER */
insert into passengers (id_customer, first_name, last_name, passenger_categ, id_place_type, price) values (1, 'rakoto', 'lala', 'adulte', 1, 1000000);
insert into passengers (id_customer, first_name, last_name, passenger_categ, id_place_type, price) values (1, 'rasoa', 'naivo', 'adulte', 1, 1000000);
insert into passengers (id_customer, first_name, last_name, passenger_categ, id_place_type, price) values (1, 'rakoto', 'nivo', 'enfant', 1, 950000);

