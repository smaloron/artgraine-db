
CREATE TABLE SCULPTURES(
  id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  title VARCHAR (50) NOT NULL,
  description VARCHAR (100),
  category VARCHAR (30) NOT NULL,
  size_in_cm INTEGER,
  insurance_price INTEGER
);
CREATE TABLE EXHIBITIONS(
  id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  title VARCHAR (50) NOT NULL,
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  departure_date DATE,
  return_date DATE,
  CONSTRAINT departure_date_check CHECK (departure_date <= start_date),
  CONSTRAINT return_date_check CHECK (return_date >= end_date)
);


CREATE TABLE SCULPTURES_RESERVATIONS(
  sculpture_id INTEGER NOT NULL,
  exhibition_id INTEGER NOT NULL,
  CONSTRAINT sculptures_reservations_pk PRIMARY KEY (sculpture_id, exhibition_id),
  CONSTRAINT sculpture_reservation_fk
  FOREIGN KEY (sculpture_id)
  REFERENCES APP.SCULPTURES(id),
  CONSTRAINT exhibition_reservation_fk
  FOREIGN KEY (exhibition_id)
  REFERENCES APP.EXHIBITIONS(id)
);