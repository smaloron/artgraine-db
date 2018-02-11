CREATE TABLE SCULPTURES(
  id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  title VARCHAR (50) NOT NULL,
  description VARCHAR (100),
  category VARCHAR (30) NOT NULL,
  size_in_cm INTEGER,
  insurancePrice INTEGER
);

CREATE TABLE EXHIBITION(
  id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  title VARCHAR (50) NOT NULL,
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  departure_date DATE,
  return_date DATE,
  CONSTRAINT departure_date_check CHECK (departure_date >= start_date),
  CONSTRAINT return_date_check CHECK (return_date >= end_date)
);